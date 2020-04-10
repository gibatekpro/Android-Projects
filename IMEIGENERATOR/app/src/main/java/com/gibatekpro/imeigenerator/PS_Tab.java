package com.gibatekpro.imeigenerator;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.gibatekpro.imeigenerator.Adapters.PS_Models_Adapter;
import com.gibatekpro.imeigenerator.Adapters.PS_Phones_Adapter;
import com.gibatekpro.imeigenerator.Adapters.PS_Specs_Adapter;
import com.gibatekpro.imeigenerator.ListItems.PS_Models_list_item;
import com.gibatekpro.imeigenerator.ListItems.PS_Phones_list_item;
import com.gibatekpro.imeigenerator.ListItems.PS_Specs_list_item;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.List;

public class PS_Tab extends Fragment {

    //RecyclerView Initialization
    RecyclerView models_recyclerView;

    //RecyclerView Initialization for devices
    RecyclerView device_recyclerView;

    //RecyclerView Initialization for Specs
    RecyclerView specs_recyclerView;
    RecyclerView.Adapter specs_adapter;

    //List Item Initialization
    private List<PS_Models_list_item> listItems = new ArrayList<>();

    //List Item Initialization
    private List<PS_Specs_list_item> specs_listItems;

    //List Item Initialization for devices
    private List<PS_Phones_list_item> device_listItems;
    Button back, compare;

    //public TextView view2;

    public static String[] selection;

    String itemClick = "";

    private PS_Models_Adapter adapter;

    private InterstitialAd psInterstitialAd;
    private AdView mAdView;
    private AdRequest adRequest;

    private int ad = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ps_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        back = getView().findViewById(R.id.back_lay);
        compare = getView().findViewById(R.id.compare);

        //RecyclerView
        models_recyclerView = getView().findViewById(R.id.models_recyclerview);
        models_recyclerView.setHasFixedSize(true);
        models_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PS_Models_Adapter(listItems, getContext());
        models_recyclerView.setAdapter(adapter);
        //[END RecyclerView Function]

        //RecyclerView for devices
        device_recyclerView = getView().findViewById(R.id.phones_recyclerview);
        device_recyclerView.setHasFixedSize(true);
        device_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        //RecyclerView for specs
        specs_recyclerView = getView().findViewById(R.id.specs_recyclerview);
        specs_recyclerView.setHasFixedSize(true);
        specs_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //[START Initialization]
        //listItems
        //[END Initialization]

        //[START Initialization]
        device_listItems = new ArrayList<>();
        //[END Initialization]

        //[START Initialization]
        specs_listItems = new ArrayList<>();
        //[END Initialization]

        back.setVisibility(View.GONE);
        models_recyclerView.setVisibility(View.GONE);
        specs_recyclerView.setVisibility(View.GONE);
        device_recyclerView.setVisibility(View.VISIBLE);
        compare.setVisibility(View.VISIBLE);

        //[START Devices display]
        if (device_listItems.size() > 0) {
            device_listItems.clear();
        }

        String[] device = PS_Tab.this.getResources().getStringArray(R.array.Devices);

        for (String aDevice : device) {

            PS_Phones_list_item device_listItem = new PS_Phones_list_item(aDevice);

            device_listItems.add(device_listItem);

        }

        //[START RecyclerView for devices Function]
        RecyclerView.Adapter device_adapter = new PS_Phones_Adapter(device_listItems, getContext());
        device_recyclerView.setAdapter(device_adapter);
        //[END RecyclerView Function]

        //[END Devices  display]


        //<editor-fold desc="Ads">
        mAdView = getView().findViewById(R.id.psadView);
        adRequest = new AdRequest.Builder().build();
        try {
            mAdView.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdView.setAdListener(new AdListener() {
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

        psInterstitialAd = new InterstitialAd(getContext());
        psInterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/8862120323");//IMEI.Ps.Interstitial
        psInterstitialAd.loadAd(new AdRequest.Builder().build());
        psInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                psInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        //</editor-fold>

        //This is for the copy button on esch recyclerview item
        ((PS_Phones_Adapter) device_adapter).setOnItemClickListener(new PS_Phones_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                //[START Devices display]
                if (listItems.size() > 0) {
                    listItems.clear();
                }

                back.setVisibility(View.VISIBLE);
                models_recyclerView.setVisibility(View.VISIBLE);
                specs_recyclerView.setVisibility(View.GONE);
                device_recyclerView.setVisibility(View.GONE);
                compare.setVisibility(View.GONE);

                itemClick = device_listItems.get(position).getPhone();

                String[] model = getDevice(itemClick);

                for (String aModel : model) {

                    PS_Models_list_item listItem = new PS_Models_list_item(aModel);

                    listItems.add(listItem);

                }

                //[END Devices  display]
                //[START RecyclerView for devices Function]

                //Animation
                Context context = models_recyclerView.getContext();
                LayoutAnimationController controller;

                controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fade_in);

                //set anim
                models_recyclerView.setLayoutAnimation(controller);
                models_recyclerView.getAdapter().notifyDataSetChanged();
                models_recyclerView.scheduleLayoutAnimation();

                ad++;

                try {
                    if (ad % 2 == 0) {

                        psLoadAds();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        try {
            //This is for the copy button on esch recyclerview item
            (adapter).setOnItemClickListener(new PS_Models_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {


                    //[START Specs display]
                    if (specs_listItems.size() > 0) {
                        specs_listItems.clear();
                    }

                    back.setVisibility(View.VISIBLE);
                    models_recyclerView.setVisibility(View.GONE);
                    specs_recyclerView.setVisibility(View.VISIBLE);
                    device_recyclerView.setVisibility(View.GONE);
                    compare.setVisibility(View.GONE);


                    String itemClick2 = listItems.get(position).getModel();

                    String[] specs = PS_Tab.this.getResources().getStringArray(R.array.Specs);
                    String[] specsValue = getDeviceForSpec(itemClick, itemClick2);

                    for (int i = 0; i < specs.length; i++) {

                        PS_Specs_list_item specs_listItem = new PS_Specs_list_item(specs[i], specsValue[i]);

                        specs_listItems.add(specs_listItem);

                    }

                    //[START RecyclerView for devices Function]
                    specs_adapter = new PS_Specs_Adapter(getContext(), specs_listItems);
                    specs_recyclerView.setAdapter(specs_adapter);
                    //[END RecyclerView Function]


                    //Animation
                    Context specs_context = specs_recyclerView.getContext();
                    LayoutAnimationController specs_controller;

                    specs_controller = AnimationUtils.loadLayoutAnimation(specs_context, R.anim.layout_fade_in);

                    //set anim
                    specs_recyclerView.setLayoutAnimation(specs_controller);
                    specs_recyclerView.getAdapter().notifyDataSetChanged();
                    specs_recyclerView.scheduleLayoutAnimation();


                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (models_recyclerView.getVisibility() == View.VISIBLE) {

                    back.setVisibility(View.GONE);
                    models_recyclerView.setVisibility(View.GONE);
                    specs_recyclerView.setVisibility(View.GONE);
                    device_recyclerView.setVisibility(View.VISIBLE);
                    compare.setVisibility(View.VISIBLE);

                } else if (specs_recyclerView.getVisibility() == View.VISIBLE) {

                    back.setVisibility(View.VISIBLE);
                    models_recyclerView.setVisibility(View.VISIBLE);
                    specs_recyclerView.setVisibility(View.GONE);
                    device_recyclerView.setVisibility(View.GONE);
                    compare.setVisibility(View.GONE);

                }

            }
        });

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle(R.string.number_of_phones);

                String[] num = new String[]{"2", "3", "4", "5", "6"};

                final Spinner mSpinner = mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item,
                        num);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*brain-box (gets value of spinner)*/

                        Intent intent = new Intent(getActivity(), CompareActivity.class);
                        intent.putExtra("tabs", mSpinner.getSelectedItem().toString());
                        startActivity(intent);

                    }
                });
                mBuilder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
            //
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


    public String[] getDevice(String selected) {
        String[] selecteds;

        switch (selected) {

            case "APPLE":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.apple_brands);
                break;
            case "BB":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bb_brands);
                break;
            case "HTC":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_brands);
                break;
            case "HUAWEI":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huawei_brands);
                break;
            case "INFINIX":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infinix_brands);
                break;
            case "LENOVO":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lenovo_brands);
                break;
            case "LG":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lg_brands);
                break;
            case "SAMSUNG":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.samsung_brands);
                break;
            case "TECNO":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tecno_brands);
                break;
            default:
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tecno_brands);

        }

        return selecteds;

    }

    public String[] getDeviceForSpec(String selected, String model) {
        String[] selecteds;

        switch (selected) {

            case "APPLE":
                selecteds = getapplebrand(model);
                break;
            case "BLACKBERRY":
                selecteds = getbbbrand(model);
                break;
            case "HTC":
                selecteds = gethtcbrand(model);
                break;
            case "HUAWEI":
                selecteds = gethuaweibrand(model);
                break;
            case "INFINIX":
                selecteds = getinfinixbrand(model);
                break;
            case "LENOVO":
                selecteds = getlenovobrand(model);
                break;
            case "LG":
                selecteds = getlgbrand(model);
                break;
            case "SAMSUNG":
                selecteds = getsamsungbrand(model);
                break;
            case "TECNO":
                selecteds = gettecnobrand(model);
                break;
            default:
                selecteds = gettecnobrand(model);

        }

        return selecteds;

    }

    private String[] getapplebrand(String applebrand) {
        String[] selecteds;

        switch (applebrand) {

            case "iPhone 4":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP4);
                break;

            case "iPhone 4s":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP4s);
                break;

            case "iPhone 5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP5);
                break;

            case "iPhone 5c":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP5c);
                break;

            case "iPhone 5s":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP5s);
                break;

            case "iPhone 6":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP6);
                break;

            case "iPhone 6 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP6p);
                break;

            case "iPhone 6s":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP6s);
                break;

            case "iPhone 6s Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP6sp);
                break;

            case "iPhone 7":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP7);
                break;

            case "iPhone 7 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP7p);
                break;

            case "iPhone 8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP8);
                break;

            case "iPhone 11 Pro Max":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP11pmax);
                break;

            default:
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.iP7p);

        }

        return selecteds;

    }

    private String[] getbbbrand(String bbbrand) {
        String[] selecteds;

        switch (bbbrand) {

            case "Leap":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbl);
                break;

            case "Passport":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbpass);
                break;

            case "Porsche Design P’9983":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbporchep);
                break;

            case "Porsche Design P’9983 Graphite":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbporchepg);
                break;

            case "Priv":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbpriv);
                break;

            case "Q5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbq5);
                break;

            case "Q10":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbq10);
                break;

            case "Z3":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbz3);
                break;

            case "Z10":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbz10);
                break;

            case "Z30":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbz30);
                break;
            default:
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.bbz30);
        }

        return selecteds;
    }

    private String[] gethtcbrand(String htcbrand) {
        String[] selecteds;

        switch (htcbrand) {

            case "10":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_10);
                break;

            case "10 Lifestyle":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_10_Lifestyle);
                break;

            case "Butterfly 3":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Butterfly_3);
                break;

            case "Desire 320":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_320);
                break;

            case "Desire 520":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_520);
                break;

            case "Desire 526":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_526);
                break;

            case "Desire 526G+ Dual SIM":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_526G_plus_Dual_SIM);
                break;

            case "Desire 530":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_530);
                break;

            case "Desire 626":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_626);
                break;

            case "Desire 626 (USA)":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_626_USA);
                break;

            case "Desire 630":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_630);
                break;

            case "Desire 728 Dual SIM":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_728_dual_SIM);
                break;

            case "Desire 825":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_825);
                break;

            case "Desire 826":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_826);
                break;

            case "Desire 828 Dual SIM":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_Desire_828_dual_SIM);
                break;

            case "One A9":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_One_A9);
                break;

            case "One M8s":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_One_M8s);
                break;

            case "One M9":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_One_M9);
                break;

            case "One M9+":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_One_M9PLUS);
                break;

            case "One M9s":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_One_M9s);
                break;

            case "One X9":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_One_X9);
                break;
            default:
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.htc_One_X9);

        }

        return selecteds;
    }

    private String[] gethuaweibrand(String huaweibrand) {
        String[] selecteds;

        switch (huaweibrand) {

            case "Ascend Y540":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweiascendy540);
                break;

            case "C199S":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweic199s);
                break;

            case "Enjoy 5s":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweie5s);
                break;

            case "G8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweig8);
                break;

            case "G9 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweig9p);
                break;

            case "Honor 4A":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonor4A);
                break;

            case "Honor 4C":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonor4C);
                break;

            case "Honor 5C":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonor5C);
                break;

            case "Honor 5C LTE":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonor5Cl);
                break;

            case "Honor 5X":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonor5X);
                break;

            case "Honor 7":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonor7);
                break;

            case "Honor 7i":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonor7i);
                break;

            case "Honor Bee":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonorbee);
                break;

            case "Honor Holly 2 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonorh2p);
                break;

            case "Honor Play 5X":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonorp5x);
                break;

            case "Honor V8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweihonorv8);
                break;

            case "Mate 8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweimate8);
                break;

            case "Nexus 6P":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweinexus6p);
                break;

            case "P8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweip8);
                break;

            case "P8 Lite":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweip8lite);
                break;

            case "P8 max":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweip8max);
                break;

            case "P9":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweip9);
                break;

            case "P9 Lite":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweip9lite);
                break;

            case "P9 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweip9plus);
                break;

            case "SnapTo G620":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweisnaptog620);
                break;

            case "Y3 II":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweiy3ii);
                break;

            case "Y336":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweiy336);
                break;

            case "Y5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweiy5);
                break;

            case "Y541":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweiy541);
                break;

            case "Y5 II":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweiy5ii);
                break;

            case "Y625":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweiy625);
                break;
            default:
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.huaweiy625);

        }
        return selecteds;
    }

    private String[] getinfinixbrand(String infinixbrand) {
        String[] selecteds;

        switch (infinixbrand) {

            case "Hot":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infhot);
                break;

            case "Hot 2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infhot2);
                break;

            case "Hot 3":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infhot3);
                break;

            case "Hot 3 LTE":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infhot3l);
                break;

            case "Hot 4":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infhot4);
                break;

            case "Hot 4 Lite":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infhot4l);
                break;

            case "Hot 4 Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infhot4p);
                break;

            case "Hot Note":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infhotn);
                break;

            case "Hot Note Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infhotnp);
                break;

            case "Hot S":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infhots);
                break;

            case "Note 2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infn2);
                break;

            case "Note 3":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infn3);
                break;

            case "Note 4 Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infn4);
                break;

            case "S2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infs2);
                break;

            case "S2 Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infs2p);
                break;

            case "Zero":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infz);
                break;

            case "Zero 2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infz2);
                break;

            case "Zero 2 LTE":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infz2l);
                break;

            case "Zero 3":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infz3);
                break;

            case "Zero 4":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infz4);
                break;

            case "Zero 4 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infz4p);
                break;
            default:
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.infz4p);

        }
        return selecteds;
    }

    private String[] getlenovobrand(String lenovobrand) {
        String[] selecteds;

        switch (lenovobrand) {

            case "A Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A_Plus);
                break;

            case "A616":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A616);
                break;

            case "A1000":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A1000);
                break;

            case "A1900":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A1900);
                break;

            case "A2010":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A2010);
                break;

            case "A3690":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A3690);
                break;

            case "A3900":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A3900);
                break;

            case "A5000":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A5000);
                break;

            case "A6000":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A6000);
                break;

            case "A6000 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A6000_Plus);
                break;

            case "A6000 Shot":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A6000_Shot);
                break;

            case "A7000 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A7000_Plus);
                break;

            case "A7000 Turbo":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_A7000_Turbo);
                break;

            case "C2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_C2);
                break;

            case "K3 Note":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_K3_Note);
                break;

            case "K3 Note Music":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_K3_Note_Music);
                break;

            case "K4 Note":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_K4_Note);
                break;

            case "K5 Note":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_K5_Note);
                break;

            case "K6":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_K6);
                break;

            case "K6 Note":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_K6_Note);
                break;

            case "K6 Power":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_K6_Power);
                break;

            case "K80":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_K80);
                break;

            case "Phab":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Phab);
                break;

            case "Phab Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Phab_Plus);
                break;

            case "Phab2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Phab2);
                break;

            case "Phab2 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Phab2_Plus);
                break;

            case "Phab2 Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Phab2_Pro);
                break;

            case "S60":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_S60);
                break;

            case "Vibe A":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_A);
                break;

            case "Vibe K5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_K5);
                break;

            case "Vibe K5 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_K5_Plus);
                break;

            case "Vibe P1":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_P1);
                break;

            case "Vibe P1 Turbo":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_P1_Turbo);
                break;

            case "Vibe P1m":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_P1m);
                break;

            case "Vibe S1":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_S1);
                break;

            case "Vibe S1 Lite":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_S1_Lite);
                break;

            case "Vibe X3":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_X3);
                break;

            case "Vibe X3 Lite":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_X3_Lite);
                break;

            case "Vibe X3 Youth Edition":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_Vibe_X3_Youth_Edition);
                break;

            case "ZUK Z2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_ZUK_Z2);
                break;
            default:
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.len_ZUK_Z2);

        }
        return selecteds;
    }

    private String[] getlgbrand(String lgbrand) {
        String[] selecteds;

        switch (lgbrand) {

            case "Class":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgclass);
                break;

            case "G5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgg5);
                break;

            case "G5 Lite":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgg5l);
                break;

            case "G Stylo":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgxstylo);
                break;

            case "K3":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgk3);
                break;

            case "K4":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgk4);
                break;

            case "K5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgk5);
                break;

            case "K7":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgk7);
                break;

            case "K8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgk8);
                break;

            case "K10":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgk10);
                break;

            case "Nexus 5X":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgnexus5x);
                break;

            case "Ray":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgray);
                break;

            case "Stylus 2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgxstylus2);
                break;

            case "Stylus 2 Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgxstylus2plus);
                break;

            case "V10":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgv10);
                break;

            case "Vista 2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgvista2);
                break;

            case "X5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgx5);
                break;

            case "X Cam":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgxcam);
                break;

            case "X Mach":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgxmach);
                break;

            case "X Power":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgxpow);
                break;

            case "X Screen":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgxscreen);
                break;

            case "X Skin":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgxskin);
                break;

            case "X Style":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgxstyle);
                break;
            default:
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.lgxstyle);

        }
        return selecteds;
    }

    private String[] getsamsungbrand(String samsungbrand) {
        String[] selecteds;

        switch (samsungbrand) {

            case "Galaxy A3":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyA3);
                break;

            case "Galaxy A5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyA5);
                break;

            case "Galaxy A7":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyA7);
                break;

            case "Galaxy A8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyA8);
                break;

            case "Galaxy C7 Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyC7Pro);
                break;

            case "Galaxy C9 Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyC9Pro);
                break;

            case "Galaxy Grand Prime Plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyGrandPrimePlus);
                break;

            case "Galaxy J1 mini prime":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.SamsungGalaxyJ1miniprime);
                break;

            case "Galaxy J2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyJ2);
                break;

            case "Galaxy J2 Prime":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyJ2Prime);
                break;

            case "Galaxy J2 Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyJ2Pro);
                break;

            case "Galaxy J3 Emerge":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyJ3Emerge);
                break;

            case "Galaxy J3 Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyJ3Pro);
                break;

            case "Galaxy J5 Prime":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyJ5Prime);
                break;

            case "Galaxy J7 Prime":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyJ7Prime);
                break;

            case "Galaxy Note 7":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyNote7);
                break;

            case "Galaxy On5 Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyOn5Pro);
                break;

            case "Galaxy On7":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyOn7);
                break;

            case "Galaxy On7 Pro":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyOn7Pro);
                break;

            case "Galaxy On8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyOn8);
                break;

            case "Galaxy S7 active":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyS7active);
                break;

            case "Galaxy Xcover 4":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.GalaxyXcover4);
                break;

            case "Z2":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.Z2);
                break;

            case "Z3 Corporate Edition":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.Z3CorporateEdition);
                break;
            default:
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.Z3CorporateEdition);

        }
        return selecteds;
    }

    private String[] gettecnobrand(String tecnobrand) {
        String[] selecteds;

        switch (tecnobrand) {

            case "Boom J5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tbj5);
                break;

            case "Boom J7":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tbj7);
                break;

            case "Boom J8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tbj8);
                break;

            case "Camon c5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tcamc5);
                break;

            case "Camon c7":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tcamc7);
                break;

            case "Camon c8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tcamc8);
                break;

            case "Camon c9":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tcamc9);
                break;

            case "Camon cx":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tcamcx);
                break;

            case "L5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tl5);
                break;

            case "L6":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tl6);
                break;

            case "L7":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tl7);
                break;

            case "L8":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tl8);
                break;

            case "L8 plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tl8p);
                break;

            case "L9 plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tl9p);
                break;

            case "M6":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tm6);
                break;

            case "Phantom 5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tphan5);
                break;

            case "Phantom 6":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tphan6);
                break;

            case "Phantom 6 plus":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tphan6p);
                break;

            case "Phantom 7":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tphan7);
                break;

            case "W3":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tw3);
                break;

            case "W4":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tw4);
                break;

            case "W5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tw5);
                break;

            case "W5 Lite":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.tw5l);
                break;

            case "Y4":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.ty4);
                break;

            case "Y5":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.ty5);
                break;

            case "Y6":
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.ty6);
                break;
            default:
                selecteds = PS_Tab.this.getResources().getStringArray(R.array.ty6);

        }

        return selecteds;
    }

    //Tutorial
    public void specsTutorial() {

        TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
                        TapTarget.forView(getView().findViewById(R.id.containers), getString(R.string.ps_tut),
                                getString(R.string.ps_tuts))
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
                                .targetRadius(100),
                        TapTarget.forView(getView().findViewById(R.id.compare), getString(R.string.ps_tutss),
                                getString(R.string.ps_t_tuts))
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
                                .targetRadius(80))                  // Specify the target radius (in dp)
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


    public void psLoadAds() {

        if (psInterstitialAd.isLoaded()) {
            psInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

}
