package com.gibatekpro.imeigenerator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.Random;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class spanishgen extends AppCompatActivity {

    private String imeiNum = "";
    private Toolbar toolbar;
    private Button new_button, about_button, imei2, cust;
    private InterstitialAd mInterstitialAd;
    private EditText inputimei;
    private TextView corrimei, instruct, imei;
    private int REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spanishgen);

        showTutorial();

        com.gibatekpro.imeigenerator.spanishAppRater.app_launched(this);

        new_button = (Button) findViewById(R.id.new_button);
        about_button = (Button) findViewById(R.id.about_button);
        cust = (Button) findViewById(R.id.cust);
        inputimei = (EditText) findViewById(R.id.inputimei);
        instruct = (TextView) findViewById(R.id.instruct);
        corrimei = (TextView) findViewById(R.id.corrimei);
        imei = (TextView) findViewById(R.id.imei);
        instruct.setTextColor(Color.parseColor("#FFFFFF"));
        instruct.setText("PARA GENERAR PERSONALIZADA IMEI  \n" + "INTRODUZCA LOS DIGITOS DESEADOS ABAJO");

        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());


        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadInterstitial();
                showInterstitial();

                int count = 0;


                String rins = inputimei.getText().toString();

                genran run = new genran(rins);

                for (int i = 0; i < rins.length(); i++) {
                    if (rins.charAt(i) != ' ') {
                        ++count;
                    }
                }
                if (count == 0) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if0());
                }
                if (count == 1) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if1());
                }
                if (count == 2) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if2());
                }
                if (count == 3) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if3());
                }
                if (count == 4) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if4());
                }
                if (count == 5) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if5());
                }
                if (count == 6) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if6());
                }
                if (count == 7) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if7());
                }
                if (count == 8) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if8());
                }
                if (count == 9) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if9());
                }
                if (count == 10) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if10());
                }
                if (count == 11) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if11());
                }
                if (count == 12) {
                    corrimei.setText("PERSONALIZADA IMEI");
                    imei.setText(run.if12());
                }

            }


        });


        about_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTutorial();
            }
        });


        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(com.gibatekpro.imeigenerator.spanishgen.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Telefonos");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(com.gibatekpro.imeigenerator.spanishgen.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.device_arrays));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/

                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Seleccione Telefono…")) {
                            generateImei(String.valueOf(mSpinner.getSelectedItem()));
                            Toast.makeText(com.gibatekpro.imeigenerator.spanishgen.this,
                                    mSpinner.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                            corrimei.setText(mSpinner.getSelectedItem().toString() + " IMEI");

                        }
                    }
                });
                mBuilder.setNegativeButton("Dejar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();

                loadInterstitial();
                showInterstitial();

                dialog.show();

            }
        });
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.imeigen_interstitial_spangen));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                new_button.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                new_button.setEnabled(true);
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                return;
            }
        });
        return interstitialAd;
    }

    //add items into spinner dynamically
    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        new_button.setEnabled(true);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
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
            case "Infinix Hot Note X551":
                imeiNum = "35842906";
                break;
            case "Infinix Hot S X521":
                imeiNum = "35906407";
                break;
            case "Infinix Note 3 X601":
                imeiNum = "35997307";
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
    private void generateImei(String phone) {
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
        imei.setText(sb.toString());
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

    /*@Override
    protected void onStart() {
        super.onStart();

        RateThisApp.onStart(this);
        RateThisApp.showRateDialogIfNeeded(this);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spanmain_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigate) {
            startActivity(new Intent(this, spanishanalyzer.class));
        }
        if (id == R.id.home) {

            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
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
        TutorialItem tutorialItem1 = new TutorialItem("ENCUENTRA MI IMEI", "Simplemente pulse el  'ENCUENTRA MI IMEI'  botón para obtener el IMEI de su teléfono con facilidad y velocidad.",
                R.color.colorAccent, R.drawable.esphelp1);

        // You can also add gifs, [IN BETA YET] (because Glide is awesome!)
        TutorialItem tutorialItem2 = new TutorialItem("ANALIZAR CUALQUIER IMEI", "Esta aplicacion te ayuda a analizar cualquier IMEI simplemente introduciendo los IMEI digitos y presionando el 'ANALIZAR IMEI' boton.",
                R.color.help2, R.drawable.esphelp2);


        TutorialItem tutorialItem3 = new TutorialItem("GENERAR PERSONALIZADA IMEI", "Una herramienta esencial para generar IMEI digitos personalizados introduciendo los primeros 0-12 digitos.",
                R.color.help3, R.drawable.esphelp3);


        TutorialItem tutorialItem4 = new TutorialItem("GENERAR UN IMEI ESPECIFICO", "Esta funcion le permite generar IMEI digitos para telefonos especificos.",
                R.color.help4, R.drawable.esphelp4);

        TutorialItem tutorialItem5 = new TutorialItem("Calificar Aplicacion", "Por favor, apoya al desarrollador calificando esta aplicacion.",
                R.color.help5, R.drawable.help6);


        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);
        tutorialItems.add(tutorialItem5);


        return tutorialItems;
    }
}
