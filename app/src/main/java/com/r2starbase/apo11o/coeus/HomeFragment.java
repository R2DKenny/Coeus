package com.r2starbase.apo11o.coeus;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Originally created by apo11o on 2/1/16.
 */
public class HomeFragment extends Fragment {
    public final static String TAG = "HomeFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(HomeFragment.TAG, "onCreateView");
        return inflater.inflate(R.layout.home_fragment, container, false);
    }
}
