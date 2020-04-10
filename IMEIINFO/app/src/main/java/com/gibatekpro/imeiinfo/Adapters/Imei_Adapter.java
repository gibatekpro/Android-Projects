package com.gibatekpro.imeiinfo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gibatekpro.imeiinfo.ListItems.Imei_list_item;
import com.gibatekpro.imeiinfo.R;

import java.util.List;

public class Imei_Adapter extends RecyclerView.Adapter<Imei_Adapter.CustomImeiViewHolder> {

    private List<Imei_list_item> imei_list_items;
    private Context context;
    private OnItemClickListener mlistener;

    public Imei_Adapter(List<Imei_list_item> imei_list_items, Context context) {
        this.imei_list_items = imei_list_items;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomImeiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.imei_list_layout, viewGroup, false);

        return new CustomImeiViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomImeiViewHolder customImeiViewHolder, int i) {

        Imei_list_item customImeiListItem = imei_list_items.get(i);

        customImeiViewHolder.imei_item.setText(customImeiListItem.getImei());

    }

    @Override
    public int getItemCount() {
        return imei_list_items.size();
    }

    public interface OnItemClickListener {
        void onCopyClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener){
        mlistener = listener;
    }

    static class CustomImeiViewHolder extends RecyclerView.ViewHolder {

        TextView imei_item;
        private ImageButton copy;


        CustomImeiViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            imei_item = itemView.findViewById(R.id.item_display);
            copy = itemView.findViewById(R.id.copy);

            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCopyClick(position);
                        }
                    }
                }
            });

        }
    }


}
