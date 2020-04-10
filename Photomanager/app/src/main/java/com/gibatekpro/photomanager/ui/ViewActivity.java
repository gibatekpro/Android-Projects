package com.gibatekpro.photomanager.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gibatekpro.photomanager.BaseApp;
import com.gibatekpro.photomanager.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ViewActivity extends BaseApp {
    File file;
    ImageView image;
    TextView image_name;
    TextView folder;
    TextView type;
    TextView size;
    TextView lastMod;
    CheckBox hidden;
    CheckBox writable;
    Button open;
    Button save;
    Button share;

    private int action = 0;
    private String params = "";

    public static final int ACTION_NONE = 0;
    public static final int ACTION_SHARE = 1;
    public static final int ACTION_OPEN = 2;
    private static final int ACTION_EDIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        String path = getIntent().getStringExtra("image_path");
        if (path == null || path.isEmpty()) {
            showSnackMessage("Unknown error");
            finish();
        }


        file = new File(path);

        image = (ImageView) findViewById(R.id.image);
        Picasso.with(ViewActivity.this).load(file).resize(100, 100).centerCrop().into(image, new Callback() {
            @Override
            public void onSuccess() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadRealPhoto();
                    }
                }, 500);
            }

            @Override
            public void onError() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadRealPhoto();
                    }
                }, 500);
            }
        });

        //interstitial ads
        initInterstitialAds("ca-app-pub-1488497497647050/2847769522", "E9C1DEC2F04C63973F213FD83AAEFEC8");

        initSupportToolbar(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(file.getName());

        image_name = (TextView) findViewById(R.id.title);
        folder = (TextView) findViewById(R.id.folder);
        type = (TextView) findViewById(R.id.type);
        size = (TextView) findViewById(R.id.size);
        lastMod = (TextView) findViewById(R.id.lam);
        hidden = (CheckBox) findViewById(R.id.hidden);
        writable = (CheckBox) findViewById(R.id.write);
        open = (Button) findViewById(R.id.view);
        save = (Button) findViewById(R.id.save);
        share = (Button) findViewById(R.id.share);

        image_name.setText(file.getName().replace(getExtension(file.getName()), ""));
        folder.setText(file.getParent());
        type.setText(getExtension(file.getName()).replace(".", "").toUpperCase());
        size.setText(formatSize(file.length()));
        lastMod.setText(millisecondsToDate(file.lastModified(), "yyyy-MM-dd HH:mm:ss"));
        hidden.setChecked(file.isHidden());
        writable.setChecked(file.canWrite());

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInter(ACTION_OPEN, file.getAbsolutePath());
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterDelay(ACTION_SHARE, file.getAbsolutePath());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterDelay(ACTION_EDIT, file.getAbsolutePath());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void performAction() {
        //showSnackMessage("from view activity");
        Intent intent;
        String authorities;
        Uri uri;
        switch (action) {
            case ACTION_OPEN:
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                authorities = this.getPackageName() + ".fileprovider";

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    uri = FileProvider.getUriForFile(this, authorities, file);
//
//                } else {
                    uri = Uri.fromFile(file);
                //}
                //intent.addFlags();
                //intent.addFlags();
                // intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setDataAndType(uri, getMimeType(file.getName()));

//                List<ResolveInfo> rInfo = this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                for (ResolveInfo resolve : rInfo) {
//                    String packageName = resolve.activityInfo.packageName;
//                    this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                }
                try {
                    startActivity(Intent.createChooser(intent, "Open with"));
                } catch (ActivityNotFoundException e) {
                    showSnackMessage("Activity not found");
                }
                break;
            case ACTION_EDIT:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                    showSnackMessage("Might not work on Nougat+ devices. This is a known issue.", Snackbar.LENGTH_LONG);
                intent = new Intent();
                intent.setAction(Intent.ACTION_EDIT);
                authorities = this.getPackageName() + ".fileprovider";

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    uri = FileProvider.getUriForFile(this, authorities, file);
//                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                } else {
                    uri = Uri.fromFile(file);
                //}
                // intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setDataAndType(uri, getMimeType(file.getName()));
                try {
                    startActivity(Intent.createChooser(intent, "Edit with"));
                } catch (ActivityNotFoundException e) {
                    showSnackMessage("Activity not found");
                }
                break;
            case ACTION_SHARE:
                shareImage(params);
            default:
                break;
        }
        action = ACTION_NONE;
        params = "";
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String formatSize(long imageSize) {
        return android.text.format.Formatter.formatFileSize(this, imageSize);
    }

    public String millisecondsToDate(long milliseconds, String format) {
        return DateFormat.format(format, milliseconds).toString();
    }

    public String getMimeType(String fileName) {
        String ext = getExtension(fileName).replace(".", "");
        if (ext != null || !ext.isEmpty()) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getMimeTypeFromExtension(ext);
        }
        return null;
    }

    private void loadRealPhoto() {
        Picasso.with(ViewActivity.this).load(file).fit().centerInside().placeholder(image.getDrawable()).into(image);
    }

    @Override
    public void finish() {
        //Picasso.with(ViewActivity.this).load(file).resize(150, 150).centerCrop().into(image);
        setResult(Activity.RESULT_OK);
        super.finish();
    }

    @Override
    public void showHelp() {

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
}
