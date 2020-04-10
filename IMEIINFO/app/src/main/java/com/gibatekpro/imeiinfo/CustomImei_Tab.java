package com.gibatekpro.imeiinfo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import com.gibatekpro.imeiinfo.Adapters.Imei_Adapter;
import com.gibatekpro.imeiinfo.ListItems.Imei_list_item;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.CLIPBOARD_SERVICE;

public class CustomImei_Tab extends Fragment implements RewardedVideoAdListener {


    //Widgets
    EditText input;
    Button generate, clear, copy_all;

    //RecyclerView Initialization
    private RecyclerView custom_recyclerView;

    //List Item Initialization
    private List<Imei_list_item> imei_list_item = new ArrayList<>();

    //All values of recyclerview
    String recycler_string;

    //This will be used to append all generated strings
    //For the purpose of copy to clipboard
    StringBuilder stringBuilder_clip;

    RelativeLayout relativeLayout;

    Imei_list_item customImeiListItem;

    //private InterstitialAd customInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    private AdRequest adRequest;
    private AdView mAdView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_imei_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //EditText Views
        input = getView().findViewById(R.id.input);
        relativeLayout = getView().findViewById(R.id.custom_main_content);

        //Button Views
        generate = getView().findViewById(R.id.generate);
        copy_all = getView().findViewById(R.id.copy_all);
        clear = getView().findViewById(R.id.clear);

        copy_all.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);

        //[START Call to RecyclerView]
        custom_recyclerView = getView().findViewById(R.id.custom_recyclerview);
        custom_recyclerView.setHasFixedSize(true);
        custom_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //[START Call to custom_recyclerView]

        //This initializes clipboard for copy function
        Object clipboardService = getContext().getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager) clipboardService;

        //<editor-fold desc="Ads">
        /*customInterstitialAd = new InterstitialAd(getContext());
        customInterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/3470648722");//IMEI.Custom.Interstitial
        customInterstitialAd.loadAd(new AdRequest.Builder().build());
        customInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                customInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });*/

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        mAdView = getView().findViewById(R.id.cadView);

        adRequest = new AdRequest.Builder().build();
        try {
            mAdView.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                mAdView.setVisibility(View.GONE);

                try {
                    mAdView.loadAd(adRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                mAdView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();

                mAdView.setVisibility(View.VISIBLE);

                try {
                    mAdView.loadAd(adRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
        //</editor-fold>

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );
                inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);

                copy_all.setVisibility(View.VISIBLE);
                clear.setVisibility(View.VISIBLE);
                custom_recyclerView.setVisibility(View.VISIBLE);


                //Reset data to zero
                recycler_string = "";

                stringBuilder_clip = new StringBuilder();

                //[START Remove Old Values]
                if (imei_list_item.size() > 0)
                    imei_list_item.clear();
                //[END Remove Old Values]

                for (int i = 0; i < 50; i++) {

                    try {
                        String items = generator_plus_luhn();

                        customImeiListItem = new Imei_list_item(String.valueOf(items));

                        imei_list_item.add(customImeiListItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                recycler_string = String.valueOf(stringBuilder_clip);

                //[START RecyclerView Function]
                RecyclerView.Adapter adapter = new Imei_Adapter(imei_list_item, getContext());
                custom_recyclerView.setAdapter(adapter);
                //[END RecyclerView Function]

                //This is for the copy button on esch recyclerview item
                ((Imei_Adapter) adapter).setOnItemClickListener(new Imei_Adapter.OnItemClickListener() {
                    @Override
                    public void onCopyClick(int position) {
                        String fish = imei_list_item.get(position).getImei();

                        ClipData clipData = ClipData.newPlainText("extracts", fish);
                        clipboardManager.setPrimaryClip(clipData);

                        Snackbar snackbar = Snackbar
                                .make(getView().findViewById(R.id.custom_main_content), R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT);
                        snackbar.show();

                    }
                });

                //Animation
                Context context = custom_recyclerView.getContext();
                LayoutAnimationController controller;

                controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fade_in);

                //set anim
                custom_recyclerView.setLayoutAnimation(controller);
                custom_recyclerView.getAdapter().notifyDataSetChanged();
                custom_recyclerView.scheduleLayoutAnimation();

                try {
                    if (mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        copy_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipData clipData = ClipData.newPlainText("extracts", recycler_string);
                clipboardManager.setPrimaryClip(clipData);

                Snackbar snackbar = Snackbar
                        .make(getView().findViewById(R.id.custom_main_content), R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT);
                snackbar.show();

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                copy_all.setVisibility(View.GONE);
                clear.setVisibility(View.GONE);
                custom_recyclerView.setVisibility(View.GONE);


            }
        });



    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                InputMethodManager mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mImm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                mImm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
            }
        }
    }

    //Function calculates and returns luhn digit after an input of 14 digits
    private int luhn(String input_gotten) {
        int luhn;
        int split_part_num;
        int split_part_num2;
        int input_sum = 0;


        //This will split the input
        String parts[] = input_gotten.split("");

        //This will check if we are in every 2nd character
        int i = 0;

        //This breaks down the full input
        for (String split_part : parts) {

            //This !split_part.equals("") ensures that no empty string is retrieved
            if (!split_part.equals("")) {

                i++;

                //This is to select every 2nd character
                if (i % 2 == 0) {

                    //This is to convert every 2nd character to integer
                    split_part_num = Integer.valueOf(split_part);

                    //Log.d("TAG", "logger  " + split_part_num);

                    //This will multiply the integer by 2 (an important
                    // step in the calculation of luhn digit)
                    split_part_num *= 2;

                    //This will check if the multiple above are 2 digits
                    if (split_part_num > 9) {

                        //convert it to string and separate it
                        String dual_multiple = String.valueOf(split_part_num);
                        String dual_break[] = dual_multiple.split("");

                        for (String dual_breaks : dual_break) {

                            //This !dual_breaks.equals("") ensures that no empty string is retrieved
                            if (!dual_breaks.equals("")) {

                                //This adds the split to the master sum
                                input_sum += Integer.valueOf(dual_breaks);

                            }

                        }

                        //This will check if the multiple above are single digits
                    } else {

                        //This adds the single digits to the master sum
                        input_sum += split_part_num;

                    }

                } else {

                    //This is to convert every first character to integer
                    split_part_num2 = Integer.valueOf(split_part);

                    //This adds the single digits to the master sum
                    input_sum += split_part_num2;

                }

            }

        }

        if (input_sum % 10 != 0) {

            //This calculates the luhn by subtracting the sum from
            // the nearest Tens of the sum
            luhn = (((input_sum / 10) + 1) * 10) - input_sum;

        } else {

            luhn = 0;

        }

        return luhn;

    }

    //Function accepts input and generates random numbers
    private String random_num_generator(String input) {
        String custom;

        //This will be executed if the user does not input anything
        if (input.equals("")) {

            //This will be the first 2 values of the IMEI
            String default_string = "35";

            //Build string
            StringBuilder join = new StringBuilder();
            StringBuilder join_default_string_to_random = new StringBuilder();

            //produce a certain number of random integers
            for (int j = 0; j < 12; j++) {

                Random random = new Random();

                //random.nextInt((max-min) + 1) + min
                int ran_num = random.nextInt(9) + 1;

                //append rand generated
                join.append(ran_num);

            }

            join_default_string_to_random.append(default_string).append(join);

            //This adds the integer generated to the general string
            custom = String.valueOf(join_default_string_to_random);

        } else {

            int i = 0;

            //This splits input
            String input_split[] = input.split("");

            //Build string
            StringBuilder join = new StringBuilder();

            //This selects every character
            for (String input_splits : input_split) {

                //Checks for empty input
                if (!input_splits.equals("")) {

                    i++;

                    //This re-appends all the characters
                    join.append(input_splits);

                }

            }

            if (i < 14) {

                //This calculate how many random nubers we
                // have to produce
                int remainder = 14 - i;

                //produce a certain number of random integers
                for (int j = 0; j < remainder; j++) {

                    Random random = new Random();

                    //random.nextInt((max-min) + 1) + min
                    int ran_num = random.nextInt(9) + 1;

                    //This adds the integer generated to the general string
                    join.append(ran_num);

                }

            }

            custom = String.valueOf(join);

        }

        return custom;

    }

    //Function Appends the Generator to the luhn and adds it to list items
    private String generator_plus_luhn() {

        //This string builder will append input to luhn
        StringBuilder stringBuilder = new StringBuilder();

        //the luhn accepts the output of the random_num_generator()
        //And use it to calculate the luhn
        String generated_imei = random_num_generator(input.getText().toString());
        String luhn = String.valueOf(luhn(generated_imei));

        stringBuilder.append(generated_imei).append(luhn);

        //This will be used to append all generated strings
        stringBuilder_clip.append(String.valueOf(stringBuilder)).append("\n");



        return stringBuilder.toString();

    }

    //Tutorial
    public void custom_tutorial() {

        TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
                        TapTarget.forView(getView().findViewById(R.id.generate), getString(R.string.generate_custom_imei),
                                getString(R.string.generatecust_tut))
                                .outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white2)   // Specify a color for the target circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.colorAccent)      // Specify the color of the title text
                                .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white2)  // Specify the color of the description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(100))                  // Specify the target radius (in dp)
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                });
        sequence.start();

    }

    //<editor-fold desc="Ads">
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-1488497497647050/9535253983",
                new AdRequest.Builder().build());
    }
    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        // Load the next rewarded video ad.
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
    /*public void customLoadAds() {

        if (customInterstitialAd.isLoaded()) {
            customInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }*/
    //</editor-fold>
}
