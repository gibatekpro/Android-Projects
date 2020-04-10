package com.devappliance.devapplibrary.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class ItemsRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context mContext;
    public ArrayList<T> itemsList;
    private RecyclerView mRecyclerView;

    public ItemsRecyclerViewAdapter(Context mContext, ArrayList<T> itemsList) {
        this.mContext = mContext;
        this.itemsList = itemsList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    private T removeItem(int position) {
        T item = itemsList.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    private void insertItem(int position, T item) {
        itemsList.add(position, item);
        notifyItemInserted(position);
    }
}