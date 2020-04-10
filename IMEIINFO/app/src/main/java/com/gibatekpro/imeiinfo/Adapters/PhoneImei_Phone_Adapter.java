package com.gibatekpro.imeiinfo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gibatekpro.imeiinfo.ListItems.PhoneImei_Phone_list_item;
import com.gibatekpro.imeiinfo.R;

import java.util.List;

public class PhoneImei_Phone_Adapter extends RecyclerView.Adapter<PhoneImei_Phone_Adapter.PhonesViewHolder> {

    public PhoneImei_Phone_Adapter(List<PhoneImei_Phone_list_item> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    List<PhoneImei_Phone_list_item>listItems;
    Context context;
    private OnItemClickListener mlistener;

    @NonNull
    @Override
    public PhonesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.phone_imei_phones_list_layout, viewGroup, false);

        return new PhonesViewHolder(view, mlistener);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener){
        mlistener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull PhonesViewHolder phonesViewHolder, int i) {

        final PhoneImei_Phone_list_item device_listItem = listItems.get(i);

        phonesViewHolder.phone.setText(device_listItem.getPhone());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class PhonesViewHolder extends RecyclerView.ViewHolder {

        public TextView phone;

        PhonesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            phone = itemView.findViewById(R.id.phones);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }

    }

}

