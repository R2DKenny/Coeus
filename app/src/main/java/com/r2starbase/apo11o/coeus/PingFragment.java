package com.r2starbase.apo11o.coeus;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apo11o.
 */
public class PingFragment extends Fragment {
    private RecyclerView ladView = null;
    private RecyclerView.LayoutManager ladLayoutManager = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ping_fragment, container, false);
        ladView = (RecyclerView) root.findViewById(R.id.ping_view);

        ladView.setHasFixedSize(true);

        ladLayoutManager = new LinearLayoutManager(getActivity());
        ladView.setLayoutManager(ladLayoutManager);

        List<DeviceInfo> dataset = generateDummyData();
        ladView.setAdapter(new DeviceListAdapter(dataset));

        return root;
    }

    private List<DeviceInfo> generateDummyData() {
        List<DeviceInfo> dummy = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            dummy.add(new DeviceInfo(Integer.toString(i)));
        }
        return dummy;
    }
}
