package com.gibatekpro.myusbotg;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.gms.ads.AdView;

import static android.content.ContentValues.TAG;

/**
 * Created by Gibah on 11/24/2017.
 */

public class rate_frag extends Fragment {

    private final static String APP_PACKAGE_NAME = "com.gibatekpro.myusbotg";
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rate_frag, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*MobileAds.initialize(getActivity().getApplicationContext(),
                "ca-app-pub-1488497497647050~2609508862");
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        Button rate_app = getView().findViewById(R.id.rate_the_app);


        rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RatingDialog ratingDialog = new RatingDialog.Builder(getActivity())
                        .icon(getActivity().getResources().getDrawable(R.drawable.icon))
                        .threshold(4)
                        .title(getActivity().getString(R.string.rate_app))
                        .titleTextColor(R.color.black)
                        .positiveButtonText(getActivity().getString(R.string.not_now))
                        .negativeButtonText(getActivity().getString(R.string.no))
                        .positiveButtonTextColor(R.color.black)
                        .negativeButtonTextColor(R.color.black)
                        .formTitle(getActivity().getString(R.string.Submit_Feedback))
                        .formHint(getActivity().getString(R.string.Tell_us_where_to_improve))
                        .formSubmitText(getActivity().getString(R.string.Submit))
                        .formCancelText(getActivity().getString(R.string.Cancel))
                        .ratingBarColor(R.color.black)
                        .playstoreUrl("https://play.google.com/store/apps/details?id=com.gibatekpro.myusbotg")
                        .onThresholdCleared(new RatingDialog.Builder.RatingThresholdClearedListener() {
                            @Override
                            public void onThresholdCleared(RatingDialog ratingDialog, float rating, boolean thresholdCleared) {
                                String pName = getActivity().getPackageName();
                                try {
                                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=" + APP_PACKAGE_NAME)));
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(getActivity(), R.string.You_have_pressed_Rate_Button, Toast.LENGTH_SHORT).show();
                                }
                                ratingDialog.dismiss();
                            }
                        })
                        .onRatingChanged(new RatingDialog.Builder.RatingDialogListener() {
                            @Override
                            public void onRatingSelected(float rating, boolean thresholdCleared) {

                            }
                        })
                        .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                            @Override
                            public void onFormSubmitted(String feedback) {
                                Log.i(TAG, "Feedback:" + feedback);
                            }
                        }).build();
                ratingDialog.show();
            }
        });

    }

    public void rate() {
        String pName = getActivity().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pName)));
        }
    }
}
