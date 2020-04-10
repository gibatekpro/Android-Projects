package com.gibatekpro.myusbotg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Gibah on 11/24/2017.
 */

public class info_frag extends Fragment {

    private Button button;
    private TextView textView, device_named, device_name, IMEI1_named, IMEI1, IMEI2_named, IMEI2, subscriberid_named, subscriberid, dual_sim_named, dual_sim, sim_1_ready_named, sim_1_ready, sim_2_ready_named, sim_2_ready;
    private static int TELEPHONE = 23;
    String imeiSIM1;
    String imeiSIM2;
    boolean isSIM1Ready;
    boolean isSIM2Ready;
    boolean isDualSIM;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MobileAds.initialize(getActivity().getApplicationContext(),
                "ca-app-pub-1488497497647050~2609508862");
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.my_usb_otg_interstitial_dev_info));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        button = getView().findViewById(R.id.button);
        device_name = getView().findViewById(R.id.device_name);
        IMEI1 = getView().findViewById(R.id.IMEI1);
        IMEI2 = getView().findViewById(R.id.IMEI2);
        subscriberid = getView().findViewById(R.id.subscriberid);
        dual_sim = getView().findViewById(R.id.dual_sim);
        sim_1_ready = getView().findViewById(R.id.sim_1_ready);
        sim_2_ready = getView().findViewById(R.id.sim_2_ready);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermission(Manifest.permission.READ_PHONE_STATE, TELEPHONE);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }



                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });


    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
        } else {

            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(getActivity());

            imeiSIM1 = telephonyInfo.getImsiSIM1();
            imeiSIM2 = telephonyInfo.getImsiSIM2();

            isSIM1Ready = telephonyInfo.isSIM1Ready();
            isSIM2Ready = telephonyInfo.isSIM2Ready();

            isDualSIM = telephonyInfo.isDualSIM();


            IMEI1.setText(imeiSIM1);
            IMEI2.setText(imeiSIM2);
            dual_sim.setText(String.valueOf(isDualSIM));
            sim_1_ready.setText(String.valueOf(isSIM1Ready));
            sim_2_ready.setText(String.valueOf(isSIM2Ready));
            device_name.setText(getDeviceName());
            subscriberid.setText(imsi());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == TELEPHONE) {

                TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(getActivity());

                imeiSIM1 = telephonyInfo.getImsiSIM1();
                imeiSIM2 = telephonyInfo.getImsiSIM2();

                isSIM1Ready = telephonyInfo.isSIM1Ready();
                isSIM2Ready = telephonyInfo.isSIM2Ready();

                isDualSIM = telephonyInfo.isDualSIM();

                IMEI1.setText(imeiSIM1);
                IMEI2.setText(imeiSIM2);
                dual_sim.setText(String.valueOf(isDualSIM));
                sim_1_ready.setText(String.valueOf(isSIM1Ready));
                sim_2_ready.setText(String.valueOf(isSIM2Ready));
                device_name.setText(getDeviceName());
                subscriberid.setText(imsi());

            }

            Toast.makeText(getActivity(), R.string.Permission_granted, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.Permission_denied, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String board = android.os.Build.BOARD;
        String product = android.os.Build.PRODUCT;
        String id = android.os.Build.ID;
        //String os = System.getProperty("os.version"); // OS version
        int sdk = android.os.Build.VERSION.SDK_INT;     // API Level
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;/* + "\n" +
                board + "\n" +
                product + "\n" +
                id + "\n" +
                sdk;*/
    }

    public String imsi() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imsi = telephonyManager.getSubscriberId();
        return imsi;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


}
