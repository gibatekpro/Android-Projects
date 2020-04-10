package com.devappliance.devapplibrary.ads;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AdsService {
    private static AdsService ourInstance;
    private Context mContext;
    private InterstitialAd mInterstitialAd;
    private AdActionListener adActionListener;
    private int ACTION_COUNTER = 0;

    private int interstitialAdInterval = 4;

    private AdsService(Context context) {
        this.mContext = context;
    }

    public static AdsService get(@NonNull Context context) {
        if (ourInstance == null) {
            ourInstance = new AdsService(context.getApplicationContext());
        }
        return ourInstance;
    }

    public void init(@NonNull String appId) {
        MobileAds.initialize(mContext, appId);
    }


    public void setInterstitialAdInterval(int interstitialAdInterval) {
        this.interstitialAdInterval = interstitialAdInterval;
    }

    public void loadBannerAd(@NonNull View view) {
        AdRequest.Builder adRequest = new AdRequest.Builder();
        ((AdView) view).loadAd(adRequest.build());
    }

    public void initialiseInterstitialAd(@NonNull String adUnitId) {
        initialiseInterstitialAd(adUnitId, 4);
    }

    public void initialiseInterstitialAd(@NonNull String adUnitId, int adActionInterval) {
        this.interstitialAdInterval = adActionInterval;
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
//                loadInterstitialAd();
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

    private void performAction() {
        if (adActionListener != null) {
            adActionListener.doThis();
        }
        adActionListener = null;
    }

    private int getActionLimitCounter() {
        return this.interstitialAdInterval;
    }

    private void loadInterstitialAd() {
        if (!mInterstitialAd.isLoading()) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    public void showInterstitialAd(boolean delay, AdActionListener adActionListener) {
        this.adActionListener = adActionListener;
        Log.e("XXX", "showInterstitialAd: " + delay);
        if (mInterstitialAd.isLoaded()) {
            Log.e("XXX", "showInterstitialAd: add is loaded");
            if (delay) {
                if (ACTION_COUNTER % getActionLimitCounter() == 0) {
                    Log.e("XXX", "showInterstitialAd: called show");
                    try {
                        mInterstitialAd.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
    }

    public void showInterstitialAd(boolean delay) {
        showInterstitialAd(delay, null);
    }

    public interface AdActionListener {
        void doThis();
    }
}
