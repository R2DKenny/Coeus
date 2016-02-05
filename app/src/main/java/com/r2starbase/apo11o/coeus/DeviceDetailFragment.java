package com.r2starbase.apo11o.coeus;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Originally created by apo11o on 2/4/16.
 */
public class DeviceDetailFragment extends Fragment {
    public final static String TAG = "DeviceDetailFragment";
    private DeviceInfo device;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_detail_fragment, container, false);
    }
}
