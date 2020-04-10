package com.gibatekpro.imeigenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
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
import android.widget.ImageButton;
import android.widget.TextView;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.gibatekpro.imeigenerator.Adapters.Imei_Adapter;
import com.gibatekpro.imeigenerator.Adapters.PhoneImei_Phone_Adapter;
import com.gibatekpro.imeigenerator.ListItems.Imei_list_item;
import com.gibatekpro.imeigenerator.ListItems.PhoneImei_Phone_list_item;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static android.content.Context.CLIPBOARD_SERVICE;

public class PhoneImei_Tab extends Fragment {

    //RecyclerViews
    RecyclerView phoneRecyclerview;
    RecyclerView imeiRecyclerview;

    //Lists
    List<PhoneImei_Phone_list_item> phoneImei_phone_list_items = new ArrayList<>();
    List<Imei_list_item> imei_list_items = new ArrayList<>();

    //Adapters
    RecyclerView.Adapter phoneAdapter;
    RecyclerView.Adapter imeiAdapter;

    //This will be used to append all generated strings
    //For the purpose of copy to clipboard
    StringBuilder stringBuilder_clip;

    //All values of recyclerview
    String recycler_string;

    //Widgets
    TextView select, selected;
    ImageButton back;

    //layouts
    ConstraintLayout selectLay, selectedLay;

    private InterstitialAd deviceInterstitialAd;
    private AdView mAdView;
    private AdRequest adRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.phone_imei_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Find Views
        phoneRecyclerview = getView().findViewById(R.id.phones_recyclerview);
        imeiRecyclerview = getView().findViewById(R.id.imei_recyclerview);
        select = getView().findViewById(R.id.select);
        selected = getView().findViewById(R.id.selected);
        selectLay = getView().findViewById(R.id.lay1);
        selectedLay = getView().findViewById(R.id.lay2);
        back = getView().findViewById(R.id.back);

        //show devices
        phoneRecyclerView();

        //This initializes clipboard for copy function
        Object clipboardService = getContext().getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager) clipboardService;

        //visibility options
        phoneRecyclerview.setVisibility(View.VISIBLE);
        selectLay.setVisibility(View.VISIBLE);
        selectedLay.setVisibility(View.GONE);
        imeiRecyclerview.setVisibility(View.GONE);

        //<editor-fold desc="Ads">
        deviceInterstitialAd = new InterstitialAd(getContext());
        deviceInterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/2349641022");//IMEI.Device.Interstitial
        deviceInterstitialAd.loadAd(new AdRequest.Builder().build());
        deviceInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                deviceInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        mAdView = getView().findViewById(R.id.phadView);
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

        ((PhoneImei_Phone_Adapter) phoneAdapter).setOnItemClickListener(new PhoneImei_Phone_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                String selectedPhone = phoneImei_phone_list_items.get(position).getPhone();


                //visibility options
                phoneRecyclerview.setVisibility(View.GONE);
                selectLay.setVisibility(View.GONE);
                selectedLay.setVisibility(View.VISIBLE);
                imeiRecyclerview.setVisibility(View.VISIBLE);

                selected.setText(getString(R.string.imei_selected) + selectedPhone);

                //Reset data to zero
                recycler_string = "";

                stringBuilder_clip = new StringBuilder();

                //[START Remove Old Values]
                if (imei_list_items.size() > 0)
                    imei_list_items.clear();
                //[END Remove Old Values]

                for (int j = 0; j < 50; j++) {

                    try {
                        generator_plus_luhn(selectedPhone);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                imeiRecyclerview.setHasFixedSize(true);
                imeiRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                imeiAdapter = new Imei_Adapter(imei_list_items, getContext());
                imeiRecyclerview.setAdapter(imeiAdapter);

                //Animation
                Context context = imeiRecyclerview.getContext();
                LayoutAnimationController controller;

                controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fade_in);

                //set anim
                imeiRecyclerview.setLayoutAnimation(controller);
                imeiRecyclerview.getAdapter().notifyDataSetChanged();
                imeiRecyclerview.scheduleLayoutAnimation();

                //This is for the copy button on each recyclerview item
                ((Imei_Adapter) imeiAdapter).setOnItemClickListener(new Imei_Adapter.OnItemClickListener() {
                    @Override
                    public void onCopyClick(int position) {
                        String fish = imei_list_items.get(position).getImei();

                        ClipData clipData = ClipData.newPlainText("extracts", fish);
                        clipboardManager.setPrimaryClip(clipData);

                        Snackbar snackbar = Snackbar
                                .make(getView().findViewById(R.id.rel), R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT);
                        snackbar.show();

                    }
                });

                try {
                    deviceLoadAds();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //visibility options
                phoneRecyclerview.setVisibility(View.VISIBLE);
                selectLay.setVisibility(View.VISIBLE);
                selectedLay.setVisibility(View.GONE);
                imeiRecyclerview.setVisibility(View.GONE);

                try {
                    deviceLoadAds();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

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
    private void generator_plus_luhn(String phone) {

        //This string builder will append input to luhn
        StringBuilder stringBuilder = new StringBuilder();

        //the luhn accepts the output of the random_num_generator()
        //And use it to calculate the luhn
        String generated_imei = random_num_generator(getBrandImei(phone));
        String luhn = String.valueOf(luhn(generated_imei));

        stringBuilder.append(generated_imei).append(luhn);

        //This will be used to append all generated strings
        stringBuilder_clip.append(String.valueOf(stringBuilder)).append("\n");

        Imei_list_item list_item = new Imei_list_item(String.valueOf(stringBuilder));
        imei_list_items.add(list_item);

    }


    private void phoneRecyclerView() {

        if (phoneImei_phone_list_items.size()>0) {
            phoneImei_phone_list_items.clear();
        }

        String[] phones = getContext().getResources().getStringArray(R.array.device_arrays);
        for (String aPhone : phones) {

            PhoneImei_Phone_list_item phoneImei_phone_list_item = new PhoneImei_Phone_list_item(aPhone);

            phoneImei_phone_list_items.add(phoneImei_phone_list_item);

        }

        phoneRecyclerview.setHasFixedSize(true);
        phoneRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        phoneAdapter = new PhoneImei_Phone_Adapter(phoneImei_phone_list_items, getContext());
        phoneRecyclerview.setAdapter(phoneAdapter);

    }

    //get the selected dropdown list value
    private String getBrandImei(String phone) {

        //Device returned's initial imei values
        String imeiNum;

        switch (phone) {
            case "BlackBerry Z10":
                imeiNum = "35292205";
                break;
            case "BlackBerry Z3":
                imeiNum = "35265006";
                break;
            case "Gionee A1":
                imeiNum = "86385403";
                break;
            case "Gionee F103 Pro":
                imeiNum = "86159703";
                break;
            case "Gionee F205":
                imeiNum = "86855903";
                break;
            case "Gionee M5 Mini":
                imeiNum = "86974002";
                break;
            case "Gionee P5 W":
                imeiNum = "86910202";
                break;
            case "Gionee P7 Max":
                imeiNum = "86173603";
                break;
            case "Gionee S10":
                imeiNum = "86710303";
                break;
            case "Gionee S11 Lite":
                imeiNum = "86708403";
                break;
            case "Google Pixel 3":
                imeiNum = "35721609";
                break;
            case "HTC 10":
                imeiNum = "35426107";
                break;
            case "HTC A9":
                imeiNum = "35263707";
                break;
            case "HTC Desire 626":
                imeiNum = "35199307";
                break;
            case "HTC Desire 700":
                imeiNum = "35175406";
                break;
            case "HTC Desire 728":
                imeiNum = "35998806";
                break;
            case "HTC Desire 816":
                imeiNum = "35279506";
                break;
            case "HTC Desire 820":
                imeiNum = "35538306";
                break;
            case "HTC Desire U":
                imeiNum = "35721205";
                break;
            case "HTC One M7":
                imeiNum = "35597205";
                break;
            case "HTC One M8":
                imeiNum = "35871805";
                break;
            case "HTC One M9":
                imeiNum = "99000428";
                break;
            case "HTC One M9+":
                imeiNum = "35881206";
                break;
            case "HTC One X":
                imeiNum = "35658104";
                break;
            case "HTC Rhyme S510b":
                imeiNum = "35807104";
                break;
            case "HTC Sensation":
                imeiNum = "35644004";
                break;
            case "HTC U12+":
                imeiNum = "35322209";
                break;
            case "HTC Wildfire S":
                imeiNum = "35780504";
                break;
            case "Huawei Ascend Y210":
                imeiNum = "86809601";
                break;
            case "Huawei Mate 10 Lite":
                imeiNum = "86622103";
                break;
            case "Huawei P10":
                imeiNum = "86279003";
                break;
            case "Huawei P8 Max":
                imeiNum = "86778202";
                break;
            case "Huawei Y360-U61":
                imeiNum = "86726102";
                break;
            case "Huawei Y6 2018 (ATU-LX3)":
                imeiNum = "86726503";
                break;
            case "Huawei Y7":
                imeiNum = "86356003";
                break;
            case "Infinix Alpha X570":
                imeiNum = "35820205";
                break;
            case "Infinix Hot":
                imeiNum = "35749106";
                break;
            case "Infinix Hot 2":
                imeiNum = "35986906";
                break;
            case "Infinix Hot 3 X554":
                imeiNum = "35660207";
                break;
            case "Infinix Hot 5 Lite X559":
                imeiNum = "35880808";
                break;
            case "Infinix Hot Note X551":
                imeiNum = "35842906";
                break;
            case "Infinix Hot S X521":
                imeiNum = "35906407";
                break;
            case "Infinix Note 3 X601":
                imeiNum = "35997307";
                break;
            case "Infinix Note 4 X572":
                imeiNum = "35863908";
                break;
            case "Infinix S2 Pro X522":
                imeiNum = "35452708";
                break;
            case "Infinix S5":
                imeiNum = "35795010";
                break;
            case "Infinix Zero":
                imeiNum = "35585906";
                break;
            case "Infinix Zero 4 X555":
                imeiNum = "35230208";
                break;
            case "iPhone 4":
                imeiNum = "01279500";
                break;
            case "iPhone 5":
                imeiNum = "99000226";
                break;
            case "iPhone 5s":
                imeiNum = "35799805";
                break;
            case "iPhone 6":
                imeiNum = "35207406";
                break;
            case "iPhone 6+":
                imeiNum = "35924906";
                break;
            case "iPhone 6S+":
                imeiNum = "35877899";
                break;
            case "iPhone 8":
                imeiNum = "35377808";
                break;
            case "iPhone X":
                imeiNum = "35304509";
                break;
            case "iPhone XS":
                imeiNum = "35721609";
                break;
            case "itel INOTE 1502":
                imeiNum = "35847106";
                break;
            case "itel IT 1351":
                imeiNum = "35901505";
                break;
            case "itel IT 1355":
                imeiNum = "35928007";
                break;
            case "itel IT 1408":
                imeiNum = "35789907";
                break;
            case "itel IT 1701":
                imeiNum = "35875006";
                break;
            case "itel Prime 2":
                imeiNum = "35481807";
                break;
            case "LG G2 F320K":
                imeiNum = "35645705";
                break;
            case "LG G3 D855":
                imeiNum = "35567306";
                break;
            case "LG G3 VS985":
                imeiNum = "35245206";
                break;
            case "LG G4 H812":
                imeiNum = "35980306";
                break;
            case "LG G4 H818P":
                imeiNum = "35949206";
                break;
            case "LG G5 H830":
                imeiNum = "35882907";
                break;
            case "LG Leon H340Y":
                imeiNum = "35977706";
                break;
            case "LG Nexus 5 D821":
                imeiNum = "35349006";
                break;
            case "LG Optimus L7 P700":
                imeiNum = "35224805";
                break;
            case "LG Pro Lite D685":
                imeiNum = "35851308";
                break;
            case "LG Stylus 2 K520DY":
                imeiNum = "35795207";
                break;
            case "LG V10 H961N":
                imeiNum = "35382907";
                break;
            case "Microsoft Lumia 430":
                imeiNum = "35184407";
                break;
            case "Microsoft Lumia 525":
                imeiNum = "35918205";
                break;
            case "Microsoft Lumia 535":
                imeiNum = "35973006";
                break;
            case "Microsoft Lumia 540":
                imeiNum = "35860806";
                break;
            case "Microsoft Lumia 650":
                imeiNum = "35512607";
                break;
            case "Microsoft Lumia 730":
                imeiNum = "35515106";
                break;
            case "myPhone 5300":
                imeiNum = "35842904";
                break;
            case "Samsung Galaxy A7 SM-A710Y/DS":
                imeiNum = "35602307";
                break;
            case "Samsung Galaxy Core II":
                imeiNum = "35675306";
                break;
            case "Samsung Galaxy GP":
                imeiNum = "35721306";
                break;
            case "Samsung Galaxy Grand Prime SM-G530F":
                imeiNum = "35721306";
                break;
            case "Samsung Galaxy J1":
                imeiNum = "35734807";
                break;
            case "Samsung Galaxy J5 Prime SM-G570F":
                imeiNum = "35294808";
                break;
            case "Samsung Galaxy J7 SM-j700H/DS":
                imeiNum = "35888806";
                break;
            case "Samsung Galaxy Note GT-N7000":
                imeiNum = "35182305";
                break;
            case "Samsung Galaxy Note 2 GT-N7100":
                imeiNum = "35178505";
                break;
            case "Samsung Galaxy Note 5 SM-N920C":
                imeiNum = "35802307";
                break;
            case "Samsung Galaxy Note 10":
                imeiNum = "35533210";
                break;
            case "Samsung Galaxy Note 10 SM-N970F":
                imeiNum = "35754010";
                break;
            case "Samsung Galaxy S Duos 2 GT-S7582":
                imeiNum = "35155006";
                break;
            case "Samsung Galaxy S5 SM-G900F":
                imeiNum = "35255806";
                break;
            case "Samsung Galaxy S7 SM-G930F":
                imeiNum = "35355508";
                break;
            case "Samsung Galaxy S7 Edge SM-G935F":
                imeiNum = "35732907";
                break;
            case "Samsung Galaxy S8+ SM-G955FD":
                imeiNum = "35785108";
                break;
            case "Samsung Galaxy S9":
                imeiNum = "35271109";
                break;
            case "Samsung Galaxy S10":
                imeiNum = "35488910";
                break;
            case "Sony Xperia C":
                imeiNum = "35653405";
                break;
            case "Sony Xperia M C2005":
                imeiNum = "35809905";
                break;
            case "Sony Xperia Tipo ST21i2":
                imeiNum = "35485705";
                break;
            case "Sony Xperia XA Ultra F3213":
                imeiNum = "35605907";
                break;
            case "Sony Xperia XZ F8332":
                imeiNum = "35287508";
                break;
            case "Sony Xperia Z2 D6503":
                imeiNum = "35472406";
                break;
            case "Sony Xperia Z3+":
                imeiNum = "35905706";
                break;
            case "Sony Xperia Zultra C6802":
                imeiNum = "35765605";
                break;
            case "Tecno C8":
                imeiNum = "35238607";
                break;
            case "Tecno C9":
                imeiNum = "35807307";
                break;
            case "Tecno CX":
                imeiNum = "35553208";
                break;
            case "Tecno J8":
                imeiNum = "35676607";
                break;
            case "Tecno L8":
                imeiNum = "35708507";
                break;
            case "Tecno M7":
                imeiNum = "35892905";
                break;
            case "Tecno Phantom 5":
                imeiNum = "35226207";
                break;
            case "Tecno W1":
                imeiNum = "35521208";
                break;
            case "Tecno W3":
                imeiNum = "35277108";
                break;
            case "Tecno W4":
                imeiNum = "35727407";
                break;
            case "Tecno Y3":
                imeiNum = "35874806";
                break;
            case "Tecno Y6":
                imeiNum = "35238807";
                break;
            case "Xiaomi Mi 9T Pro":
                imeiNum = "86511004";
                break;
            case "Xiaomi Mi A1":
                imeiNum = "86756003";
                break;
            case "Xiaomi Mi A2":
                imeiNum = "86498404";
                break;
            case "Xiaomi Mi A3":
                imeiNum = "86848904";
                break;
            case "Xiaomi Mi Mix 2":
                imeiNum = "86903302";
                break;
            case "Xiaomi Redmi 4A":
                imeiNum = "86955402";
                break;
            case "Xiaomi Redmi 5A":
                imeiNum = "86841703";
                break;
            case "Xiaomi Redmi S2":
                imeiNum = "86804103";
                break;
            default:
                imeiNum = "";
        }

        return imeiNum;
    }

    //Tutorial
    public void specific_tutorial() {

        TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
                        TapTarget.forView(getView().findViewById(R.id.containers2), getString(R.string.spec_imeis),
                                getString(R.string.spec_imei_tuts))
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

    public void deviceLoadAds() {

        if (deviceInterstitialAd.isLoaded()) {
            deviceInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

}