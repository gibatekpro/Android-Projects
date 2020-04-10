package com.gibatekpro.imeigenerator;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.gibatekpro.imeigenerator.Adapters.AnalyzeImei_Adapter;
import com.gibatekpro.imeigenerator.ListItems.AnalyzeImei_list_item;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.println;

public class Analyzer_Tab extends Fragment {

    //List initialization
    private List<AnalyzeImei_list_item> analyzerListItems;

    //RecyclerView initialization
    private RecyclerView analyzer_recyclerView;

    //RecyclerView Adapter initialization and Declaration
    private AnalyzeImei_Adapter adapter;

    //EditText
    private EditText input;

    private Button clear;

    //TextView
    private TextView correct, notCorrect, notComplete;

    private InterstitialAd analyzerInterstitialAd;
    private AdView mAdView;
    private AdRequest adRequest;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.analyze_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Find Views
        input = getView().findViewById(R.id.input);
        //Button
        Button analyze_button = getView().findViewById(R.id.analyze);
        analyzer_recyclerView = getView().findViewById(R.id.analyzer_recyclerview);
        clear = getView().findViewById(R.id.clear);

        analyzer_recyclerView.setHasFixedSize(true);
        analyzer_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //[START Call to RecyclerView]

        //<editor-fold desc="Ads">
        mAdView = getView().findViewById(R.id.anadView);
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

        analyzerInterstitialAd = new InterstitialAd(getContext());
        analyzerInterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/6707866044");//IMEI.Analyzer.Interstitial
        analyzerInterstitialAd.loadAd(new AdRequest.Builder().build());
        analyzerInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                analyzerInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        //</editor-fold>

        analyze_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateInput()) {
                    return;
                }


                InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );
                inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);

                try {
                    calculate();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //RecyclerView Adapter Declaration
                    adapter = new AnalyzeImei_Adapter(getContext(), analyzerListItems);
                    analyzer_recyclerView.setAdapter(adapter);
                    //Animation
                    Context context = analyzer_recyclerView.getContext();
                    LayoutAnimationController controller;

                    controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fade_in);

                    //set anim
                    analyzer_recyclerView.setLayoutAnimation(controller);
                    analyzer_recyclerView.getAdapter().notifyDataSetChanged();
                    analyzer_recyclerView.scheduleLayoutAnimation();

                try {
                    analyzerLoadAds();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                correct.setVisibility(View.GONE);
                notComplete.setVisibility(View.GONE);
                notCorrect.setVisibility(View.GONE);
                clear.setVisibility(View.GONE);
                analyzer_recyclerView.setVisibility(View.GONE);

            }
        });

    }

    //Function calculates and returns luhn digit after an input of 14 digits
    private int luhn(String input_gotten) {
        int luhn;
        int split_part_num;
        int split_part_num2;
        int input_sum = 0;


        //This will split the input
        String [] parts = input_gotten.split("");

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

    private void calculate() {

        String imei_input = input.getText().toString();
        String[] imei_split = imei_input.split("");
        StringBuilder tac = new StringBuilder();
        StringBuilder rbi = new StringBuilder();
        StringBuilder met = new StringBuilder();
        StringBuilder fac = new StringBuilder();
        StringBuilder sn = new StringBuilder();
        StringBuilder check = new StringBuilder();
        StringBuilder firstFourteen = new StringBuilder();
        StringBuilder finalImei = new StringBuilder();
        String correct_imei = "";
        int luhn = 0;

        correct = getView().findViewById(R.id.correct);
        notCorrect = getView().findViewById(R.id.not_correct);
        notComplete = getView().findViewById(R.id.not_complete);


        int i = 0;

        for (String imei_splits : imei_split) {

            if (!imei_splits.equals("")) {

                i++;

                if (i < 7) {
                    tac.append(imei_splits);
                }
                if (i < 3) {
                    rbi.append(imei_splits);
                }
                if (i > 2 && i < 7) {
                    met.append(imei_splits);
                }
                if (i > 6 && i < 9) {
                    fac.append(imei_splits);
                }
                if (i > 8 && i < 15) {
                    sn.append(imei_splits);
                }

                //if the input was 15 digits, we extract only the luhn and check if it s correct
                if (i == 15) {
                    check.append(imei_splits);
                }

                //if input was 14 digits, we extract the 14 digits as "firstFourteen"
                if (i < 15) {
                    firstFourteen.append(imei_splits);

                }
            }


        }

        String firstFourteenString = String.valueOf(firstFourteen);

        //When the input was 14 digits, the app says the imei is not complete and calculates the 15th
        // digit which is the luhn
        if (i == 14) {
            correct.setVisibility(View.GONE);
            notComplete.setVisibility(View.VISIBLE);
            notCorrect.setVisibility(View.GONE);
            clear.setVisibility(View.VISIBLE);
            analyzer_recyclerView.setVisibility(View.VISIBLE);

            //here it calculates the luhn
            luhn = luhn(input.getText().toString());

            //here, it appends the 14 digits to the luhn
            finalImei.append(firstFourteenString).append(String.valueOf(luhn));

            //here it produces the complete imei
            correct_imei = String.valueOf(finalImei);


        } else if (i == 15) {
            luhn = luhn(String.valueOf(firstFourteen));
            if (luhn == Integer.parseInt(check.toString())) {
                correct.setVisibility(View.VISIBLE);
                notComplete.setVisibility(View.GONE);
                notCorrect.setVisibility(View.GONE);
                clear.setVisibility(View.VISIBLE);
                analyzer_recyclerView.setVisibility(View.VISIBLE);
                correct_imei = String.valueOf(imei_input);
            } else if (luhn != Integer.parseInt(String.valueOf(check))) {
                correct.setVisibility(View.GONE);
                notComplete.setVisibility(View.GONE);
                notCorrect.setVisibility(View.VISIBLE);
                clear.setVisibility(View.VISIBLE);
                analyzer_recyclerView.setVisibility(View.VISIBLE);
                finalImei.append(firstFourteenString).append(String.valueOf(luhn));
                correct_imei = String.valueOf(finalImei);

            }
        }

        String[] parts = Analyzer_Tab.this.getResources().getStringArray(R.array.imeiAnalysis);
        String[] partsValue = new String[]{correct_imei, String.valueOf(tac), String.valueOf(rbi), String.valueOf(met), String.valueOf(fac), String.valueOf(sn), String.valueOf(luhn)};

        analyzerListItems = new ArrayList<>();

        for (int j = 0; j < parts.length; j++) {

            AnalyzeImei_list_item analyzer_listItem = new AnalyzeImei_list_item(parts[j], partsValue[j]);

            analyzerListItems.add(analyzer_listItem);

        }

    }

    //Hide Keyboard when scrolling through tabs
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

    // [START Input Validation]
    private boolean validateInput() {
        boolean valid = true;

        String split = input.getText().toString();
        String Splits[] = split.split("");
        int j = 0;

        for (String part : Splits) {

            j++;

            if (j < 15) {

                input.setError(getResources().getString(R.string.Input_14_or_15_digits));
                valid = false;

            } else {
                input.setError(null);
                valid = true;
            }

        }

        return valid;
    }
    // [END Input Validation]

    //Tutorial
    public void analyzer_tutorial() {

        TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
                        TapTarget.forView(getView().findViewById(R.id.analyze), getString(R.string.analyze_any_imei),
                                getString(R.string.analyze_any_imei_tut))
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

    public void analyzerLoadAds() {

        if (analyzerInterstitialAd.isLoaded()) {
            analyzerInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

}
