package com.r2starbase.apo11o.coeus;

import android.app.Fragment;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apo11o.
 */
public class DeviceListFragment extends Fragment implements WifiP2pManager.PeerListListener{
    public final static String TAG = "DeviceListFragment";
    private SwipeRefreshLayout srLayout;
    private DeviceListAdapter dlAdapter;
    private List<DeviceInfo> dList = new ArrayList<>();

    public interface DeviceListListener {
        void startDiscovery();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ping_fragment, container, false);
        RecyclerView ladView = (RecyclerView) root.findViewById(R.id.ping_view);

        ladView.setHasFixedSize(true);

        ladView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dlAdapter = new DeviceListAdapter(dList);
        ladView.setAdapter(dlAdapter);

        srLayout = (SwipeRefreshLayout) root.findViewById(R.id.ping_layout);
        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((DeviceListListener)getActivity()).startDiscovery();
            }
        });

        Log.d(DeviceListFragment.TAG, "onCreateView");

        return root;
    }

    public void stopRefresh() {
        srLayout.setRefreshing(false);
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        reloadPeers(peers);
    }

    public void reloadPeers(WifiP2pDeviceList newList) {
        List<DeviceInfo> tempList = new ArrayList<>();
        for (WifiP2pDevice dev: newList.getDeviceList()) {
            tempList.add(new DeviceInfo(dev));
        }
        dlAdapter.clear();
        dlAdapter.addAll(tempList);
    }

}
