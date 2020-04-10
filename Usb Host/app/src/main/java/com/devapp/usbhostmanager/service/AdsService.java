package com.devapp.usbhostmanager.service;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AdsService {
    private static AdsService ourInstance;
    private Activity mContext;
    private InterstitialAd mInterstitialAd;
    private int interstitialAdCallDelay = 4;
    private int interstitialAdDelayCounter = 1;
    private OnInterstitialAdDismissed onInterstitialAdDismissed;
    private String[] testDeviceIds;

    private AdsService(Activity context) {
        mContext = context;
    }

    private AdsService(Activity context, String appId) {
        mContext = context;
        MobileAds.initialize(context, appId);
    }

    public static AdsService with(Activity context) {
        if (ourInstance == null) {
            ourInstance = new AdsService(context);
        }
        return ourInstance;
    }

    public static AdsService with(Activity context, String appId) {
        if (ourInstance == null) {
            ourInstance = new AdsService(context, appId);
        }
        return ourInstance;
    }

    public static void destroy() {
        ourInstance = null;
    }

    @RequiresPermission("android.permission.INTERNET")
    public static void setBanner(@IdRes int id, @NonNull View rootView, String... testDeviceIds) {
        AdView adView = (AdView) rootView.findViewById(id);
        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (testDeviceIds != null) {
            for (String testDevice : testDeviceIds) {
                adRequest.addTestDevice(testDevice);
            }
        }
        adView.loadAd(adRequest.build());
    }

    @RequiresPermission("android.permission.INTERNET")
    public static void setBanner(@IdRes int id, @NonNull Activity activity, String... testDeviceIds) {
        AdView adView = (AdView) activity.findViewById(id);
        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (testDeviceIds != null) {
            for (String testDevice : testDeviceIds) {
                adRequest.addTestDevice(testDevice);
            }
        }
        adView.loadAd(adRequest.build());
    }

    @RequiresPermission("android.permission.INTERNET")
    public AdsService setBanner(@IdRes int id) {
        AdView adView = (AdView) mContext.findViewById(id);
        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (testDeviceIds != null) {
            for (String testDevice : testDeviceIds) {
                adRequest.addTestDevice(testDevice);
            }
        }
        adView.loadAd(adRequest.build());
        return this;
    }

    @RequiresPermission("android.permission.INTERNET")
    public AdsService setInterstitialAd(String adUnitId) {
        mInterstitialAd = new InterstitialAd(mContext);
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
        return this;
    }

    private void performAction() {
        if (onInterstitialAdDismissed != null) {
            this.onInterstitialAdDismissed.performAction(mContext);
        }
    }

    public int getInterstitialAdCallDelay() {
        return interstitialAdCallDelay;
    }

    public AdsService setInterstitialAdCallDelay(int interstitialAdCallDelay) {
        if (interstitialAdCallDelay < 1) {
            interstitialAdCallDelay = 1;
        }
        this.interstitialAdCallDelay = interstitialAdCallDelay;
        return this;
    }

    private int getInterstitialAdDelayCounter() {
        return interstitialAdDelayCounter;
    }

    private void incrementCounter() {
        interstitialAdDelayCounter++;
    }

    public void showInterstitialAd(Boolean delay, OnInterstitialAdDismissed onInterstitialAdDismissed) throws AdsServiceSetupException {
        if (mInterstitialAd == null) {
            if (onInterstitialAdDismissed != null) {
                onInterstitialAdDismissed.performAction(mContext);
            } else {
                return;
            }
        }
        if (delay == null) {
            delay = false;
        }
        if (mInterstitialAd != null) {
            if (mInterstitialAd.isLoaded()) {
                if (delay) {
                    if (getInterstitialAdCallDelay() % getInterstitialAdDelayCounter() == 0) {
                        this.onInterstitialAdDismissed = onInterstitialAdDismissed;
                        mInterstitialAd.show();
                    } else {
                        onInterstitialAdDismissed.performAction(mContext);
                    }
                    incrementCounter();
                } else {
                    this.onInterstitialAdDismissed = onInterstitialAdDismissed;
                    mInterstitialAd.show();
                }
            } else {
                loadInterstitialAd();
                onInterstitialAdDismissed.performAction(mContext);
            }
        } else {
//            loadInterstitialAd();
            onInterstitialAdDismissed.performAction(mContext);
        }
    }

    public void showInterstitialAd(OnInterstitialAdDismissed onInterstitialAdDismissed) throws AdsServiceSetupException {
        if (mInterstitialAd == null) {
            onInterstitialAdDismissed.performAction(mContext);
            return;
        }
        if (mInterstitialAd.isLoaded()) {
            this.onInterstitialAdDismissed = onInterstitialAdDismissed;
            mInterstitialAd.show();
        } else {
            loadInterstitialAd();
            onInterstitialAdDismissed.performAction(mContext);
        }
    }

    public void showInterstitialAd() throws AdsServiceSetupException {
        if (mInterstitialAd == null) {
            onInterstitialAdDismissed.performAction(mContext);
            return;
        }
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            loadInterstitialAd();
        }
    }

    private void loadInterstitialAd() {
        if (!mInterstitialAd.isLoading()) {
            AdRequest.Builder adRequest = new AdRequest.Builder();
            if (testDeviceIds != null) {
                for (String testDeviceID : testDeviceIds) {
                    adRequest.addTestDevice(testDeviceID);
                }
            }
            mInterstitialAd.loadAd(adRequest.build());
        }
    }

    public AdsService setTestDeviceIds(String... testDeviceIds) {
        this.testDeviceIds = testDeviceIds;
        return this;
    }
//    public AdsService addTestId(String testDeviceId) {
//        this.testDeviceIds = testDeviceIds;
//        return this;
//    }

    public interface OnInterstitialAdDismissed {
        void performAction(Context context);
    }
}
