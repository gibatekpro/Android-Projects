package com.gibatekpro.tipsnodds.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gibatekpro.tipsnodds.ListItems.ListItem_pending;
import com.gibatekpro.tipsnodds.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gibah on 12/13/2017.
 */

public class MyAdapter_pending extends RecyclerView.Adapter<MyAdapter_pending.ViewHolder> {

    private List<ListItem_pending> listItems;
    private Context context;

    public MyAdapter_pending(List<ListItem_pending> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    private static int TYPE_AD = 1;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d("Tony", "onCreateViewHolder: " + viewType);

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {



        ListItem_pending listItem = listItems.get(position);

        holder.imageViewcheck_icon.setImageResource(listItem.getCheck_icon());
        holder.imageViewflag.setImageResource(listItem.getFlag());
        holder.textViewcountry.setText(listItem.getCountry());
        holder.textViewdate.setText(listItem.getDate());
        holder.textViewhome.setText(listItem.getHome());
        holder.textViewvs.setText(listItem.getVs());
        holder.textViewaway.setText(listItem.getAway());
        holder.textViewtip.setText(listItem.getTip());
        holder.textViewtime.setText(listItem.getTime());
        holder.textViewodd.setText(listItem.getOdd());
        //holder.image.setImageResource(listItem.getIcon());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView imageViewcheck_icon;
        public ImageView imageViewflag;
        public TextView textViewcountry;
        public TextView textViewdate;
        public TextView textViewhome;
        public TextView textViewvs;
        public TextView textViewaway;
        public TextView textViewtip;
        public TextView textViewtime;
        public TextView textViewodd;
        //public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);


            imageViewcheck_icon = itemView.findViewById(R.id.check_icon);
            imageViewflag = itemView.findViewById(R.id.flags);
            textViewcountry = itemView.findViewById(R.id.countrys);
            textViewdate = itemView.findViewById(R.id.date);
            textViewhome = itemView.findViewById(R.id.home);
            textViewvs = itemView.findViewById(R.id.vs);
            textViewaway = itemView.findViewById(R.id.away);
            textViewtip = itemView.findViewById(R.id.tips);
            textViewtime = itemView.findViewById(R.id.time);
            textViewodd = itemView.findViewById(R.id.odds);
            //image = itemView.findViewById(R.id.image);

        }
    }

}
