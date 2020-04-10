package com.gibatekpro.myusbotg;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import static android.content.ContentValues.TAG;


public class otg_frag extends Fragment {
    private TextView textView;
    private static int TELEPHONE = 23;
    private final static String APP_TITLE = "My USB OTG";
    private final static String APP_PACKAGE_NAME = "com.gibatekpro.myusbotg";

    //Initialized to 0 and 3 only for test purposes. In Real app, change this
    private final static int DAYS_UNTIL_PROMPT = 0;
    private final static int LAUNCH_UNTIL_PROMPT = 3;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.otg_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app_launched(getContext());

        MobileAds.initialize(getActivity().getApplicationContext(),
                "ca-app-pub-1488497497647050~2609508862");
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.my_usb_otg_interstitial_otg));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        Button button = getView().findViewById(R.id.button);
        textView = getView().findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //askForPermission(Manifest.permission.READ_PHONE_STATE, TELEPHONE);

                textView.setText(testOtgSupport());

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }



                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });


    }

    private String testOtgSupport() {
        return getActivity().getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_USB_HOST) ? getString(R.string.supports_OTG) : getString(R.string.does_not_support_otg);
    }

    /*private void askForPermission(String permission, Integer requestCode) {
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
            textView.setText(testOtgSupport());
        }

    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == TELEPHONE) {

                textView.setText(testOtgSupport());
            }

            Toast.makeText(getActivity(), R.string.Permission_granted, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.Permission_denied, Toast.LENGTH_SHORT).show();
        }
    }*/


    public static void app_launched(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("rate_app", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        Long date_firstLaunch = prefs.getLong("date_first_launch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_first_launch", date_firstLaunch);
        }

        if (launch_count >= LAUNCH_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(context, editor);
            }
        }
        editor.commit();
    }

    private static void showRateDialog(final Context context, final SharedPreferences.Editor editor) {

        Dialog dialog = new Dialog(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String message = context.getString(R.string.if_you_enjoy_using)
                + APP_TITLE
                + context.getString(R.string.please_take_a_moment_to_rate_the_app)
                + context.getString(R.string.thanks_for_your_support);

        builder.setMessage(message)
                .setTitle(context.getString(R.string.rate) + APP_TITLE)
                .setIcon(context.getApplicationInfo().icon)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.rate), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putBoolean("dontshowagain", true);
                        editor.commit();

                        //if app hasn't been uploaded to market get an exception
                        //for test, we catch it here and show a text.
                        try {
                            //context.startActivity(new Intent(Intent.ACTION_VIEW,
                            //        Uri.parse("market://details?id=" + APP_PACKAGE_NAME)));
                            final RatingDialog ratingDialog = new RatingDialog.Builder(context)
                                    .icon(context.getResources().getDrawable(R.drawable.icon))
                                    .threshold(4)
                                    .title(context.getString(R.string.rate_app))
                                    .titleTextColor(R.color.black)
                                    .positiveButtonText(context.getString(R.string.not_now))
                                    .negativeButtonText(context.getString(R.string.no))
                                    .positiveButtonTextColor(R.color.black)
                                    .negativeButtonTextColor(R.color.black)
                                    .formTitle(context.getString(R.string.Submit_Feedback))
                                    .formHint(context.getString(R.string.Tell_us_where_to_improve))
                                    .formSubmitText(context.getString(R.string.Submit))
                                    .formCancelText(context.getString(R.string.Cancel))
                                    .ratingBarColor(R.color.black)
                                    .playstoreUrl("https://play.google.com/store/apps/details?id=com.gibatekpro.myusbotg")
                                    .onThresholdCleared(new RatingDialog.Builder.RatingThresholdClearedListener() {
                                        @Override
                                        public void onThresholdCleared(RatingDialog ratingDialog, float rating, boolean thresholdCleared) {
                                            String pName = context.getPackageName();
                                            try {
                                                context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("market://details?id=" + APP_PACKAGE_NAME)));
                                            } catch (ActivityNotFoundException e) {
                                                Toast.makeText(context, R.string.You_have_pressed_Rate_Button, Toast.LENGTH_SHORT).show();
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
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, R.string.You_have_pressed_Rate_Button, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(context.getString(R.string.not_now), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, R.string.You_have_pressed_Not_Now_button, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editor != null) {
                            editor.putBoolean("dontshowagain", true);
                            editor.commit();
                        }

                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }


}
