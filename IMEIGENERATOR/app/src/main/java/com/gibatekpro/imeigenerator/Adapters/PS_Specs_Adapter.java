package com.gibatekpro.imeigenerator.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gibatekpro.imeigenerator.ListItems.PS_Specs_list_item;
import com.gibatekpro.imeigenerator.R;

import java.util.List;

public class PS_Specs_Adapter extends RecyclerView.Adapter<PS_Specs_Adapter.SpecsViewHolder> {

    public PS_Specs_Adapter(Context context, List<PS_Specs_list_item> specs_listItems) {
        this.context = context;
        this.specs_listItems = specs_listItems;
    }

    Context context;
    List<PS_Specs_list_item> specs_listItems;

    @NonNull
    @Override
    public  SpecsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ps_specs_list_layout, viewGroup, false);

        return new SpecsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecsViewHolder specsViewHolder, int i) {

        final PS_Specs_list_item specs_listItem = specs_listItems.get(i);

        specsViewHolder.specs.setText(specs_listItem.getSpecs());
        specsViewHolder.specsValue.setText(specs_listItem.getSpecsValue());

    }

    @Override
    public int getItemCount() {
        return specs_listItems.size();
    }

    static class SpecsViewHolder extends RecyclerView.ViewHolder {

        TextView specs, specsValue;

        SpecsViewHolder(@NonNull View itemView) {
            super(itemView);

            specs = itemView.findViewById(R.id.specs);
            specsValue = itemView.findViewById(R.id.specsValue);

        }
    }


}
