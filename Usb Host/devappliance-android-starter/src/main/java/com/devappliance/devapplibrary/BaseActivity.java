package com.devappliance.devapplibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.devappliance.devapplibrary.dto.SupportedLanguage;
import com.devappliance.devapplibrary.helper.PermissionRequest;
import com.devappliance.devapplibrary.util.AppUtil;
import com.devappliance.devapplibrary.util.LocaleHelper;
import com.devappliance.devapplibrary.util.MessageUtil;
import com.devappliance.devapplibrary.util.PreferenceUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String LOCALE_SETTINGS_KEY = "locale";
    protected InterstitialAd mInterstitialAd;
    private int ACTION_COUNTER = 0;

    public void initializeAdmob(String appId) {
        MobileAds.initialize(this, appId);
    }

    public void loadBannerAd(@IdRes int id) {
        AdRequest.Builder adRequest = new AdRequest.Builder();
        ((AdView) findViewById(id)).loadAd(adRequest.build());
    }

    public void initialiseInterstitialAds(String adUnitId) {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(adUnitId);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                performAction();
                loadInterstitialAd();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                performAction();
                loadInterstitialAd();
            }

            @Override
            public void onAdOpened() {
                loadInterstitialAd();
            }

            @Override
            public void onAdLoaded() {

            }
        });
        loadInterstitialAd();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    protected void performAction() {

    }

    protected int getActionLimitCounter() {
        return 4;
    }

    protected void loadInterstitialAd() {
        if (!mInterstitialAd.isLoading()) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    protected void showInterstitialAd(Boolean delay) {
        if (delay == null) {
            delay = false;
        }
        Log.e("XXX", "showInterstitialAd: " + delay);
        if (mInterstitialAd.isLoaded()) {
            Log.e("XXX", "showInterstitialAd: add is loaded");
            if (delay) {
                if (ACTION_COUNTER % getActionLimitCounter() == 0) {
                    Log.e("XXX", "showInterstitialAd: called show");
                    mInterstitialAd.show();
                } else {
                    performAction();
                }
                ACTION_COUNTER++;
            } else {
                mInterstitialAd.show();
            }
        } else {
            loadInterstitialAd();
            performAction();
        }

        Log.e("XXX", "showInterstitialAd: counter value " + ACTION_COUNTER);
    }

    public void requestPermission(@NonNull final PermissionRequest permissionRequest) {
        if (ContextCompat.checkSelfPermission(this, permissionRequest.getPermission()) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionRequest.getPermission())) {
                if (!TextUtils.isEmpty(permissionRequest.getRationale())) {
                    MessageUtil.showMessage(permissionRequest.getRationale(),
                            "Grant Permission", (dialog, which) -> requestPermission(permissionRequest), this);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permissionRequest.getPermission()}, permissionRequest.getRequestCode());
            }
        } else {
            permissionRequest.doSomething(this);
        }
    }

    public void startActivityWithSlideInRightAnimation(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right_400, R.anim.slide_out_left_400);
    }

    protected void about(Activity context) {
        PackageInfo pInfo = null;
        String version = "";
        Date buildDate = new Date();
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            buildDate = new Date(BuildConfig.RELEASETIME);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "N/A";
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.app_name));
        alert.setMessage(String.format("Version: %s | %s", version, new SimpleDateFormat("dd/MM/yyyy").format(buildDate)));
        alert.setPositiveButton("More apps", (dialog, which) -> {
            AppUtil.viewMoreApps("DevAppliance", context);
        }).setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> {

        });
        alert.show();
    }

    protected void about(String devName, Activity context) {
        PackageInfo pInfo = null;
        String version = "";
        Date buildDate = new Date();
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            buildDate = new Date(BuildConfig.RELEASETIME);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "N/A";
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.app_name));
        alert.setMessage(String.format("Version: %s | %s", version, new SimpleDateFormat("dd/MM/yyyy").format(buildDate)));
        alert.setPositiveButton("More apps", (dialog, which) -> {
            AppUtil.viewMoreApps(devName, context);
        }).setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> {

        });
        alert.show();
    }

    protected void showRateDialog() {
        RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(3F)
                .session(4)
                .onRatingBarFormSumbit(feedback -> {

                }).build();
        ratingDialog.show();
    }

    protected List<SupportedLanguage> getSupportedLanguages() {
        List<SupportedLanguage> supportedLanguages = new ArrayList<>();
        SupportedLanguage sl = new SupportedLanguage("English", "en");
        supportedLanguages.add(sl);
        return supportedLanguages;
    }


    protected void languageSettings(String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);

        SupportedLanguage currentLocale = new SupportedLanguage("", PreferenceUtil.get(this.getApplicationContext(), LOCALE_SETTINGS_KEY, getResources().getConfiguration().locale.getLanguage()));

        List<SupportedLanguage> supportedLanguages = getSupportedLanguages();
        if (supportedLanguages == null || supportedLanguages.isEmpty()) {
            throw new IllegalArgumentException("Supported languages not set");
        }
        int selected = supportedLanguages.indexOf(currentLocale);

        List<String> list = new ArrayList<>();
        for (SupportedLanguage it : supportedLanguages) {
            String name = it.getName();
            list.add(name);
        }
        final String[] locales = list.toArray(new String[]{});

        alert.setSingleChoiceItems(locales, selected, (dialogInterface, i) -> {
            SharedPreferences.Editor settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            settings.putString(LOCALE_SETTINGS_KEY, supportedLanguages.get(i).getCode());
            settings.apply();
            dialogInterface.dismiss();
            recreate();
//            internationalise(true, activity);
        });
        alert.show();
    }
}
