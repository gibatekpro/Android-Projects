package com.gibatekpro.psd;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;


public class MainSpecs extends AppCompatActivity {

    TextView name, named, model, modeled, opsys, opsysed, processor, processored, network, networked,
            appearance, appearanced, display, displayed, camera,
            cameraed, memory, memoryed, con_sensor, con_sensored, battery, batteryed, other, othered, tocompare;
    Button apple, bb, htc, huawei, infinix, lenovo, lg, tecno, samsung, compare;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private int REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainspecs);

        showTutorial();

        //englishAppRater.app_launched(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-1488497497647050~4670577950");

        mAdView = (AdView) findViewById(R.id.badView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.PSD_HOME_INTERSTITIAL));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        name = (TextView) findViewById(R.id.name);
        named = (TextView) findViewById(R.id.named);
        model = (TextView) findViewById(R.id.model);
        modeled = (TextView) findViewById(R.id.modeled);
        opsys = (TextView) findViewById(R.id.opsys);
        opsysed = (TextView) findViewById(R.id.opsysed);
        processor = (TextView) findViewById(R.id.processor);
        processored = (TextView) findViewById(R.id.processored);
        network = (TextView) findViewById(R.id.network);
        networked = (TextView) findViewById(R.id.networked);
        appearance = (TextView) findViewById(R.id.appearance);
        appearanced = (TextView) findViewById(R.id.appearanced);
        display = (TextView) findViewById(R.id.display);
        displayed = (TextView) findViewById(R.id.displayed);
        camera = (TextView) findViewById(R.id.camera);
        cameraed = (TextView) findViewById(R.id.cameraed);
        memory = (TextView) findViewById(R.id.memory);
        memoryed = (TextView) findViewById(R.id.memoryed);
        con_sensor = (TextView) findViewById(R.id.con_sensor);
        con_sensored = (TextView) findViewById(R.id.con_sensored);
        battery = (TextView) findViewById(R.id.battery);
        batteryed = (TextView) findViewById(R.id.batteryed);
        other = (TextView) findViewById(R.id.other);
        othered = (TextView) findViewById(R.id.othered);
        tocompare = (TextView) findViewById(R.id.tocompare);
        compare = (Button) findViewById(R.id.compare);
        apple = (Button) findViewById(R.id.apple);
        bb = (Button) findViewById(R.id.bb);
        htc = (Button) findViewById(R.id.htc);
        huawei = (Button) findViewById(R.id.huawei);
        infinix = (Button) findViewById(R.id.infinix);
        lenovo = (Button) findViewById(R.id.lenovo);
        lg = (Button) findViewById(R.id.lg);
        samsung = (Button) findViewById(R.id.samsung);
        tecno = (Button) findViewById(R.id.tecno);

        tocompare.setText(R.string.compare_instruction);


        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainSpecs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Number of Devices");

                String[] num = new String[]{"2", "3", "4", "5", "6"};

                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainSpecs.this,
                        android.R.layout.simple_spinner_item,
                        num);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/

                        Intent intent = new Intent(MainSpecs.this, CompareActivity.class);
                        intent.putExtra("tabs", mSpinner.getSelectedItem().toString());
                        startActivity(intent);

                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

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

        apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainSpecs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Apple");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainSpecs.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.apple_brands));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Brand…")) {
                            dialogInterface.dismiss();

                            //display message
                            getapplebrand(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(MainSpecs.this,
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainSpecs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("BlackBerry");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainSpecs.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.bb_brands));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Brand…")) {
                            dialogInterface.dismiss();

                            //display message
                            getbbbrand(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(MainSpecs.this,
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

        htc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainSpecs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("HTC");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainSpecs.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.htc_brands));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Brand…")) {
                            dialogInterface.dismiss();

                            //display message
                            gethtcbrand(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(MainSpecs.this,
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

        huawei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainSpecs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Huawei");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainSpecs.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.huawei_brands));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Brand…")) {
                            dialogInterface.dismiss();

                            //display message
                            gethuaweibrand(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(MainSpecs.this,
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

        infinix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainSpecs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Infinix");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainSpecs.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.infinix_brands));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Brand…")) {
                            dialogInterface.dismiss();

                            //display message
                            getinfinixbrand(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(MainSpecs.this,
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

        lenovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainSpecs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Lenovo");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainSpecs.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.lenovo_brands));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Brand…")) {
                            dialogInterface.dismiss();

                            //display message
                            getlenovobrand(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(MainSpecs.this,
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainSpecs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("LG");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainSpecs.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.lg_brands));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Brand…")) {
                            dialogInterface.dismiss();

                            //display message
                            getlgbrand(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(MainSpecs.this,
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

        samsung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainSpecs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Samsung");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainSpecs.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.samsung_brands));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Brand…")) {
                            dialogInterface.dismiss();

                            //display message
                            getsamsungbrand(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(MainSpecs.this,
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });


        tecno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainSpecs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Tecno");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainSpecs.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.tecno_brands));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Brand…")) {
                            dialogInterface.dismiss();

                            //display message
                            gettecnobrand(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(MainSpecs.this,
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

    }

    private void getapplebrand(String applebrand) {

        switch (applebrand) {

            case "iPhone 4":

                specifications();

                named.setText(getString(R.string.iP4_named));
                modeled.setText(getString(R.string.iP4_modeled));
                opsysed.setText(getString(R.string.iP4_opsysed));
                processored.setText(getString(R.string.iP4_processored));
                networked.setText(getString(R.string.iP4_networked));
                appearanced.setText(getString(R.string.iP4_appearanced));
                displayed.setText(getString(R.string.iP4_displayed));
                cameraed.setText(getString(R.string.iP4_cameraed));
                memoryed.setText(getString(R.string.iP4_memoryed));
                con_sensored.setText(getString(R.string.iP4_con_sensored));
                batteryed.setText(getString(R.string.iP4_batteryed));
                othered.setText(getString(R.string.iP4_othered));
                break;

            case "iPhone 4s":

                specifications();

                named.setText(getString(R.string.iP4s_named));
                modeled.setText(getString(R.string.iP4s_modeled));
                opsysed.setText(getString(R.string.iP4s_opsysed));
                processored.setText(getString(R.string.iP4s_processored));
                networked.setText(getString(R.string.iP4s_networked));
                appearanced.setText(getString(R.string.iP4s_appearanced));
                displayed.setText(getString(R.string.iP4s_displayed));
                cameraed.setText(getString(R.string.iP4s_cameraed));
                memoryed.setText(getString(R.string.iP4s_memoryed));
                con_sensored.setText(getString(R.string.iP4s_con_sensored));
                batteryed.setText(getString(R.string.iP4s_batteryed));
                othered.setText(getString(R.string.iP4s_othered));
                break;

            case "iPhone 5":

                specifications();

                named.setText(getString(R.string.iP5_named));
                modeled.setText(getString(R.string.iP5_modeled));
                opsysed.setText(getString(R.string.iP5_opsysed));
                processored.setText(getString(R.string.iP5_processored));
                networked.setText(getString(R.string.iP5_networked));
                appearanced.setText(getString(R.string.iP5_appearanced));
                displayed.setText(getString(R.string.iP5_displayed));
                cameraed.setText(getString(R.string.iP5_cameraed));
                memoryed.setText(getString(R.string.iP5_memoryed));
                con_sensored.setText(getString(R.string.iP5_con_sensored));
                batteryed.setText(getString(R.string.iP5_batteryed));
                othered.setText(getString(R.string.iP5_othered));
                break;

            case "iPhone 5c":

                specifications();

                named.setText(getString(R.string.iP5c_named));
                modeled.setText(getString(R.string.iP5c_modeled));
                opsysed.setText(getString(R.string.iP5c_opsysed));
                processored.setText(getString(R.string.iP5c_processored));
                networked.setText(getString(R.string.iP5c_networked));
                appearanced.setText(getString(R.string.iP5c_appearanced));
                displayed.setText(getString(R.string.iP5c_displayed));
                cameraed.setText(getString(R.string.iP5c_cameraed));
                memoryed.setText(getString(R.string.iP5c_memoryed));
                con_sensored.setText(getString(R.string.iP5c_con_sensored));
                batteryed.setText(getString(R.string.iP5c_batteryed));
                othered.setText(getString(R.string.iP5c_othered));
                break;

            case "iPhone 5s":

                specifications();

                named.setText(getString(R.string.iP5s_named));
                modeled.setText(getString(R.string.iP5s_modeled));
                opsysed.setText(getString(R.string.iP5s_opsysed));
                processored.setText(getString(R.string.iP5s_processored));
                networked.setText(getString(R.string.iP5s_networked));
                appearanced.setText(getString(R.string.iP5s_appearanced));
                displayed.setText(getString(R.string.iP5s_displayed));
                cameraed.setText(getString(R.string.iP5s_cameraed));
                memoryed.setText(getString(R.string.iP5s_memoryed));
                con_sensored.setText(getString(R.string.iP5s_con_sensored));
                batteryed.setText(getString(R.string.iP5s_batteryed));
                othered.setText(getString(R.string.iP5s_othered));
                break;

            case "iPhone 6":

                specifications();

                named.setText(getString(R.string.iP6_named));
                modeled.setText(getString(R.string.iP6_modeled));
                opsysed.setText(getString(R.string.iP6_opsysed));
                processored.setText(getString(R.string.iP6_processored));
                networked.setText(getString(R.string.iP6_networked));
                appearanced.setText(getString(R.string.iP6_appearanced));
                displayed.setText(getString(R.string.iP6_displayed));
                cameraed.setText(getString(R.string.iP6_cameraed));
                memoryed.setText(getString(R.string.iP6_memoryed));
                con_sensored.setText(getString(R.string.iP6_con_sensored));
                batteryed.setText(getString(R.string.iP6_batteryed));
                othered.setText(getString(R.string.iP6_othered));
                break;

            case "iPhone 6 Plus":

                specifications();

                named.setText(getString(R.string.iP6p_named));
                modeled.setText(getString(R.string.iP6p_modeled));
                opsysed.setText(getString(R.string.iP6p_opsysed));
                processored.setText(getString(R.string.iP6p_processored));
                networked.setText(getString(R.string.iP6p_networked));
                appearanced.setText(getString(R.string.iP6p_appearanced));
                displayed.setText(getString(R.string.iP6p_displayed));
                cameraed.setText(getString(R.string.iP6p_cameraed));
                memoryed.setText(getString(R.string.iP6p_memoryed));
                con_sensored.setText(getString(R.string.iP6p_con_sensored));
                batteryed.setText(getString(R.string.iP6p_batteryed));
                othered.setText(getString(R.string.iP6p_othered));
                break;

            case "iPhone 6s":

                specifications();

                named.setText(getString(R.string.iP6s_named));
                modeled.setText(getString(R.string.iP6s_modeled));
                opsysed.setText(getString(R.string.iP6s_opsysed));
                processored.setText(getString(R.string.iP6s_processored));
                networked.setText(getString(R.string.iP6s_networked));
                appearanced.setText(getString(R.string.iP6s_appearanced));
                displayed.setText(getString(R.string.iP6s_displayed));
                cameraed.setText(getString(R.string.iP6s_cameraed));
                memoryed.setText(getString(R.string.iP6s_memoryed));
                con_sensored.setText(getString(R.string.iP6s_con_sensored));
                batteryed.setText(getString(R.string.iP6s_batteryed));
                othered.setText(getString(R.string.iP6s_othered));
                break;

            case "iPhone 6s Plus":

                specifications();

                named.setText(getString(R.string.iP6sp_named));
                modeled.setText(getString(R.string.iP6sp_modeled));
                opsysed.setText(getString(R.string.iP6sp_opsysed));
                processored.setText(getString(R.string.iP6sp_processored));
                networked.setText(getString(R.string.iP6sp_networked));
                appearanced.setText(getString(R.string.iP6sp_appearanced));
                displayed.setText(getString(R.string.iP6sp_displayed));
                cameraed.setText(getString(R.string.iP6sp_cameraed));
                memoryed.setText(getString(R.string.iP6sp_memoryed));
                con_sensored.setText(getString(R.string.iP6sp_con_sensored));
                batteryed.setText(getString(R.string.iP6sp_batteryed));
                othered.setText(getString(R.string.iP6sp_othered));
                break;

            case "iPhone 7":

                specifications();

                named.setText(getString(R.string.iP7_named));
                modeled.setText(getString(R.string.iP7_modeled));
                opsysed.setText(getString(R.string.iP7_opsysed));
                processored.setText(getString(R.string.iP7_processored));
                networked.setText(getString(R.string.iP7_networked));
                appearanced.setText(getString(R.string.iP7_appearanced));
                displayed.setText(getString(R.string.iP7_displayed));
                cameraed.setText(getString(R.string.iP7_cameraed));
                memoryed.setText(getString(R.string.iP7_memoryed));
                con_sensored.setText(getString(R.string.iP7_con_sensored));
                batteryed.setText(getString(R.string.iP7_batteryed));
                othered.setText(getString(R.string.iP7_othered));
                break;

            case "iPhone 7 Plus":

                specifications();

                named.setText(getString(R.string.iP7p_named));
                modeled.setText(getString(R.string.iP7p_modeled));
                opsysed.setText(getString(R.string.iP7p_opsysed));
                processored.setText(getString(R.string.iP7p_processored));
                networked.setText(getString(R.string.iP7p_networked));
                appearanced.setText(getString(R.string.iP7p_appearanced));
                displayed.setText(getString(R.string.iP7p_displayed));
                cameraed.setText(getString(R.string.iP7p_cameraed));
                memoryed.setText(getString(R.string.iP7p_memoryed));
                con_sensored.setText(getString(R.string.iP7p_con_sensored));
                batteryed.setText(getString(R.string.iP7p_batteryed));
                othered.setText(getString(R.string.iP7p_othered));
                break;

        }
    }

    private void getbbbrand(String bbbrand) {

        switch (bbbrand) {

            case "Leap":

                specifications();

                named.setText(getString(R.string.bbl_named));
                modeled.setText(getString(R.string.bbl_modeled));
                opsysed.setText(getString(R.string.bbl_opsysed));
                processored.setText(getString(R.string.bbl_processored));
                networked.setText(getString(R.string.bbl_networked));
                appearanced.setText(getString(R.string.bbl_appearanced));
                displayed.setText(getString(R.string.bbl_displayed));
                cameraed.setText(getString(R.string.bbl_cameraed));
                memoryed.setText(getString(R.string.bbl_memoryed));
                con_sensored.setText(getString(R.string.bbl_con_sensored));
                batteryed.setText(getString(R.string.bbl_batteryed));
                othered.setText(getString(R.string.bbl_othered));
                break;

            case "Passport":

                specifications();

                named.setText(getString(R.string.bbpass_named));
                modeled.setText(getString(R.string.bbpass_modeled));
                opsysed.setText(getString(R.string.bbpass_opsysed));
                processored.setText(getString(R.string.bbpass_processored));
                networked.setText(getString(R.string.bbpass_networked));
                appearanced.setText(getString(R.string.bbpass_appearanced));
                displayed.setText(getString(R.string.bbpass_displayed));
                cameraed.setText(getString(R.string.bbpass_cameraed));
                memoryed.setText(getString(R.string.bbpass_memoryed));
                con_sensored.setText(getString(R.string.bbpass_con_sensored));
                batteryed.setText(getString(R.string.bbpass_batteryed));
                othered.setText(getString(R.string.bbpass_othered));
                break;

            case "Porsche Design P’9983":

                specifications();

                named.setText(getString(R.string.bbporchep_named));
                modeled.setText(getString(R.string.bbporchep_modeled));
                opsysed.setText(getString(R.string.bbporchep_opsysed));
                processored.setText(getString(R.string.bbporchep_processored));
                networked.setText(getString(R.string.bbporchep_networked));
                appearanced.setText(getString(R.string.bbporchep_appearanced));
                displayed.setText(getString(R.string.bbporchep_displayed));
                cameraed.setText(getString(R.string.bbporchep_cameraed));
                memoryed.setText(getString(R.string.bbporchep_memoryed));
                con_sensored.setText(getString(R.string.bbporchep_con_sensored));
                batteryed.setText(getString(R.string.bbporchep_batteryed));
                othered.setText(getString(R.string.bbporchep_othered));
                break;

            case "Porsche Design P’9983 Graphite":

                specifications();

                named.setText(getString(R.string.bbporchepg_named));
                modeled.setText(getString(R.string.bbporchepg_modeled));
                opsysed.setText(getString(R.string.bbporchepg_opsysed));
                processored.setText(getString(R.string.bbporchepg_processored));
                networked.setText(getString(R.string.bbporchepg_networked));
                appearanced.setText(getString(R.string.bbporchepg_appearanced));
                displayed.setText(getString(R.string.bbporchepg_displayed));
                cameraed.setText(getString(R.string.bbporchepg_cameraed));
                memoryed.setText(getString(R.string.bbporchepg_memoryed));
                con_sensored.setText(getString(R.string.bbporchepg_con_sensored));
                batteryed.setText(getString(R.string.bbporchepg_batteryed));
                othered.setText(getString(R.string.bbporchepg_othered));
                break;

            case "Priv":

                specifications();

                named.setText(getString(R.string.bbpriv_named));
                modeled.setText(getString(R.string.bbpriv_modeled));
                opsysed.setText(getString(R.string.bbpriv_opsysed));
                processored.setText(getString(R.string.bbpriv_processored));
                networked.setText(getString(R.string.bbpriv_networked));
                appearanced.setText(getString(R.string.bbpriv_appearanced));
                displayed.setText(getString(R.string.bbpriv_displayed));
                cameraed.setText(getString(R.string.bbpriv_cameraed));
                memoryed.setText(getString(R.string.bbpriv_memoryed));
                con_sensored.setText(getString(R.string.bbpriv_con_sensored));
                batteryed.setText(getString(R.string.bbpriv_batteryed));
                othered.setText(getString(R.string.bbpriv_othered));
                break;

            case "Q5":

                specifications();

                named.setText(getString(R.string.bbq5_named));
                modeled.setText(getString(R.string.bbq5_modeled));
                opsysed.setText(getString(R.string.bbq5_opsysed));
                processored.setText(getString(R.string.bbq5_processored));
                networked.setText(getString(R.string.bbq5_networked));
                appearanced.setText(getString(R.string.bbq5_appearanced));
                displayed.setText(getString(R.string.bbq5_displayed));
                cameraed.setText(getString(R.string.bbq5_cameraed));
                memoryed.setText(getString(R.string.bbq5_memoryed));
                con_sensored.setText(getString(R.string.bbq5_con_sensored));
                batteryed.setText(getString(R.string.bbq5_batteryed));
                othered.setText(getString(R.string.bbq5_othered));
                break;

            case "Q10":

                specifications();

                named.setText(getString(R.string.bbq10_named));
                modeled.setText(getString(R.string.bbq10_modeled));
                opsysed.setText(getString(R.string.bbq10_opsysed));
                processored.setText(getString(R.string.bbq10_processored));
                networked.setText(getString(R.string.bbq10_networked));
                appearanced.setText(getString(R.string.bbq10_appearanced));
                displayed.setText(getString(R.string.bbq10_displayed));
                cameraed.setText(getString(R.string.bbq10_cameraed));
                memoryed.setText(getString(R.string.bbq10_memoryed));
                con_sensored.setText(getString(R.string.bbq10_con_sensored));
                batteryed.setText(getString(R.string.bbq10_batteryed));
                othered.setText(getString(R.string.bbq10_othered));
                break;

            case "Z3":

                specifications();

                named.setText(getString(R.string.bbz3_named));
                modeled.setText(getString(R.string.bbz3_modeled));
                opsysed.setText(getString(R.string.bbz3_opsysed));
                processored.setText(getString(R.string.bbz3_processored));
                networked.setText(getString(R.string.bbz3_networked));
                appearanced.setText(getString(R.string.bbz3_appearanced));
                displayed.setText(getString(R.string.bbz3_displayed));
                cameraed.setText(getString(R.string.bbz3_cameraed));
                memoryed.setText(getString(R.string.bbz3_memoryed));
                con_sensored.setText(getString(R.string.bbz3_con_sensored));
                batteryed.setText(getString(R.string.bbz3_batteryed));
                othered.setText(getString(R.string.bbz3_othered));
                break;

            case "Z10":

                specifications();

                named.setText(getString(R.string.bbz10_named));
                modeled.setText(getString(R.string.bbz10_modeled));
                opsysed.setText(getString(R.string.bbz10_opsysed));
                processored.setText(getString(R.string.bbz10_processored));
                networked.setText(getString(R.string.bbz10_networked));
                appearanced.setText(getString(R.string.bbz10_appearanced));
                displayed.setText(getString(R.string.bbz10_displayed));
                cameraed.setText(getString(R.string.bbz10_cameraed));
                memoryed.setText(getString(R.string.bbz10_memoryed));
                con_sensored.setText(getString(R.string.bbz10_con_sensored));
                batteryed.setText(getString(R.string.bbz10_batteryed));
                othered.setText(getString(R.string.bbz10_othered));
                break;

            case "Z30":

                specifications();

                named.setText(getString(R.string.bbz30_named));
                modeled.setText(getString(R.string.bbz30_modeled));
                opsysed.setText(getString(R.string.bbz30_opsysed));
                processored.setText(getString(R.string.bbz30_processored));
                networked.setText(getString(R.string.bbz30_networked));
                appearanced.setText(getString(R.string.bbz30_appearanced));
                displayed.setText(getString(R.string.bbz30_displayed));
                cameraed.setText(getString(R.string.bbz30_cameraed));
                memoryed.setText(getString(R.string.bbz30_memoryed));
                con_sensored.setText(getString(R.string.bbz30_con_sensored));
                batteryed.setText(getString(R.string.bbz30_batteryed));
                othered.setText(getString(R.string.bbz30_othered));
                break;

        }
    }

    private void gethtcbrand(String htcbrand) {

        switch (htcbrand) {

            case "10":

                specifications();

                named.setText(getString(R.string.htc_10_named));
                modeled.setText(getString(R.string.htc_10_modeled));
                opsysed.setText(getString(R.string.htc_10_opsysed));
                processored.setText(getString(R.string.htc_10_processored));
                networked.setText(getString(R.string.htc_10_networked));
                appearanced.setText(getString(R.string.htc_10_appearanced));
                displayed.setText(getString(R.string.htc_10_displayed));
                cameraed.setText(getString(R.string.htc_10_cameraed));
                memoryed.setText(getString(R.string.htc_10_memoryed));
                con_sensored.setText(getString(R.string.htc_10_con_sensored));
                batteryed.setText(getString(R.string.htc_10_batteryed));
                othered.setText(getString(R.string.htc_10_othered));
                break;

            case "10 Lifestyle":

                specifications();

                named.setText(getString(R.string.htc_10_Lifestyle_named));
                modeled.setText(getString(R.string.htc_10_Lifestyle_modeled));
                opsysed.setText(getString(R.string.htc_10_Lifestyle_opsysed));
                processored.setText(getString(R.string.htc_10_Lifestyle_processored));
                networked.setText(getString(R.string.htc_10_Lifestyle_networked));
                appearanced.setText(getString(R.string.htc_10_Lifestyle_appearanced));
                displayed.setText(getString(R.string.htc_10_Lifestyle_displayed));
                cameraed.setText(getString(R.string.htc_10_Lifestyle_cameraed));
                memoryed.setText(getString(R.string.htc_10_Lifestyle_memoryed));
                con_sensored.setText(getString(R.string.htc_10_Lifestyle_con_sensored));
                batteryed.setText(getString(R.string.htc_10_Lifestyle_batteryed));
                othered.setText(getString(R.string.htc_10_Lifestyle_othered));
                break;

            case "Butterfly 3":

                specifications();

                named.setText(getString(R.string.htc_Butterfly_3_named));
                modeled.setText(getString(R.string.htc_Butterfly_3_modeled));
                opsysed.setText(getString(R.string.htc_Butterfly_3_opsysed));
                processored.setText(getString(R.string.htc_Butterfly_3_processored));
                networked.setText(getString(R.string.htc_Butterfly_3_networked));
                appearanced.setText(getString(R.string.htc_Butterfly_3_appearanced));
                displayed.setText(getString(R.string.htc_Butterfly_3_displayed));
                cameraed.setText(getString(R.string.htc_Butterfly_3_cameraed));
                memoryed.setText(getString(R.string.htc_Butterfly_3_memoryed));
                con_sensored.setText(getString(R.string.htc_Butterfly_3_con_sensored));
                batteryed.setText(getString(R.string.htc_Butterfly_3_batteryed));
                othered.setText(getString(R.string.htc_Butterfly_3_othered));
                break;

            case "Desire 320":

                specifications();

                named.setText(getString(R.string.htc_Desire_320_named));
                modeled.setText(getString(R.string.htc_Desire_320_modeled));
                opsysed.setText(getString(R.string.htc_Desire_320_opsysed));
                processored.setText(getString(R.string.htc_Desire_320_processored));
                networked.setText(getString(R.string.htc_Desire_320_networked));
                appearanced.setText(getString(R.string.htc_Desire_320_appearanced));
                displayed.setText(getString(R.string.htc_Desire_320_displayed));
                cameraed.setText(getString(R.string.htc_Desire_320_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_320_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_320_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_320_batteryed));
                othered.setText(getString(R.string.htc_Desire_320_othered));
                break;

            case "Desire 520":

                specifications();

                named.setText(getString(R.string.htc_Desire_520_named));
                modeled.setText(getString(R.string.htc_Desire_520_modeled));
                opsysed.setText(getString(R.string.htc_Desire_520_opsysed));
                processored.setText(getString(R.string.htc_Desire_520_processored));
                networked.setText(getString(R.string.htc_Desire_520_networked));
                appearanced.setText(getString(R.string.htc_Desire_520_appearanced));
                displayed.setText(getString(R.string.htc_Desire_520_displayed));
                cameraed.setText(getString(R.string.htc_Desire_520_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_520_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_520_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_520_batteryed));
                othered.setText(getString(R.string.htc_Desire_520_othered));
                break;

            case "Desire 526":

                specifications();

                named.setText(getString(R.string.htc_Desire_526_named));
                modeled.setText(getString(R.string.htc_Desire_526_modeled));
                opsysed.setText(getString(R.string.htc_Desire_526_opsysed));
                processored.setText(getString(R.string.htc_Desire_526_processored));
                networked.setText(getString(R.string.htc_Desire_526_networked));
                appearanced.setText(getString(R.string.htc_Desire_526_appearanced));
                displayed.setText(getString(R.string.htc_Desire_526_displayed));
                cameraed.setText(getString(R.string.htc_Desire_526_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_526_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_526_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_526_batteryed));
                othered.setText(getString(R.string.htc_Desire_526_othered));
                break;

            case "Desire 526G+ Dual SIM":

                specifications();

                named.setText(getString(R.string.htc_Desire_526G_Dual_SIM_named));
                modeled.setText(getString(R.string.htc_Desire_526G_Dual_SIM_modeled));
                opsysed.setText(getString(R.string.htc_Desire_526G_Dual_SIM_opsysed));
                processored.setText(getString(R.string.htc_Desire_526G_Dual_SIM_processored));
                networked.setText(getString(R.string.htc_Desire_526G_Dual_SIM_networked));
                appearanced.setText(getString(R.string.htc_Desire_526G_Dual_SIM_appearanced));
                displayed.setText(getString(R.string.htc_Desire_526G_Dual_SIM_displayed));
                cameraed.setText(getString(R.string.htc_Desire_526G_Dual_SIM_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_526G_Dual_SIM_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_526G_Dual_SIM_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_526G_Dual_SIM_batteryed));
                othered.setText(getString(R.string.htc_Desire_526G_Dual_SIM_othered));
                break;

            case "Desire 530":

                specifications();

                named.setText(getString(R.string.htc_Desire_530_named));
                modeled.setText(getString(R.string.htc_Desire_530_modeled));
                opsysed.setText(getString(R.string.htc_Desire_530_opsysed));
                processored.setText(getString(R.string.htc_Desire_530_processored));
                networked.setText(getString(R.string.htc_Desire_530_networked));
                appearanced.setText(getString(R.string.htc_Desire_530_appearanced));
                displayed.setText(getString(R.string.htc_Desire_530_displayed));
                cameraed.setText(getString(R.string.htc_Desire_530_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_530_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_530_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_530_batteryed));
                othered.setText(getString(R.string.htc_Desire_530_othered));
                break;

            case "Desire 626 (USA)":

                specifications();

                named.setText(getString(R.string.htc_Desire_626_USA_named));
                modeled.setText(getString(R.string.htc_Desire_626_USA_modeled));
                opsysed.setText(getString(R.string.htc_Desire_626_USA_opsysed));
                processored.setText(getString(R.string.htc_Desire_626_USA_processored));
                networked.setText(getString(R.string.htc_Desire_626_USA_networked));
                appearanced.setText(getString(R.string.htc_Desire_626_USA_appearanced));
                displayed.setText(getString(R.string.htc_Desire_626_USA_displayed));
                cameraed.setText(getString(R.string.htc_Desire_626_USA_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_626_USA_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_626_USA_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_626_USA_batteryed));
                othered.setText(getString(R.string.htc_Desire_626_USA_othered));
                break;

            case "Desire 630":

                specifications();

                named.setText(getString(R.string.htc_Desire_630_named));
                modeled.setText(getString(R.string.htc_Desire_630_modeled));
                opsysed.setText(getString(R.string.htc_Desire_630_opsysed));
                processored.setText(getString(R.string.htc_Desire_630_processored));
                networked.setText(getString(R.string.htc_Desire_630_networked));
                appearanced.setText(getString(R.string.htc_Desire_630_appearanced));
                displayed.setText(getString(R.string.htc_Desire_630_displayed));
                cameraed.setText(getString(R.string.htc_Desire_630_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_630_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_630_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_630_batteryed));
                othered.setText(getString(R.string.htc_Desire_630_othered));
                break;

            case "Desire 728 Dual SIM":

                specifications();

                named.setText(getString(R.string.htc_Desire_728_dual_SIM_named));
                modeled.setText(getString(R.string.htc_Desire_728_dual_SIM_modeled));
                opsysed.setText(getString(R.string.htc_Desire_728_dual_SIM_opsysed));
                processored.setText(getString(R.string.htc_Desire_728_dual_SIM_processored));
                networked.setText(getString(R.string.htc_Desire_728_dual_SIM_networked));
                appearanced.setText(getString(R.string.htc_Desire_728_dual_SIM_appearanced));
                displayed.setText(getString(R.string.htc_Desire_728_dual_SIM_displayed));
                cameraed.setText(getString(R.string.htc_Desire_728_dual_SIM_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_728_dual_SIM_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_728_dual_SIM_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_728_dual_SIM_batteryed));
                othered.setText(getString(R.string.htc_Desire_728_dual_SIM_othered));
                break;

            case "Desire 825":

                specifications();

                named.setText(getString(R.string.htc_Desire_825_named));
                modeled.setText(getString(R.string.htc_Desire_825_modeled));
                opsysed.setText(getString(R.string.htc_Desire_825_opsysed));
                processored.setText(getString(R.string.htc_Desire_825_processored));
                networked.setText(getString(R.string.htc_Desire_825_networked));
                appearanced.setText(getString(R.string.htc_Desire_825_appearanced));
                displayed.setText(getString(R.string.htc_Desire_825_displayed));
                cameraed.setText(getString(R.string.htc_Desire_825_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_825_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_825_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_825_batteryed));
                othered.setText(getString(R.string.htc_Desire_825_othered));
                break;

            case "Desire 826":

                specifications();

                named.setText(getString(R.string.htc_Desire_826_named));
                modeled.setText(getString(R.string.htc_Desire_826_modeled));
                opsysed.setText(getString(R.string.htc_Desire_826_opsysed));
                processored.setText(getString(R.string.htc_Desire_826_processored));
                networked.setText(getString(R.string.htc_Desire_826_networked));
                appearanced.setText(getString(R.string.htc_Desire_826_appearanced));
                displayed.setText(getString(R.string.htc_Desire_826_displayed));
                cameraed.setText(getString(R.string.htc_Desire_826_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_826_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_826_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_826_batteryed));
                othered.setText(getString(R.string.htc_Desire_826_othered));
                break;

            case "Desire 828 Dual SIM":

                specifications();

                named.setText(getString(R.string.htc_Desire_828_dual_SIM_named));
                modeled.setText(getString(R.string.htc_Desire_828_dual_SIM_modeled));
                opsysed.setText(getString(R.string.htc_Desire_828_dual_SIM_opsysed));
                processored.setText(getString(R.string.htc_Desire_828_dual_SIM_processored));
                networked.setText(getString(R.string.htc_Desire_828_dual_SIM_networked));
                appearanced.setText(getString(R.string.htc_Desire_828_dual_SIM_appearanced));
                displayed.setText(getString(R.string.htc_Desire_828_dual_SIM_displayed));
                cameraed.setText(getString(R.string.htc_Desire_828_dual_SIM_cameraed));
                memoryed.setText(getString(R.string.htc_Desire_828_dual_SIM_memoryed));
                con_sensored.setText(getString(R.string.htc_Desire_828_dual_SIM_con_sensored));
                batteryed.setText(getString(R.string.htc_Desire_828_dual_SIM_batteryed));
                othered.setText(getString(R.string.htc_Desire_828_dual_SIM_othered));
                break;

            case "One A9":

                specifications();

                named.setText(getString(R.string.htc_One_A9_named));
                modeled.setText(getString(R.string.htc_One_A9_modeled));
                opsysed.setText(getString(R.string.htc_One_A9_opsysed));
                processored.setText(getString(R.string.htc_One_A9_processored));
                networked.setText(getString(R.string.htc_One_A9_networked));
                appearanced.setText(getString(R.string.htc_One_A9_appearanced));
                displayed.setText(getString(R.string.htc_One_A9_displayed));
                cameraed.setText(getString(R.string.htc_One_A9_cameraed));
                memoryed.setText(getString(R.string.htc_One_A9_memoryed));
                con_sensored.setText(getString(R.string.htc_One_A9_con_sensored));
                batteryed.setText(getString(R.string.htc_One_A9_batteryed));
                othered.setText(getString(R.string.htc_One_A9_othered));
                break;

            case "One M8s":

                specifications();

                named.setText(getString(R.string.htc_One_M8s_named));
                modeled.setText(getString(R.string.htc_One_M8s_modeled));
                opsysed.setText(getString(R.string.htc_One_M8s_opsysed));
                processored.setText(getString(R.string.htc_One_M8s_processored));
                networked.setText(getString(R.string.htc_One_M8s_networked));
                appearanced.setText(getString(R.string.htc_One_M8s_appearanced));
                displayed.setText(getString(R.string.htc_One_M8s_displayed));
                cameraed.setText(getString(R.string.htc_One_M8s_cameraed));
                memoryed.setText(getString(R.string.htc_One_M8s_memoryed));
                con_sensored.setText(getString(R.string.htc_One_M8s_con_sensored));
                batteryed.setText(getString(R.string.htc_One_M8s_batteryed));
                othered.setText(getString(R.string.htc_One_M8s_othered));
                break;

            case "One M9":

                specifications();

                named.setText(getString(R.string.htc_One_M9_named));
                modeled.setText(getString(R.string.htc_One_M9_modeled));
                opsysed.setText(getString(R.string.htc_One_M9_opsysed));
                processored.setText(getString(R.string.htc_One_M9_processored));
                networked.setText(getString(R.string.htc_One_M9_networked));
                appearanced.setText(getString(R.string.htc_One_M9_appearanced));
                displayed.setText(getString(R.string.htc_One_M9_displayed));
                cameraed.setText(getString(R.string.htc_One_M9_cameraed));
                memoryed.setText(getString(R.string.htc_One_M9_memoryed));
                con_sensored.setText(getString(R.string.htc_One_M9_con_sensored));
                batteryed.setText(getString(R.string.htc_One_M9_batteryed));
                othered.setText(getString(R.string.htc_One_M9_othered));
                break;

            case "One M9+":

                specifications();

                named.setText(getString(R.string.htc_One_M9PLUS_named));
                modeled.setText(getString(R.string.htc_One_M9PLUS_modeled));
                opsysed.setText(getString(R.string.htc_One_M9PLUS_opsysed));
                processored.setText(getString(R.string.htc_One_M9PLUS_processored));
                networked.setText(getString(R.string.htc_One_M9PLUS_networked));
                appearanced.setText(getString(R.string.htc_One_M9PLUS_appearanced));
                displayed.setText(getString(R.string.htc_One_M9PLUS_displayed));
                cameraed.setText(getString(R.string.htc_One_M9PLUS_cameraed));
                memoryed.setText(getString(R.string.htc_One_M9PLUS_memoryed));
                con_sensored.setText(getString(R.string.htc_One_M9PLUS_con_sensored));
                batteryed.setText(getString(R.string.htc_One_M9PLUS_batteryed));
                othered.setText(getString(R.string.htc_One_M9PLUS_othered));
                break;

            case "One M9s":

                specifications();

                named.setText(getString(R.string.htc_One_M9s_named));
                modeled.setText(getString(R.string.htc_One_M9s_modeled));
                opsysed.setText(getString(R.string.htc_One_M9s_opsysed));
                processored.setText(getString(R.string.htc_One_M9s_processored));
                networked.setText(getString(R.string.htc_One_M9s_networked));
                appearanced.setText(getString(R.string.htc_One_M9s_appearanced));
                displayed.setText(getString(R.string.htc_One_M9s_displayed));
                cameraed.setText(getString(R.string.htc_One_M9s_cameraed));
                memoryed.setText(getString(R.string.htc_One_M9s_memoryed));
                con_sensored.setText(getString(R.string.htc_One_M9s_con_sensored));
                batteryed.setText(getString(R.string.htc_One_M9s_batteryed));
                othered.setText(getString(R.string.htc_One_M9s_othered));
                break;

            case "One X9":

                specifications();

                named.setText(getString(R.string.htc_One_X9_named));
                modeled.setText(getString(R.string.htc_One_X9_modeled));
                opsysed.setText(getString(R.string.htc_One_X9_opsysed));
                processored.setText(getString(R.string.htc_One_X9_processored));
                networked.setText(getString(R.string.htc_One_X9_networked));
                appearanced.setText(getString(R.string.htc_One_X9_appearanced));
                displayed.setText(getString(R.string.htc_One_X9_displayed));
                cameraed.setText(getString(R.string.htc_One_X9_cameraed));
                memoryed.setText(getString(R.string.htc_One_X9_memoryed));
                con_sensored.setText(getString(R.string.htc_One_X9_con_sensored));
                batteryed.setText(getString(R.string.htc_One_X9_batteryed));
                othered.setText(getString(R.string.htc_One_X9_othered));
                break;

        }
    }

    private void gethuaweibrand(String huaweibrand) {

        switch (huaweibrand) {

            case "Ascend Y540":

                specifications();

                named.setText(getString(R.string.huaweiascendy540_named));
                modeled.setText(getString(R.string.huaweiascendy540_modeled));
                opsysed.setText(getString(R.string.huaweiascendy540_opsysed));
                processored.setText(getString(R.string.huaweiascendy540_processored));
                networked.setText(getString(R.string.huaweiascendy540_networked));
                appearanced.setText(getString(R.string.huaweiascendy540_appearanced));
                displayed.setText(getString(R.string.huaweiascendy540_displayed));
                cameraed.setText(getString(R.string.huaweiascendy540_cameraed));
                memoryed.setText(getString(R.string.huaweiascendy540_memoryed));
                con_sensored.setText(getString(R.string.huaweiascendy540_con_sensored));
                batteryed.setText(getString(R.string.huaweiascendy540_batteryed));
                othered.setText(getString(R.string.huaweiascendy540_othered));
                break;

            case "C199S":

                specifications();

                named.setText(getString(R.string.huaweic199s_named));
                modeled.setText(getString(R.string.huaweic199s_modeled));
                opsysed.setText(getString(R.string.huaweic199s_opsysed));
                processored.setText(getString(R.string.huaweic199s_processored));
                networked.setText(getString(R.string.huaweic199s_networked));
                appearanced.setText(getString(R.string.huaweic199s_appearanced));
                displayed.setText(getString(R.string.huaweic199s_displayed));
                cameraed.setText(getString(R.string.huaweic199s_cameraed));
                memoryed.setText(getString(R.string.huaweic199s_memoryed));
                con_sensored.setText(getString(R.string.huaweic199s_con_sensored));
                batteryed.setText(getString(R.string.huaweic199s_batteryed));
                othered.setText(getString(R.string.huaweic199s_othered));
                break;

            case "Enjoy 5s":

                specifications();

                named.setText(getString(R.string.huaweie5s_named));
                modeled.setText(getString(R.string.huaweie5s_modeled));
                opsysed.setText(getString(R.string.huaweie5s_opsysed));
                processored.setText(getString(R.string.huaweie5s_processored));
                networked.setText(getString(R.string.huaweie5s_networked));
                appearanced.setText(getString(R.string.huaweie5s_appearanced));
                displayed.setText(getString(R.string.huaweie5s_displayed));
                cameraed.setText(getString(R.string.huaweie5s_cameraed));
                memoryed.setText(getString(R.string.huaweie5s_memoryed));
                con_sensored.setText(getString(R.string.huaweie5s_con_sensored));
                batteryed.setText(getString(R.string.huaweie5s_batteryed));
                othered.setText(getString(R.string.huaweie5s_othered));
                break;

            case "G8":

                specifications();

                named.setText(getString(R.string.huaweig8_named));
                modeled.setText(getString(R.string.huaweig8_modeled));
                opsysed.setText(getString(R.string.huaweig8_opsysed));
                processored.setText(getString(R.string.huaweig8_processored));
                networked.setText(getString(R.string.huaweig8_networked));
                appearanced.setText(getString(R.string.huaweig8_appearanced));
                displayed.setText(getString(R.string.huaweig8_displayed));
                cameraed.setText(getString(R.string.huaweig8_cameraed));
                memoryed.setText(getString(R.string.huaweig8_memoryed));
                con_sensored.setText(getString(R.string.huaweig8_con_sensored));
                batteryed.setText(getString(R.string.huaweig8_batteryed));
                othered.setText(getString(R.string.huaweig8_othered));
                break;

            case "G9 Plus":

                specifications();

                named.setText(getString(R.string.huaweig9p_named));
                modeled.setText(getString(R.string.huaweig9p_modeled));
                opsysed.setText(getString(R.string.huaweig9p_opsysed));
                processored.setText(getString(R.string.huaweig9p_processored));
                networked.setText(getString(R.string.huaweig9p_networked));
                appearanced.setText(getString(R.string.huaweig9p_appearanced));
                displayed.setText(getString(R.string.huaweig9p_displayed));
                cameraed.setText(getString(R.string.huaweig9p_cameraed));
                memoryed.setText(getString(R.string.huaweig9p_memoryed));
                con_sensored.setText(getString(R.string.huaweig9p_con_sensored));
                batteryed.setText(getString(R.string.huaweig9p_batteryed));
                othered.setText(getString(R.string.huaweig9p_othered));
                break;

            case "Honor 4A":

                specifications();

                named.setText(getString(R.string.huaweihonor4A_named));
                modeled.setText(getString(R.string.huaweihonor4A_modeled));
                opsysed.setText(getString(R.string.huaweihonor4A_opsysed));
                processored.setText(getString(R.string.huaweihonor4A_processored));
                networked.setText(getString(R.string.huaweihonor4A_networked));
                appearanced.setText(getString(R.string.huaweihonor4A_appearanced));
                displayed.setText(getString(R.string.huaweihonor4A_displayed));
                cameraed.setText(getString(R.string.huaweihonor4A_cameraed));
                memoryed.setText(getString(R.string.huaweihonor4A_memoryed));
                con_sensored.setText(getString(R.string.huaweihonor4A_con_sensored));
                batteryed.setText(getString(R.string.huaweihonor4A_batteryed));
                othered.setText(getString(R.string.huaweihonor4A_othered));
                break;

            case "Honor 4C":

                specifications();

                named.setText(getString(R.string.huaweihonor4C_named));
                modeled.setText(getString(R.string.huaweihonor4C_modeled));
                opsysed.setText(getString(R.string.huaweihonor4C_opsysed));
                processored.setText(getString(R.string.huaweihonor4C_processored));
                networked.setText(getString(R.string.huaweihonor4C_networked));
                appearanced.setText(getString(R.string.huaweihonor4C_appearanced));
                displayed.setText(getString(R.string.huaweihonor4C_displayed));
                cameraed.setText(getString(R.string.huaweihonor4C_cameraed));
                memoryed.setText(getString(R.string.huaweihonor4C_memoryed));
                con_sensored.setText(getString(R.string.huaweihonor4C_con_sensored));
                batteryed.setText(getString(R.string.huaweihonor4C_batteryed));
                othered.setText(getString(R.string.huaweihonor4C_othered));
                break;

            case "Honor 5C":

                specifications();

                named.setText(getString(R.string.huaweihonor5C_named));
                modeled.setText(getString(R.string.huaweihonor5C_modeled));
                opsysed.setText(getString(R.string.huaweihonor5C_opsysed));
                processored.setText(getString(R.string.huaweihonor5C_processored));
                networked.setText(getString(R.string.huaweihonor5C_networked));
                appearanced.setText(getString(R.string.huaweihonor5C_appearanced));
                displayed.setText(getString(R.string.huaweihonor5C_displayed));
                cameraed.setText(getString(R.string.huaweihonor5C_cameraed));
                memoryed.setText(getString(R.string.huaweihonor5C_memoryed));
                con_sensored.setText(getString(R.string.huaweihonor5C_con_sensored));
                batteryed.setText(getString(R.string.huaweihonor5C_batteryed));
                othered.setText(getString(R.string.huaweihonor5C_othered));
                break;

            case "Honor 5C LTE":

                specifications();

                named.setText(getString(R.string.huaweihonor5Cl_named));
                modeled.setText(getString(R.string.huaweihonor5Cl_modeled));
                opsysed.setText(getString(R.string.huaweihonor5Cl_opsysed));
                processored.setText(getString(R.string.huaweihonor5Cl_processored));
                networked.setText(getString(R.string.huaweihonor5Cl_networked));
                appearanced.setText(getString(R.string.huaweihonor5Cl_appearanced));
                displayed.setText(getString(R.string.huaweihonor5Cl_displayed));
                cameraed.setText(getString(R.string.huaweihonor5Cl_cameraed));
                memoryed.setText(getString(R.string.huaweihonor5Cl_memoryed));
                con_sensored.setText(getString(R.string.huaweihonor5Cl_con_sensored));
                batteryed.setText(getString(R.string.huaweihonor5Cl_batteryed));
                othered.setText(getString(R.string.huaweihonor5Cl_othered));
                break;

            case "Honor 5X":

                specifications();

                named.setText(getString(R.string.huaweihonor5X_named));
                modeled.setText(getString(R.string.huaweihonor5X_modeled));
                opsysed.setText(getString(R.string.huaweihonor5X_opsysed));
                processored.setText(getString(R.string.huaweihonor5X_processored));
                networked.setText(getString(R.string.huaweihonor5X_networked));
                appearanced.setText(getString(R.string.huaweihonor5X_appearanced));
                displayed.setText(getString(R.string.huaweihonor5X_displayed));
                cameraed.setText(getString(R.string.huaweihonor5X_cameraed));
                memoryed.setText(getString(R.string.huaweihonor5X_memoryed));
                con_sensored.setText(getString(R.string.huaweihonor5X_con_sensored));
                batteryed.setText(getString(R.string.huaweihonor5X_batteryed));
                othered.setText(getString(R.string.huaweihonor5X_othered));
                break;

            case "Honor 7":

                specifications();

                named.setText(getString(R.string.huaweihonor7_named));
                modeled.setText(getString(R.string.huaweihonor7_modeled));
                opsysed.setText(getString(R.string.huaweihonor7_opsysed));
                processored.setText(getString(R.string.huaweihonor7_processored));
                networked.setText(getString(R.string.huaweihonor7_networked));
                appearanced.setText(getString(R.string.huaweihonor7_appearanced));
                displayed.setText(getString(R.string.huaweihonor7_displayed));
                cameraed.setText(getString(R.string.huaweihonor7_cameraed));
                memoryed.setText(getString(R.string.huaweihonor7_memoryed));
                con_sensored.setText(getString(R.string.huaweihonor7_con_sensored));
                batteryed.setText(getString(R.string.huaweihonor7_batteryed));
                othered.setText(getString(R.string.huaweihonor7_othered));
                break;

            case "Honor 7i":

                specifications();

                named.setText(getString(R.string.huaweihonor7i_named));
                modeled.setText(getString(R.string.huaweihonor7i_modeled));
                opsysed.setText(getString(R.string.huaweihonor7i_opsysed));
                processored.setText(getString(R.string.huaweihonor7i_processored));
                networked.setText(getString(R.string.huaweihonor7i_networked));
                appearanced.setText(getString(R.string.huaweihonor7i_appearanced));
                displayed.setText(getString(R.string.huaweihonor7i_displayed));
                cameraed.setText(getString(R.string.huaweihonor7i_cameraed));
                memoryed.setText(getString(R.string.huaweihonor7i_memoryed));
                con_sensored.setText(getString(R.string.huaweihonor7i_con_sensored));
                batteryed.setText(getString(R.string.huaweihonor7i_batteryed));
                othered.setText(getString(R.string.huaweihonor7i_othered));
                break;

            case "Honor Bee":

                specifications();

                named.setText(getString(R.string.huaweihonorbee_named));
                modeled.setText(getString(R.string.huaweihonorbee_modeled));
                opsysed.setText(getString(R.string.huaweihonorbee_opsysed));
                processored.setText(getString(R.string.huaweihonorbee_processored));
                networked.setText(getString(R.string.huaweihonorbee_networked));
                appearanced.setText(getString(R.string.huaweihonorbee_appearanced));
                displayed.setText(getString(R.string.huaweihonorbee_displayed));
                cameraed.setText(getString(R.string.huaweihonorbee_cameraed));
                memoryed.setText(getString(R.string.huaweihonorbee_memoryed));
                con_sensored.setText(getString(R.string.huaweihonorbee_con_sensored));
                batteryed.setText(getString(R.string.huaweihonorbee_batteryed));
                othered.setText(getString(R.string.huaweihonorbee_othered));
                break;

            case "Honor Holly 2 Plus":

                specifications();

                named.setText(getString(R.string.huaweihonorh2p_named));
                modeled.setText(getString(R.string.huaweihonorh2p_modeled));
                opsysed.setText(getString(R.string.huaweihonorh2p_opsysed));
                processored.setText(getString(R.string.huaweihonorh2p_processored));
                networked.setText(getString(R.string.huaweihonorh2p_networked));
                appearanced.setText(getString(R.string.huaweihonorh2p_appearanced));
                displayed.setText(getString(R.string.huaweihonorh2p_displayed));
                cameraed.setText(getString(R.string.huaweihonorh2p_cameraed));
                memoryed.setText(getString(R.string.huaweihonorh2p_memoryed));
                con_sensored.setText(getString(R.string.huaweihonorh2p_con_sensored));
                batteryed.setText(getString(R.string.huaweihonorh2p_batteryed));
                othered.setText(getString(R.string.huaweihonorh2p_othered));
                break;

            case "Honor Play 5X":

                specifications();

                named.setText(getString(R.string.huaweihonorp5x_named));
                modeled.setText(getString(R.string.huaweihonorp5x_modeled));
                opsysed.setText(getString(R.string.huaweihonorp5x_opsysed));
                processored.setText(getString(R.string.huaweihonorp5x_processored));
                networked.setText(getString(R.string.huaweihonorp5x_networked));
                appearanced.setText(getString(R.string.huaweihonorp5x_appearanced));
                displayed.setText(getString(R.string.huaweihonorp5x_displayed));
                cameraed.setText(getString(R.string.huaweihonorp5x_cameraed));
                memoryed.setText(getString(R.string.huaweihonorp5x_memoryed));
                con_sensored.setText(getString(R.string.huaweihonorp5x_con_sensored));
                batteryed.setText(getString(R.string.huaweihonorp5x_batteryed));
                othered.setText(getString(R.string.huaweihonorp5x_othered));
                break;

            case "Honor V8":

                specifications();

                named.setText(getString(R.string.huaweihonorv8_named));
                modeled.setText(getString(R.string.huaweihonorv8_modeled));
                opsysed.setText(getString(R.string.huaweihonorv8_opsysed));
                processored.setText(getString(R.string.huaweihonorv8_processored));
                networked.setText(getString(R.string.huaweihonorv8_networked));
                appearanced.setText(getString(R.string.huaweihonorv8_appearanced));
                displayed.setText(getString(R.string.huaweihonorv8_displayed));
                cameraed.setText(getString(R.string.huaweihonorv8_cameraed));
                memoryed.setText(getString(R.string.huaweihonorv8_memoryed));
                con_sensored.setText(getString(R.string.huaweihonorv8_con_sensored));
                batteryed.setText(getString(R.string.huaweihonorv8_batteryed));
                othered.setText(getString(R.string.huaweihonorv8_othered));
                break;

            case "Mate 8":

                specifications();

                named.setText(getString(R.string.huaweimate8_named));
                modeled.setText(getString(R.string.huaweimate8_modeled));
                opsysed.setText(getString(R.string.huaweimate8_opsysed));
                processored.setText(getString(R.string.huaweimate8_processored));
                networked.setText(getString(R.string.huaweimate8_networked));
                appearanced.setText(getString(R.string.huaweimate8_appearanced));
                displayed.setText(getString(R.string.huaweimate8_displayed));
                cameraed.setText(getString(R.string.huaweimate8_cameraed));
                memoryed.setText(getString(R.string.huaweimate8_memoryed));
                con_sensored.setText(getString(R.string.huaweimate8_con_sensored));
                batteryed.setText(getString(R.string.huaweimate8_batteryed));
                othered.setText(getString(R.string.huaweimate8_othered));
                break;

            case "Nexus 6P":

                specifications();

                named.setText(getString(R.string.huaweinexus6p_named));
                modeled.setText(getString(R.string.huaweinexus6p_modeled));
                opsysed.setText(getString(R.string.huaweinexus6p_opsysed));
                processored.setText(getString(R.string.huaweinexus6p_processored));
                networked.setText(getString(R.string.huaweinexus6p_networked));
                appearanced.setText(getString(R.string.huaweinexus6p_appearanced));
                displayed.setText(getString(R.string.huaweinexus6p_displayed));
                cameraed.setText(getString(R.string.huaweinexus6p_cameraed));
                memoryed.setText(getString(R.string.huaweinexus6p_memoryed));
                con_sensored.setText(getString(R.string.huaweinexus6p_con_sensored));
                batteryed.setText(getString(R.string.huaweinexus6p_batteryed));
                othered.setText(getString(R.string.huaweinexus6p_othered));
                break;

            case "P8":

                specifications();

                named.setText(getString(R.string.huaweip8_named));
                modeled.setText(getString(R.string.huaweip8_modeled));
                opsysed.setText(getString(R.string.huaweip8_opsysed));
                processored.setText(getString(R.string.huaweip8_processored));
                networked.setText(getString(R.string.huaweip8_networked));
                appearanced.setText(getString(R.string.huaweip8_appearanced));
                displayed.setText(getString(R.string.huaweip8_displayed));
                cameraed.setText(getString(R.string.huaweip8_cameraed));
                memoryed.setText(getString(R.string.huaweip8_memoryed));
                con_sensored.setText(getString(R.string.huaweip8_con_sensored));
                batteryed.setText(getString(R.string.huaweip8_batteryed));
                othered.setText(getString(R.string.huaweip8_othered));
                break;

            case "P8 Lite":

                specifications();

                named.setText(getString(R.string.huaweip8lite_named));
                modeled.setText(getString(R.string.huaweip8lite_modeled));
                opsysed.setText(getString(R.string.huaweip8lite_opsysed));
                processored.setText(getString(R.string.huaweip8lite_processored));
                networked.setText(getString(R.string.huaweip8lite_networked));
                appearanced.setText(getString(R.string.huaweip8lite_appearanced));
                displayed.setText(getString(R.string.huaweip8lite_displayed));
                cameraed.setText(getString(R.string.huaweip8lite_cameraed));
                memoryed.setText(getString(R.string.huaweip8lite_memoryed));
                con_sensored.setText(getString(R.string.huaweip8lite_con_sensored));
                batteryed.setText(getString(R.string.huaweip8lite_batteryed));
                othered.setText(getString(R.string.huaweip8lite_othered));
                break;

            case "P8 max":

                specifications();

                named.setText(getString(R.string.huaweip8max_named));
                modeled.setText(getString(R.string.huaweip8max_modeled));
                opsysed.setText(getString(R.string.huaweip8max_opsysed));
                processored.setText(getString(R.string.huaweip8max_processored));
                networked.setText(getString(R.string.huaweip8max_networked));
                appearanced.setText(getString(R.string.huaweip8max_appearanced));
                displayed.setText(getString(R.string.huaweip8max_displayed));
                cameraed.setText(getString(R.string.huaweip8max_cameraed));
                memoryed.setText(getString(R.string.huaweip8max_memoryed));
                con_sensored.setText(getString(R.string.huaweip8max_con_sensored));
                batteryed.setText(getString(R.string.huaweip8max_batteryed));
                othered.setText(getString(R.string.huaweip8max_othered));
                break;

            case "P9":

                specifications();

                named.setText(getString(R.string.huaweip9_named));
                modeled.setText(getString(R.string.huaweip9_modeled));
                opsysed.setText(getString(R.string.huaweip9_opsysed));
                processored.setText(getString(R.string.huaweip9_processored));
                networked.setText(getString(R.string.huaweip9_networked));
                appearanced.setText(getString(R.string.huaweip9_appearanced));
                displayed.setText(getString(R.string.huaweip9_displayed));
                cameraed.setText(getString(R.string.huaweip9_cameraed));
                memoryed.setText(getString(R.string.huaweip9_memoryed));
                con_sensored.setText(getString(R.string.huaweip9_con_sensored));
                batteryed.setText(getString(R.string.huaweip9_batteryed));
                othered.setText(getString(R.string.huaweip9_othered));
                break;

            case "P9 Lite":

                specifications();

                named.setText(getString(R.string.huaweip9lite_named));
                modeled.setText(getString(R.string.huaweip9lite_modeled));
                opsysed.setText(getString(R.string.huaweip9lite_opsysed));
                processored.setText(getString(R.string.huaweip9lite_processored));
                networked.setText(getString(R.string.huaweip9lite_networked));
                appearanced.setText(getString(R.string.huaweip9lite_appearanced));
                displayed.setText(getString(R.string.huaweip9lite_displayed));
                cameraed.setText(getString(R.string.huaweip9lite_cameraed));
                memoryed.setText(getString(R.string.huaweip9lite_memoryed));
                con_sensored.setText(getString(R.string.huaweip9lite_con_sensored));
                batteryed.setText(getString(R.string.huaweip9lite_batteryed));
                othered.setText(getString(R.string.huaweip9lite_othered));
                break;

            case "P9 Plus":

                specifications();

                named.setText(getString(R.string.huaweip9plus_named));
                modeled.setText(getString(R.string.huaweip9plus_modeled));
                opsysed.setText(getString(R.string.huaweip9plus_opsysed));
                processored.setText(getString(R.string.huaweip9plus_processored));
                networked.setText(getString(R.string.huaweip9plus_networked));
                appearanced.setText(getString(R.string.huaweip9plus_appearanced));
                displayed.setText(getString(R.string.huaweip9plus_displayed));
                cameraed.setText(getString(R.string.huaweip9plus_cameraed));
                memoryed.setText(getString(R.string.huaweip9plus_memoryed));
                con_sensored.setText(getString(R.string.huaweip9plus_con_sensored));
                batteryed.setText(getString(R.string.huaweip9plus_batteryed));
                othered.setText(getString(R.string.huaweip9plus_othered));
                break;

            case "SnapTo G620":

                specifications();

                named.setText(getString(R.string.huaweisnaptog620_named));
                modeled.setText(getString(R.string.huaweisnaptog620_modeled));
                opsysed.setText(getString(R.string.huaweisnaptog620_opsysed));
                processored.setText(getString(R.string.huaweisnaptog620_processored));
                networked.setText(getString(R.string.huaweisnaptog620_networked));
                appearanced.setText(getString(R.string.huaweisnaptog620_appearanced));
                displayed.setText(getString(R.string.huaweisnaptog620_displayed));
                cameraed.setText(getString(R.string.huaweisnaptog620_cameraed));
                memoryed.setText(getString(R.string.huaweisnaptog620_memoryed));
                con_sensored.setText(getString(R.string.huaweisnaptog620_con_sensored));
                batteryed.setText(getString(R.string.huaweisnaptog620_batteryed));
                othered.setText(getString(R.string.huaweisnaptog620_othered));
                break;

            case "Y3 II":

                specifications();

                named.setText(getString(R.string.huaweiy3ii_named));
                modeled.setText(getString(R.string.huaweiy3ii_modeled));
                opsysed.setText(getString(R.string.huaweiy3ii_opsysed));
                processored.setText(getString(R.string.huaweiy3ii_processored));
                networked.setText(getString(R.string.huaweiy3ii_networked));
                appearanced.setText(getString(R.string.huaweiy3ii_appearanced));
                displayed.setText(getString(R.string.huaweiy3ii_displayed));
                cameraed.setText(getString(R.string.huaweiy3ii_cameraed));
                memoryed.setText(getString(R.string.huaweiy3ii_memoryed));
                con_sensored.setText(getString(R.string.huaweiy3ii_con_sensored));
                batteryed.setText(getString(R.string.huaweiy3ii_batteryed));
                othered.setText(getString(R.string.huaweiy3ii_othered));
                break;

            case "Y336":

                specifications();

                named.setText(getString(R.string.huaweiy336_named));
                modeled.setText(getString(R.string.huaweiy336_modeled));
                opsysed.setText(getString(R.string.huaweiy336_opsysed));
                processored.setText(getString(R.string.huaweiy336_processored));
                networked.setText(getString(R.string.huaweiy336_networked));
                appearanced.setText(getString(R.string.huaweiy336_appearanced));
                displayed.setText(getString(R.string.huaweiy336_displayed));
                cameraed.setText(getString(R.string.huaweiy336_cameraed));
                memoryed.setText(getString(R.string.huaweiy336_memoryed));
                con_sensored.setText(getString(R.string.huaweiy336_con_sensored));
                batteryed.setText(getString(R.string.huaweiy336_batteryed));
                othered.setText(getString(R.string.huaweiy336_othered));
                break;

            case "Y5":

                specifications();

                named.setText(getString(R.string.huaweiy5_named));
                modeled.setText(getString(R.string.huaweiy5_modeled));
                opsysed.setText(getString(R.string.huaweiy5_opsysed));
                processored.setText(getString(R.string.huaweiy5_processored));
                networked.setText(getString(R.string.huaweiy5_networked));
                appearanced.setText(getString(R.string.huaweiy5_appearanced));
                displayed.setText(getString(R.string.huaweiy5_displayed));
                cameraed.setText(getString(R.string.huaweiy5_cameraed));
                memoryed.setText(getString(R.string.huaweiy5_memoryed));
                con_sensored.setText(getString(R.string.huaweiy5_con_sensored));
                batteryed.setText(getString(R.string.huaweiy5_batteryed));
                othered.setText(getString(R.string.huaweiy5_othered));
                break;

            case "Y541":

                specifications();

                named.setText(getString(R.string.huaweiy541_named));
                modeled.setText(getString(R.string.huaweiy541_modeled));
                opsysed.setText(getString(R.string.huaweiy541_opsysed));
                processored.setText(getString(R.string.huaweiy541_processored));
                networked.setText(getString(R.string.huaweiy541_networked));
                appearanced.setText(getString(R.string.huaweiy541_appearanced));
                displayed.setText(getString(R.string.huaweiy541_displayed));
                cameraed.setText(getString(R.string.huaweiy541_cameraed));
                memoryed.setText(getString(R.string.huaweiy541_memoryed));
                con_sensored.setText(getString(R.string.huaweiy541_con_sensored));
                batteryed.setText(getString(R.string.huaweiy541_batteryed));
                othered.setText(getString(R.string.huaweiy541_othered));
                break;

            case "Y5 II":

                specifications();

                named.setText(getString(R.string.huaweiy5ii_named));
                modeled.setText(getString(R.string.huaweiy5ii_modeled));
                opsysed.setText(getString(R.string.huaweiy5ii_opsysed));
                processored.setText(getString(R.string.huaweiy5ii_processored));
                networked.setText(getString(R.string.huaweiy5ii_networked));
                appearanced.setText(getString(R.string.huaweiy5ii_appearanced));
                displayed.setText(getString(R.string.huaweiy5ii_displayed));
                cameraed.setText(getString(R.string.huaweiy5ii_cameraed));
                memoryed.setText(getString(R.string.huaweiy5ii_memoryed));
                con_sensored.setText(getString(R.string.huaweiy5ii_con_sensored));
                batteryed.setText(getString(R.string.huaweiy5ii_batteryed));
                othered.setText(getString(R.string.huaweiy5ii_othered));
                break;

            case "Y625":

                specifications();

                named.setText(getString(R.string.huaweiy625_named));
                modeled.setText(getString(R.string.huaweiy625_modeled));
                opsysed.setText(getString(R.string.huaweiy625_opsysed));
                processored.setText(getString(R.string.huaweiy625_processored));
                networked.setText(getString(R.string.huaweiy625_networked));
                appearanced.setText(getString(R.string.huaweiy625_appearanced));
                displayed.setText(getString(R.string.huaweiy625_displayed));
                cameraed.setText(getString(R.string.huaweiy625_cameraed));
                memoryed.setText(getString(R.string.huaweiy625_memoryed));
                con_sensored.setText(getString(R.string.huaweiy625_con_sensored));
                batteryed.setText(getString(R.string.huaweiy625_batteryed));
                othered.setText(getString(R.string.huaweiy625_othered));
                break;

        }
    }

    private void getinfinixbrand(String infinixbrand) {

        switch (infinixbrand) {

            case "Hot":

                specifications();

                named.setText(getString(R.string.infhot_named));
                modeled.setText(getString(R.string.infhot_modeled));
                opsysed.setText(getString(R.string.infhot_opsysed));
                processored.setText(getString(R.string.infhot_processored));
                networked.setText(getString(R.string.infhot_networked));
                appearanced.setText(getString(R.string.infhot_appearanced));
                displayed.setText(getString(R.string.infhot_displayed));
                cameraed.setText(getString(R.string.infhot_cameraed));
                memoryed.setText(getString(R.string.infhot_memoryed));
                con_sensored.setText(getString(R.string.infhot_con_sensored));
                batteryed.setText(getString(R.string.infhot_batteryed));
                othered.setText(getString(R.string.infhot_othered));
                break;

            case "Hot 2":

                specifications();

                named.setText(getString(R.string.infhot2_named));
                modeled.setText(getString(R.string.infhot2_modeled));
                opsysed.setText(getString(R.string.infhot2_opsysed));
                processored.setText(getString(R.string.infhot2_processored));
                networked.setText(getString(R.string.infhot2_networked));
                appearanced.setText(getString(R.string.infhot2_appearanced));
                displayed.setText(getString(R.string.infhot2_displayed));
                cameraed.setText(getString(R.string.infhot2_cameraed));
                memoryed.setText(getString(R.string.infhot2_memoryed));
                con_sensored.setText(getString(R.string.infhot2_con_sensored));
                batteryed.setText(getString(R.string.infhot2_batteryed));
                othered.setText(getString(R.string.infhot2_othered));
                break;

            case "Hot 3":

                specifications();

                named.setText(getString(R.string.infhot3_named));
                modeled.setText(getString(R.string.infhot3_modeled));
                opsysed.setText(getString(R.string.infhot3_opsysed));
                processored.setText(getString(R.string.infhot3_processored));
                networked.setText(getString(R.string.infhot3_networked));
                appearanced.setText(getString(R.string.infhot3_appearanced));
                displayed.setText(getString(R.string.infhot3_displayed));
                cameraed.setText(getString(R.string.infhot3_cameraed));
                memoryed.setText(getString(R.string.infhot3_memoryed));
                con_sensored.setText(getString(R.string.infhot3_con_sensored));
                batteryed.setText(getString(R.string.infhot3_batteryed));
                othered.setText(getString(R.string.infhot3_othered));
                break;

            case "Hot 3 LTE":

                specifications();

                named.setText(getString(R.string.infhot3l_named));
                modeled.setText(getString(R.string.infhot3l_modeled));
                opsysed.setText(getString(R.string.infhot3l_opsysed));
                processored.setText(getString(R.string.infhot3l_processored));
                networked.setText(getString(R.string.infhot3l_networked));
                appearanced.setText(getString(R.string.infhot3l_appearanced));
                displayed.setText(getString(R.string.infhot3l_displayed));
                cameraed.setText(getString(R.string.infhot3l_cameraed));
                memoryed.setText(getString(R.string.infhot3l_memoryed));
                con_sensored.setText(getString(R.string.infhot3l_con_sensored));
                batteryed.setText(getString(R.string.infhot3l_batteryed));
                othered.setText(getString(R.string.infhot3l_othered));
                break;

            case "Hot 4":

                specifications();

                named.setText(getString(R.string.infhot4_named));
                modeled.setText(getString(R.string.infhot4_modeled));
                opsysed.setText(getString(R.string.infhot4_opsysed));
                processored.setText(getString(R.string.infhot4_processored));
                networked.setText(getString(R.string.infhot4_networked));
                appearanced.setText(getString(R.string.infhot4_appearanced));
                displayed.setText(getString(R.string.infhot4_displayed));
                cameraed.setText(getString(R.string.infhot4_cameraed));
                memoryed.setText(getString(R.string.infhot4_memoryed));
                con_sensored.setText(getString(R.string.infhot4_con_sensored));
                batteryed.setText(getString(R.string.infhot4_batteryed));
                othered.setText(getString(R.string.infhot4_othered));
                break;

            case "Hot 4 Lite":

                specifications();

                named.setText(getString(R.string.infhot4l_named));
                modeled.setText(getString(R.string.infhot4l_modeled));
                opsysed.setText(getString(R.string.infhot4l_opsysed));
                processored.setText(getString(R.string.infhot4l_processored));
                networked.setText(getString(R.string.infhot4l_networked));
                appearanced.setText(getString(R.string.infhot4l_appearanced));
                displayed.setText(getString(R.string.infhot4l_displayed));
                cameraed.setText(getString(R.string.infhot4l_cameraed));
                memoryed.setText(getString(R.string.infhot4l_memoryed));
                con_sensored.setText(getString(R.string.infhot4l_con_sensored));
                batteryed.setText(getString(R.string.infhot4l_batteryed));
                othered.setText(getString(R.string.infhot4l_othered));
                break;

            case "Hot 4 Pro":

                specifications();

                named.setText(getString(R.string.infhot4p_named));
                modeled.setText(getString(R.string.infhot4p_modeled));
                opsysed.setText(getString(R.string.infhot4p_opsysed));
                processored.setText(getString(R.string.infhot4p_processored));
                networked.setText(getString(R.string.infhot4p_networked));
                appearanced.setText(getString(R.string.infhot4p_appearanced));
                displayed.setText(getString(R.string.infhot4p_displayed));
                cameraed.setText(getString(R.string.infhot4p_cameraed));
                memoryed.setText(getString(R.string.infhot4p_memoryed));
                con_sensored.setText(getString(R.string.infhot4p_con_sensored));
                batteryed.setText(getString(R.string.infhot4p_batteryed));
                othered.setText(getString(R.string.infhot4p_othered));
                break;

            case "Hot Note":

                specifications();

                named.setText(getString(R.string.infhotn_named));
                modeled.setText(getString(R.string.infhotn_modeled));
                opsysed.setText(getString(R.string.infhotn_opsysed));
                processored.setText(getString(R.string.infhotn_processored));
                networked.setText(getString(R.string.infhotn_networked));
                appearanced.setText(getString(R.string.infhotn_appearanced));
                displayed.setText(getString(R.string.infhotn_displayed));
                cameraed.setText(getString(R.string.infhotn_cameraed));
                memoryed.setText(getString(R.string.infhotn_memoryed));
                con_sensored.setText(getString(R.string.infhotn_con_sensored));
                batteryed.setText(getString(R.string.infhotn_batteryed));
                othered.setText(getString(R.string.infhotn_othered));
                break;

            case "Hot Note Pro":

                specifications();

                named.setText(getString(R.string.infhotnp_named));
                modeled.setText(getString(R.string.infhotnp_modeled));
                opsysed.setText(getString(R.string.infhotnp_opsysed));
                processored.setText(getString(R.string.infhotnp_processored));
                networked.setText(getString(R.string.infhotnp_networked));
                appearanced.setText(getString(R.string.infhotnp_appearanced));
                displayed.setText(getString(R.string.infhotnp_displayed));
                cameraed.setText(getString(R.string.infhotnp_cameraed));
                memoryed.setText(getString(R.string.infhotnp_memoryed));
                con_sensored.setText(getString(R.string.infhotnp_con_sensored));
                batteryed.setText(getString(R.string.infhotnp_batteryed));
                othered.setText(getString(R.string.infhotnp_othered));
                break;

            case "Hot S":

                specifications();

                named.setText(getString(R.string.infhots_named));
                modeled.setText(getString(R.string.infhots_modeled));
                opsysed.setText(getString(R.string.infhots_opsysed));
                processored.setText(getString(R.string.infhots_processored));
                networked.setText(getString(R.string.infhots_networked));
                appearanced.setText(getString(R.string.infhots_appearanced));
                displayed.setText(getString(R.string.infhots_displayed));
                cameraed.setText(getString(R.string.infhots_cameraed));
                memoryed.setText(getString(R.string.infhots_memoryed));
                con_sensored.setText(getString(R.string.infhots_con_sensored));
                batteryed.setText(getString(R.string.infhots_batteryed));
                othered.setText(getString(R.string.infhots_othered));
                break;

            case "Note 2":

                specifications();

                named.setText(getString(R.string.infn2_named));
                modeled.setText(getString(R.string.infn2_modeled));
                opsysed.setText(getString(R.string.infn2_opsysed));
                processored.setText(getString(R.string.infn2_processored));
                networked.setText(getString(R.string.infn2_networked));
                appearanced.setText(getString(R.string.infn2_appearanced));
                displayed.setText(getString(R.string.infn2_displayed));
                cameraed.setText(getString(R.string.infn2_cameraed));
                memoryed.setText(getString(R.string.infn2_memoryed));
                con_sensored.setText(getString(R.string.infn2_con_sensored));
                batteryed.setText(getString(R.string.infn2_batteryed));
                othered.setText(getString(R.string.infn2_othered));
                break;

            case "Note 3":

                specifications();

                named.setText(getString(R.string.infn3_named));
                modeled.setText(getString(R.string.infn3_modeled));
                opsysed.setText(getString(R.string.infn3_opsysed));
                processored.setText(getString(R.string.infn3_processored));
                networked.setText(getString(R.string.infn3_networked));
                appearanced.setText(getString(R.string.infn3_appearanced));
                displayed.setText(getString(R.string.infn3_displayed));
                cameraed.setText(getString(R.string.infn3_cameraed));
                memoryed.setText(getString(R.string.infn3_memoryed));
                con_sensored.setText(getString(R.string.infn3_con_sensored));
                batteryed.setText(getString(R.string.infn3_batteryed));
                othered.setText(getString(R.string.infn3_othered));
                break;

            case "Note 4 Pro":

                specifications();

                named.setText(getString(R.string.infn4_named));
                modeled.setText(getString(R.string.infn4_modeled));
                opsysed.setText(getString(R.string.infn4_opsysed));
                processored.setText(getString(R.string.infn4_processored));
                networked.setText(getString(R.string.infn4_networked));
                appearanced.setText(getString(R.string.infn4_appearanced));
                displayed.setText(getString(R.string.infn4_displayed));
                cameraed.setText(getString(R.string.infn4_cameraed));
                memoryed.setText(getString(R.string.infn4_memoryed));
                con_sensored.setText(getString(R.string.infn4_con_sensored));
                batteryed.setText(getString(R.string.infn4_batteryed));
                othered.setText(getString(R.string.infn4_othered));
                break;

            case "S2":

                specifications();

                named.setText(getString(R.string.infs2_named));
                modeled.setText(getString(R.string.infs2_modeled));
                opsysed.setText(getString(R.string.infs2_opsysed));
                processored.setText(getString(R.string.infs2_processored));
                networked.setText(getString(R.string.infs2_networked));
                appearanced.setText(getString(R.string.infs2_appearanced));
                displayed.setText(getString(R.string.infs2_displayed));
                cameraed.setText(getString(R.string.infs2_cameraed));
                memoryed.setText(getString(R.string.infs2_memoryed));
                con_sensored.setText(getString(R.string.infs2_con_sensored));
                batteryed.setText(getString(R.string.infs2_batteryed));
                othered.setText(getString(R.string.infs2_othered));
                break;

            case "S2 Pro":

                specifications();

                named.setText(getString(R.string.infs2p_named));
                modeled.setText(getString(R.string.infs2p_modeled));
                opsysed.setText(getString(R.string.infs2p_opsysed));
                processored.setText(getString(R.string.infs2p_processored));
                networked.setText(getString(R.string.infs2p_networked));
                appearanced.setText(getString(R.string.infs2p_appearanced));
                displayed.setText(getString(R.string.infs2p_displayed));
                cameraed.setText(getString(R.string.infs2p_cameraed));
                memoryed.setText(getString(R.string.infs2p_memoryed));
                con_sensored.setText(getString(R.string.infs2p_con_sensored));
                batteryed.setText(getString(R.string.infs2p_batteryed));
                othered.setText(getString(R.string.infs2p_othered));
                break;

            case "Zero":

                specifications();

                named.setText(getString(R.string.infz_named));
                modeled.setText(getString(R.string.infz_modeled));
                opsysed.setText(getString(R.string.infz_opsysed));
                processored.setText(getString(R.string.infz_processored));
                networked.setText(getString(R.string.infz_networked));
                appearanced.setText(getString(R.string.infz_appearanced));
                displayed.setText(getString(R.string.infz_displayed));
                cameraed.setText(getString(R.string.infz_cameraed));
                memoryed.setText(getString(R.string.infz_memoryed));
                con_sensored.setText(getString(R.string.infz_con_sensored));
                batteryed.setText(getString(R.string.infz_batteryed));
                othered.setText(getString(R.string.infz_othered));
                break;

            case "Zero 2":

                specifications();

                named.setText(getString(R.string.infz2_named));
                modeled.setText(getString(R.string.infz2_modeled));
                opsysed.setText(getString(R.string.infz2_opsysed));
                processored.setText(getString(R.string.infz2_processored));
                networked.setText(getString(R.string.infz2_networked));
                appearanced.setText(getString(R.string.infz2_appearanced));
                displayed.setText(getString(R.string.infz2_displayed));
                cameraed.setText(getString(R.string.infz2_cameraed));
                memoryed.setText(getString(R.string.infz2_memoryed));
                con_sensored.setText(getString(R.string.infz2_con_sensored));
                batteryed.setText(getString(R.string.infz2_batteryed));
                othered.setText(getString(R.string.infz2_othered));
                break;

            case "Zero 2 LTE":

                specifications();

                named.setText(getString(R.string.infz2l_named));
                modeled.setText(getString(R.string.infz2l_modeled));
                opsysed.setText(getString(R.string.infz2l_opsysed));
                processored.setText(getString(R.string.infz2l_processored));
                networked.setText(getString(R.string.infz2l_networked));
                appearanced.setText(getString(R.string.infz2l_appearanced));
                displayed.setText(getString(R.string.infz2l_displayed));
                cameraed.setText(getString(R.string.infz2l_cameraed));
                memoryed.setText(getString(R.string.infz2l_memoryed));
                con_sensored.setText(getString(R.string.infz2l_con_sensored));
                batteryed.setText(getString(R.string.infz2l_batteryed));
                othered.setText(getString(R.string.infz2l_othered));
                break;

            case "Zero 3":

                specifications();

                named.setText(getString(R.string.infz3_named));
                modeled.setText(getString(R.string.infz3_modeled));
                opsysed.setText(getString(R.string.infz3_opsysed));
                processored.setText(getString(R.string.infz3_processored));
                networked.setText(getString(R.string.infz3_networked));
                appearanced.setText(getString(R.string.infz3_appearanced));
                displayed.setText(getString(R.string.infz3_displayed));
                cameraed.setText(getString(R.string.infz3_cameraed));
                memoryed.setText(getString(R.string.infz3_memoryed));
                con_sensored.setText(getString(R.string.infz3_con_sensored));
                batteryed.setText(getString(R.string.infz3_batteryed));
                othered.setText(getString(R.string.infz3_othered));
                break;

            case "Zero 4":

                specifications();

                named.setText(getString(R.string.infz4_named));
                modeled.setText(getString(R.string.infz4_modeled));
                opsysed.setText(getString(R.string.infz4_opsysed));
                processored.setText(getString(R.string.infz4_processored));
                networked.setText(getString(R.string.infz4_networked));
                appearanced.setText(getString(R.string.infz4_appearanced));
                displayed.setText(getString(R.string.infz4_displayed));
                cameraed.setText(getString(R.string.infz4_cameraed));
                memoryed.setText(getString(R.string.infz4_memoryed));
                con_sensored.setText(getString(R.string.infz4_con_sensored));
                batteryed.setText(getString(R.string.infz4_batteryed));
                othered.setText(getString(R.string.infz4_othered));
                break;

            case "Zero 4 Plus":

                specifications();

                named.setText(getString(R.string.infz4p_named));
                modeled.setText(getString(R.string.infz4p_modeled));
                opsysed.setText(getString(R.string.infz4p_opsysed));
                processored.setText(getString(R.string.infz4p_processored));
                networked.setText(getString(R.string.infz4p_networked));
                appearanced.setText(getString(R.string.infz4p_appearanced));
                displayed.setText(getString(R.string.infz4p_displayed));
                cameraed.setText(getString(R.string.infz4p_cameraed));
                memoryed.setText(getString(R.string.infz4p_memoryed));
                con_sensored.setText(getString(R.string.infz4p_con_sensored));
                batteryed.setText(getString(R.string.infz4p_batteryed));
                othered.setText(getString(R.string.infz4p_othered));
                break;

        }
    }

    private void getlenovobrand(String lenovobrand) {

        switch (lenovobrand) {

            case "A Plus":

                specifications();

                named.setText(getString(R.string.len_A_Plus_named));
                modeled.setText(getString(R.string.len_A_Plus_modeled));
                opsysed.setText(getString(R.string.len_A_Plus_opsysed));
                processored.setText(getString(R.string.len_A_Plus_processored));
                networked.setText(getString(R.string.len_A_Plus_networked));
                appearanced.setText(getString(R.string.len_A_Plus_appearanced));
                displayed.setText(getString(R.string.len_A_Plus_displayed));
                cameraed.setText(getString(R.string.len_A_Plus_cameraed));
                memoryed.setText(getString(R.string.len_A_Plus_memoryed));
                con_sensored.setText(getString(R.string.len_A_Plus_con_sensored));
                batteryed.setText(getString(R.string.len_A_Plus_batteryed));
                othered.setText(getString(R.string.len_A_Plus_othered));
                break;

            case "A616":

                specifications();

                named.setText(getString(R.string.len_A616_named));
                modeled.setText(getString(R.string.len_A616_modeled));
                opsysed.setText(getString(R.string.len_A616_opsysed));
                processored.setText(getString(R.string.len_A616_processored));
                networked.setText(getString(R.string.len_A616_networked));
                appearanced.setText(getString(R.string.len_A616_appearanced));
                displayed.setText(getString(R.string.len_A616_displayed));
                cameraed.setText(getString(R.string.len_A616_cameraed));
                memoryed.setText(getString(R.string.len_A616_memoryed));
                con_sensored.setText(getString(R.string.len_A616_con_sensored));
                batteryed.setText(getString(R.string.len_A616_batteryed));
                othered.setText(getString(R.string.len_A616_othered));
                break;

            case "A1000":

                specifications();

                named.setText(getString(R.string.len_A1000_named));
                modeled.setText(getString(R.string.len_A1000_modeled));
                opsysed.setText(getString(R.string.len_A1000_opsysed));
                processored.setText(getString(R.string.len_A1000_processored));
                networked.setText(getString(R.string.len_A1000_networked));
                appearanced.setText(getString(R.string.len_A1000_appearanced));
                displayed.setText(getString(R.string.len_A1000_displayed));
                cameraed.setText(getString(R.string.len_A1000_cameraed));
                memoryed.setText(getString(R.string.len_A1000_memoryed));
                con_sensored.setText(getString(R.string.len_A1000_con_sensored));
                batteryed.setText(getString(R.string.len_A1000_batteryed));
                othered.setText(getString(R.string.len_A1000_othered));
                break;

            case "A1900":

                specifications();

                named.setText(getString(R.string.len_A1900_named));
                modeled.setText(getString(R.string.len_A1900_modeled));
                opsysed.setText(getString(R.string.len_A1900_opsysed));
                processored.setText(getString(R.string.len_A1900_processored));
                networked.setText(getString(R.string.len_A1900_networked));
                appearanced.setText(getString(R.string.len_A1900_appearanced));
                displayed.setText(getString(R.string.len_A1900_displayed));
                cameraed.setText(getString(R.string.len_A1900_cameraed));
                memoryed.setText(getString(R.string.len_A1900_memoryed));
                con_sensored.setText(getString(R.string.len_A1900_con_sensored));
                batteryed.setText(getString(R.string.len_A1900_batteryed));
                othered.setText(getString(R.string.len_A1900_othered));
                break;

            case "A2010":

                specifications();

                named.setText(getString(R.string.len_A2010_named));
                modeled.setText(getString(R.string.len_A2010_modeled));
                opsysed.setText(getString(R.string.len_A2010_opsysed));
                processored.setText(getString(R.string.len_A2010_processored));
                networked.setText(getString(R.string.len_A2010_networked));
                appearanced.setText(getString(R.string.len_A2010_appearanced));
                displayed.setText(getString(R.string.len_A2010_displayed));
                cameraed.setText(getString(R.string.len_A2010_cameraed));
                memoryed.setText(getString(R.string.len_A2010_memoryed));
                con_sensored.setText(getString(R.string.len_A2010_con_sensored));
                batteryed.setText(getString(R.string.len_A2010_batteryed));
                othered.setText(getString(R.string.len_A2010_othered));
                break;

            case "A3690":

                specifications();

                named.setText(getString(R.string.len_A3690_named));
                modeled.setText(getString(R.string.len_A3690_modeled));
                opsysed.setText(getString(R.string.len_A3690_opsysed));
                processored.setText(getString(R.string.len_A3690_processored));
                networked.setText(getString(R.string.len_A3690_networked));
                appearanced.setText(getString(R.string.len_A3690_appearanced));
                displayed.setText(getString(R.string.len_A3690_displayed));
                cameraed.setText(getString(R.string.len_A3690_cameraed));
                memoryed.setText(getString(R.string.len_A3690_memoryed));
                con_sensored.setText(getString(R.string.len_A3690_con_sensored));
                batteryed.setText(getString(R.string.len_A3690_batteryed));
                othered.setText(getString(R.string.len_A3690_othered));
                break;

            case "A3900":

                specifications();

                named.setText(getString(R.string.len_A3900_named));
                modeled.setText(getString(R.string.len_A3900_modeled));
                opsysed.setText(getString(R.string.len_A3900_opsysed));
                processored.setText(getString(R.string.len_A3900_processored));
                networked.setText(getString(R.string.len_A3900_networked));
                appearanced.setText(getString(R.string.len_A3900_appearanced));
                displayed.setText(getString(R.string.len_A3900_displayed));
                cameraed.setText(getString(R.string.len_A3900_cameraed));
                memoryed.setText(getString(R.string.len_A3900_memoryed));
                con_sensored.setText(getString(R.string.len_A3900_con_sensored));
                batteryed.setText(getString(R.string.len_A3900_batteryed));
                othered.setText(getString(R.string.len_A3900_othered));
                break;

            case "A5000":

                specifications();

                named.setText(getString(R.string.len_A5000_named));
                modeled.setText(getString(R.string.len_A5000_modeled));
                opsysed.setText(getString(R.string.len_A5000_opsysed));
                processored.setText(getString(R.string.len_A5000_processored));
                networked.setText(getString(R.string.len_A5000_networked));
                appearanced.setText(getString(R.string.len_A5000_appearanced));
                displayed.setText(getString(R.string.len_A5000_displayed));
                cameraed.setText(getString(R.string.len_A5000_cameraed));
                memoryed.setText(getString(R.string.len_A5000_memoryed));
                con_sensored.setText(getString(R.string.len_A5000_con_sensored));
                batteryed.setText(getString(R.string.len_A5000_batteryed));
                othered.setText(getString(R.string.len_A5000_othered));
                break;

            case "A6000":

                specifications();

                named.setText(getString(R.string.len_A6000_named));
                modeled.setText(getString(R.string.len_A6000_modeled));
                opsysed.setText(getString(R.string.len_A6000_opsysed));
                processored.setText(getString(R.string.len_A6000_processored));
                networked.setText(getString(R.string.len_A6000_networked));
                appearanced.setText(getString(R.string.len_A6000_appearanced));
                displayed.setText(getString(R.string.len_A6000_displayed));
                cameraed.setText(getString(R.string.len_A6000_cameraed));
                memoryed.setText(getString(R.string.len_A6000_memoryed));
                con_sensored.setText(getString(R.string.len_A6000_con_sensored));
                batteryed.setText(getString(R.string.len_A6000_batteryed));
                othered.setText(getString(R.string.len_A6000_othered));
                break;

            case "A6000 Plus":

                specifications();

                named.setText(getString(R.string.len_A6000_Plus_named));
                modeled.setText(getString(R.string.len_A6000_Plus_modeled));
                opsysed.setText(getString(R.string.len_A6000_Plus_opsysed));
                processored.setText(getString(R.string.len_A6000_Plus_processored));
                networked.setText(getString(R.string.len_A6000_Plus_networked));
                appearanced.setText(getString(R.string.len_A6000_Plus_appearanced));
                displayed.setText(getString(R.string.len_A6000_Plus_displayed));
                cameraed.setText(getString(R.string.len_A6000_Plus_cameraed));
                memoryed.setText(getString(R.string.len_A6000_Plus_memoryed));
                con_sensored.setText(getString(R.string.len_A6000_Plus_con_sensored));
                batteryed.setText(getString(R.string.len_A6000_Plus_batteryed));
                othered.setText(getString(R.string.len_A6000_Plus_othered));
                break;

            case "A6000 Shot":

                specifications();

                named.setText(getString(R.string.len_A6000_Shot_named));
                modeled.setText(getString(R.string.len_A6000_Shot_modeled));
                opsysed.setText(getString(R.string.len_A6000_Shot_opsysed));
                processored.setText(getString(R.string.len_A6000_Shot_processored));
                networked.setText(getString(R.string.len_A6000_Shot_networked));
                appearanced.setText(getString(R.string.len_A6000_Shot_appearanced));
                displayed.setText(getString(R.string.len_A6000_Shot_displayed));
                cameraed.setText(getString(R.string.len_A6000_Shot_cameraed));
                memoryed.setText(getString(R.string.len_A6000_Shot_memoryed));
                con_sensored.setText(getString(R.string.len_A6000_Shot_con_sensored));
                batteryed.setText(getString(R.string.len_A6000_Shot_batteryed));
                othered.setText(getString(R.string.len_A6000_Shot_othered));
                break;

            case "A7000 Plus":

                specifications();

                named.setText(getString(R.string.len_A7000_Plus_named));
                modeled.setText(getString(R.string.len_A7000_Plus_modeled));
                opsysed.setText(getString(R.string.len_A7000_Plus_opsysed));
                processored.setText(getString(R.string.len_A7000_Plus_processored));
                networked.setText(getString(R.string.len_A7000_Plus_networked));
                appearanced.setText(getString(R.string.len_A7000_Plus_appearanced));
                displayed.setText(getString(R.string.len_A7000_Plus_displayed));
                cameraed.setText(getString(R.string.len_A7000_Plus_cameraed));
                memoryed.setText(getString(R.string.len_A7000_Plus_memoryed));
                con_sensored.setText(getString(R.string.len_A7000_Plus_con_sensored));
                batteryed.setText(getString(R.string.len_A7000_Plus_batteryed));
                othered.setText(getString(R.string.len_A7000_Plus_othered));
                break;

            case "A7000 Turbo":

                specifications();

                named.setText(getString(R.string.len_A7000_Turbo_named));
                modeled.setText(getString(R.string.len_A7000_Turbo_modeled));
                opsysed.setText(getString(R.string.len_A7000_Turbo_opsysed));
                processored.setText(getString(R.string.len_A7000_Turbo_processored));
                networked.setText(getString(R.string.len_A7000_Turbo_networked));
                appearanced.setText(getString(R.string.len_A7000_Turbo_appearanced));
                displayed.setText(getString(R.string.len_A7000_Turbo_displayed));
                cameraed.setText(getString(R.string.len_A7000_Turbo_cameraed));
                memoryed.setText(getString(R.string.len_A7000_Turbo_memoryed));
                con_sensored.setText(getString(R.string.len_A7000_Turbo_con_sensored));
                batteryed.setText(getString(R.string.len_A7000_Turbo_batteryed));
                othered.setText(getString(R.string.len_A7000_Turbo_othered));
                break;

            case "C2":

                specifications();

                named.setText(getString(R.string.len_C2_named));
                modeled.setText(getString(R.string.len_C2_modeled));
                opsysed.setText(getString(R.string.len_C2_opsysed));
                processored.setText(getString(R.string.len_C2_processored));
                networked.setText(getString(R.string.len_C2_networked));
                appearanced.setText(getString(R.string.len_C2_appearanced));
                displayed.setText(getString(R.string.len_C2_displayed));
                cameraed.setText(getString(R.string.len_C2_cameraed));
                memoryed.setText(getString(R.string.len_C2_memoryed));
                con_sensored.setText(getString(R.string.len_C2_con_sensored));
                batteryed.setText(getString(R.string.len_C2_batteryed));
                othered.setText(getString(R.string.len_C2_othered));
                break;

            case "K3 Note":

                specifications();

                named.setText(getString(R.string.len_K3_Note_named));
                modeled.setText(getString(R.string.len_K3_Note_modeled));
                opsysed.setText(getString(R.string.len_K3_Note_opsysed));
                processored.setText(getString(R.string.len_K3_Note_processored));
                networked.setText(getString(R.string.len_K3_Note_networked));
                appearanced.setText(getString(R.string.len_K3_Note_appearanced));
                displayed.setText(getString(R.string.len_K3_Note_displayed));
                cameraed.setText(getString(R.string.len_K3_Note_cameraed));
                memoryed.setText(getString(R.string.len_K3_Note_memoryed));
                con_sensored.setText(getString(R.string.len_K3_Note_con_sensored));
                batteryed.setText(getString(R.string.len_K3_Note_batteryed));
                othered.setText(getString(R.string.len_K3_Note_othered));
                break;

            case "K3 Note Music":

                specifications();

                named.setText(getString(R.string.len_K3_Note_Music_named));
                modeled.setText(getString(R.string.len_K3_Note_Music_modeled));
                opsysed.setText(getString(R.string.len_K3_Note_Music_opsysed));
                processored.setText(getString(R.string.len_K3_Note_Music_processored));
                networked.setText(getString(R.string.len_K3_Note_Music_networked));
                appearanced.setText(getString(R.string.len_K3_Note_Music_appearanced));
                displayed.setText(getString(R.string.len_K3_Note_Music_displayed));
                cameraed.setText(getString(R.string.len_K3_Note_Music_cameraed));
                memoryed.setText(getString(R.string.len_K3_Note_Music_memoryed));
                con_sensored.setText(getString(R.string.len_K3_Note_Music_con_sensored));
                batteryed.setText(getString(R.string.len_K3_Note_Music_batteryed));
                othered.setText(getString(R.string.len_K3_Note_Music_othered));
                break;

            case "K4 Note":

                specifications();

                named.setText(getString(R.string.len_K4_Note_named));
                modeled.setText(getString(R.string.len_K4_Note_modeled));
                opsysed.setText(getString(R.string.len_K4_Note_opsysed));
                processored.setText(getString(R.string.len_K4_Note_processored));
                networked.setText(getString(R.string.len_K4_Note_networked));
                appearanced.setText(getString(R.string.len_K4_Note_appearanced));
                displayed.setText(getString(R.string.len_K4_Note_displayed));
                cameraed.setText(getString(R.string.len_K4_Note_cameraed));
                memoryed.setText(getString(R.string.len_K4_Note_memoryed));
                con_sensored.setText(getString(R.string.len_K4_Note_con_sensored));
                batteryed.setText(getString(R.string.len_K4_Note_batteryed));
                othered.setText(getString(R.string.len_K4_Note_othered));
                break;

            case "K5 Note":

                specifications();

                named.setText(getString(R.string.len_K5_Note_named));
                modeled.setText(getString(R.string.len_K5_Note_modeled));
                opsysed.setText(getString(R.string.len_K5_Note_opsysed));
                processored.setText(getString(R.string.len_K5_Note_processored));
                networked.setText(getString(R.string.len_K5_Note_networked));
                appearanced.setText(getString(R.string.len_K5_Note_appearanced));
                displayed.setText(getString(R.string.len_K5_Note_displayed));
                cameraed.setText(getString(R.string.len_K5_Note_cameraed));
                memoryed.setText(getString(R.string.len_K5_Note_memoryed));
                con_sensored.setText(getString(R.string.len_K5_Note_con_sensored));
                batteryed.setText(getString(R.string.len_K5_Note_batteryed));
                othered.setText(getString(R.string.len_K5_Note_othered));
                break;

            case "K6":

                specifications();

                named.setText(getString(R.string.len_K6_named));
                modeled.setText(getString(R.string.len_K6_modeled));
                opsysed.setText(getString(R.string.len_K6_opsysed));
                processored.setText(getString(R.string.len_K6_processored));
                networked.setText(getString(R.string.len_K6_networked));
                appearanced.setText(getString(R.string.len_K6_appearanced));
                displayed.setText(getString(R.string.len_K6_displayed));
                cameraed.setText(getString(R.string.len_K6_cameraed));
                memoryed.setText(getString(R.string.len_K6_memoryed));
                con_sensored.setText(getString(R.string.len_K6_con_sensored));
                batteryed.setText(getString(R.string.len_K6_batteryed));
                othered.setText(getString(R.string.len_K6_othered));
                break;

            case "K6 Note":

                specifications();

                named.setText(getString(R.string.len_K6_Note_named));
                modeled.setText(getString(R.string.len_K6_Note_modeled));
                opsysed.setText(getString(R.string.len_K6_Note_opsysed));
                processored.setText(getString(R.string.len_K6_Note_processored));
                networked.setText(getString(R.string.len_K6_Note_networked));
                appearanced.setText(getString(R.string.len_K6_Note_appearanced));
                displayed.setText(getString(R.string.len_K6_Note_displayed));
                cameraed.setText(getString(R.string.len_K6_Note_cameraed));
                memoryed.setText(getString(R.string.len_K6_Note_memoryed));
                con_sensored.setText(getString(R.string.len_K6_Note_con_sensored));
                batteryed.setText(getString(R.string.len_K6_Note_batteryed));
                othered.setText(getString(R.string.len_K6_Note_othered));
                break;

            case "K6 Power":

                specifications();

                named.setText(getString(R.string.len_K6_Power_named));
                modeled.setText(getString(R.string.len_K6_Power_modeled));
                opsysed.setText(getString(R.string.len_K6_Power_opsysed));
                processored.setText(getString(R.string.len_K6_Power_processored));
                networked.setText(getString(R.string.len_K6_Power_networked));
                appearanced.setText(getString(R.string.len_K6_Power_appearanced));
                displayed.setText(getString(R.string.len_K6_Power_displayed));
                cameraed.setText(getString(R.string.len_K6_Power_cameraed));
                memoryed.setText(getString(R.string.len_K6_Power_memoryed));
                con_sensored.setText(getString(R.string.len_K6_Power_con_sensored));
                batteryed.setText(getString(R.string.len_K6_Power_batteryed));
                othered.setText(getString(R.string.len_K6_Power_othered));
                break;

            case "K80":

                specifications();

                named.setText(getString(R.string.len_K80_named));
                modeled.setText(getString(R.string.len_K80_modeled));
                opsysed.setText(getString(R.string.len_K80_opsysed));
                processored.setText(getString(R.string.len_K80_processored));
                networked.setText(getString(R.string.len_K80_networked));
                appearanced.setText(getString(R.string.len_K80_appearanced));
                displayed.setText(getString(R.string.len_K80_displayed));
                cameraed.setText(getString(R.string.len_K80_cameraed));
                memoryed.setText(getString(R.string.len_K80_memoryed));
                con_sensored.setText(getString(R.string.len_K80_con_sensored));
                batteryed.setText(getString(R.string.len_K80_batteryed));
                othered.setText(getString(R.string.len_K80_othered));
                break;

            case "Phab":

                specifications();

                named.setText(getString(R.string.len_Phab_named));
                modeled.setText(getString(R.string.len_Phab_modeled));
                opsysed.setText(getString(R.string.len_Phab_opsysed));
                processored.setText(getString(R.string.len_Phab_processored));
                networked.setText(getString(R.string.len_Phab_networked));
                appearanced.setText(getString(R.string.len_Phab_appearanced));
                displayed.setText(getString(R.string.len_Phab_displayed));
                cameraed.setText(getString(R.string.len_Phab_cameraed));
                memoryed.setText(getString(R.string.len_Phab_memoryed));
                con_sensored.setText(getString(R.string.len_Phab_con_sensored));
                batteryed.setText(getString(R.string.len_Phab_batteryed));
                othered.setText(getString(R.string.len_Phab_othered));
                break;

            case "Phab Plus":

                specifications();

                named.setText(getString(R.string.len_Phab_Plus_named));
                modeled.setText(getString(R.string.len_Phab_Plus_modeled));
                opsysed.setText(getString(R.string.len_Phab_Plus_opsysed));
                processored.setText(getString(R.string.len_Phab_Plus_processored));
                networked.setText(getString(R.string.len_Phab_Plus_networked));
                appearanced.setText(getString(R.string.len_Phab_Plus_appearanced));
                displayed.setText(getString(R.string.len_Phab_Plus_displayed));
                cameraed.setText(getString(R.string.len_Phab_Plus_cameraed));
                memoryed.setText(getString(R.string.len_Phab_Plus_memoryed));
                con_sensored.setText(getString(R.string.len_Phab_Plus_con_sensored));
                batteryed.setText(getString(R.string.len_Phab_Plus_batteryed));
                othered.setText(getString(R.string.len_Phab_Plus_othered));
                break;

            case "Phab2":

                specifications();

                named.setText(getString(R.string.len_Phab2_named));
                modeled.setText(getString(R.string.len_Phab2_modeled));
                opsysed.setText(getString(R.string.len_Phab2_opsysed));
                processored.setText(getString(R.string.len_Phab2_processored));
                networked.setText(getString(R.string.len_Phab2_networked));
                appearanced.setText(getString(R.string.len_Phab2_appearanced));
                displayed.setText(getString(R.string.len_Phab2_displayed));
                cameraed.setText(getString(R.string.len_Phab2_cameraed));
                memoryed.setText(getString(R.string.len_Phab2_memoryed));
                con_sensored.setText(getString(R.string.len_Phab2_con_sensored));
                batteryed.setText(getString(R.string.len_Phab2_batteryed));
                othered.setText(getString(R.string.len_Phab2_othered));
                break;

            case "Phab2 Plus":

                specifications();

                named.setText(getString(R.string.len_Phab2_Plus_named));
                modeled.setText(getString(R.string.len_Phab2_Plus_modeled));
                opsysed.setText(getString(R.string.len_Phab2_Plus_opsysed));
                processored.setText(getString(R.string.len_Phab2_Plus_processored));
                networked.setText(getString(R.string.len_Phab2_Plus_networked));
                appearanced.setText(getString(R.string.len_Phab2_Plus_appearanced));
                displayed.setText(getString(R.string.len_Phab2_Plus_displayed));
                cameraed.setText(getString(R.string.len_Phab2_Plus_cameraed));
                memoryed.setText(getString(R.string.len_Phab2_Plus_memoryed));
                con_sensored.setText(getString(R.string.len_Phab2_Plus_con_sensored));
                batteryed.setText(getString(R.string.len_Phab2_Plus_batteryed));
                othered.setText(getString(R.string.len_Phab2_Plus_othered));
                break;

            case "Phab2 Pro":

                specifications();

                named.setText(getString(R.string.len_Phab2_Pro_named));
                modeled.setText(getString(R.string.len_Phab2_Pro_modeled));
                opsysed.setText(getString(R.string.len_Phab2_Pro_opsysed));
                processored.setText(getString(R.string.len_Phab2_Pro_processored));
                networked.setText(getString(R.string.len_Phab2_Pro_networked));
                appearanced.setText(getString(R.string.len_Phab2_Pro_appearanced));
                displayed.setText(getString(R.string.len_Phab2_Pro_displayed));
                cameraed.setText(getString(R.string.len_Phab2_Pro_cameraed));
                memoryed.setText(getString(R.string.len_Phab2_Pro_memoryed));
                con_sensored.setText(getString(R.string.len_Phab2_Pro_con_sensored));
                batteryed.setText(getString(R.string.len_Phab2_Pro_batteryed));
                othered.setText(getString(R.string.len_Phab2_Pro_othered));
                break;

            case "S60":

                specifications();

                named.setText(getString(R.string.len_S60_named));
                modeled.setText(getString(R.string.len_S60_modeled));
                opsysed.setText(getString(R.string.len_S60_opsysed));
                processored.setText(getString(R.string.len_S60_processored));
                networked.setText(getString(R.string.len_S60_networked));
                appearanced.setText(getString(R.string.len_S60_appearanced));
                displayed.setText(getString(R.string.len_S60_displayed));
                cameraed.setText(getString(R.string.len_S60_cameraed));
                memoryed.setText(getString(R.string.len_S60_memoryed));
                con_sensored.setText(getString(R.string.len_S60_con_sensored));
                batteryed.setText(getString(R.string.len_S60_batteryed));
                othered.setText(getString(R.string.len_S60_othered));
                break;

            case "Vibe A":

                specifications();

                named.setText(getString(R.string.len_Vibe_A_named));
                modeled.setText(getString(R.string.len_Vibe_A_modeled));
                opsysed.setText(getString(R.string.len_Vibe_A_opsysed));
                processored.setText(getString(R.string.len_Vibe_A_processored));
                networked.setText(getString(R.string.len_Vibe_A_networked));
                appearanced.setText(getString(R.string.len_Vibe_A_appearanced));
                displayed.setText(getString(R.string.len_Vibe_A_displayed));
                cameraed.setText(getString(R.string.len_Vibe_A_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_A_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_A_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_A_batteryed));
                othered.setText(getString(R.string.len_Vibe_A_othered));
                break;

            case "Vibe K5":

                specifications();

                named.setText(getString(R.string.len_Vibe_K5_named));
                modeled.setText(getString(R.string.len_Vibe_K5_modeled));
                opsysed.setText(getString(R.string.len_Vibe_K5_opsysed));
                processored.setText(getString(R.string.len_Vibe_K5_processored));
                networked.setText(getString(R.string.len_Vibe_K5_networked));
                appearanced.setText(getString(R.string.len_Vibe_K5_appearanced));
                displayed.setText(getString(R.string.len_Vibe_K5_displayed));
                cameraed.setText(getString(R.string.len_Vibe_K5_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_K5_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_K5_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_K5_batteryed));
                othered.setText(getString(R.string.len_Vibe_K5_othered));
                break;

            case "Vibe K5 Plus":

                specifications();

                named.setText(getString(R.string.len_Vibe_K5_Plus_named));
                modeled.setText(getString(R.string.len_Vibe_K5_Plus_modeled));
                opsysed.setText(getString(R.string.len_Vibe_K5_Plus_opsysed));
                processored.setText(getString(R.string.len_Vibe_K5_Plus_processored));
                networked.setText(getString(R.string.len_Vibe_K5_Plus_networked));
                appearanced.setText(getString(R.string.len_Vibe_K5_Plus_appearanced));
                displayed.setText(getString(R.string.len_Vibe_K5_Plus_displayed));
                cameraed.setText(getString(R.string.len_Vibe_K5_Plus_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_K5_Plus_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_K5_Plus_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_K5_Plus_batteryed));
                othered.setText(getString(R.string.len_Vibe_K5_Plus_othered));
                break;

            case "Vibe P1":

                specifications();

                named.setText(getString(R.string.len_Vibe_P1_named));
                modeled.setText(getString(R.string.len_Vibe_P1_modeled));
                opsysed.setText(getString(R.string.len_Vibe_P1_opsysed));
                processored.setText(getString(R.string.len_Vibe_P1_processored));
                networked.setText(getString(R.string.len_Vibe_P1_networked));
                appearanced.setText(getString(R.string.len_Vibe_P1_appearanced));
                displayed.setText(getString(R.string.len_Vibe_P1_displayed));
                cameraed.setText(getString(R.string.len_Vibe_P1_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_P1_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_P1_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_P1_batteryed));
                othered.setText(getString(R.string.len_Vibe_P1_othered));
                break;

            case "Vibe P1 Turbo":

                specifications();

                named.setText(getString(R.string.len_Vibe_P1_Turbo_named));
                modeled.setText(getString(R.string.len_Vibe_P1_Turbo_modeled));
                opsysed.setText(getString(R.string.len_Vibe_P1_Turbo_opsysed));
                processored.setText(getString(R.string.len_Vibe_P1_Turbo_processored));
                networked.setText(getString(R.string.len_Vibe_P1_Turbo_networked));
                appearanced.setText(getString(R.string.len_Vibe_P1_Turbo_appearanced));
                displayed.setText(getString(R.string.len_Vibe_P1_Turbo_displayed));
                cameraed.setText(getString(R.string.len_Vibe_P1_Turbo_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_P1_Turbo_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_P1_Turbo_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_P1_Turbo_batteryed));
                othered.setText(getString(R.string.len_Vibe_P1_Turbo_othered));
                break;

            case "Vibe P1m":

                specifications();

                named.setText(getString(R.string.len_Vibe_P1m_named));
                modeled.setText(getString(R.string.len_Vibe_P1m_modeled));
                opsysed.setText(getString(R.string.len_Vibe_P1m_opsysed));
                processored.setText(getString(R.string.len_Vibe_P1m_processored));
                networked.setText(getString(R.string.len_Vibe_P1m_networked));
                appearanced.setText(getString(R.string.len_Vibe_P1m_appearanced));
                displayed.setText(getString(R.string.len_Vibe_P1m_displayed));
                cameraed.setText(getString(R.string.len_Vibe_P1m_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_P1m_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_P1m_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_P1m_batteryed));
                othered.setText(getString(R.string.len_Vibe_P1m_othered));
                break;

            case "Vibe S1":

                specifications();

                named.setText(getString(R.string.len_Vibe_S1_named));
                modeled.setText(getString(R.string.len_Vibe_S1_modeled));
                opsysed.setText(getString(R.string.len_Vibe_S1_opsysed));
                processored.setText(getString(R.string.len_Vibe_S1_processored));
                networked.setText(getString(R.string.len_Vibe_S1_networked));
                appearanced.setText(getString(R.string.len_Vibe_S1_appearanced));
                displayed.setText(getString(R.string.len_Vibe_S1_displayed));
                cameraed.setText(getString(R.string.len_Vibe_S1_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_S1_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_S1_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_S1_batteryed));
                othered.setText(getString(R.string.len_Vibe_S1_othered));
                break;

            case "Vibe S1 Lite":

                specifications();

                named.setText(getString(R.string.len_Vibe_S1_Lite_named));
                modeled.setText(getString(R.string.len_Vibe_S1_Lite_modeled));
                opsysed.setText(getString(R.string.len_Vibe_S1_Lite_opsysed));
                processored.setText(getString(R.string.len_Vibe_S1_Lite_processored));
                networked.setText(getString(R.string.len_Vibe_S1_Lite_networked));
                appearanced.setText(getString(R.string.len_Vibe_S1_Lite_appearanced));
                displayed.setText(getString(R.string.len_Vibe_S1_Lite_displayed));
                cameraed.setText(getString(R.string.len_Vibe_S1_Lite_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_S1_Lite_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_S1_Lite_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_S1_Lite_batteryed));
                othered.setText(getString(R.string.len_Vibe_S1_Lite_othered));
                break;

            case "Vibe X3":

                specifications();

                named.setText(getString(R.string.len_Vibe_X3_named));
                modeled.setText(getString(R.string.len_Vibe_X3_modeled));
                opsysed.setText(getString(R.string.len_Vibe_X3_opsysed));
                processored.setText(getString(R.string.len_Vibe_X3_processored));
                networked.setText(getString(R.string.len_Vibe_X3_networked));
                appearanced.setText(getString(R.string.len_Vibe_X3_appearanced));
                displayed.setText(getString(R.string.len_Vibe_X3_displayed));
                cameraed.setText(getString(R.string.len_Vibe_X3_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_X3_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_X3_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_X3_batteryed));
                othered.setText(getString(R.string.len_Vibe_X3_othered));
                break;

            case "Vibe X3 Lite":

                specifications();

                named.setText(getString(R.string.len_Vibe_X3_Lite_named));
                modeled.setText(getString(R.string.len_Vibe_X3_Lite_modeled));
                opsysed.setText(getString(R.string.len_Vibe_X3_Lite_opsysed));
                processored.setText(getString(R.string.len_Vibe_X3_Lite_processored));
                networked.setText(getString(R.string.len_Vibe_X3_Lite_networked));
                appearanced.setText(getString(R.string.len_Vibe_X3_Lite_appearanced));
                displayed.setText(getString(R.string.len_Vibe_X3_Lite_displayed));
                cameraed.setText(getString(R.string.len_Vibe_X3_Lite_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_X3_Lite_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_X3_Lite_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_X3_Lite_batteryed));
                othered.setText(getString(R.string.len_Vibe_X3_Lite_othered));
                break;

            case "Vibe X3 Youth Edition":

                specifications();

                named.setText(getString(R.string.len_Vibe_X3_Youth_Edition_named));
                modeled.setText(getString(R.string.len_Vibe_X3_Youth_Edition_modeled));
                opsysed.setText(getString(R.string.len_Vibe_X3_Youth_Edition_opsysed));
                processored.setText(getString(R.string.len_Vibe_X3_Youth_Edition_processored));
                networked.setText(getString(R.string.len_Vibe_X3_Youth_Edition_networked));
                appearanced.setText(getString(R.string.len_Vibe_X3_Youth_Edition_appearanced));
                displayed.setText(getString(R.string.len_Vibe_X3_Youth_Edition_displayed));
                cameraed.setText(getString(R.string.len_Vibe_X3_Youth_Edition_cameraed));
                memoryed.setText(getString(R.string.len_Vibe_X3_Youth_Edition_memoryed));
                con_sensored.setText(getString(R.string.len_Vibe_X3_Youth_Edition_con_sensored));
                batteryed.setText(getString(R.string.len_Vibe_X3_Youth_Edition_batteryed));
                othered.setText(getString(R.string.len_Vibe_X3_Youth_Edition_othered));
                break;

            case "ZUK Z2":

                specifications();

                named.setText(getString(R.string.len_ZUK_Z2_named));
                modeled.setText(getString(R.string.len_ZUK_Z2_modeled));
                opsysed.setText(getString(R.string.len_ZUK_Z2_opsysed));
                processored.setText(getString(R.string.len_ZUK_Z2_processored));
                networked.setText(getString(R.string.len_ZUK_Z2_networked));
                appearanced.setText(getString(R.string.len_ZUK_Z2_appearanced));
                displayed.setText(getString(R.string.len_ZUK_Z2_displayed));
                cameraed.setText(getString(R.string.len_ZUK_Z2_cameraed));
                memoryed.setText(getString(R.string.len_ZUK_Z2_memoryed));
                con_sensored.setText(getString(R.string.len_ZUK_Z2_con_sensored));
                batteryed.setText(getString(R.string.len_ZUK_Z2_batteryed));
                othered.setText(getString(R.string.len_ZUK_Z2_othered));
                break;

        }
    }

    private void getlgbrand(String lgbrand) {

        switch (lgbrand) {

            case "Class":

                specifications();

                named.setText(getString(R.string.lgclass_named));
                modeled.setText(getString(R.string.lgclass_modeled));
                opsysed.setText(getString(R.string.lgclass_opsysed));
                processored.setText(getString(R.string.lgclass_processored));
                networked.setText(getString(R.string.lgclass_networked));
                appearanced.setText(getString(R.string.lgclass_appearanced));
                displayed.setText(getString(R.string.lgclass_displayed));
                cameraed.setText(getString(R.string.lgclass_cameraed));
                memoryed.setText(getString(R.string.lgclass_memoryed));
                con_sensored.setText(getString(R.string.lgclass_con_sensored));
                batteryed.setText(getString(R.string.lgclass_batteryed));
                othered.setText(getString(R.string.lgclass_othered));
                break;

            case "G5":

                specifications();

                named.setText(getString(R.string.lgg5_named));
                modeled.setText(getString(R.string.lgg5_modeled));
                opsysed.setText(getString(R.string.lgg5_opsysed));
                processored.setText(getString(R.string.lgg5_processored));
                networked.setText(getString(R.string.lgg5_networked));
                appearanced.setText(getString(R.string.lgg5_appearanced));
                displayed.setText(getString(R.string.lgg5_displayed));
                cameraed.setText(getString(R.string.lgg5_cameraed));
                memoryed.setText(getString(R.string.lgg5_memoryed));
                con_sensored.setText(getString(R.string.lgg5_con_sensored));
                batteryed.setText(getString(R.string.lgg5_batteryed));
                othered.setText(getString(R.string.lgg5_othered));
                break;

            case "G5 Lite":

                specifications();

                named.setText(getString(R.string.lgg5l_named));
                modeled.setText(getString(R.string.lgg5l_modeled));
                opsysed.setText(getString(R.string.lgg5l_opsysed));
                processored.setText(getString(R.string.lgg5l_processored));
                networked.setText(getString(R.string.lgg5l_networked));
                appearanced.setText(getString(R.string.lgg5l_appearanced));
                displayed.setText(getString(R.string.lgg5l_displayed));
                cameraed.setText(getString(R.string.lgg5l_cameraed));
                memoryed.setText(getString(R.string.lgg5l_memoryed));
                con_sensored.setText(getString(R.string.lgg5l_con_sensored));
                batteryed.setText(getString(R.string.lgg5l_batteryed));
                othered.setText(getString(R.string.lgg5l_othered));
                break;

            case "G Stylo":

                specifications();

                named.setText(getString(R.string.lgxstylo_named));
                modeled.setText(getString(R.string.lgxstylo_modeled));
                opsysed.setText(getString(R.string.lgxstylo_opsysed));
                processored.setText(getString(R.string.lgxstylo_processored));
                networked.setText(getString(R.string.lgxstylo_networked));
                appearanced.setText(getString(R.string.lgxstylo_appearanced));
                displayed.setText(getString(R.string.lgxstylo_displayed));
                cameraed.setText(getString(R.string.lgxstylo_cameraed));
                memoryed.setText(getString(R.string.lgxstylo_memoryed));
                con_sensored.setText(getString(R.string.lgxstylo_con_sensored));
                batteryed.setText(getString(R.string.lgxstylo_batteryed));
                othered.setText(getString(R.string.lgxstylo_othered));
                break;

            case "K3":

                specifications();

                named.setText(getString(R.string.lgk3_named));
                modeled.setText(getString(R.string.lgk3_modeled));
                opsysed.setText(getString(R.string.lgk3_opsysed));
                processored.setText(getString(R.string.lgk3_processored));
                networked.setText(getString(R.string.lgk3_networked));
                appearanced.setText(getString(R.string.lgk3_appearanced));
                displayed.setText(getString(R.string.lgk3_displayed));
                cameraed.setText(getString(R.string.lgk3_cameraed));
                memoryed.setText(getString(R.string.lgk3_memoryed));
                con_sensored.setText(getString(R.string.lgk3_con_sensored));
                batteryed.setText(getString(R.string.lgk3_batteryed));
                othered.setText(getString(R.string.lgk3_othered));
                break;

            case "K4":

                specifications();

                named.setText(getString(R.string.lgk4_named));
                modeled.setText(getString(R.string.lgk4_modeled));
                opsysed.setText(getString(R.string.lgk4_opsysed));
                processored.setText(getString(R.string.lgk4_processored));
                networked.setText(getString(R.string.lgk4_networked));
                appearanced.setText(getString(R.string.lgk4_appearanced));
                displayed.setText(getString(R.string.lgk4_displayed));
                cameraed.setText(getString(R.string.lgk4_cameraed));
                memoryed.setText(getString(R.string.lgk4_memoryed));
                con_sensored.setText(getString(R.string.lgk4_con_sensored));
                batteryed.setText(getString(R.string.lgk4_batteryed));
                othered.setText(getString(R.string.lgk4_othered));
                break;

            case "K5":

                specifications();

                named.setText(getString(R.string.lgk5_named));
                modeled.setText(getString(R.string.lgk5_modeled));
                opsysed.setText(getString(R.string.lgk5_opsysed));
                processored.setText(getString(R.string.lgk5_processored));
                networked.setText(getString(R.string.lgk5_networked));
                appearanced.setText(getString(R.string.lgk5_appearanced));
                displayed.setText(getString(R.string.lgk5_displayed));
                cameraed.setText(getString(R.string.lgk5_cameraed));
                memoryed.setText(getString(R.string.lgk5_memoryed));
                con_sensored.setText(getString(R.string.lgk5_con_sensored));
                batteryed.setText(getString(R.string.lgk5_batteryed));
                othered.setText(getString(R.string.lgk5_othered));
                break;

            case "K7":

                specifications();

                named.setText(getString(R.string.lgk7_named));
                modeled.setText(getString(R.string.lgk7_modeled));
                opsysed.setText(getString(R.string.lgk7_opsysed));
                processored.setText(getString(R.string.lgk7_processored));
                networked.setText(getString(R.string.lgk7_networked));
                appearanced.setText(getString(R.string.lgk7_appearanced));
                displayed.setText(getString(R.string.lgk7_displayed));
                cameraed.setText(getString(R.string.lgk7_cameraed));
                memoryed.setText(getString(R.string.lgk7_memoryed));
                con_sensored.setText(getString(R.string.lgk7_con_sensored));
                batteryed.setText(getString(R.string.lgk7_batteryed));
                othered.setText(getString(R.string.lgk7_othered));
                break;

            case "K8":

                specifications();

                named.setText(getString(R.string.lgk8_named));
                modeled.setText(getString(R.string.lgk8_modeled));
                opsysed.setText(getString(R.string.lgk8_opsysed));
                processored.setText(getString(R.string.lgk8_processored));
                networked.setText(getString(R.string.lgk8_networked));
                appearanced.setText(getString(R.string.lgk8_appearanced));
                displayed.setText(getString(R.string.lgk8_displayed));
                cameraed.setText(getString(R.string.lgk8_cameraed));
                memoryed.setText(getString(R.string.lgk8_memoryed));
                con_sensored.setText(getString(R.string.lgk8_con_sensored));
                batteryed.setText(getString(R.string.lgk8_batteryed));
                othered.setText(getString(R.string.lgk8_othered));
                break;

            case "K10":

                specifications();

                named.setText(getString(R.string.lgk10_named));
                modeled.setText(getString(R.string.lgk10_modeled));
                opsysed.setText(getString(R.string.lgk10_opsysed));
                processored.setText(getString(R.string.lgk10_processored));
                networked.setText(getString(R.string.lgk10_networked));
                appearanced.setText(getString(R.string.lgk10_appearanced));
                displayed.setText(getString(R.string.lgk10_displayed));
                cameraed.setText(getString(R.string.lgk10_cameraed));
                memoryed.setText(getString(R.string.lgk10_memoryed));
                con_sensored.setText(getString(R.string.lgk10_con_sensored));
                batteryed.setText(getString(R.string.lgk10_batteryed));
                othered.setText(getString(R.string.lgk10_othered));
                break;

            case "Nexus 5X":

                specifications();

                named.setText(getString(R.string.lgnexus5x_named));
                modeled.setText(getString(R.string.lgnexus5x_modeled));
                opsysed.setText(getString(R.string.lgnexus5x_opsysed));
                processored.setText(getString(R.string.lgnexus5x_processored));
                networked.setText(getString(R.string.lgnexus5x_networked));
                appearanced.setText(getString(R.string.lgnexus5x_appearanced));
                displayed.setText(getString(R.string.lgnexus5x_displayed));
                cameraed.setText(getString(R.string.lgnexus5x_cameraed));
                memoryed.setText(getString(R.string.lgnexus5x_memoryed));
                con_sensored.setText(getString(R.string.lgnexus5x_con_sensored));
                batteryed.setText(getString(R.string.lgnexus5x_batteryed));
                othered.setText(getString(R.string.lgnexus5x_othered));
                break;

            case "Ray":

                specifications();

                named.setText(getString(R.string.lgray_named));
                modeled.setText(getString(R.string.lgray_modeled));
                opsysed.setText(getString(R.string.lgray_opsysed));
                processored.setText(getString(R.string.lgray_processored));
                networked.setText(getString(R.string.lgray_networked));
                appearanced.setText(getString(R.string.lgray_appearanced));
                displayed.setText(getString(R.string.lgray_displayed));
                cameraed.setText(getString(R.string.lgray_cameraed));
                memoryed.setText(getString(R.string.lgray_memoryed));
                con_sensored.setText(getString(R.string.lgray_con_sensored));
                batteryed.setText(getString(R.string.lgray_batteryed));
                othered.setText(getString(R.string.lgray_othered));
                break;

            case "Stylus 2":

                specifications();

                named.setText(getString(R.string.lgxstylus2_named));
                modeled.setText(getString(R.string.lgxstylus2_modeled));
                opsysed.setText(getString(R.string.lgxstylus2_opsysed));
                processored.setText(getString(R.string.lgxstylus2_processored));
                networked.setText(getString(R.string.lgxstylus2_networked));
                appearanced.setText(getString(R.string.lgxstylus2_appearanced));
                displayed.setText(getString(R.string.lgxstylus2_displayed));
                cameraed.setText(getString(R.string.lgxstylus2_cameraed));
                memoryed.setText(getString(R.string.lgxstylus2_memoryed));
                con_sensored.setText(getString(R.string.lgxstylus2_con_sensored));
                batteryed.setText(getString(R.string.lgxstylus2_batteryed));
                othered.setText(getString(R.string.lgxstylus2_othered));
                break;

            case "Stylus 2 Plus":

                specifications();

                named.setText(getString(R.string.lgxstylus2plus_named));
                modeled.setText(getString(R.string.lgxstylus2plus_modeled));
                opsysed.setText(getString(R.string.lgxstylus2plus_opsysed));
                processored.setText(getString(R.string.lgxstylus2plus_processored));
                networked.setText(getString(R.string.lgxstylus2plus_networked));
                appearanced.setText(getString(R.string.lgxstylus2plus_appearanced));
                displayed.setText(getString(R.string.lgxstylus2plus_displayed));
                cameraed.setText(getString(R.string.lgxstylus2plus_cameraed));
                memoryed.setText(getString(R.string.lgxstylus2plus_memoryed));
                con_sensored.setText(getString(R.string.lgxstylus2plus_con_sensored));
                batteryed.setText(getString(R.string.lgxstylus2plus_batteryed));
                othered.setText(getString(R.string.lgxstylus2plus_othered));
                break;

            case "V10":

                specifications();

                named.setText(getString(R.string.lgv10_named));
                modeled.setText(getString(R.string.lgv10_modeled));
                opsysed.setText(getString(R.string.lgv10_opsysed));
                processored.setText(getString(R.string.lgv10_processored));
                networked.setText(getString(R.string.lgv10_networked));
                appearanced.setText(getString(R.string.lgv10_appearanced));
                displayed.setText(getString(R.string.lgv10_displayed));
                cameraed.setText(getString(R.string.lgv10_cameraed));
                memoryed.setText(getString(R.string.lgv10_memoryed));
                con_sensored.setText(getString(R.string.lgv10_con_sensored));
                batteryed.setText(getString(R.string.lgv10_batteryed));
                othered.setText(getString(R.string.lgv10_othered));
                break;

            case "Vista 2":

                specifications();

                named.setText(getString(R.string.lgvista2_named));
                modeled.setText(getString(R.string.lgvista2_modeled));
                opsysed.setText(getString(R.string.lgvista2_opsysed));
                processored.setText(getString(R.string.lgvista2_processored));
                networked.setText(getString(R.string.lgvista2_networked));
                appearanced.setText(getString(R.string.lgvista2_appearanced));
                displayed.setText(getString(R.string.lgvista2_displayed));
                cameraed.setText(getString(R.string.lgvista2_cameraed));
                memoryed.setText(getString(R.string.lgvista2_memoryed));
                con_sensored.setText(getString(R.string.lgvista2_con_sensored));
                batteryed.setText(getString(R.string.lgvista2_batteryed));
                othered.setText(getString(R.string.lgvista2_othered));
                break;

            case "X5":

                specifications();

                named.setText(getString(R.string.lgx5_named));
                modeled.setText(getString(R.string.lgx5_modeled));
                opsysed.setText(getString(R.string.lgx5_opsysed));
                processored.setText(getString(R.string.lgx5_processored));
                networked.setText(getString(R.string.lgx5_networked));
                appearanced.setText(getString(R.string.lgx5_appearanced));
                displayed.setText(getString(R.string.lgx5_displayed));
                cameraed.setText(getString(R.string.lgx5_cameraed));
                memoryed.setText(getString(R.string.lgx5_memoryed));
                con_sensored.setText(getString(R.string.lgx5_con_sensored));
                batteryed.setText(getString(R.string.lgx5_batteryed));
                othered.setText(getString(R.string.lgx5_othered));
                break;

            case "X Cam":

                specifications();

                named.setText(getString(R.string.lgxcam_named));
                modeled.setText(getString(R.string.lgxcam_modeled));
                opsysed.setText(getString(R.string.lgxcam_opsysed));
                processored.setText(getString(R.string.lgxcam_processored));
                networked.setText(getString(R.string.lgxcam_networked));
                appearanced.setText(getString(R.string.lgxcam_appearanced));
                displayed.setText(getString(R.string.lgxcam_displayed));
                cameraed.setText(getString(R.string.lgxcam_cameraed));
                memoryed.setText(getString(R.string.lgxcam_memoryed));
                con_sensored.setText(getString(R.string.lgxcam_con_sensored));
                batteryed.setText(getString(R.string.lgxcam_batteryed));
                othered.setText(getString(R.string.lgxcam_othered));
                break;

            case "X Mach":

                specifications();

                named.setText(getString(R.string.lgxmach_named));
                modeled.setText(getString(R.string.lgxmach_modeled));
                opsysed.setText(getString(R.string.lgxmach_opsysed));
                processored.setText(getString(R.string.lgxmach_processored));
                networked.setText(getString(R.string.lgxmach_networked));
                appearanced.setText(getString(R.string.lgxmach_appearanced));
                displayed.setText(getString(R.string.lgxmach_displayed));
                cameraed.setText(getString(R.string.lgxmach_cameraed));
                memoryed.setText(getString(R.string.lgxmach_memoryed));
                con_sensored.setText(getString(R.string.lgxmach_con_sensored));
                batteryed.setText(getString(R.string.lgxmach_batteryed));
                othered.setText(getString(R.string.lgxmach_othered));
                break;

            case "X Power":

                specifications();

                named.setText(getString(R.string.lgxpow_named));
                modeled.setText(getString(R.string.lgxpow_modeled));
                opsysed.setText(getString(R.string.lgxpow_opsysed));
                processored.setText(getString(R.string.lgxpow_processored));
                networked.setText(getString(R.string.lgxpow_networked));
                appearanced.setText(getString(R.string.lgxpow_appearanced));
                displayed.setText(getString(R.string.lgxpow_displayed));
                cameraed.setText(getString(R.string.lgxpow_cameraed));
                memoryed.setText(getString(R.string.lgxpow_memoryed));
                con_sensored.setText(getString(R.string.lgxpow_con_sensored));
                batteryed.setText(getString(R.string.lgxpow_batteryed));
                othered.setText(getString(R.string.lgxpow_othered));
                break;

            case "X Screen":

                specifications();

                named.setText(getString(R.string.lgxscreen_named));
                modeled.setText(getString(R.string.lgxscreen_modeled));
                opsysed.setText(getString(R.string.lgxscreen_opsysed));
                processored.setText(getString(R.string.lgxscreen_processored));
                networked.setText(getString(R.string.lgxscreen_networked));
                appearanced.setText(getString(R.string.lgxscreen_appearanced));
                displayed.setText(getString(R.string.lgxscreen_displayed));
                cameraed.setText(getString(R.string.lgxscreen_cameraed));
                memoryed.setText(getString(R.string.lgxscreen_memoryed));
                con_sensored.setText(getString(R.string.lgxscreen_con_sensored));
                batteryed.setText(getString(R.string.lgxscreen_batteryed));
                othered.setText(getString(R.string.lgxscreen_othered));
                break;

            case "X Skin":

                specifications();

                named.setText(getString(R.string.lgxskin_named));
                modeled.setText(getString(R.string.lgxskin_modeled));
                opsysed.setText(getString(R.string.lgxskin_opsysed));
                processored.setText(getString(R.string.lgxskin_processored));
                networked.setText(getString(R.string.lgxskin_networked));
                appearanced.setText(getString(R.string.lgxskin_appearanced));
                displayed.setText(getString(R.string.lgxskin_displayed));
                cameraed.setText(getString(R.string.lgxskin_cameraed));
                memoryed.setText(getString(R.string.lgxskin_memoryed));
                con_sensored.setText(getString(R.string.lgxskin_con_sensored));
                batteryed.setText(getString(R.string.lgxskin_batteryed));
                othered.setText(getString(R.string.lgxskin_othered));
                break;

            case "X Style":

                specifications();

                named.setText(getString(R.string.lgxstyle_named));
                modeled.setText(getString(R.string.lgxstyle_modeled));
                opsysed.setText(getString(R.string.lgxstyle_opsysed));
                processored.setText(getString(R.string.lgxstyle_processored));
                networked.setText(getString(R.string.lgxstyle_networked));
                appearanced.setText(getString(R.string.lgxstyle_appearanced));
                displayed.setText(getString(R.string.lgxstyle_displayed));
                cameraed.setText(getString(R.string.lgxstyle_cameraed));
                memoryed.setText(getString(R.string.lgxstyle_memoryed));
                con_sensored.setText(getString(R.string.lgxstyle_con_sensored));
                batteryed.setText(getString(R.string.lgxstyle_batteryed));
                othered.setText(getString(R.string.lgxstyle_othered));
                break;

        }
    }

    private void getsamsungbrand(String samsungbrand) {

        switch (samsungbrand) {

            case "Galaxy A3":

                specifications();

                named.setText(getString(R.string.GalaxyA3_named));
                modeled.setText(getString(R.string.GalaxyA3_modeled));
                opsysed.setText(getString(R.string.GalaxyA3_opsysed));
                processored.setText(getString(R.string.GalaxyA3_processored));
                networked.setText(getString(R.string.GalaxyA3_networked));
                appearanced.setText(getString(R.string.GalaxyA3_appearanced));
                displayed.setText(getString(R.string.GalaxyA3_displayed));
                cameraed.setText(getString(R.string.GalaxyA3_cameraed));
                memoryed.setText(getString(R.string.GalaxyA3_memoryed));
                con_sensored.setText(getString(R.string.GalaxyA3_con_sensored));
                batteryed.setText(getString(R.string.GalaxyA3_batteryed));
                othered.setText(getString(R.string.GalaxyA3_othered));
                break;

            case "Galaxy A5":

                specifications();

                named.setText(getString(R.string.GalaxyA5_named));
                modeled.setText(getString(R.string.GalaxyA5_modeled));
                opsysed.setText(getString(R.string.GalaxyA5_opsysed));
                processored.setText(getString(R.string.GalaxyA5_processored));
                networked.setText(getString(R.string.GalaxyA5_networked));
                appearanced.setText(getString(R.string.GalaxyA5_appearanced));
                displayed.setText(getString(R.string.GalaxyA5_displayed));
                cameraed.setText(getString(R.string.GalaxyA5_cameraed));
                memoryed.setText(getString(R.string.GalaxyA5_memoryed));
                con_sensored.setText(getString(R.string.GalaxyA5_con_sensored));
                batteryed.setText(getString(R.string.GalaxyA5_batteryed));
                othered.setText(getString(R.string.GalaxyA5_othered));
                break;

            case "Galaxy A7":

                specifications();

                named.setText(getString(R.string.GalaxyA7_named));
                modeled.setText(getString(R.string.GalaxyA7_modeled));
                opsysed.setText(getString(R.string.GalaxyA7_opsysed));
                processored.setText(getString(R.string.GalaxyA7_processored));
                networked.setText(getString(R.string.GalaxyA7_networked));
                appearanced.setText(getString(R.string.GalaxyA7_appearanced));
                displayed.setText(getString(R.string.GalaxyA7_displayed));
                cameraed.setText(getString(R.string.GalaxyA7_cameraed));
                memoryed.setText(getString(R.string.GalaxyA7_memoryed));
                con_sensored.setText(getString(R.string.GalaxyA7_con_sensored));
                batteryed.setText(getString(R.string.GalaxyA7_batteryed));
                othered.setText(getString(R.string.GalaxyA7_othered));
                break;

            case "Galaxy A8":

                specifications();

                named.setText(getString(R.string.GalaxyA8_named));
                modeled.setText(getString(R.string.GalaxyA8_modeled));
                opsysed.setText(getString(R.string.GalaxyA8_opsysed));
                processored.setText(getString(R.string.GalaxyA8_processored));
                networked.setText(getString(R.string.GalaxyA8_networked));
                appearanced.setText(getString(R.string.GalaxyA8_appearanced));
                displayed.setText(getString(R.string.GalaxyA8_displayed));
                cameraed.setText(getString(R.string.GalaxyA8_cameraed));
                memoryed.setText(getString(R.string.GalaxyA8_memoryed));
                con_sensored.setText(getString(R.string.GalaxyA8_con_sensored));
                batteryed.setText(getString(R.string.GalaxyA8_batteryed));
                othered.setText(getString(R.string.GalaxyA8_othered));
                break;

            case "Galaxy C7 Pro":

                specifications();

                named.setText(getString(R.string.GalaxyC7Pro_named));
                modeled.setText(getString(R.string.GalaxyC7Pro_modeled));
                opsysed.setText(getString(R.string.GalaxyC7Pro_opsysed));
                processored.setText(getString(R.string.GalaxyC7Pro_processored));
                networked.setText(getString(R.string.GalaxyC7Pro_networked));
                appearanced.setText(getString(R.string.GalaxyC7Pro_appearanced));
                displayed.setText(getString(R.string.GalaxyC7Pro_displayed));
                cameraed.setText(getString(R.string.GalaxyC7Pro_cameraed));
                memoryed.setText(getString(R.string.GalaxyC7Pro_memoryed));
                con_sensored.setText(getString(R.string.GalaxyC7Pro_con_sensored));
                batteryed.setText(getString(R.string.GalaxyC7Pro_batteryed));
                othered.setText(getString(R.string.GalaxyC7Pro_othered));
                break;

            case "Galaxy C9 Pro":

                specifications();

                named.setText(getString(R.string.GalaxyC9Pro_named));
                modeled.setText(getString(R.string.GalaxyC9Pro_modeled));
                opsysed.setText(getString(R.string.GalaxyC9Pro_opsysed));
                processored.setText(getString(R.string.GalaxyC9Pro_processored));
                networked.setText(getString(R.string.GalaxyC9Pro_networked));
                appearanced.setText(getString(R.string.GalaxyC9Pro_appearanced));
                displayed.setText(getString(R.string.GalaxyC9Pro_displayed));
                cameraed.setText(getString(R.string.GalaxyC9Pro_cameraed));
                memoryed.setText(getString(R.string.GalaxyC9Pro_memoryed));
                con_sensored.setText(getString(R.string.GalaxyC9Pro_con_sensored));
                batteryed.setText(getString(R.string.GalaxyC9Pro_batteryed));
                othered.setText(getString(R.string.GalaxyC9Pro_othered));
                break;

            case "Galaxy Grand Prime Plus":

                specifications();

                named.setText(getString(R.string.GalaxyGrandPrimePlus_named));
                modeled.setText(getString(R.string.GalaxyGrandPrimePlus_modeled));
                opsysed.setText(getString(R.string.GalaxyGrandPrimePlus_opsysed));
                processored.setText(getString(R.string.GalaxyGrandPrimePlus_processored));
                networked.setText(getString(R.string.GalaxyGrandPrimePlus_networked));
                appearanced.setText(getString(R.string.GalaxyGrandPrimePlus_appearanced));
                displayed.setText(getString(R.string.GalaxyGrandPrimePlus_displayed));
                cameraed.setText(getString(R.string.GalaxyGrandPrimePlus_cameraed));
                memoryed.setText(getString(R.string.GalaxyGrandPrimePlus_memoryed));
                con_sensored.setText(getString(R.string.GalaxyGrandPrimePlus_con_sensored));
                batteryed.setText(getString(R.string.GalaxyGrandPrimePlus_batteryed));
                othered.setText(getString(R.string.GalaxyGrandPrimePlus_othered));
                break;

            case "Galaxy J1 mini prime":

                specifications();

                named.setText(getString(R.string.SamsungGalaxyJ1miniprime_named));
                modeled.setText(getString(R.string.SamsungGalaxyJ1miniprime_modeled));
                opsysed.setText(getString(R.string.SamsungGalaxyJ1miniprime_opsysed));
                processored.setText(getString(R.string.SamsungGalaxyJ1miniprime_processored));
                networked.setText(getString(R.string.SamsungGalaxyJ1miniprime_networked));
                appearanced.setText(getString(R.string.SamsungGalaxyJ1miniprime_appearanced));
                displayed.setText(getString(R.string.SamsungGalaxyJ1miniprime_displayed));
                cameraed.setText(getString(R.string.SamsungGalaxyJ1miniprime_cameraed));
                memoryed.setText(getString(R.string.SamsungGalaxyJ1miniprime_memoryed));
                con_sensored.setText(getString(R.string.SamsungGalaxyJ1miniprime_con_sensored));
                batteryed.setText(getString(R.string.SamsungGalaxyJ1miniprime_batteryed));
                othered.setText(getString(R.string.SamsungGalaxyJ1miniprime_othered));
                break;

            case "Galaxy J2":

                specifications();

                named.setText(getString(R.string.GalaxyJ2_named));
                modeled.setText(getString(R.string.GalaxyJ2_modeled));
                opsysed.setText(getString(R.string.GalaxyJ2_opsysed));
                processored.setText(getString(R.string.GalaxyJ2_processored));
                networked.setText(getString(R.string.GalaxyJ2_networked));
                appearanced.setText(getString(R.string.GalaxyJ2_appearanced));
                displayed.setText(getString(R.string.GalaxyJ2_displayed));
                cameraed.setText(getString(R.string.GalaxyJ2_cameraed));
                memoryed.setText(getString(R.string.GalaxyJ2_memoryed));
                con_sensored.setText(getString(R.string.GalaxyJ2_con_sensored));
                batteryed.setText(getString(R.string.GalaxyJ2_batteryed));
                othered.setText(getString(R.string.GalaxyJ2_othered));
                break;

            case "Galaxy J2 Prime":

                specifications();

                named.setText(getString(R.string.GalaxyJ2Prime_named));
                modeled.setText(getString(R.string.GalaxyJ2Prime_modeled));
                opsysed.setText(getString(R.string.GalaxyJ2Prime_opsysed));
                processored.setText(getString(R.string.GalaxyJ2Prime_processored));
                networked.setText(getString(R.string.GalaxyJ2Prime_networked));
                appearanced.setText(getString(R.string.GalaxyJ2Prime_appearanced));
                displayed.setText(getString(R.string.GalaxyJ2Prime_displayed));
                cameraed.setText(getString(R.string.GalaxyJ2Prime_cameraed));
                memoryed.setText(getString(R.string.GalaxyJ2Prime_memoryed));
                con_sensored.setText(getString(R.string.GalaxyJ2Prime_con_sensored));
                batteryed.setText(getString(R.string.GalaxyJ2Prime_batteryed));
                othered.setText(getString(R.string.GalaxyJ2Prime_othered));
                break;

            case "Galaxy J2 Pro":

                specifications();

                named.setText(getString(R.string.GalaxyJ2Pro_named));
                modeled.setText(getString(R.string.GalaxyJ2Pro_modeled));
                opsysed.setText(getString(R.string.GalaxyJ2Pro_opsysed));
                processored.setText(getString(R.string.GalaxyJ2Pro_processored));
                networked.setText(getString(R.string.GalaxyJ2Pro_networked));
                appearanced.setText(getString(R.string.GalaxyJ2Pro_appearanced));
                displayed.setText(getString(R.string.GalaxyJ2Pro_displayed));
                cameraed.setText(getString(R.string.GalaxyJ2Pro_cameraed));
                memoryed.setText(getString(R.string.GalaxyJ2Pro_memoryed));
                con_sensored.setText(getString(R.string.GalaxyJ2Pro_con_sensored));
                batteryed.setText(getString(R.string.GalaxyJ2Pro_batteryed));
                othered.setText(getString(R.string.GalaxyJ2Pro_othered));
                break;

            case "Galaxy J3 Emerge":

                specifications();

                named.setText(getString(R.string.GalaxyJ3Emerge_named));
                modeled.setText(getString(R.string.GalaxyJ3Emerge_modeled));
                opsysed.setText(getString(R.string.GalaxyJ3Emerge_opsysed));
                processored.setText(getString(R.string.GalaxyJ3Emerge_processored));
                networked.setText(getString(R.string.GalaxyJ3Emerge_networked));
                appearanced.setText(getString(R.string.GalaxyJ3Emerge_appearanced));
                displayed.setText(getString(R.string.GalaxyJ3Emerge_displayed));
                cameraed.setText(getString(R.string.GalaxyJ3Emerge_cameraed));
                memoryed.setText(getString(R.string.GalaxyJ3Emerge_memoryed));
                con_sensored.setText(getString(R.string.GalaxyJ3Emerge_con_sensored));
                batteryed.setText(getString(R.string.GalaxyJ3Emerge_batteryed));
                othered.setText(getString(R.string.GalaxyJ3Emerge_othered));
                break;

            case "Galaxy J3 Pro":

                specifications();

                named.setText(getString(R.string.GalaxyJ3Pro_named));
                modeled.setText(getString(R.string.GalaxyJ3Pro_modeled));
                opsysed.setText(getString(R.string.GalaxyJ3Pro_opsysed));
                processored.setText(getString(R.string.GalaxyJ3Pro_processored));
                networked.setText(getString(R.string.GalaxyJ3Pro_networked));
                appearanced.setText(getString(R.string.GalaxyJ3Pro_appearanced));
                displayed.setText(getString(R.string.GalaxyJ3Pro_displayed));
                cameraed.setText(getString(R.string.GalaxyJ3Pro_cameraed));
                memoryed.setText(getString(R.string.GalaxyJ3Pro_memoryed));
                con_sensored.setText(getString(R.string.GalaxyJ3Pro_con_sensored));
                batteryed.setText(getString(R.string.GalaxyJ3Pro_batteryed));
                othered.setText(getString(R.string.GalaxyJ3Pro_othered));
                break;

            case "Galaxy J5 Prime":

                specifications();

                named.setText(getString(R.string.GalaxyJ5Prime_named));
                modeled.setText(getString(R.string.GalaxyJ5Prime_modeled));
                opsysed.setText(getString(R.string.GalaxyJ5Prime_opsysed));
                processored.setText(getString(R.string.GalaxyJ5Prime_processored));
                networked.setText(getString(R.string.GalaxyJ5Prime_networked));
                appearanced.setText(getString(R.string.GalaxyJ5Prime_appearanced));
                displayed.setText(getString(R.string.GalaxyJ5Prime_displayed));
                cameraed.setText(getString(R.string.GalaxyJ5Prime_cameraed));
                memoryed.setText(getString(R.string.GalaxyJ5Prime_memoryed));
                con_sensored.setText(getString(R.string.GalaxyJ5Prime_con_sensored));
                batteryed.setText(getString(R.string.GalaxyJ5Prime_batteryed));
                othered.setText(getString(R.string.GalaxyJ5Prime_othered));
                break;

            case "Galaxy J7 Prime":

                specifications();

                named.setText(getString(R.string.GalaxyJ7Prime_named));
                modeled.setText(getString(R.string.GalaxyJ7Prime_modeled));
                opsysed.setText(getString(R.string.GalaxyJ7Prime_opsysed));
                processored.setText(getString(R.string.GalaxyJ7Prime_processored));
                networked.setText(getString(R.string.GalaxyJ7Prime_networked));
                appearanced.setText(getString(R.string.GalaxyJ7Prime_appearanced));
                displayed.setText(getString(R.string.GalaxyJ7Prime_displayed));
                cameraed.setText(getString(R.string.GalaxyJ7Prime_cameraed));
                memoryed.setText(getString(R.string.GalaxyJ7Prime_memoryed));
                con_sensored.setText(getString(R.string.GalaxyJ7Prime_con_sensored));
                batteryed.setText(getString(R.string.GalaxyJ7Prime_batteryed));
                othered.setText(getString(R.string.GalaxyJ7Prime_othered));
                break;

            case "Galaxy Note 7":

                specifications();

                named.setText(getString(R.string.GalaxyNote7_named));
                modeled.setText(getString(R.string.GalaxyNote7_modeled));
                opsysed.setText(getString(R.string.GalaxyNote7_opsysed));
                processored.setText(getString(R.string.GalaxyNote7_processored));
                networked.setText(getString(R.string.GalaxyNote7_networked));
                appearanced.setText(getString(R.string.GalaxyNote7_appearanced));
                displayed.setText(getString(R.string.GalaxyNote7_displayed));
                cameraed.setText(getString(R.string.GalaxyNote7_cameraed));
                memoryed.setText(getString(R.string.GalaxyNote7_memoryed));
                con_sensored.setText(getString(R.string.GalaxyNote7_con_sensored));
                batteryed.setText(getString(R.string.GalaxyNote7_batteryed));
                othered.setText(getString(R.string.GalaxyNote7_othered));
                break;

            case "Galaxy On5 Pro":

                specifications();

                named.setText(getString(R.string.GalaxyOn5Pro_named));
                modeled.setText(getString(R.string.GalaxyOn5Pro_modeled));
                opsysed.setText(getString(R.string.GalaxyOn5Pro_opsysed));
                processored.setText(getString(R.string.GalaxyOn5Pro_processored));
                networked.setText(getString(R.string.GalaxyOn5Pro_networked));
                appearanced.setText(getString(R.string.GalaxyOn5Pro_appearanced));
                displayed.setText(getString(R.string.GalaxyOn5Pro_displayed));
                cameraed.setText(getString(R.string.GalaxyOn5Pro_cameraed));
                memoryed.setText(getString(R.string.GalaxyOn5Pro_memoryed));
                con_sensored.setText(getString(R.string.GalaxyOn5Pro_con_sensored));
                batteryed.setText(getString(R.string.GalaxyOn5Pro_batteryed));
                othered.setText(getString(R.string.GalaxyOn5Pro_othered));
                break;

            case "Galaxy On7":

                specifications();

                named.setText(getString(R.string.GalaxyOn7_named));
                modeled.setText(getString(R.string.GalaxyOn7_modeled));
                opsysed.setText(getString(R.string.GalaxyOn7_opsysed));
                processored.setText(getString(R.string.GalaxyOn7_processored));
                networked.setText(getString(R.string.GalaxyOn7_networked));
                appearanced.setText(getString(R.string.GalaxyOn7_appearanced));
                displayed.setText(getString(R.string.GalaxyOn7_displayed));
                cameraed.setText(getString(R.string.GalaxyOn7_cameraed));
                memoryed.setText(getString(R.string.GalaxyOn7_memoryed));
                con_sensored.setText(getString(R.string.GalaxyOn7_con_sensored));
                batteryed.setText(getString(R.string.GalaxyOn7_batteryed));
                othered.setText(getString(R.string.GalaxyOn7_othered));
                break;

            case "Galaxy On7 Pro":

                specifications();

                named.setText(getString(R.string.GalaxyOn7Pro_named));
                modeled.setText(getString(R.string.GalaxyOn7Pro_modeled));
                opsysed.setText(getString(R.string.GalaxyOn7Pro_opsysed));
                processored.setText(getString(R.string.GalaxyOn7Pro_processored));
                networked.setText(getString(R.string.GalaxyOn7Pro_networked));
                appearanced.setText(getString(R.string.GalaxyOn7Pro_appearanced));
                displayed.setText(getString(R.string.GalaxyOn7Pro_displayed));
                cameraed.setText(getString(R.string.GalaxyOn7Pro_cameraed));
                memoryed.setText(getString(R.string.GalaxyOn7Pro_memoryed));
                con_sensored.setText(getString(R.string.GalaxyOn7Pro_con_sensored));
                batteryed.setText(getString(R.string.GalaxyOn7Pro_batteryed));
                othered.setText(getString(R.string.GalaxyOn7Pro_othered));
                break;

            case "Galaxy On8":

                specifications();

                named.setText(getString(R.string.GalaxyOn8_named));
                modeled.setText(getString(R.string.GalaxyOn8_modeled));
                opsysed.setText(getString(R.string.GalaxyOn8_opsysed));
                processored.setText(getString(R.string.GalaxyOn8_processored));
                networked.setText(getString(R.string.GalaxyOn8_networked));
                appearanced.setText(getString(R.string.GalaxyOn8_appearanced));
                displayed.setText(getString(R.string.GalaxyOn8_displayed));
                cameraed.setText(getString(R.string.GalaxyOn8_cameraed));
                memoryed.setText(getString(R.string.GalaxyOn8_memoryed));
                con_sensored.setText(getString(R.string.GalaxyOn8_con_sensored));
                batteryed.setText(getString(R.string.GalaxyOn8_batteryed));
                othered.setText(getString(R.string.GalaxyOn8_othered));
                break;

            case "Galaxy S7 active":

                specifications();

                named.setText(getString(R.string.GalaxyS7active_named));
                modeled.setText(getString(R.string.GalaxyS7active_modeled));
                opsysed.setText(getString(R.string.GalaxyS7active_opsysed));
                processored.setText(getString(R.string.GalaxyS7active_processored));
                networked.setText(getString(R.string.GalaxyS7active_networked));
                appearanced.setText(getString(R.string.GalaxyS7active_appearanced));
                displayed.setText(getString(R.string.GalaxyS7active_displayed));
                cameraed.setText(getString(R.string.GalaxyS7active_cameraed));
                memoryed.setText(getString(R.string.GalaxyS7active_memoryed));
                con_sensored.setText(getString(R.string.GalaxyS7active_con_sensored));
                batteryed.setText(getString(R.string.GalaxyS7active_batteryed));
                othered.setText(getString(R.string.GalaxyS7active_othered));
                break;

            case "Galaxy Xcover 4":

                specifications();

                named.setText(getString(R.string.GalaxyXcover4_named));
                modeled.setText(getString(R.string.GalaxyXcover4_modeled));
                opsysed.setText(getString(R.string.GalaxyXcover4_opsysed));
                processored.setText(getString(R.string.GalaxyXcover4_processored));
                networked.setText(getString(R.string.GalaxyXcover4_networked));
                appearanced.setText(getString(R.string.GalaxyXcover4_appearanced));
                displayed.setText(getString(R.string.GalaxyXcover4_displayed));
                cameraed.setText(getString(R.string.GalaxyXcover4_cameraed));
                memoryed.setText(getString(R.string.GalaxyXcover4_memoryed));
                con_sensored.setText(getString(R.string.GalaxyXcover4_con_sensored));
                batteryed.setText(getString(R.string.GalaxyXcover4_batteryed));
                othered.setText(getString(R.string.GalaxyXcover4_othered));
                break;

            case "Z2":

                specifications();

                named.setText(getString(R.string.Z2_named));
                modeled.setText(getString(R.string.Z2_modeled));
                opsysed.setText(getString(R.string.Z2_opsysed));
                processored.setText(getString(R.string.Z2_processored));
                networked.setText(getString(R.string.Z2_networked));
                appearanced.setText(getString(R.string.Z2_appearanced));
                displayed.setText(getString(R.string.Z2_displayed));
                cameraed.setText(getString(R.string.Z2_cameraed));
                memoryed.setText(getString(R.string.Z2_memoryed));
                con_sensored.setText(getString(R.string.Z2_con_sensored));
                batteryed.setText(getString(R.string.Z2_batteryed));
                othered.setText(getString(R.string.Z2_othered));
                break;

            case "Z3 Corporate Edition":

                specifications();

                named.setText(getString(R.string.Z3CorporateEdition_named));
                modeled.setText(getString(R.string.Z3CorporateEdition_modeled));
                opsysed.setText(getString(R.string.Z3CorporateEdition_opsysed));
                processored.setText(getString(R.string.Z3CorporateEdition_processored));
                networked.setText(getString(R.string.Z3CorporateEdition_networked));
                appearanced.setText(getString(R.string.Z3CorporateEdition_appearanced));
                displayed.setText(getString(R.string.Z3CorporateEdition_displayed));
                cameraed.setText(getString(R.string.Z3CorporateEdition_cameraed));
                memoryed.setText(getString(R.string.Z3CorporateEdition_memoryed));
                con_sensored.setText(getString(R.string.Z3CorporateEdition_con_sensored));
                batteryed.setText(getString(R.string.Z3CorporateEdition_batteryed));
                othered.setText(getString(R.string.Z3CorporateEdition_othered));
                break;

        }
    }

    private void gettecnobrand(String tecnobrand) {

        switch (tecnobrand) {

            case "Boom J5":

                specifications();

                named.setText(getString(R.string.tbj5_named));
                modeled.setText(getString(R.string.tbj5_modeled));
                opsysed.setText(getString(R.string.tbj5_opsysed));
                processored.setText(getString(R.string.tbj5_processored));
                networked.setText(getString(R.string.tbj5_networked));
                appearanced.setText(getString(R.string.tbj5_appearanced));
                displayed.setText(getString(R.string.tbj5_displayed));
                cameraed.setText(getString(R.string.tbj5_cameraed));
                memoryed.setText(getString(R.string.tbj5_memoryed));
                con_sensored.setText(getString(R.string.tbj5_con_sensored));
                batteryed.setText(getString(R.string.tbj5_batteryed));
                othered.setText(getString(R.string.tbj5_othered));
                break;

            case "Boom J7":

                specifications();

                named.setText(getString(R.string.tbj7_named));
                modeled.setText(getString(R.string.tbj7_modeled));
                opsysed.setText(getString(R.string.tbj7_opsysed));
                processored.setText(getString(R.string.tbj7_processored));
                networked.setText(getString(R.string.tbj7_networked));
                appearanced.setText(getString(R.string.tbj7_appearanced));
                displayed.setText(getString(R.string.tbj7_displayed));
                cameraed.setText(getString(R.string.tbj7_cameraed));
                memoryed.setText(getString(R.string.tbj7_memoryed));
                con_sensored.setText(getString(R.string.tbj7_con_sensored));
                batteryed.setText(getString(R.string.tbj7_batteryed));
                othered.setText(getString(R.string.tbj7_othered));
                break;

            case "Boom J8":

                specifications();

                named.setText(getString(R.string.tbj8_named));
                modeled.setText(getString(R.string.tbj8_modeled));
                opsysed.setText(getString(R.string.tbj8_opsysed));
                processored.setText(getString(R.string.tbj8_processored));
                networked.setText(getString(R.string.tbj8_networked));
                appearanced.setText(getString(R.string.tbj8_appearanced));
                displayed.setText(getString(R.string.tbj8_displayed));
                cameraed.setText(getString(R.string.tbj8_cameraed));
                memoryed.setText(getString(R.string.tbj8_memoryed));
                con_sensored.setText(getString(R.string.tbj8_con_sensored));
                batteryed.setText(getString(R.string.tbj8_batteryed));
                othered.setText(getString(R.string.tbj8_othered));
                break;

            case "Camon c5":

                specifications();

                named.setText(getString(R.string.tcamc5_named));
                modeled.setText(getString(R.string.tcamc5_modeled));
                opsysed.setText(getString(R.string.tcamc5_opsysed));
                processored.setText(getString(R.string.tcamc5_processored));
                networked.setText(getString(R.string.tcamc5_networked));
                appearanced.setText(getString(R.string.tcamc5_appearanced));
                displayed.setText(getString(R.string.tcamc5_displayed));
                cameraed.setText(getString(R.string.tcamc5_cameraed));
                memoryed.setText(getString(R.string.tcamc5_memoryed));
                con_sensored.setText(getString(R.string.tcamc5_con_sensored));
                batteryed.setText(getString(R.string.tcamc5_batteryed));
                othered.setText(getString(R.string.tcamc5_othered));
                break;

            case "Camon c7":

                specifications();

                named.setText(getString(R.string.tcamc7_named));
                modeled.setText(getString(R.string.tcamc7_modeled));
                opsysed.setText(getString(R.string.tcamc7_opsysed));
                processored.setText(getString(R.string.tcamc7_processored));
                networked.setText(getString(R.string.tcamc7_networked));
                appearanced.setText(getString(R.string.tcamc7_appearanced));
                displayed.setText(getString(R.string.tcamc7_displayed));
                cameraed.setText(getString(R.string.tcamc7_cameraed));
                memoryed.setText(getString(R.string.tcamc7_memoryed));
                con_sensored.setText(getString(R.string.tcamc7_con_sensored));
                batteryed.setText(getString(R.string.tcamc7_batteryed));
                othered.setText(getString(R.string.tcamc7_othered));
                break;

            case "Camon c8":

                specifications();

                named.setText(getString(R.string.tcamc8_named));
                modeled.setText(getString(R.string.tcamc8_modeled));
                opsysed.setText(getString(R.string.tcamc8_opsysed));
                processored.setText(getString(R.string.tcamc8_processored));
                networked.setText(getString(R.string.tcamc8_networked));
                appearanced.setText(getString(R.string.tcamc8_appearanced));
                displayed.setText(getString(R.string.tcamc8_displayed));
                cameraed.setText(getString(R.string.tcamc8_cameraed));
                memoryed.setText(getString(R.string.tcamc8_memoryed));
                con_sensored.setText(getString(R.string.tcamc8_con_sensored));
                batteryed.setText(getString(R.string.tcamc8_batteryed));
                othered.setText(getString(R.string.tcamc8_othered));
                break;

            case "Camon c9":

                specifications();

                named.setText(getString(R.string.tcamc9_named));
                modeled.setText(getString(R.string.tcamc9_modeled));
                opsysed.setText(getString(R.string.tcamc9_opsysed));
                processored.setText(getString(R.string.tcamc9_processored));
                networked.setText(getString(R.string.tcamc9_networked));
                appearanced.setText(getString(R.string.tcamc9_appearanced));
                displayed.setText(getString(R.string.tcamc9_displayed));
                cameraed.setText(getString(R.string.tcamc9_cameraed));
                memoryed.setText(getString(R.string.tcamc9_memoryed));
                con_sensored.setText(getString(R.string.tcamc9_con_sensored));
                batteryed.setText(getString(R.string.tcamc9_batteryed));
                othered.setText(getString(R.string.tcamc9_othered));
                break;

            case "Camon cx":

                specifications();

                named.setText(getString(R.string.tcamcx_named));
                modeled.setText(getString(R.string.tcamcx_modeled));
                opsysed.setText(getString(R.string.tcamcx_opsysed));
                processored.setText(getString(R.string.tcamcx_processored));
                networked.setText(getString(R.string.tcamcx_networked));
                appearanced.setText(getString(R.string.tcamcx_appearanced));
                displayed.setText(getString(R.string.tcamcx_displayed));
                cameraed.setText(getString(R.string.tcamcx_cameraed));
                memoryed.setText(getString(R.string.tcamcx_memoryed));
                con_sensored.setText(getString(R.string.tcamcx_con_sensored));
                batteryed.setText(getString(R.string.tcamcx_batteryed));
                othered.setText(getString(R.string.tcamcx_othered));
                break;

            case "L5":

                specifications();

                named.setText(getString(R.string.tl5_named));
                modeled.setText(getString(R.string.tl5_modeled));
                opsysed.setText(getString(R.string.tl5_opsysed));
                processored.setText(getString(R.string.tl5_processored));
                networked.setText(getString(R.string.tl5_networked));
                appearanced.setText(getString(R.string.tl5_appearanced));
                displayed.setText(getString(R.string.tl5_displayed));
                cameraed.setText(getString(R.string.tl5_cameraed));
                memoryed.setText(getString(R.string.tl5_memoryed));
                con_sensored.setText(getString(R.string.tl5_con_sensored));
                batteryed.setText(getString(R.string.tl5_batteryed));
                othered.setText(getString(R.string.tl5_othered));
                break;

            case "L6":

                specifications();

                named.setText(getString(R.string.tl6_named));
                modeled.setText(getString(R.string.tl6_modeled));
                opsysed.setText(getString(R.string.tl6_opsysed));
                processored.setText(getString(R.string.tl6_processored));
                networked.setText(getString(R.string.tl6_networked));
                appearanced.setText(getString(R.string.tl6_appearanced));
                displayed.setText(getString(R.string.tl6_displayed));
                cameraed.setText(getString(R.string.tl6_cameraed));
                memoryed.setText(getString(R.string.tl6_memoryed));
                con_sensored.setText(getString(R.string.tl6_con_sensored));
                batteryed.setText(getString(R.string.tl6_batteryed));
                othered.setText(getString(R.string.tl6_othered));
                break;

            case "L7":

                specifications();

                named.setText(getString(R.string.tl7_named));
                modeled.setText(getString(R.string.tl7_modeled));
                opsysed.setText(getString(R.string.tl7_opsysed));
                processored.setText(getString(R.string.tl7_processored));
                networked.setText(getString(R.string.tl7_networked));
                appearanced.setText(getString(R.string.tl7_appearanced));
                displayed.setText(getString(R.string.tl7_displayed));
                cameraed.setText(getString(R.string.tl7_cameraed));
                memoryed.setText(getString(R.string.tl7_memoryed));
                con_sensored.setText(getString(R.string.tl7_con_sensored));
                batteryed.setText(getString(R.string.tl7_batteryed));
                othered.setText(getString(R.string.tl7_othered));
                break;

            case "L8":

                specifications();

                named.setText(getString(R.string.tl8_named));
                modeled.setText(getString(R.string.tl8_modeled));
                opsysed.setText(getString(R.string.tl8_opsysed));
                processored.setText(getString(R.string.tl8_processored));
                networked.setText(getString(R.string.tl8_networked));
                appearanced.setText(getString(R.string.tl8_appearanced));
                displayed.setText(getString(R.string.tl8_displayed));
                cameraed.setText(getString(R.string.tl8_cameraed));
                memoryed.setText(getString(R.string.tl8_memoryed));
                con_sensored.setText(getString(R.string.tl8_con_sensored));
                batteryed.setText(getString(R.string.tl8_batteryed));
                othered.setText(getString(R.string.tl8_othered));
                break;

            case "L8 plus":

                specifications();

                named.setText(getString(R.string.tl8p_named));
                modeled.setText(getString(R.string.tl8p_modeled));
                opsysed.setText(getString(R.string.tl8p_opsysed));
                processored.setText(getString(R.string.tl8p_processored));
                networked.setText(getString(R.string.tl8p_networked));
                appearanced.setText(getString(R.string.tl8p_appearanced));
                displayed.setText(getString(R.string.tl8p_displayed));
                cameraed.setText(getString(R.string.tl8p_cameraed));
                memoryed.setText(getString(R.string.tl8p_memoryed));
                con_sensored.setText(getString(R.string.tl8p_con_sensored));
                batteryed.setText(getString(R.string.tl8p_batteryed));
                othered.setText(getString(R.string.tl8p_othered));
                break;

            case "L9 plus":

                specifications();

                named.setText(getString(R.string.tl9p_named));
                modeled.setText(getString(R.string.tl9p_modeled));
                opsysed.setText(getString(R.string.tl9p_opsysed));
                processored.setText(getString(R.string.tl9p_processored));
                networked.setText(getString(R.string.tl9p_networked));
                appearanced.setText(getString(R.string.tl9p_appearanced));
                displayed.setText(getString(R.string.tl9p_displayed));
                cameraed.setText(getString(R.string.tl9p_cameraed));
                memoryed.setText(getString(R.string.tl9p_memoryed));
                con_sensored.setText(getString(R.string.tl9p_con_sensored));
                batteryed.setText(getString(R.string.tl9p_batteryed));
                othered.setText(getString(R.string.tl9p_othered));
                break;

            case "M6":

                specifications();

                named.setText(getString(R.string.tm6_named));
                modeled.setText(getString(R.string.tm6_modeled));
                opsysed.setText(getString(R.string.tm6_opsysed));
                processored.setText(getString(R.string.tm6_processored));
                networked.setText(getString(R.string.tm6_networked));
                appearanced.setText(getString(R.string.tm6_appearanced));
                displayed.setText(getString(R.string.tm6_displayed));
                cameraed.setText(getString(R.string.tm6_cameraed));
                memoryed.setText(getString(R.string.tm6_memoryed));
                con_sensored.setText(getString(R.string.tm6_con_sensored));
                batteryed.setText(getString(R.string.tm6_batteryed));
                othered.setText(getString(R.string.tm6_othered));
                break;

            case "Phantom 5":

                specifications();

                named.setText(getString(R.string.tphan5_named));
                modeled.setText(getString(R.string.tphan5_modeled));
                opsysed.setText(getString(R.string.tphan5_opsysed));
                processored.setText(getString(R.string.tphan5_processored));
                networked.setText(getString(R.string.tphan5_networked));
                appearanced.setText(getString(R.string.tphan5_appearanced));
                displayed.setText(getString(R.string.tphan5_displayed));
                cameraed.setText(getString(R.string.tphan5_cameraed));
                memoryed.setText(getString(R.string.tphan5_memoryed));
                con_sensored.setText(getString(R.string.tphan5_con_sensored));
                batteryed.setText(getString(R.string.tphan5_batteryed));
                othered.setText(getString(R.string.tphan5_othered));
                break;

            case "Phantom 6":

                specifications();

                named.setText(getString(R.string.tphan6_named));
                modeled.setText(getString(R.string.tphan6_modeled));
                opsysed.setText(getString(R.string.tphan6_opsysed));
                processored.setText(getString(R.string.tphan6_processored));
                networked.setText(getString(R.string.tphan6_networked));
                appearanced.setText(getString(R.string.tphan6_appearanced));
                displayed.setText(getString(R.string.tphan6_displayed));
                cameraed.setText(getString(R.string.tphan6_cameraed));
                memoryed.setText(getString(R.string.tphan6_memoryed));
                con_sensored.setText(getString(R.string.tphan6_con_sensored));
                batteryed.setText(getString(R.string.tphan6_batteryed));
                othered.setText(getString(R.string.tphan6_othered));
                break;

            case "Phantom 6 plus":

                specifications();

                named.setText(getString(R.string.tphan6p_named));
                modeled.setText(getString(R.string.tphan6p_modeled));
                opsysed.setText(getString(R.string.tphan6p_opsysed));
                processored.setText(getString(R.string.tphan6p_processored));
                networked.setText(getString(R.string.tphan6p_networked));
                appearanced.setText(getString(R.string.tphan6p_appearanced));
                displayed.setText(getString(R.string.tphan6p_displayed));
                cameraed.setText(getString(R.string.tphan6p_cameraed));
                memoryed.setText(getString(R.string.tphan6p_memoryed));
                con_sensored.setText(getString(R.string.tphan6p_con_sensored));
                batteryed.setText(getString(R.string.tphan6p_batteryed));
                othered.setText(getString(R.string.tphan6p_othered));
                break;

            case "Phantom 7":

                specifications();

                named.setText(getString(R.string.tphan7_named));
                modeled.setText(getString(R.string.tphan7_modeled));
                opsysed.setText(getString(R.string.tphan7_opsysed));
                processored.setText(getString(R.string.tphan7_processored));
                networked.setText(getString(R.string.tphan7_networked));
                appearanced.setText(getString(R.string.tphan7_appearanced));
                displayed.setText(getString(R.string.tphan7_displayed));
                cameraed.setText(getString(R.string.tphan7_cameraed));
                memoryed.setText(getString(R.string.tphan7_memoryed));
                con_sensored.setText(getString(R.string.tphan7_con_sensored));
                batteryed.setText(getString(R.string.tphan7_batteryed));
                othered.setText(getString(R.string.tphan7_othered));
                break;

            case "W3":

                specifications();

                named.setText(getString(R.string.tw3_named));
                modeled.setText(getString(R.string.tw3_modeled));
                opsysed.setText(getString(R.string.tw3_opsysed));
                processored.setText(getString(R.string.tw3_processored));
                networked.setText(getString(R.string.tw3_networked));
                appearanced.setText(getString(R.string.tw3_appearanced));
                displayed.setText(getString(R.string.tw3_displayed));
                cameraed.setText(getString(R.string.tw3_cameraed));
                memoryed.setText(getString(R.string.tw3_memoryed));
                con_sensored.setText(getString(R.string.tw3_con_sensored));
                batteryed.setText(getString(R.string.tw3_batteryed));
                othered.setText(getString(R.string.tw3_othered));
                break;

            case "W4":

                specifications();

                named.setText(getString(R.string.tw4_named));
                modeled.setText(getString(R.string.tw4_modeled));
                opsysed.setText(getString(R.string.tw4_opsysed));
                processored.setText(getString(R.string.tw4_processored));
                networked.setText(getString(R.string.tw4_networked));
                appearanced.setText(getString(R.string.tw4_appearanced));
                displayed.setText(getString(R.string.tw4_displayed));
                cameraed.setText(getString(R.string.tw4_cameraed));
                memoryed.setText(getString(R.string.tw4_memoryed));
                con_sensored.setText(getString(R.string.tw4_con_sensored));
                batteryed.setText(getString(R.string.tw4_batteryed));
                othered.setText(getString(R.string.tw4_othered));
                break;

            case "W5":

                specifications();

                named.setText(getString(R.string.tw5_named));
                modeled.setText(getString(R.string.tw5_modeled));
                opsysed.setText(getString(R.string.tw5_opsysed));
                processored.setText(getString(R.string.tw5_processored));
                networked.setText(getString(R.string.tw5_networked));
                appearanced.setText(getString(R.string.tw5_appearanced));
                displayed.setText(getString(R.string.tw5_displayed));
                cameraed.setText(getString(R.string.tw5_cameraed));
                memoryed.setText(getString(R.string.tw5_memoryed));
                con_sensored.setText(getString(R.string.tw5_con_sensored));
                batteryed.setText(getString(R.string.tw5_batteryed));
                othered.setText(getString(R.string.tw5_othered));
                break;

            case "W5 Lite":

                specifications();

                named.setText(getString(R.string.tw5l_named));
                modeled.setText(getString(R.string.tw5l_modeled));
                opsysed.setText(getString(R.string.tw5l_opsysed));
                processored.setText(getString(R.string.tw5l_processored));
                networked.setText(getString(R.string.tw5l_networked));
                appearanced.setText(getString(R.string.tw5l_appearanced));
                displayed.setText(getString(R.string.tw5l_displayed));
                cameraed.setText(getString(R.string.tw5l_cameraed));
                memoryed.setText(getString(R.string.tw5l_memoryed));
                con_sensored.setText(getString(R.string.tw5l_con_sensored));
                batteryed.setText(getString(R.string.tw5l_batteryed));
                othered.setText(getString(R.string.tw5l_othered));
                break;

            case "Y4":

                specifications();

                named.setText(getString(R.string.ty4_named));
                modeled.setText(getString(R.string.ty4_modeled));
                opsysed.setText(getString(R.string.ty4_opsysed));
                processored.setText(getString(R.string.ty4_processored));
                networked.setText(getString(R.string.ty4_networked));
                appearanced.setText(getString(R.string.ty4_appearanced));
                displayed.setText(getString(R.string.ty4_displayed));
                cameraed.setText(getString(R.string.ty4_cameraed));
                memoryed.setText(getString(R.string.ty4_memoryed));
                con_sensored.setText(getString(R.string.ty4_con_sensored));
                batteryed.setText(getString(R.string.ty4_batteryed));
                othered.setText(getString(R.string.ty4_othered));
                break;

            case "Y5":

                specifications();

                named.setText(getString(R.string.ty5_named));
                modeled.setText(getString(R.string.ty5_modeled));
                opsysed.setText(getString(R.string.ty5_opsysed));
                processored.setText(getString(R.string.ty5_processored));
                networked.setText(getString(R.string.ty5_networked));
                appearanced.setText(getString(R.string.ty5_appearanced));
                displayed.setText(getString(R.string.ty5_displayed));
                cameraed.setText(getString(R.string.ty5_cameraed));
                memoryed.setText(getString(R.string.ty5_memoryed));
                con_sensored.setText(getString(R.string.ty5_con_sensored));
                batteryed.setText(getString(R.string.ty5_batteryed));
                othered.setText(getString(R.string.ty5_othered));
                break;

            case "Y6":

                specifications();

                named.setText(getString(R.string.ty6_named));
                modeled.setText(getString(R.string.ty6_modeled));
                opsysed.setText(getString(R.string.ty6_opsysed));
                processored.setText(getString(R.string.ty6_processored));
                networked.setText(getString(R.string.ty6_networked));
                appearanced.setText(getString(R.string.ty6_appearanced));
                displayed.setText(getString(R.string.ty6_displayed));
                cameraed.setText(getString(R.string.ty6_cameraed));
                memoryed.setText(getString(R.string.ty6_memoryed));
                con_sensored.setText(getString(R.string.ty6_con_sensored));
                batteryed.setText(getString(R.string.ty6_batteryed));
                othered.setText(getString(R.string.ty6_othered));
                break;

        }
    }

    //Method sets specifications

    public void specifications() {
        name.setText(getString(R.string.name));
        model.setText(getString(R.string.model));
        opsys.setText(getString(R.string.opsys));
        processor.setText(getString(R.string.processor));
        network.setText(getString(R.string.network));
        appearance.setText(getString(R.string.appearance));
        display.setText(getString(R.string.display));
        camera.setText(getString(R.string.camera));
        memory.setText(getString(R.string.memory));
        con_sensor.setText(getString(R.string.con_sensor));
        battery.setText(getString(R.string.battery));
        other.setText(getString(R.string.other));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds recyclerView to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            /*case R.id.action_about:
                final Dialog dialog = new Dialog(MainSpecs.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_about);
                dialog.show();

                Button moreApps = (Button) dialog.findViewById(R.id.accept);
                Button dismiss = (Button) dialog.findViewById(R.id.back);


                moreApps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewMoreApps("gibatekpro");
                        //moreApps();
                        dialog.cancel();
                    }
                });

                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                return true;*/
            /*case R.id.action_rate:
              rate();
            return true;*/
            case R.id.action_help:
                loadTutorial();
                return true;
            /*case R.id.action_viewmore:
            viewMoreApps("gibatekpro");
            return true;*/
            //case R.id.action_share:
            //shareApp(getString(R.string.app_name), MainActivity.this);
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void viewMoreApps(@NonNull String devName) {
        //noinspection ConstantConditions
        if (devName == null || devName.isEmpty()) {
            throwIllegalArgument("Developer name cannot be null or empty.", "Viewmoreapps");
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + devName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=pub:" + devName)));
        }
    }

    private void throwIllegalArgument(@NonNull String message, @NonNull String cause) {
        Exception r = new NullPointerException(cause);
        throw new IllegalArgumentException(message, r);
    }

    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    public void showTutorial() {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (s.getBoolean("Help", true)) {
            loadTutorial();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            s.edit().putBoolean("Help", false).apply();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem("MULTIPLE DEVICES", "Simply press the button of the device and select the brand to get specifications.",
                R.color.colorhelp1, R.drawable.help1);

        // You can also add gifs, [IN BETA YET] (because Glide is awesome!)
        TutorialItem tutorialItem2 = new TutorialItem("COMPARE DEVICES", "Click the button and select the number of devices you wish to compare.",
                R.color.colorhelp2, R.drawable.help2);


        TutorialItem tutorialItem3 = new TutorialItem("EASY COMPARISON", "Slide through tabs easily to view specs of devices you are comparing.",
                R.color.colorhelp3, R.drawable.help3);


        TutorialItem tutorialItem4 = new TutorialItem("RATE APP", "Please support the developer by taking out time to rate this app. It is a fast and easy process.",
                R.color.colorhelp4, R.drawable.help4);


        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    public void rate() {
        String pName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pName)));
        }
    }
}



