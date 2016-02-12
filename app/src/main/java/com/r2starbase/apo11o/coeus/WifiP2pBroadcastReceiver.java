package com.r2starbase.apo11o.coeus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Originally created by apo11o on 2/4/16.
 */
public class WifiP2pBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "WifiP2pBRecv";
    private WifiP2pManager pManager;
    private WifiP2pManager.Channel pChannel;
    private CoeusActivity pActivity;

    public WifiP2pBroadcastReceiver(WifiP2pManager pManager,
                                    WifiP2pManager.Channel pChannel,
                                    CoeusActivity pActivity) {
        this.pManager = pManager;
        this.pChannel = pChannel;
        this.pActivity = pActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Log.d(WifiP2pBroadcastReceiver.TAG, "Wifi P2P Enabled");
                this.pActivity.setIsWifiP2pEnabled(true);
            } else {
                Log.d(WifiP2pBroadcastReceiver.TAG, "Wifi P2P Disabled");
                this.pActivity.setIsWifiP2pEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.d(WifiP2pBroadcastReceiver.TAG, "Wifi Peer Changed");
            if (pManager != null) {
                pManager.requestPeers(pChannel, (WifiP2pManager.PeerListListener) pActivity
                        .getFragmentManager().findFragmentByTag(DeviceListFragment.TAG));
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.d(WifiP2pBroadcastReceiver.TAG, "Wifi Connection Changed");
            if (pManager != null) {
                NetworkInfo ni = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (ni.isConnected()) {
                    Log.d(WifiP2pBroadcastReceiver.TAG, "isConnected check passed");
                    DeviceDetailFragment ddf = (DeviceDetailFragment) pActivity
                            .getFragmentManager().findFragmentByTag(DeviceDetailFragment.TAG);
                    Log.d(WifiP2pBroadcastReceiver.TAG, "Is null: " + (ddf == null));
                    pManager.requestConnectionInfo(pChannel, ddf);
                }
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.d(WifiP2pBroadcastReceiver.TAG, "Wifi Device Changed");
        }
    }
}
