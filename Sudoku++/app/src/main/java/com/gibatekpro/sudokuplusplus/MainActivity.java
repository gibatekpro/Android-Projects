package com.gibatekpro.sudokuplusplus;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.kobakei.ratethisapp.RateThisApp;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

import static com.gibatekpro.sudokuplusplus.AesCbcWithIntegrity.generateKeyFromPassword;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    private static final String TAG = "NSUDOKU";
    Button new_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-1488497497647050~4917675923");
        if (!BuildConfig.DEBUG) {
            if (isDebuggable(getApplicationContext())) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            itsMyApp();
        }
        setContentView(R.layout.activity_main);


        AdView mAdView = (AdView) findViewById(R.id.myAdView);

        AdRequest adRequest = new AdRequest.Builder()

                .build();


        mAdView.loadAd(adRequest);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //show help
        showHelp();

        //continue button
        Button continue_button = (Button) findViewById(R.id.continue_button);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.newgame);
                dialog.show();

                Button standard = (Button) dialog.findViewById(R.id.standard);
                Button serial = (Button) dialog.findViewById(R.id.serial);
                Button choice = (Button) dialog.findViewById(R.id.choice);


                standard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        //continue standard game
                        loadSavedStandardGame();
                    }
                });

                serial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //continue serial game

                        dialog.cancel();
                        loadSavedSerialGame();
                    }
                });

                choice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        //continue standard game
                        loadSavedChoiceGame();
                    }
                });
            }
        });

        //new game button
        new_button = (Button) findViewById(R.id.new_button);
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.newgame);
                dialog.show();

                Button standard = (Button) dialog.findViewById(R.id.standard);
                Button serial = (Button) dialog.findViewById(R.id.serial);
                Button choice = (Button) dialog.findViewById(R.id.choice);


                standard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();

                        //show difficulty dialog
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

                serial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //start game activity
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        Bundle b = new Bundle();
                        b.putString("mode", "serial");
                        intent.putExtras(b);
                        startActivity(intent);

                        dialog.cancel();
                    }
                });

                choice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                        //

                        //show difficulty dialog
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.choice_game, null);
                        mBuilder.setTitle("Clues");
                        final EditText editText = (EditText) mView.findViewById(R.id.cedit);

                        mBuilder.setPositiveButton("Play", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int clues = 17;
                                try {
                                    clues = parseInt(editText.getText().toString());
                                } catch (NumberFormatException nfe) {
                                }

                                if (clues > 80 || clues < 1)
                                    clues = 17;


                                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                Bundle b = new Bundle();
                                b.putString("mode", "choice");
                                b.putInt("clues", clues);
                                intent.putExtras(b);
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
                });
            }
        });

        Button hi = (Button) findViewById(R.id.hi);
        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.newgame);
                dialog.show();

                Button standard = (Button) dialog.findViewById(R.id.standard);
                Button serial = (Button) dialog.findViewById(R.id.serial);
                Button choice = (Button) dialog.findViewById(R.id.choice);
                choice.setVisibility(View.GONE);


                standard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();

                        //show difficulty dialog
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

                serial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //display hi scores
                        dialog.cancel();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Serial");
                        String[] scores = new String[]{"1. Level " + prefs.getInt("se_L_score_1", 0) + " [ " + secondsTotime(prefs.getLong("se_T_score_1", 0)) + " ]", "2. Level " + prefs.getInt("se_L_score_2", 0) + " [ " + secondsTotime(prefs.getLong("se_T_score_2", 0)) + " ]", "3. Level " + prefs.getInt("se_L_score_3", 0) + " [ " + secondsTotime(prefs.getLong("se_T_score_3", 0)) + " ]"};
                        builder.setItems(scores, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
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
        Button about = (Button) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.hi_score, null);
                mBuilder.setTitle("About");
                TextView details = (TextView) mView.findViewById(R.id.text);
                details.setText("Developed by gibatekpro. Contact me by sending an email to gibatekpro@gmail.com regarding any issues, problems or bugs. Please, rate this app in whatever store you downloaded from.");


                mBuilder.setPositiveButton("Roger that", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        //exit button
        Button exit = (Button) findViewById(R.id.exit);
        exit.setVisibility(View.GONE);
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

    private void loadSavedSerialGame() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!settings.contains("s_grid")) {
            displayMessage("Serial game not found.", Snackbar.LENGTH_LONG);
            return;
        }
        String message = "Loading saved game...";
        displayMessage(message, Snackbar.LENGTH_SHORT);

        //start game activity with continue flag
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("continue", true);
        b.putString("mode", "serial");
        intent.putExtras(b);
        startActivity(intent);
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
    private void loadSavedChoiceGame() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!settings.contains("grid_ch")) {
            displayMessage("Choice game not found.", Snackbar.LENGTH_LONG);
            return;
        }
        String message = "Loading saved game...";
        displayMessage(message, Snackbar.LENGTH_SHORT);

        //start game activity with continue flag
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("continue", true);
        b.putString("mode", "choice");
        intent.putExtras(b);
        startActivity(intent);
    }

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
        TutorialItem tutorialItem1 = new TutorialItem("Exciting game modes", "STANDARD mode for your regular dose of sudoku with varying difficulty. And in SERIAL mode, you work your way up from easier games to the hardest ones.", R.color.colorAccent, R.drawable.help_gamemode);

        TutorialItem tutorialItem2 = new TutorialItem("Help points", "Stuck? Use the help buttons to get yourself out of tricky situations. \nVIEW ERRORS will show invalid cells while HINT will display the selected cell's correct value. \nUse wisely and strategically.", R.color.colorPrimary, R.drawable.help2);

        TutorialItem tutorialItem3 = new TutorialItem("Out of HP (Help points)?", "Use the GET HP button to get more help points. All you have to do is watch a video ad and you will be rewarded with help points at the end.", R.color.help3, R.drawable.help3);

        TutorialItem tutorialItem4 = new TutorialItem("Rate it", "Don't forget to rate this awesome app in whatever store you downloaded it from.", R.color.help4, R.drawable.help4);

       /* // You can also add gifs, [IN BETA YET] (because Glide is awesome!)
        TutorialItem tutorialItem1 = new TutorialItem(context.getString(R.string.slide_1_african_story_books), context.getString(R.string.slide_1_african_story_books_subtitle),
                R.color.slide_1, R.drawable.gif_drawable, true);*/
        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

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

    private static boolean isDebuggable(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * Decrypts an encrypted string value
     *
     * @param text The string to be decrypted
     * @return The decrypted string
     */
    private String decrypt(String text) {
        String decryptedText = "";
        try {
            AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(text);

            decryptedText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, generateKeyFromPassword(getAppSignature(getApplicationContext()), getString(R.string.salt)));
            // Log.i(TAG, "Decrypted: " + decryptedText);
        } catch (IllegalArgumentException e) {
            //Log.e(TAG, "IllegalArgumentException", e);
        } catch (GeneralSecurityException e) {
            //Log.e(TAG, "GeneralSecurityException", e);
        } catch (UnsupportedEncodingException e) {
            //Log.e(TAG, "UnsupportedEncodingException", e);
        }
        return decryptedText;
    }

    private String getAppSignature(Context context) {
        String sig = "";
        try {
            Signature[] signatures = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            sig = signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException ex) {

        }
        return sig;
    }

    private void itsMyApp() {

        if (!getAppSignature(getApplicationContext()).equals(getMyString(R.string.sign))) {
            kill();
        }
       /* if (checkAppInstall()) {
            kill();
        }*/
    }

    /**
     * Helper method to decrypt a string from the string resource file
     *
     * @param string The ID of the string to be decrypted
     * @return The decrypted string value (returns an empty string if decrytion fails)
     */
    private String getMyString(int string) {
        String value = "";
        value = decrypt(getString(string));
        return value;
    }

    private void kill() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
