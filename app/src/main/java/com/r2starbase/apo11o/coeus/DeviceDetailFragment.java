package com.r2starbase.apo11o.coeus;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Originally created by apo11o on 2/4/16.
 */
public class DeviceDetailFragment extends Fragment implements WifiP2pManager.ConnectionInfoListener {
    static final int FILE_CHOOSE_CODE = 1;
    public final static String TAG = "DeviceDetailFragment";
    private View detailView;
    private DeviceInfo device;
    private WifiP2pInfo pInfo;

    public interface DeviceDetailListener {
        void connect(WifiP2pConfig config);
        void disconnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        detailView = inflater.inflate(R.layout.device_detail_fragment, container, false);
        // Setup the buttons
        detailView.findViewById(R.id.device_connect_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.getDevice().deviceAddress;
                ((DeviceDetailListener) getActivity()).connect(config);
            }
        });
        detailView.findViewById(R.id.device_disconnect_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeviceDetailListener) getActivity()).disconnect();
            }
        });
        detailView.findViewById(R.id.device_transfer_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, DeviceDetailFragment.FILE_CHOOSE_CODE);
            }
        });
        updateDeviceDetail(this.device);
        return detailView;
    }

    public void updateDeviceDetail(DeviceInfo device) {
        TextView v;
        if (device != null) {
            v = (TextView) detailView.findViewById(R.id.device_detail_name);
            v.setText(device.getDeviceName());
            v = (TextView) detailView.findViewById(R.id.device_detail_status);
            v.setText(device.getDeviceStatusMsg());
            v = (TextView) detailView.findViewById(R.id.device_detail_address);
            v.setText(device.getDeviceAddress());
            //v = (TextView) detailView.findViewById(R.id.device_detail_other);
            //v.setText(device.getDevice().toString());
        }
    }

    public void updateNetworkDetail(WifiP2pInfo info) {
        TextView v;
        if (info != null) {
            v = (TextView) detailView.findViewById(R.id.device_detail_ip);
            v.setText(info.groupOwnerAddress.getHostAddress());
            v = (TextView) detailView.findViewById(R.id.device_detail_go_status);
            v.setText((info.isGroupOwner) ? getResources().getText(R.string.yes) : getResources().getText(R.string.no));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.pInfo != null) {
            Uri uri = data.getData();
            TextView tv = (TextView) detailView.findViewById(R.id.device_detail_transfer_status);
            tv.setText("Sending: " + uri);
            Intent sIntent = new Intent(getActivity(), ContentTransferService.class);
            sIntent.setAction(ContentTransferService.ACTION_SEND_FILE);
            sIntent.putExtra(ContentTransferService.EXTRAS_FILE_PATH, uri.toString());
            sIntent.putExtra(ContentTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
                    pInfo.groupOwnerAddress.getAddress());
            sIntent.putExtra(ContentTransferService.EXTRAS_GROUP_OWNER_PORT, CoeusActivity.SERVER_PORT);
            getActivity().startService(sIntent);
        } else {
            Toast.makeText(getActivity(), "Wifi P2P info missing", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        Log.d(DeviceDetailFragment.TAG, "onConnectionInfoAvailable");
        this.pInfo = info;
        updateNetworkDetail(this.pInfo);

        if (info.groupFormed && info.isGroupOwner) {
            Toast.makeText(getActivity(), "FileServer started", Toast.LENGTH_SHORT).show();
            new FileServerAsyncTask(getActivity()).execute();
        }
    }

    public void setDevice(DeviceInfo device) {
        this.device = device;
    }

    public static class FileServerAsyncTask extends AsyncTask<Void, Void, String> {
        private Context ctx;

        public FileServerAsyncTask(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                ServerSocket ss = new ServerSocket(CoeusActivity.SERVER_PORT);
                Socket sock = ss.accept();
                final File f = new File(Environment.getExternalStorageDirectory() + "/"
                        + ctx.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
                        + ".jpg");
                File dirs = new File(f.getParent());
                if (!dirs.exists()) {
                    if (!dirs.mkdirs()) {
                        Log.d(DeviceDetailFragment.TAG, "Directory not created");
                    }
                }
                if (!f.createNewFile()) {
                    Log.d(DeviceDetailFragment.TAG, "New file creation failed");
                }
                InputStream is = sock.getInputStream();
                ContentTransferService.transferContent(is, new FileOutputStream(f));
                ss.close();
                return f.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                Toast.makeText(this.ctx, "File stored: " + s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
