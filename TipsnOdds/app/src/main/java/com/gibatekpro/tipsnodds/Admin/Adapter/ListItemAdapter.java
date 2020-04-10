package com.gibatekpro.tipsnodds.Admin.Adapter;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gibatekpro.tipsnodds.Admin.Model.ToDo;
import com.gibatekpro.tipsnodds.Admin.frag_pending;
import com.gibatekpro.tipsnodds.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{

    ItemClickListener itemClickListener;
    ImageView flags;
    CircleImageView check_icon;
    TextView countrys, time, date, home, vs, away, tips, odds;

    public ListItemViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        check_icon = itemView.findViewById(R.id.check_icon);
        flags = itemView.findViewById(R.id.flags);
        countrys = itemView.findViewById(R.id.countrys);
        time = itemView.findViewById(R.id.time);
        date = itemView.findViewById(R.id.date);
        home = itemView.findViewById(R.id.home);
        vs = itemView.findViewById(R.id.vs);
        away = itemView.findViewById(R.id.away);
        tips = itemView.findViewById(R.id.tips);
        odds = itemView.findViewById(R.id.odds);

    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(),"DELETE PENDING");
        contextMenu.add(0,1,getAdapterPosition(),"MOVE PENDING TO PLAYED");

    }
}

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder>{


    frag_pending mainActivity;
    List <ToDo>  todoList;

    public ListItemAdapter(frag_pending mainActivity, List<ToDo> todoList) {
        this.mainActivity = mainActivity;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainActivity.getActivity());
        View view  = inflater.inflate(R.layout.custom_row,parent,false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {

        //set data for item
        holder.check_icon.setImageResource(todoList.get(position).getCheck_icon());
        holder.flags.setImageResource(todoList.get(position).getFlag());
        holder.countrys.setText(todoList.get(position).getCountry());
        holder.date.setText(todoList.get(position).getDate());
        holder.home.setText(todoList.get(position).getHome());
        holder.vs.setText(todoList.get(position).getVs());
        holder.away.setText(todoList.get(position).getAway());
        holder.tips.setText(todoList.get(position).getTip());
        holder.time.setText(todoList.get(position).getTime());
        holder.odds.setText(todoList.get(position).getOdd());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                //When user select item, data will auto set for Edit Text View
                //mainActivity.flags.setImageResource(todoList.get(position).getFlag());
                mainActivity.country_edit.setText(todoList.get(position).getCountry());
                mainActivity.date_edit.setText(todoList.get(position).getDate());
                mainActivity.home_club_edit.setText(todoList.get(position).getHome());
                //mainActivity.vs.setText(todoList.get(position).getVs());
                mainActivity.away_club_edit.setText(todoList.get(position).getAway());
                mainActivity.tip_edit.setText(todoList.get(position).getTip());
                mainActivity.time_edit.setText(todoList.get(position).getTime());
                mainActivity.odds_edit.setText(todoList.get(position).getOdd());

                frag_pending.isUpdate = true; //set flag isUpdate to true
                frag_pending.idUpdate = todoList.get(position).getId();

            }
        });

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
