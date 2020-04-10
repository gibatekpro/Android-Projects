package com.gibatekpro.imeigenerator.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gibatekpro.imeigenerator.ListItems.PS_Models_list_item;
import com.gibatekpro.imeigenerator.R;

import java.util.List;

public class PS_Models_Adapter extends RecyclerView.Adapter<PS_Models_Adapter.ModelViewHolder> {

    private List<PS_Models_list_item> listItems;
    private Context context;
    private OnItemClickListener mClickListener;

    public PS_Models_Adapter(List<PS_Models_list_item> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);

    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener){
        mClickListener = onItemClickListener;
    }



    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ps_models_list_layout, viewGroup, false);

        return new ModelViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder modelViewHolder, int i) {

        PS_Models_list_item model_listItem = listItems.get(i);

        modelViewHolder.model.setText(model_listItem.getModel());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ModelViewHolder extends RecyclerView.ViewHolder {

        public TextView model;

        ModelViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            model = itemView.findViewById(R.id.model);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                        }
                    }

                }
            });
        }

    }


}
