package com.devappliance.devapplibrary.interfaces;

import android.content.Context;

public interface FragmentInteractionListener {
    void showInterstitialAd(Boolean delay, Context context, Action action);

    void scanDevice();
}
