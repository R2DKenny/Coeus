package com.r2starbase.apo11o.coeus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by apo11o.
 */
public class LocalAreaDeviceHandler extends Fragment {
    static LocalAreaDeviceHandler instance = null;

    public static Fragment newInstance(Context ctx) {
        if (instance == null) {
            instance = new LocalAreaDeviceHandler();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
