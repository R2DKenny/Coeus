package com.r2starbase.apo11o.coeus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Originally created by apo11o on 2/1/16.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListHolder> {
    private List<DeviceInfo> deviceList;

    public DeviceListAdapter(List<DeviceInfo> dList) {
        deviceList = dList;
    }

    @Override
    public DeviceListAdapter.DeviceListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_card, parent, false);
        return new DeviceListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeviceListHolder holder, int position) {
        DeviceInfo di = this.deviceList.get(position);
        holder.nameView.setText(di.getDeviceName());
        holder.aliveFlagView.setText(di.isAlive() ? "Alive" : "Dead");
        holder.lastAliveView.setText(di.getLastAlive().toString());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class DeviceListHolder extends RecyclerView.ViewHolder {
        protected TextView nameView;
        protected Button aliveFlagView;
        protected TextView lastAliveView;

        public DeviceListHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.device_name);
            aliveFlagView = (Button) itemView.findViewById(R.id.device_alive_flag);
            lastAliveView = (TextView) itemView.findViewById(R.id.device_last_alive);
        }
    }
}
