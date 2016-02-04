package com.r2starbase.apo11o.coeus;

import android.app.Fragment;
import android.net.wifi.p2p.WifiP2pDevice;
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
public class DeviceListFragment extends Fragment {
    public final static String TAG = "DeviceListFragment";
    private SwipeRefreshLayout srLayout;
    private DeviceListAdapter dlAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ping_fragment, container, false);
        RecyclerView ladView = (RecyclerView) root.findViewById(R.id.ping_view);

        ladView.setHasFixedSize(true);

        ladView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dlAdapter = new DeviceListAdapter(generateDummyData(3));
        ladView.setAdapter(dlAdapter);

        srLayout = (SwipeRefreshLayout) root.findViewById(R.id.ping_layout);
        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dlAdapter.clear();
                dlAdapter.addAll(generateDummyData(5));
                srLayout.setRefreshing(false);
            }
        });

        Log.d(DeviceListFragment.TAG, "onCreateView");

        return root;
    }

    private List<DeviceInfo> generateDummyData(int count) {
        List<DeviceInfo> dummy = new ArrayList<>();
        dummy.add(new DeviceInfo(new WifiP2pDevice()));
        for (int i = 0; i < count; ++i) {
            DeviceInfo newDi = new DeviceInfo();
            newDi.setDeviceName("Dummy");
            newDi.setDeviceAddress("blah");
            newDi.setDeviceStatus(WifiP2pDevice.UNAVAILABLE);
            dummy.add(newDi);
        }
        return dummy;
    }
}
