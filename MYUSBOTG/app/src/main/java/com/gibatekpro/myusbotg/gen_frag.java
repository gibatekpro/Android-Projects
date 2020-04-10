package com.gibatekpro.myusbotg;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

/**
 * Created by Gibah on 11/24/2017.
 */

public class gen_frag extends Fragment {

    private String imeiNum = "";
    private Toolbar toolbar;
    private Button new_button, about_button, cust;
    private InterstitialAd mInterstitialAd;
    private EditText inputimei;
    private TextView corrimei1, corrimei2, corrimei3, corrimei4, corrimei5, instruct, imei1, imei2, imei3, imei4, imei5;
    private AdView mAdView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gen_frag, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.my_usb_otg_interstitial_dev_info));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        new_button = getView().findViewById(R.id.new_button);
        cust = getView().findViewById(R.id.cust);
        inputimei = getView().findViewById(R.id.inputimei);
        instruct = getView().findViewById(R.id.instruct);
        corrimei1 = getView().findViewById(R.id.corrimei1);
        corrimei2 = getView().findViewById(R.id.corrimei2);
        corrimei3 = getView().findViewById(R.id.corrimei3);
        corrimei4 = getView().findViewById(R.id.corrimei4);
        corrimei5 = getView().findViewById(R.id.corrimei5);
        imei1 = getView().findViewById(R.id.imei1);
        imei2 = getView().findViewById(R.id.imei2);
        imei3 = getView().findViewById(R.id.imei3);
        imei4 = getView().findViewById(R.id.imei4);
        imei5 = getView().findViewById(R.id.imei5);
        instruct.setTextColor(Color.parseColor("#000000"));
        instruct.setText(R.string.to_gen_cust_imei_input_desired_digit_below);

        MobileAds.initialize(getActivity().getApplicationContext(),
                "ca-app-pub-1488497497647050~2609508862");
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;


                String rins = inputimei.getText().toString();

                genran run = new genran(rins);

                for (int i = 0; i < rins.length(); i++) {
                    if (rins.charAt(i) != ' ') {
                        ++count;
                    }
                }
                if (count == 0) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if0());
                    imei2.setText(run.if0());
                    imei3.setText(run.if0());
                    imei4.setText(run.if0());
                    imei5.setText(run.if0());
                }
                if (count == 1) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if1());
                    imei2.setText(run.if1());
                    imei3.setText(run.if1());
                    imei4.setText(run.if1());
                    imei5.setText(run.if1());
                }
                if (count == 2) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if2());
                    imei2.setText(run.if2());
                    imei3.setText(run.if2());
                    imei4.setText(run.if2());
                    imei5.setText(run.if2());
                }
                if (count == 3) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if3());
                    imei2.setText(run.if3());
                    imei3.setText(run.if3());
                    imei4.setText(run.if3());
                    imei5.setText(run.if3());
                }
                if (count == 4) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if4());
                    imei2.setText(run.if4());
                    imei3.setText(run.if4());
                    imei4.setText(run.if4());
                    imei5.setText(run.if4());
                }
                if (count == 5) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if5());
                    imei2.setText(run.if5());
                    imei3.setText(run.if5());
                    imei4.setText(run.if5());
                    imei5.setText(run.if5());
                }
                if (count == 6) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if6());
                    imei2.setText(run.if6());
                    imei3.setText(run.if6());
                    imei4.setText(run.if6());
                    imei5.setText(run.if6());
                }
                if (count == 7) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if7());
                    imei2.setText(run.if7());
                    imei3.setText(run.if7());
                    imei4.setText(run.if7());
                    imei5.setText(run.if7());
                }
                if (count == 8) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if8());
                    imei2.setText(run.if8());
                    imei3.setText(run.if8());
                    imei4.setText(run.if8());
                    imei5.setText(run.if8());
                }
                if (count == 9) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if9());
                    imei2.setText(run.if9());
                    imei3.setText(run.if9());
                    imei4.setText(run.if9());
                    imei5.setText(run.if9());
                }
                if (count == 10) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if10());
                    imei2.setText(run.if10());
                    imei3.setText(run.if10());
                    imei4.setText(run.if10());
                    imei5.setText(run.if10());
                }
                if (count == 11) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if11());
                    imei2.setText(run.if11());
                    imei3.setText(run.if11());
                    imei4.setText(run.if11());
                    imei5.setText(run.if11());
                }
                if (count == 12) {
                    corrimei1.setText(R.string.custom_imei);
                    corrimei2.setText(R.string.custom_imei);
                    corrimei3.setText(R.string.custom_imei);
                    corrimei4.setText(R.string.custom_imei);
                    corrimei5.setText(R.string.custom_imei);

                    imei1.setText(run.if12());
                    imei2.setText(run.if12());
                    imei3.setText(run.if12());
                    imei4.setText(run.if12());
                    imei5.setText(run.if12());
                }

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }


        });


        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle(R.string.Devices);
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.device_arrays));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/

                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Device…")) {
                            generateImei1(String.valueOf(mSpinner.getSelectedItem()));
                            generateImei2(String.valueOf(mSpinner.getSelectedItem()));
                            generateImei3(String.valueOf(mSpinner.getSelectedItem()));
                            generateImei4(String.valueOf(mSpinner.getSelectedItem()));
                            generateImei5(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(getActivity(),
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                            corrimei1.setText(mSpinner.getSelectedItem().toString() + " IMEI");
                            corrimei2.setText(mSpinner.getSelectedItem().toString() + " IMEI");
                            corrimei3.setText(mSpinner.getSelectedItem().toString() + " IMEI");
                            corrimei4.setText(mSpinner.getSelectedItem().toString() + " IMEI");
                            corrimei5.setText(mSpinner.getSelectedItem().toString() + " IMEI");

                        }
                    }
                });
                mBuilder.setNegativeButton(R.string.Dismiss, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                dialog.show();

            }
        });


    }


    //get the selected dropdown list value


    private void getBrandImei(String phone) {

        switch (phone) {
            case "BlackBerry 8300":
                imeiNum = "35826501";
                break;
            case "BlackBerry 9220":
                imeiNum = "35541505";
                break;
            case "BlackBerry 9300":
                imeiNum = "35803304";
                break;
            case "BlackBerry 9315":
                imeiNum = "35309905";
                break;
            case "BlackBerry 9320":
                imeiNum = "35487205";
                break;
            case "BlackBerry 9360":
                imeiNum = "35892204";
                break;
            case "BlackBerry 9800":
                imeiNum = "35349104";
                break;
            case "BlackBerry 9900":
                imeiNum = "35968304";
                break;
            case "BlackBerry 9930":
                imeiNum = "35856504";
                break;
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
            case "Gionee M5 Mini":
                imeiNum = "86974002";
                break;
            case "Gionee P5 W":
                imeiNum = "86910202";
                break;
            case "Gionee P7 Max":
                imeiNum = "86173603";
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
            case "HTC Wildfire S":
                imeiNum = "35780504";
                break;
            case "Huawei P8 Max":
                imeiNum = "86778202";
                break;
            case "Huawei Y360-U61":
                imeiNum = "86726102";
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
            default:
                imeiNum = "";
        }
    }

    /**
     * Generates the IMEI of a phone
     *
     * @param phone The phone who's IMEI is to be generated
     */
    private void generateImei1(String phone) {
        getBrandImei(phone);        //initializes imeiNum with brand specific IMEI
        StringBuilder sb = new StringBuilder();

        Random rn = new Random();
        int maximum = 9;
        int minimum = 0;
        int range = maximum - minimum + 1;
        sb.append(imeiNum);
        Log.e("XXxxXX", "generateImei: " + imeiNum);
        if (imeiNum.isEmpty()) {        //generate random 14 digit numbers
            for (int i = 0; i < 14; i++) {
                sb.append(rn.nextInt(range) + minimum);
            }
        } else {        //generate the last 6 digits before luhn
            for (int i = 0; i < 6; i++) {
                sb.append(rn.nextInt(range) + minimum);
            }
        }
        sb.append(calculateLuhndigit(sb.toString()));       //calculate luhn digit of string in string builder
        imei1.setText(sb.toString());

    }

    private void generateImei2(String phone) {
        getBrandImei(phone);        //initializes imeiNum with brand specific IMEI
        StringBuilder sb = new StringBuilder();

        Random rn = new Random();
        int maximum = 9;
        int minimum = 0;
        int range = maximum - minimum + 1;
        sb.append(imeiNum);
        Log.e("XXxxXX", "generateImei: " + imeiNum);
        if (imeiNum.isEmpty()) {        //generate random 14 digit numbers
            for (int i = 0; i < 14; i++) {
                sb.append(rn.nextInt(range) + minimum);
            }
        } else {        //generate the last 6 digits before luhn
            for (int i = 0; i < 6; i++) {
                sb.append(rn.nextInt(range) + minimum);
            }
        }
        sb.append(calculateLuhndigit(sb.toString()));       //calculate luhn digit of string in string builder
        imei2.setText(sb.toString());

    }

    private void generateImei3(String phone) {
        getBrandImei(phone);        //initializes imeiNum with brand specific IMEI
        StringBuilder sb = new StringBuilder();

        Random rn = new Random();
        int maximum = 9;
        int minimum = 0;
        int range = maximum - minimum + 1;
        sb.append(imeiNum);
        Log.e("XXxxXX", "generateImei: " + imeiNum);
        if (imeiNum.isEmpty()) {        //generate random 14 digit numbers
            for (int i = 0; i < 14; i++) {
                sb.append(rn.nextInt(range) + minimum);
            }
        } else {        //generate the last 6 digits before luhn
            for (int i = 0; i < 6; i++) {
                sb.append(rn.nextInt(range) + minimum);
            }
        }
        sb.append(calculateLuhndigit(sb.toString()));       //calculate luhn digit of string in string builder
        imei3.setText(sb.toString());

    }

    private void generateImei4(String phone) {
        getBrandImei(phone);        //initializes imeiNum with brand specific IMEI
        StringBuilder sb = new StringBuilder();

        Random rn = new Random();
        int maximum = 9;
        int minimum = 0;
        int range = maximum - minimum + 1;
        sb.append(imeiNum);
        Log.e("XXxxXX", "generateImei: " + imeiNum);
        if (imeiNum.isEmpty()) {        //generate random 14 digit numbers
            for (int i = 0; i < 14; i++) {
                sb.append(rn.nextInt(range) + minimum);
            }
        } else {        //generate the last 6 digits before luhn
            for (int i = 0; i < 6; i++) {
                sb.append(rn.nextInt(range) + minimum);
            }
        }
        sb.append(calculateLuhndigit(sb.toString()));       //calculate luhn digit of string in string builder
        imei4.setText(sb.toString());

    }

    private void generateImei5(String phone) {
        getBrandImei(phone);        //initializes imeiNum with brand specific IMEI
        StringBuilder sb = new StringBuilder();

        Random rn = new Random();
        int maximum = 9;
        int minimum = 0;
        int range = maximum - minimum + 1;
        sb.append(imeiNum);
        Log.e("XXxxXX", "generateImei: " + imeiNum);
        if (imeiNum.isEmpty()) {        //generate random 14 digit numbers
            for (int i = 0; i < 14; i++) {
                sb.append(rn.nextInt(range) + minimum);
            }
        } else {        //generate the last 6 digits before luhn
            for (int i = 0; i < 6; i++) {
                sb.append(rn.nextInt(range) + minimum);
            }
        }
        sb.append(calculateLuhndigit(sb.toString()));       //calculate luhn digit of string in string builder
        imei5.setText(sb.toString());
    }

    public int calculateLuhndigit(String number) {
        try {
            Double.parseDouble(number.trim().replace(" ", ""));
            int sum = 0;
            boolean alt = true;
            for (int i = number.length() - 1; i >= 0; i--) {
                int n = Integer.parseInt(number.substring(i, i + 1));
                if (alt) {
                    n *= 2;
                    if (n > 9) {
                        n -= 9;
                    }
                }
                sum += n;
                alt = !alt;
            }
            return (sum % 10) == 0 ? 0 : 10 - (sum % 10);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return -1;
    }





}
