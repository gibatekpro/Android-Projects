package com.gibatekpro.photomanager.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.gibatekpro.photomanager.BaseApp;
import com.gibatekpro.photomanager.BuildConfig;
import com.gibatekpro.photomanager.Item;
import com.gibatekpro.photomanager.R;
import com.gibatekpro.photomanager.adapters.ItemAdapter;
import com.gibatekpro.photomanager.helpers.ItemClickSupport;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

//import io.realm.DynamicRealm;
//import io.realm.Realm;
//import io.realm.RealmConfiguration;
//import io.realm.RealmMigration;

public class MainActivity extends BaseApp {

    private static final int REQUEST_CODE = 12345;
    private static final String TAG = "NLAND";
    private static final int READ_STORAGE = 10987;
    private static final int VIEW_ACT = 63545;
    private InterstitialAd m_InterstitialAd;

    int ad_delay = 0;
    private static final int AD_TIMES = 3;

    private static final String REQUEST_TAG = "jot";
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManger;
    private ItemAdapter items_adapter;
    private ArrayList<Item> listItem = new ArrayList<>();
    // private RequestQueue queue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout error;
    //  ProgressBar pBar;

    //Realm realm;
    //RealmHelper helper;
    private Paint p = new Paint();
    private View view;
    private int action;
    private String params;

    private int sortOrder = SORT_DESCENDING;
    private int sortType = SORT_NONE;

    private static final int SORT_NONE = 0;
    private static final int SORT_NAME = 1;
    private static final int SORT_DATE = 2;
    private static final int SORT_FOLDER = 3;
    private static final int SORT_DUPLICATE = 4;

    private static final int SORT_ASCENDING = 1;
    private static final int SORT_DESCENDING = -1;

    public static final int ACTION_NONE = 0;
    public static final int ACTION_SHARE = 1;
    public static final int ACTION_OPEN = 2;


    //private BackgroundAlarm alarm = new BackgroundAlarm();
    public boolean multiSelectMode = false;
    private int selectedCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG) {
            secure(R.string.sign, R.string.salt);
        }

        //interstitial ads
        initInterstitialAds("ca-app-pub-1488497497647050/2847769522", "E9C1DEC2F04C63973F213FD83AAEFEC8");
        setContentView(R.layout.activity_main);
        initSupportToolbar(R.id.toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listItem.size() > 1)
                    showMenu();
                else
                    showSnackMessage("Load your photos before accessing the menu.");
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.BLACK, getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //loadImages();
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_STORAGE);
            }
        });

        error = (RelativeLayout) findViewById(R.id.error);

        recyclerView = (RecyclerView) findViewById(R.id.content);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        items_adapter = new ItemAdapter(listItem, MainActivity.this);

        mLayoutManger = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManger);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(items_adapter);
        initSwipe();
        ItemClickSupport.addTo(recyclerView)
                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, final int position, View v) {
                        Item obj = listItem.get(position);
                        if (obj.getType() == Item.TYPE_AD)
                            return true;
                        multiSelectMode = true;
                        listItem.get(position).setSelected(true);
                        items_adapter.notifyDataSetChanged();
                        selectedCounter++;
                        selectionStarted();
                        return true;
                    }
                })
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Item obj = listItem.get(position);
                        if (obj.getType() == Item.TYPE_ITEM) {
                            viewFile(v, position);
                        }
                    }
                });
        Button refresh = (Button) findViewById(R.id.button_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInter();
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_STORAGE);
            }
        });

        initHelp();
    }

    @Override
    public void showHelp() {
        loadTutorial();
    }

    private void showInterDelay(int action, String params) {
        this.action = action;
        this.params = params;
        showInterstitialWithDelay();
    }

    private void showInterDelay() {
        this.action = ACTION_NONE;
        this.params = "";
        showInterstitialWithDelay();
    }

    private void showInter(int action, String params) {
        this.action = action;
        this.params = params;
        showInterstitial();
    }

    private void showInter() {
        this.action = ACTION_NONE;
        this.params = "";
        showInterstitial();
    }

    public void selectionStarted(View view, boolean checked, int adapterPosition) {
        if (checked) {
            selectedCounter++;
        } else {
            selectedCounter--;
        }
        if (selectedCounter > 0) {
            multiSelectMode = true;
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(selectedCounter + (selectedCounter > 1 ? " photos selected" : " photo selected"));
            if (selectedCounter == 1) {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.menu_selected);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } else {
            resetToolBar();
        }
    }

    public void selectionStarted() {
        if (selectedCounter > 0) {
            multiSelectMode = true;
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(selectedCounter + (selectedCounter > 1 ? " photos selected" : " photo selected"));
            if (selectedCounter == 1) {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
                }
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.menu_selected);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } else {
            resetToolBar();
        }
    }

    private void resetToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        multiSelectMode = false;
        items_adapter.notifyDataSetChanged();
        toolbar.setTitle(R.string.app_name);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void showMenu() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        final String[] menu = new String[]{"Sort photos", "Reload photos"};
        builderSingle.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selected = menu[i];
                switch (selected.toLowerCase()) {
                    case "sort photos":
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                        final String[] sort_type = new String[]{"None", "By name", "By date modified", "By folder", "By duplicate"};
                        builderSingle.setItems(sort_type, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                showInterDelay();
                                sortType = i;
                                sortOrder = sortOrder * -1;
                                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_STORAGE);
                            }
                        });
                        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builderSingle.show();
                        break;
                    default:
                        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_STORAGE);
                        break;
                }
            }
        });

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builderSingle.create();
        dialog.show();
    }

    private void deleteSelectedItems() {
        if (selectedCounter < 1) {
            showSnackMessage("Please, select photos to delete.");
            return;
        }
        int deleted, failed;
        //ArrayList<Integer> positions = new ArrayList<>();
        deleted = failed = 0;
//        for (int i = 0; i < listItem.size(); i++) {
//            Item item = listItem.get(i);
//            if (item.isSelected()) {
//                File file = new File(item.getAbsolutePathOfImage());
//                if (file.delete()) {
//                    deleted++;
//                    positions.add(i);
//                    getContentResolver().delete(Uri.fromFile(file),null,null);
//                } else {
//                    failed++;
//                }
//            }
//        }
        for (Iterator<Item> iterator = listItem.iterator(); iterator.hasNext(); ) {
            Item item = iterator.next();
            if (item.getType() == Item.TYPE_ITEM && item.isSelected()) {
                File file = new File(item.getAbsolutePathOfImage());
                if (file.delete()) {
                    deleted++;
                    //  positions.add(i);
                    //getContentResolver().delete(Uri.fromFile(file), null, null);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(file)));
                } else {
                    failed++;
                }
            }
        }
        getAllShownImagesPath(MainActivity.this);

        String message = deleted > 0 ? deleted == 1 ? "The photo was successfully deleted." : deleted + " photos deleted successfully." : "No photo was deleted.";
        AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
        message = message + (failed > 0 ? failed == 1 ? "\nFailed to delete 1 photo." : "\nFailed to delete " + failed + " photos." : "");
        builderInner.setMessage(message);
        builderInner.setTitle("Delete");
        builderInner.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showInterDelay();
            }
        });
        builderInner.show();
    }

    private void viewFile(View view, int position) {
        Item item = listItem.get(position);

        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
        intent.putExtra("image_path", item.getAbsolutePathOfImage());
        Pair<View, String> pair1 = new Pair<>(view.findViewById(R.id.thumbnail), getString(R.string.transition_name_name));
        // Pair<View, String> pair2 = new Pair<>(view.findViewById(R.id.title), getString(R.string.transition_name));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair1);
        ActivityCompat.startActivityForResult(MainActivity.this, intent, VIEW_ACT, options.toBundle());
    }

    private String getMessage(String path) {
        File file = new File(path);
        String name = file.getName();
        String ext = name.substring(name.lastIndexOf("."));
        String folder = file.getParent();
        String size = formatSize(file.length());
        String isHidden = String.valueOf(file.isHidden());
        String writable = String.valueOf(file.canWrite());
        String lastMod = millisecondsToDate(file.lastModified(), "yyyy-MM-dd HH:mm:ss");
        return "Name: " + name.replace(ext, "") + "\n\nPath: " + folder + "\n\nFile type: " + ext.replace(".", "").toUpperCase() + "\n\nSize: " + size + "\n\nHidden: " + toInitialCaps(isHidden) + "\n\nWritable: " + toInitialCaps(writable) + "\n\nLast Modified: " + lastMod;
    }

    private String formatSize(long imageSize) {
        return android.text.format.Formatter.formatFileSize(MainActivity.this, imageSize);
    }

    private void loadImages() {
        getAllShownImagesPath(this);
    }


    private void getAllShownImagesPath(Activity activity) {
        swipeRefreshLayout.setRefreshing(true);
        selectedCounter = 0;
        resetToolBar();

        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage, imageFileName, imageFolder = null;
        long imageSize;
        boolean duplicate = false;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        listItem.clear();
        while (cursor.moveToNext()) {
            File file = new File(cursor.getString(column_index_data));
            absolutePathOfImage = file.getAbsolutePath();
            imageFileName = file.getName();
            imageFolder = file.getParent();
            imageSize = file.length();
            for (Item item : listItem) {
                duplicate = false;
                if (item.getImageFileName().equals(imageFileName)) {
                    duplicate = true;
                    break;
                }
            }
            if (file.canRead())
                listItem.add(new Item(imageFileName, imageFolder, absolutePathOfImage, imageSize, file.lastModified(), file.isHidden(), file.canWrite(), duplicate));

        }
        cursor.close();
        if (listItem.size() > 0) {

            if (sortType != SORT_NONE)
                Collections.sort(listItem, new Comparator<Item>() {
                    @Override
                    public int compare(Item item, Item t1) {
                        switch (sortType) {
                            case SORT_FOLDER:
                                return (item.getImageFolder().toLowerCase().compareTo(t1.getImageFolder().toLowerCase())) * sortOrder;
                            case SORT_DATE:
                                return (String.valueOf(item.getLastModified()).compareTo(String.valueOf(t1.getLastModified()))) * sortOrder;
                            case SORT_DUPLICATE:
                                return (String.valueOf(item.isDuplicate()).compareTo(String.valueOf(t1.isDuplicate()))) * sortOrder;
                            default:
                                return (item.getImageFileName().toLowerCase().compareTo(t1.getImageFileName().toLowerCase())) * sortOrder;
                        }
                    }
                });
            RelativeLayout rel = (RelativeLayout) findViewById(R.id.error);
            rel.setVisibility(View.GONE);
        } else {
            showSnackMessage("Oops! No photos were found.");
        }


        for (int i = 0; i < listItem.size(); i++) {
            if ((i + 3) % ItemAdapter.AD_POSITION == 0)
                listItem.add(i, new Item(Item.TYPE_AD));
        }
        items_adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            loadImages();
        }
    }

    @Override
    public void onBackPressed() {
        if (multiSelectMode) {
            deselectSelectedItems();
        } else {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coord);
            Snackbar.make(coordinatorLayout, "Exit app?", Snackbar.LENGTH_SHORT)
                    .setAction("Yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_dark))
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds recyclerView to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                deselectSelectedItems();
                return true;
            case R.id.action_about:
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_about);
                dialog.show();

                Button moreApps = (Button) dialog.findViewById(R.id.accept);
                Button dismiss = (Button) dialog.findViewById(R.id.back);


                moreApps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewMoreApps("gibatekpro");
                        //moreApps();
                        dialog.cancel();
                    }
                });

                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                return true;
            case R.id.action_rate:
                rate();
                return true;
            case R.id.action_help:
                loadTutorial();
                return true;
            case R.id.action_viewmore:
                viewMoreApps("gibatekpro");
                return true;
            case R.id.action_share:
                shareApp(getString(R.string.app_name), MainActivity.this);
                return true;
            case R.id.action_share_sel:
                shareSelectedItems();
                break;
            case R.id.action_delete:
                AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
                builderInner.setTitle("Delete");
                builderInner.setMessage("Are you sure you want to delete the selected items? This action is irreversible.");
                builderInner.setTitle("Delete");
                builderInner.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // showInterDelay();
                        dialog.dismiss();
                        deleteSelectedItems();
                    }
                });
                builderInner.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareSelectedItems() {
        if (selectedCounter < 1) {
            showSnackMessage("Please, select photos to share.");
            return;
        }
        //ArrayList<Integer> positions = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < listItem.size(); i++) {
            Item item = listItem.get(i);
            if (item.isSelected()) {
                paths.add(item.getAbsolutePathOfImage());
            }
        }
        shareImages(paths);
        deselectSelectedItems();
    }

    private void deselectSelectedItems() {
        int position = 0;
        for (Item item : listItem) {
            if (item.isSelected()) {
                item.setSelected(false);
                items_adapter.updateItem(item, position);
            }
            position++;
        }
        selectedCounter = 0;
        resetToolBar();
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                Item item = items_adapter.getItem(position);
                if (direction == ItemTouchHelper.LEFT) {
                    deleteItem(position);
                } else {
                    removeView(position);
                    showInterDelay(ACTION_SHARE, listItem.get(position).getAbsolutePathOfImage());
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(getResources().getColor(R.color.share));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = drawableToBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_share));
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(getResources().getColor(R.color.delete));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = drawableToBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete));
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void deleteItem(final int position) {
        final Item item = items_adapter.getItem(position);
        if (item.getType() == Item.TYPE_AD) {
            items_adapter.notifyItemChanged(position);
            return;
        }
        items_adapter.removeItem(position);
        Snackbar.make((CoordinatorLayout) findViewById(R.id.coord), "Item has been deleted.", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        items_adapter.addItem(item, position);
                    }
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        switch (event) {
                            case DISMISS_EVENT_SWIPE:
                            case DISMISS_EVENT_CONSECUTIVE:
                            case DISMISS_EVENT_TIMEOUT:
                                File fileToDelete = new File(item.getAbsolutePathOfImage());
                                if (!fileToDelete.delete()) {
                                    showSnackMessage("Unable to delete file for some reason.");
                                }
                                break;
                        }
                        super.onDismissed(transientBottomBar, event);
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.delete))
                .show();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void removeView(int position) {
        items_adapter.notifyItemChanged(position);
    }

    @Override
    public void performAction() {
        //showSnackMessage("from Main activity");
        switch (action) {
            case ACTION_OPEN:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(params)), "image/*");
                startActivity(Intent.createChooser(intent, "Open in"));
                break;
            case ACTION_SHARE:
                shareImage(params);
            default:
                break;
        }
        action = ACTION_NONE;
        params = "";
    }

    /**
     * Shows the in-app tutorials
     */
    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);
    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem("All in one place", "It couldn't get any easier in " + getString(R.string.app_name) + " with all your photos in one place.", R.color.colorPrimary, R.drawable.help2);

        TutorialItem tutorialItem2 = new TutorialItem("Swipe support!", "Swipe to perform common tasks without hassle. ", R.color.colorPrimary, R.drawable.help1);

        TutorialItem tutorialItem3 = new TutorialItem("Find duplicates", "This icon appears on any photo that is a copy of another (detected by name).", R.color.colorAccent, R.drawable.help3);
       /* // You can also add gifs, [IN BETA YET] (because Glide is awesome!)
        TutorialItem tutorialItem1 = new TutorialItem(context.getString(R.string.slide_1_african_story_books), context.getString(R.string.slide_1_african_story_books_subtitle),
                R.color.slide_1, R.drawable.gif_drawable, true);*/
        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem2);

        return tutorialItems;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    settings.edit().putBoolean("show_help", false).apply();
                    showSnackMessage("Press the 'help' menu to show help at any time.");
                    return;
                case VIEW_ACT:
                    getAllShownImagesPath(this);
                    return;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case READ_STORAGE:
                    loadImages();
                    break;
            }
        } else {
            showSnackMessage("This permission is needed to read images.");
        }
    }
}
