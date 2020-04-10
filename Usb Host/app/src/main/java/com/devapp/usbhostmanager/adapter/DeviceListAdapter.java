package com.devapp.usbhostmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devapp.usbhostmanager.R;
import com.devapp.usbhostmanager.models.DeviceModel;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    private List<DeviceModel> deviceModelList;
    private Context mContext;

    public DeviceListAdapter(List<DeviceModel> deviceModelList, Context mContext) {
        this.deviceModelList = deviceModelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DeviceModel deviceModel = deviceModelList.get(i);
        viewHolder.title.setText(deviceModel.getProductName());
        viewHolder.subtitle.setText(deviceModel.getDeviceName());
        viewHolder.meta.setText(deviceModel.getVersion());
    }

    @Override
    public int getItemCount() {
        return deviceModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, subtitle, meta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            subtitle = itemView.findViewById(R.id.subtitleTextView);
            meta = itemView.findViewById(R.id.metaTextView);
        }
    }
}
