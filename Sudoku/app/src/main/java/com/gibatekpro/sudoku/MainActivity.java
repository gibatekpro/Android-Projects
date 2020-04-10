package com.gibatekpro.sudoku;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.kobakei.ratethisapp.RateThisApp;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

/**
 * Created by Anthony Gibah on 6/7/2017.
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    Button new_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/7504845656");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        //Native ad
        MobileAds.initialize(this.getApplicationContext(), "ca-app-pub-1488497497647050~6742428320");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //toolbar
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //show help
        showHelp();

        //continue button
        Button continue_button = (Button) findViewById(R.id.continue_button);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //continue standard game
                loadSavedStandardGame();

            }
        });

        //new game button
        new_button = (Button) findViewById(R.id.new_button);
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Difficulty");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.difficulty));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Difficulty…")) {
                            dialogInterface.dismiss();

                            //display message
                            displayMessage(mSpinner.getSelectedItem().toString(), Snackbar.LENGTH_SHORT);
                            Intent intent = new Intent(MainActivity.this, GameActivity.class);
                            Bundle b = new Bundle();
                            b.putString("mode", "standard");
                            b.putInt("difficulty", mSpinner.getSelectedItemPosition());
                            intent.putExtras(b);
                            startActivity(intent);
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

        Button hi = (Button) findViewById(R.id.hi);
        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show difficulty dialog

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Difficulty");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                /*set an adapter that holds the value of the list*/
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.difficulty));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         /*brain-box (gets value of spinner)*/
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Difficulty…")) {
                            dialogInterface.dismiss();
                            //display scores
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle(mSpinner.getSelectedItem().toString());
                            String[] scores = new String[]{"", "", ""};
                            if (mSpinner.getSelectedItemPosition() == 1) {
                                scores = new String[]{"1] " + secondsTotime(prefs.getLong("e_score_1", 0)), "2] " + secondsTotime(prefs.getLong("e_score_2", 0)), "3] " + secondsTotime(prefs.getLong("e_score_3", 0))};
                            } else if (mSpinner.getSelectedItemPosition() == 2) {
                                scores = new String[]{"1] " + secondsTotime(prefs.getLong("m_score_1", 0)), "2] " + secondsTotime(prefs.getLong("m_score_2", 0)), "3] " + secondsTotime(prefs.getLong("m_score_3", 0))};
                            } else if (mSpinner.getSelectedItemPosition() == 3) {
                                scores = new String[]{"1] " + secondsTotime(prefs.getLong("h_score_1", 0)), "2] " + secondsTotime(prefs.getLong("h_score_2", 0)), "3] " + secondsTotime(prefs.getLong("h_score_3", 0))};
                            } else {
                                scores = new String[]{"1] " + secondsTotime(prefs.getLong("v_score_1", 0)), "2] " + secondsTotime(prefs.getLong("v_score_2", 0)), "3] " + secondsTotime(prefs.getLong("v_score_3", 0))};
                            }

                            builder.setItems(scores, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
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

        //tutorial button
        Button tut = (Button) findViewById(R.id.tut);
        tut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTutorial();
            }
        });

        //about button


        //exit button
        Button exit = (Button) findViewById(R.id.exit);
        //exit.setVisibility(View.GONE);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        RateThisApp.showRateDialogIfNeeded(this);
    }

    private String secondsTotime(Long sec) {
        String time = "";
        long uptime = sec;

        long days = TimeUnit.SECONDS
                .toDays(uptime);
        if (days != 0) {
            time = time + days + "d:";
        }
        uptime -= TimeUnit.DAYS.toSeconds(days);

        long hours = TimeUnit.SECONDS
                .toHours(uptime);
        if (hours != 0) {
            time = time + hours + "h:";
        }
        uptime -= TimeUnit.HOURS.toSeconds(hours);

        long minutes = TimeUnit.SECONDS
                .toMinutes(uptime);
        if (minutes != 0) {
            time = time + minutes + "m:";
        }
        uptime -= TimeUnit.MINUTES.toSeconds(minutes);

        long seconds = TimeUnit.SECONDS
                .toSeconds(uptime);
        time = time + seconds + "s";

        return time;
    }


    /**
     * Used for knowing when the tutorial page has finished
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            settings.edit().putBoolean("show_help", false).apply();
            displayMessage("Tap the 'TUTORIAL' button to show this again.", Snackbar.LENGTH_LONG);
        }
    }

    /**
     * Loads the last saved game
     */
    private void loadSavedStandardGame() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!settings.contains("grid_st")) {
            displayMessage("Standard game not found.", Snackbar.LENGTH_LONG);
            return;
        }
        String message = "Loading saved game...";
        displayMessage(message, Snackbar.LENGTH_SHORT);

        //start game activity with continue flag
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("continue", true);
        b.putString("mode", "standard");
        intent.putExtras(b);
        startActivity(intent);
    }

    /**
     * Loads the last saved game
     */

    private void showHelp() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean show_help = settings.getBoolean("show_help", true);
        if (show_help) {
            loadTutorial();
        }
    }


    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem("GAME IDEA", "A number should not be repeated on a row , column or 3x3 box.", R.color.help1, R.drawable.help1);

        TutorialItem tutorialItem2 = new TutorialItem("CHECK FOR ERRORS", "Use the check button to check for mistakes. Mistakes will be highlighted with red. Use Cancel to erase.", R.color.colorPrimary, R.drawable.help2);

        TutorialItem tutorialItem3 = new TutorialItem("GAME LEVELS", "Task yourself. Select the level that best suits your skill.", R.color.help3, R.drawable.help3);

        TutorialItem tutorialItem4 = new TutorialItem("HIGH SCORE", "Upon completing each game, your time spent is saved. The least time is the High score.", R.color.help4, R.drawable.help4);

        TutorialItem tutorialItem5 = new TutorialItem("TUTORIAL", "Hit the tutorial button to view this tutorial again.", R.color.selectorColor, R.drawable.help9);

        TutorialItem tutorialItem6 = new TutorialItem("RATE GAME", "Rate the app to support developers of this game by gibatekpro.", R.color.help4, R.drawable.help6);
       /* // You can also add gifs, [IN BETA YET] (because Glide is awesome!)
        TutorialItem tutorialItem1 = new TutorialItem(context.getString(R.string.slide_1_african_story_books), context.getString(R.string.slide_1_african_story_books_subtitle),
                R.color.slide_1, R.drawable.gif_drawable, true);*/
        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);
        tutorialItems.add(tutorialItem5);
        tutorialItems.add(tutorialItem6);

        return tutorialItems;
    }

    /**
     * Displays a snackbar message for the user
     *
     * @param message The message to be displayed
     * @param duraton The duration in which the message should be displayed
     */
    private void displayMessage(String message, int duraton) {
        Snackbar.make(new_button, message, duraton).show();
    }


    /**
     * Decrypts an encrypted string value
     *
     * @param text The string to be decrypted
     * @return The decrypted string
     */


}

