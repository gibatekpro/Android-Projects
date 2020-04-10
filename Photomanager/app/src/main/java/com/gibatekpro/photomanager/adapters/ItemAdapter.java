package com.gibatekpro.photomanager.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gibatekpro.photomanager.Item;
import com.gibatekpro.photomanager.R;
import com.gibatekpro.photomanager.ui.MainActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

//import io.realm.Realm;
public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Item> itemList;
    private TreeSet<Integer> adList = new TreeSet<Integer>();

    ///Realm realm;
    //RealmHelper realmHelper;

    private Context context;

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_MAX_COUNT = 3;
    public static final int AD_POSITION = 8;

    private boolean selectFlag = false;

    public ItemAdapter(ArrayList<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    //item view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, description, author;//,comments;
        public ImageView thumbnail, fav;
        public ImageButton menu;
        public CheckBox selected;
        public CardView cardView;
        public ArrayList<Object> defaultBg = new ArrayList<>(4);

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardView);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            author = (TextView) view.findViewById(R.id.author);
            //comments = (TextView) view.findViewById(R.id.author);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            fav = (ImageView) view.findViewById(R.id.fav_);
            selected = (CheckBox) view.findViewById(R.id.select);
            selected.setOnClickListener(this);

            defaultBg.add(title.getTextColors());
            defaultBg.add(description.getTextColors());
            defaultBg.add(author.getTextColors());
            defaultBg.add(cardView.getBackground());
            //menu = (ImageButton) view.findViewById(R.id._menu);
            //menu.setOnClickListener(this);
            //nativeAdView = (NativeExpressAdView) view.findViewById(R.id.adview);
        }

        @Override
        public void onClick(View view) {
            boolean selected = ((CheckBox) view).isChecked();
            itemList.get(getAdapterPosition()).setSelected(selected);
            if (!selectFlag)
                updateItem(itemList.get(getAdapterPosition()), getAdapterPosition());
            ((MainActivity) context).selectionStarted(view, selected, getAdapterPosition());

        }
    }

    //ad view holder
    public class adViewHolder extends RecyclerView.ViewHolder {
        //public AdView nativeAdView;
        public NativeExpressAdView nativeAdView;

        public adViewHolder(final View view) {
            super(view);
            //nativeAdView = (AdView) view.findViewById(R.id.adview);
            //nativeAdView = (NativeExpressAdView) view.findViewById(R.id.adViewN);
            nativeAdView = new NativeExpressAdView(context);
            nativeAdView.setAdUnitId("ca-app-pub-1488497497647050/4882905929");//("ca-app-pub-5688626796086116/5482071185");
            final View c = view.findViewById(R.id.cardAdView);
            view.post(new Runnable() {
                @Override
                public void run() {
                    int adHeight = 135;
                    float density = context.getResources().getDisplayMetrics().density;
                    final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) c.getLayoutParams();
                    c.getLayoutParams().height = (int) ((adHeight + lp.topMargin + lp.bottomMargin) * density);
                    c.setLayoutParams(c.getLayoutParams());
//                    int a = c.getWidth();
//                    float b = c.getWidth() / density;
//                    int d = lp.leftMargin;
                    int adwidth = (int) (c.getWidth() / density) - 10;
                    adwidth = adwidth < 10 ? 280 : adwidth > 1200 ? 1280 : adwidth;
                    AdSize size = new AdSize(
                            adwidth, adHeight
                    );
                    nativeAdView.setAdSize(size);
                    nativeAdView.loadAd(new AdRequest.Builder().addTestDevice("E9C1DEC2F04C63973F213FD83AAEFEC8").build());
                }
            });

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Item.TYPE_AD:
                final View adItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_layout, parent, false);
                return new adViewHolder(adItemView);
            case Item.TYPE_ITEM:
            default:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
                return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Item.TYPE_AD:

                adViewHolder adViewHolder = (ItemAdapter.adViewHolder) holder;
                NativeExpressAdView nativeExpressAdView = (NativeExpressAdView) adViewHolder.nativeAdView;

                ViewGroup card = (ViewGroup) adViewHolder.itemView;
                card.removeAllViews();

                if (nativeExpressAdView.getParent() != null) {
                    ((ViewGroup) nativeExpressAdView.getParent()).removeView(nativeExpressAdView);
                }

                card.addView(nativeExpressAdView);
//                final adViewHolder adViewHolder = (adViewHolder) holder;
//                AdRequest request = new AdRequest.Builder()
//                        .addTestDevice("E9C1DEC2F04C63973F213FD83AAEFEC8")
//                        .build();
//                adViewHolder.nativeAdView.loadAd(request);
                break;
            case Item.TYPE_ITEM:
            default:
                final ViewHolder viewHolder = (ViewHolder) holder;
                Item item = itemList.get(position);
                viewHolder.title.setText(item.getImageFileName());
                ViewCompat.setTransitionName(viewHolder.description, item.getImageFolder());
                viewHolder.description.setText(item.getImageFolder().replace("/storage/emulated/0", "Phone:/"));
                viewHolder.author.setText(formatSize(item.getImageSize()));
                selectFlag = true;
                viewHolder.selected.setChecked(item.isSelected());
                selectFlag = false;
                String imgL = item.getAbsolutePathOfImage();
                if (imgL != null && !imgL.isEmpty()) {
                    viewHolder.thumbnail.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(Uri.fromFile(new File(imgL))).resize(100, 100).centerCrop().placeholder(R.mipmap.ic_launcher).into(viewHolder.thumbnail);

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Picasso.with(context).load(Uri.fromFile(new File(item.getAbsolutePathOfImage()))).resize(200,200).centerInside().fetch();
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Picasso.with(context).load(Uri.fromFile(new File(imgL))).fetch();
//                    }
                } else {
                    viewHolder.thumbnail.setVisibility(View.GONE);
                }
                if (item.isDuplicate()) {
                    viewHolder.fav.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(R.drawable.dupl).fit().into(viewHolder.fav);
                } else {
                    viewHolder.fav.setVisibility(View.GONE);
                }
                if (((MainActivity) context).multiSelectMode) {
                    viewHolder.selected.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.selected.setVisibility(View.GONE);
                }
                if (item.isSelected()) {
                    viewHolder.title.setTextColor(ContextCompat.getColor(this.context, R.color.white));
                    viewHolder.description.setTextColor(ContextCompat.getColor(this.context, R.color.white));
                    viewHolder.author.setTextColor(ContextCompat.getColor(this.context, R.color.white));
                    viewHolder.cardView.setBackgroundColor(ContextCompat.getColor(this.context, R.color.selected));
                } else {
                    viewHolder.title.setTextColor((ColorStateList) viewHolder.defaultBg.get(0));
                    viewHolder.description.setTextColor((ColorStateList) viewHolder.defaultBg.get(1));
                    viewHolder.author.setTextColor((ColorStateList) viewHolder.defaultBg.get(2));
                    viewHolder.cardView.setBackground((Drawable) viewHolder.defaultBg.get(3));
                }
                break;
        }
    }

    private String formatSize(long imageSize) {
        return android.text.format.Formatter.formatFileSize(context, imageSize);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public Item getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        Item item = itemList.get(position);
        return item.getType();
    }

    public void updateItem(Item item, int position) {
        itemList.set(position, item);
        notifyItemChanged(position);
        //realmHelper.update(item);
    }

    public void addItem(Item item) {
        itemList.add(item);
        notifyItemInserted(itemList.size());
    }

    public void addItem(Item item, int position) {
        itemList.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, itemList.size());
        //realmHelper.update(item);
    }

    public void removeItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
        //item.setAction(Item.DELETED);
        //realmHelper.update(item);
    }


    private String getTimeDiff(long time) {
        long uptime = System.currentTimeMillis() - time;

        long days = TimeUnit.MILLISECONDS
                .toDays(uptime);
        if (days > 0) {
            return (days == 1 ? "yesterday" : days + " days ago");
        }
        uptime -= TimeUnit.DAYS.toMillis(days);

        long hours = TimeUnit.MILLISECONDS
                .toHours(uptime);
        if (hours > 0) {
            return hours == 1 ? "An hour ago" : hours + " hours ago";
        }
        uptime -= TimeUnit.HOURS.toMillis(hours);

        long minutes = TimeUnit.MILLISECONDS
                .toMinutes(uptime);
        if (minutes > 0) {
            return minutes == 1 ? "A minute ago" : minutes + " minutes ago";
        }
        uptime -= TimeUnit.MINUTES.toMillis(minutes);

        long seconds = TimeUnit.MILLISECONDS
                .toSeconds(uptime);
        return seconds < 3 ? "Now" : seconds + " seconds ago";
    }

    private int getColor(int read) {
        if (read == Item.READ)
            return R.color.read;
        return R.color.unread;
    }
}
