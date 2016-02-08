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
public class DeviceDetailFragment extends Fragment implements WifiP2pManager.ConnectionInfoListener{
    static final int FILE_CHOOSE_CODE = 1;
    public final static String TAG = "DeviceDetailFragment";
    private View detailView;
    private DeviceInfo device;
    private WifiP2pInfo pInfo;

    public interface DeviceDetailListener {
        void connect(WifiP2pConfig config);
        void disconnect();
        void onChannelDisconnect();
        void cancelDisconnect();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        detailView = inflater.inflate(R.layout.device_detail_fragment, container, false);
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
                intent.setType("*/*");
                startActivityForResult(intent, DeviceDetailFragment.FILE_CHOOSE_CODE);
            }
        });
        TextView tv = (TextView)detailView.findViewById(R.id.device_detail_name);
        tv.setText(this.device.getDeviceName());
        tv = (TextView)detailView.findViewById(R.id.device_detail_status);
        tv.setText(this.device.getDeviceStatusMsg());
        tv = (TextView)detailView.findViewById(R.id.device_detail_other);
        tv.setText(device.getDevice().toString());
        return detailView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = data.getData();
        Intent sIntent = new Intent(getActivity(), ContentTransferService.class);
        sIntent.setAction(ContentTransferService.ACTION_SEND_FILE);
        sIntent.putExtra(ContentTransferService.EXTRAS_FILE_PATH, uri.toString());
        sIntent.putExtra(ContentTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
                pInfo.groupOwnerAddress.getAddress());
        sIntent.putExtra(ContentTransferService.EXTRAS_GROUP_OWNER_PORT, 8988);
        Toast.makeText(getActivity(), "Sending: " + uri.toString(), Toast.LENGTH_SHORT).show();
        getActivity().startService(sIntent);
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        this.pInfo = info;

        if (info.groupFormed && info.isGroupOwner) {
            new FileServerAsyncTask(getActivity()).execute();
        }
    }

    public void setupDevice(DeviceInfo device) {
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
                ServerSocket ss = new ServerSocket(8988);
                Socket sock = ss.accept();
                final File f = new File(Environment.getExternalStorageDirectory() + "/"
                        + ctx.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
                        + ".jpg");
                File dirs = new File(f.getParent());
                if (!dirs.exists()) {
                    dirs.mkdirs();
                }
                f.createNewFile();
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
