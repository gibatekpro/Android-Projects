package com.gibatekpro.sudokusolverandcreator;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gibatekpro.sudokusolverandcreator.buttonsgrid.buttongridview;
import com.gibatekpro.sudokusolverandcreator.engine.gameengine;
import com.gibatekpro.sudokusolverandcreator.sudokugrid.sudokugridview;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kobakei.ratethisapp.RateThisApp;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

import static com.gibatekpro.sudokusolverandcreator.AesCbcWithIntegrity.generateKeyFromPassword;
import static java.lang.Integer.parseInt;

public class GameActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1456;
    //private RewardedVideoAd mAd;
    private String mode = "standard";
    Context context;
    private InterstitialAd m_InterstitialAd;
    private int adAction = 0;
    AesCbcWithIntegrity.SecretKeys key;

    private static final int NO_ACTION = 0;
    private static final int SERIAL_NEXT_LEVEL = 1;
    private static final int SHOW_HINTS = 2;
    private static final int VIEW_ERRORS = 3;
    private static final int EXIT_ACT = 4;

    // private boolean timer_on_pause = false;
    private boolean rewarded = false;

    private int INTER_COUNTER = 1;
    private static int AD_COUNTER = 3;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (!BuildConfig.DEBUG) {
            if (isDebuggable(getApplicationContext())) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            try {
                key = generateKeyFromPassword(getAppSignature(getApplicationContext()), getString(R.string.salt));
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

            itsMyApp();
        }
        context = getApplicationContext();
        money();

        showHelp();

        // loads the solution activity on click
        Button create = (Button) findViewById(R.id.cgame);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.choice_game, null);
                mBuilder.setTitle("Clues");
                final EditText editText = (EditText) mView.findViewById(R.id.cedit);

                mBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        int clues = 17;
                        try {
                            clues = parseInt(editText.getText().toString());
                        } catch (NumberFormatException nfe) {
                            displayMessage("Invalid entry.", Snackbar.LENGTH_SHORT);
                            dialogInterface.dismiss();
                        }

                        if (clues > 80 || clues < 1)
                            clues = 17;
                        createSudoku(GameActivity.this, clues);
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

        //Solve a game
        final Button solve = (Button) findViewById(R.id.hgame);
        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameengine.getInstance().sudokuSolverIsRunning()) {
                    gameengine.getInstance().SolveSudoku(GameActivity.this);
                    solve.setText("Stop");
                } else {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.hi_score, null);
                    mBuilder.setTitle("Abort");
                    final TextView text = (TextView) mView.findViewById(R.id.text);
                    text.setText("An operation is already going on. Are you sure you want to abort the current operation?");

                    mBuilder.setPositiveButton("Abort", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            gameengine.getInstance().SolveSudoku(GameActivity.this);
                            dialogInterface.dismiss();
                        }
                    });
                    mBuilder.setNegativeButton("Back", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }
            }
        });

        //clear button
        Button clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (INTER_COUNTER % AD_COUNTER == 0) {
                    showInterstitial(NO_ACTION);
                }
                gameengine.getInstance().createEmptyGrid(GameActivity.this);
                INTER_COUNTER++;
            }
        });

        //validate button
        Button check = (Button) findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (INTER_COUNTER % AD_COUNTER == 0) {
                    showInterstitial(NO_ACTION);
                }
                if (gameengine.getInstance().checkGame()) {
                    displayMessage("Sudoku is valid.", Snackbar.LENGTH_LONG);
                } else {
                    displayMessage("Invalid sudoku game.", Snackbar.LENGTH_LONG);
                }
                INTER_COUNTER++;
            }
        });

        //tutorial
        Button tuts = (Button) findViewById(R.id.tutorial);
        tuts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTutorial();
            }
        });

        //about
        Button about = (Button) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.hi_score, null);
                mBuilder.setTitle("About");
                TextView details = (TextView) mView.findViewById(R.id.text);
                details.setText("Developed by GIBATEKPRO. Contact me by sending an email to gibatekpro@gmail.com regarding any issues, problems or bugs. Please, rate this app in whatever store you downloaded from.");

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

        LinearLayout main_parent = (LinearLayout) findViewById(R.id.main_parent);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            main_parent.setOrientation(LinearLayout.HORIZONTAL);
        }

        //keypads
        dialog = new Dialog(GameActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buttons_grid);

        buttongridview bgv = (buttongridview) dialog.findViewById(R.id.buttongridview);
        bgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dialog.cancel();

                gameengine.getInstance().setNumber(position + 1);
            }
        });

        //sudoku grid
        sudokugridview sgv = (sudokugridview) findViewById(R.id.sudokugridview);
        sgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int x = position / 9;
                int y = position % 9;

                gameengine.getInstance().setSelectedPosition(x, y);
              //  if (!isHardwareKeyboardAvailable())
                    dialog.show();
            }
        });

        gameengine.getInstance().createEmptyGrid(GameActivity.this, new gameengine.Listener() {

            @Override
            public void onSudokuGenerated() {
                showInterstitial(NO_ACTION);
            }

            @Override
            public void onSudokuSolved() {
                final Button solve = (Button) findViewById(R.id.hgame);
                solve.setText("Solve sudoku");
                showInterstitial(NO_ACTION);
            }

            @Override
            public void onErrorCreatingGrid(String error) {
                displayMessage(error, Snackbar.LENGTH_LONG);
            }

            @Override
            public void onSudokuSolveCanceled() {
                final Button solve = (Button) findViewById(R.id.hgame);
                solve.setText("Solve sudoku");
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        RateThisApp.showRateDialogIfNeeded(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LinearLayout main_parent = (LinearLayout) findViewById(R.id.main_parent);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            main_parent.setOrientation(LinearLayout.VERTICAL);
        } else {
            main_parent.setOrientation(LinearLayout.HORIZONTAL);
        }
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

    @Override
    public void onDestroy() {
        //mAd.destroy(this);
        gameengine.getInstance().cancelSolveGame();
        super.onDestroy();
    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_1:
//                gameengine.getInstance().setNumber(1);
//                return true;
//            case KeyEvent.KEYCODE_2:
//                gameengine.getInstance().setNumber(2);
//                return true;
//            case KeyEvent.KEYCODE_3:
//                gameengine.getInstance().setNumber(3);
//                return true;
//            case KeyEvent.KEYCODE_4:
//                gameengine.getInstance().setNumber(4);
//                return true;
//            case KeyEvent.KEYCODE_5:
//                gameengine.getInstance().setNumber(5);
//                return true;
//            case KeyEvent.KEYCODE_6:
//                gameengine.getInstance().setNumber(6);
//                return true;
//            case KeyEvent.KEYCODE_7:
//                gameengine.getInstance().setNumber(7);
//                return true;
//            case KeyEvent.KEYCODE_8:
//                gameengine.getInstance().setNumber(8);
//                return true;
//            case KeyEvent.KEYCODE_9:
//                gameengine.getInstance().setNumber(9);
//                return true;
//            case KeyEvent.KEYCODE_BACK:
//                gameengine.getInstance().setNumber(0);
//                return true;
//            default:
//                gameengine.getInstance().setNumber(9);
//                return super.onKeyUp(keyCode, event);
//        }
//
//    }

    private boolean isHardwareKeyboardAvailable() {
        return getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS;
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
        TutorialItem tutorialItem1 = new TutorialItem("Create", "Create awesome sudoku puzzles with as many clues as you want. Or as little as you want.", R.color.colorAccent, R.drawable.help_gamemode);

        TutorialItem tutorialItem2 = new TutorialItem("Solve", "Solve any sudoku puzzle that has a solution with even as little as no clues. Create a sudoku or enter the clues manually and click solve.", R.color.colorPrimaryDark, R.drawable.help2);

        TutorialItem tutorialItem3 = new TutorialItem("Validate", "Validate solved sudokus or manually enter the values to validate. Ensure you always validate sudoku after solving it.", R.color.help3, R.drawable.help3);

        TutorialItem tutorialItem4 = new TutorialItem("Get creative!!", "You can create solvable sudoku with awesome patterns. Input a pattern and solve it to get the solution.", R.color.help4, R.drawable.help_gif);

        TutorialItem tutorialItem5 = new TutorialItem("Rate it", "Don't forget to rate this awesome app in whatever store you downloaded it from.", R.color.help5, R.drawable.help4);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);
        tutorialItems.add(tutorialItem5);

        return tutorialItems;
    }

    private String convertToString(long time) {
        String timeToString = "";
        long uptime = time;

        long days = TimeUnit.SECONDS
                .toDays(uptime);
        if (days != 0) {
            timeToString = timeToString + days + "d: ";
        }
        uptime -= TimeUnit.DAYS.toSeconds(days);

        long hours = TimeUnit.SECONDS
                .toHours(uptime);
        if (hours != 0) {
            timeToString = timeToString + hours + "h: ";
        }
        uptime -= TimeUnit.HOURS.toSeconds(hours);

        long minutes = TimeUnit.SECONDS
                .toMinutes(uptime);
        if (minutes != 0) {
            timeToString = timeToString + minutes + "m: ";
        }
        uptime -= TimeUnit.MINUTES.toSeconds(minutes);

        long seconds = TimeUnit.SECONDS
                .toSeconds(uptime);
        timeToString = timeToString + seconds + "s";
        return timeToString;
    }

    /**
     * Loads a new sudoku game in standard mode
     *
     * @param context The context which the game exists
     * @param holes   The difficulty of the game
     */
    private void createSudoku(Context context, int holes) {
        gameengine.getInstance().createChoiceGame(context, 81 - holes);
    }

    @Override
    protected void onPause() {
        //mAd.pause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        //mAd.resume(this);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //confirm game exit
        CoordinatorLayout screen = (CoordinatorLayout) findViewById(R.id.screen);
        Snackbar.make(screen, "Exit?", Snackbar.LENGTH_SHORT).setAction("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).setActionTextColor(Color.GREEN).show();
    }


    /**
     * Displays a snackbar message for the user
     *
     * @param message The message to be displayed
     * @param duraton The duration in which the message should be displayed
     */
    private void displayMessage(String message, int duraton) {
        Snackbar.make(findViewById(R.id.screen), message, duraton).show();
    }
//
//    private void loadRewardedVideoAd() {
//        //mAd.loadAd("ca-app-pub-5688626796086116/4566328388", new AdRequest.Builder().addTestDevice("E9C1DEC2F04C63973F213FD83AAEFEC8").build());
//    }

    private void money() {
        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        //adView.setAdUnitId("ca-app-pub-5688626796086116/9727297989");
        AdRequest adRequest = new AdRequest.Builder()
                .build();//.addTestDevice("@string/test_device_ID").build();
        adView.loadAd(adRequest);
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        m_InterstitialAd = new InterstitialAd(this);
        m_InterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/5250152724");
        m_InterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                //resumeGameTime();
                loadInterstitial();
                performAdAction();
            }
        });
        loadInterstitial();


    }

    /*
    *Method call for showing Interstitial ads
     */
    private void showInterstitial(int action) {
        adAction = action;
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (m_InterstitialAd != null && m_InterstitialAd.isLoaded()) {
            // pauseGameTime();
            m_InterstitialAd.show();
        } else {
            performAdAction();
            loadInterstitial();
        }
    }

    private void loadInterstitial() {
        // log("Loading new ad");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E9C1DEC2F04C63973F213FD83AAEFEC8").build();/*
                .addTestDevice("@string/test_device_ID")*/
        m_InterstitialAd.loadAd(adRequest);
    }

    private void performAdAction() {
        switch (adAction) {
            case SERIAL_NEXT_LEVEL:
               // gameengine.getInstance().loadNextLevel();
                break;
            case SHOW_HINTS:
//                if (!gameengine.getInstance().viewHint()) {
//                    displayMessage("Help points exhausted. Use the GET HP button to get more.", Snackbar.LENGTH_LONG);
//                }
                break;
            case VIEW_ERRORS:
//                if (!gameengine.getInstance().viewSolution()) {
//                    displayMessage("Help points exhausted. Use the GET HP button to get more.", Snackbar.LENGTH_LONG);
//                }
                break;
            case EXIT_ACT:
                finish();
                break;
            default:
                break;
        }
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
     * Decrypts an encrypted string value
     *
     * @param text The string to be decrypted
     * @return The decrypted string
     */
    private String decrypt(String text) {
        String decryptedText = "";
        try {
            AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(text);

            decryptedText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, key);
            // Log.i(TAG, "Decrypted: " + decryptedText);
        } catch (IllegalArgumentException | GeneralSecurityException | UnsupportedEncodingException e) {
            //Log.e(TAG, "IllegalArgumentException", e);
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

    private void itsMyApp() {

        if (!getAppSignature(getApplicationContext()).equals(getMyString(R.string.sign))) {
            kill();
        }
       /* if (checkAppInstall()) {
            kill();
        }*/
    }

    private static boolean isDebuggable(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private void kill() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
