package com.gibatekpro.stakeapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private Context context;
    private List<DataList> dataList;

    public DataAdapter(Context context, List<DataList> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.itemview, null, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder dataViewHolder, int i) {

        DataList dataLists = dataList.get(i);

        dataViewHolder.roundv.setText(String.valueOf(dataLists.getdRound()));
        dataViewHolder.stakev.setText(String.valueOf(dataLists.getdStake()));
        dataViewHolder.oddv.setText(String.valueOf(dataLists.getdOdd()));
        dataViewHolder.returnsv.setText(String.valueOf(dataLists.getdReturns()));
        dataViewHolder.profitv.setText(String.valueOf(dataLists.getdProfit()));


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        TextView roundv, stakev, oddv, returnsv, profitv;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            roundv = itemView.findViewById(R.id.vRound);
            stakev = itemView.findViewById(R.id.vStake);
            oddv = itemView.findViewById(R.id.vOdd);
            returnsv = itemView.findViewById(R.id.vReturns);
            profitv = itemView.findViewById(R.id.vProfit);

        }
    }

}
