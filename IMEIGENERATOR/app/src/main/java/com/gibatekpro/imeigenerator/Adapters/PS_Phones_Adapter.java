package com.gibatekpro.imeigenerator.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gibatekpro.imeigenerator.ListItems.PS_Phones_list_item;
import com.gibatekpro.imeigenerator.R;

import java.util.List;

public class PS_Phones_Adapter extends RecyclerView.Adapter<PS_Phones_Adapter.PhonesViewHolder> {

    public PS_Phones_Adapter(List<PS_Phones_list_item> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    List<PS_Phones_list_item>listItems;
    Context context;
    private OnItemClickListener mlistener;

    @NonNull
    @Override
    public PhonesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.ps_phones_list_layout, viewGroup, false);

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

        final PS_Phones_list_item device_listItem = listItems.get(i);

        phonesViewHolder.phone.setText(device_listItem.getPhone());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class PhonesViewHolder extends RecyclerView.ViewHolder {

        public TextView phone;

        public PhonesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            phone = itemView.findViewById(R.id.phone);

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

