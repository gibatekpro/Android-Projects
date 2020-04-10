package com.gibatekpro.imeiinfo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gibatekpro.imeiinfo.ListItems.AnalyzeImei_list_item;
import com.gibatekpro.imeiinfo.R;

import java.util.List;

public class AnalyzeImei_Adapter extends RecyclerView.Adapter<AnalyzeImei_Adapter.AnalyzeImeiViewHolder> {

    public AnalyzeImei_Adapter(Context context, List<AnalyzeImei_list_item> analyzerListItems) {
        this.context = context;
        this.analyzerListItems = analyzerListItems;
    }

    private Context context;
    private List<AnalyzeImei_list_item> analyzerListItems;

    @NonNull
    @Override
    public AnalyzeImeiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.analyze_imei_list_layout, viewGroup, false);

        return new AnalyzeImeiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyzeImeiViewHolder analyzeImeiViewHolder, int i) {

        AnalyzeImei_list_item analyzer_listItem = analyzerListItems.get(i);

        analyzeImeiViewHolder.parts.setText(analyzer_listItem.getParts());
        analyzeImeiViewHolder.partsValue.setText(analyzer_listItem.getPartsValue());

    }

    @Override
    public int getItemCount() {
        return analyzerListItems.size();
    }

    public static class AnalyzeImeiViewHolder extends RecyclerView.ViewHolder {

        public TextView parts, partsValue;

        public AnalyzeImeiViewHolder(@NonNull View itemView) {
            super(itemView);

            parts = itemView.findViewById(R.id.parts);
            partsValue = itemView.findViewById(R.id.partsValue);

        }
    }
}
