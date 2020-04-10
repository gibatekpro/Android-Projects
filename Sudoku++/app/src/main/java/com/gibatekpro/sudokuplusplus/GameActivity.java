package com.gibatekpro.sudokuplusplus;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gibatekpro.sudokuplusplus.engine.gameengine;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import static com.gibatekpro.sudokuplusplus.AesCbcWithIntegrity.generateKeyFromPassword;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class GameActivity extends AppCompatActivity {

    private RewardedVideoAd mAd;
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
    private static final int STANDARD_NEXT_LEVEL = 5;

    // private boolean timer_on_pause = false;
    private boolean rewarded = false;

    private int HINT_COUNTER = 1;
    private int LEVEL_COUNTER = 1;


    TextView levelText;
    TextView hintsText;
    TextView timeText;
    TextView modeText;
    TextView diffText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        /*if (!BuildConfig.DEBUG) {
            if (isDebuggable(getApplicationContext())) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            itsMyApp();
        }*/
        context = getApplicationContext();
        money();

        // Use an activity context to get the rewarded video instance.
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {

            @Override
            public void onRewarded(RewardItem reward) {
                setHints(getCurrentHints() + 1);
                rewarded = true;
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
            }

            @Override
            public void onRewardedVideoAdClosed() {
                loadRewardedVideoAd();
                if (rewarded) {
                    displayMessage("Help points rewarded", Snackbar.LENGTH_LONG);
                    rewarded = false;
                } else {
                    displayMessage("Complete the short video ad to earn your reward.", Snackbar.LENGTH_LONG);
                }
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onRewardedVideoCompleted() {

            }

            @Override
            public void onRewardedVideoAdLoaded() {
                Button gh = (Button) findViewById(R.id.ghints);
                gh.setEnabled(true);
            }

            @Override
            public void onRewardedVideoAdOpened() {
            }

            @Override
            public void onRewardedVideoStarted() {
            }
        });

        loadRewardedVideoAd();

//        //toolbar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // loads the solution activity on click
        Button cgame = (Button) findViewById(R.id.cgame);
        cgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HINT_COUNTER % 2 == 0) {
                    showInterstitial(VIEW_ERRORS);
                } else {
                    adAction = VIEW_ERRORS;
                    performAdAction();
                }
                HINT_COUNTER++;
            }
        });

        //hint button
        Button hgame = (Button) findViewById(R.id.hgame);
        hgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HINT_COUNTER % 2 == 0) {
                    showInterstitial(SHOW_HINTS);
                } else {
                    adAction = SHOW_HINTS;
                    performAdAction();
                }
                HINT_COUNTER++;
            }
        });

        //get help button
        Button gh = (Button) findViewById(R.id.ghints);
        gh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAd.isLoaded()) {
                    mAd.show();
                } else {
                    loadRewardedVideoAd();
                    displayMessage("Check your internet connection and try again.", Snackbar.LENGTH_LONG);
                }
            }
        });


        levelText = (TextView) findViewById(R.id.level);
        hintsText = (TextView) findViewById(R.id.hints);
        timeText = (TextView) findViewById(R.id.time);
        modeText = (TextView) findViewById(R.id.mode);
        diffText = (TextView) findViewById(R.id.diff);

        LinearLayout main_parent = (LinearLayout) findViewById(R.id.main_parent);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            main_parent.setOrientation(LinearLayout.HORIZONTAL);
        }
        try {
            key = generateKeyFromPassword(getAppSignature(getApplicationContext()), getString(R.string.salt));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState != null)
            return;

        Bundle b = getIntent().getExtras();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mode = b.getString("mode");
        boolean cont = b.getBoolean("continue");
        if (cont) {
            if (gameengine.SERIAL_GAME.equals(b.getString("mode"))) {
                String grid = decrypt(mPrefs.getString("s_grid", "NaN"));
                String values = decrypt(mPrefs.getString("s_values", "NaN"));
                String solution = decrypt(mPrefs.getString("s_solution", "NaN"));

                int currentLevel = parseInt(decrypt(mPrefs.getString("s_level", "NaN")));
                int hints = parseInt(decrypt(mPrefs.getString("s_hints", "NaN")));
                long time = parseLong(decrypt(mPrefs.getString("s_time", "NaN")));
                // int difficulty = mPrefs.getInt("s_difficulty", 0);
                setSerialInterface();
                gameengine.getInstance().loadSerialGrid(this, grid, values, solution, currentLevel, hints, time);
            } else if (gameengine.CHOICE_GAME.equals(b.getString("mode"))) {
                String grid = decrypt(mPrefs.getString("grid_ch", "NaN"));
                String values = decrypt(mPrefs.getString("values_ch", "NaN"));
                String solution = decrypt(mPrefs.getString("solution_ch", "NaN"));

                int currentLevel = parseInt(decrypt(mPrefs.getString("level_ch", "NaN")));
                int hints = parseInt(decrypt(mPrefs.getString("hints_ch", "NaN")));
                //int viewErrors = mPrefs.getInt("viewErrors_ch", 0);
                long time = parseLong(decrypt(mPrefs.getString("time_ch", "NaN")));
                //int difficulty = mPrefs.getInt("difficulty_ch", 0);
                setChoiceInterface();
                gameengine.getInstance().loadChoiceGrid(this, grid, values, solution, currentLevel, hints, time);
            } else {
                String grid = decrypt(mPrefs.getString("grid_st", "NaN"));
                String values = decrypt(mPrefs.getString("values_st", "NaN"));
                String solution = decrypt(mPrefs.getString("solution_st", "NaN"));

                //int currentLevel = parseInt(decrypt(mPrefs.getString("level_st", "NaN")));
                int hints = parseInt(decrypt(mPrefs.getString("hints_st", "NAN")));
                // int viewErrors = mPrefs.getInt("viewErrors_st", 0);
                long time = parseLong(decrypt(mPrefs.getString("time_st", "NaN")));
                int difficulty = parseInt(decrypt(mPrefs.getString("difficulty_st", "NaN")));
                setStandardInterface();
                gameengine.getInstance().loadGrid(this, grid, values, solution, hints, time, difficulty);
            }
            return;
        }

        switch (mode) {
            case gameengine.SERIAL_GAME:
                loadSerialGame(this);
                break;
            case gameengine.CHOICE_GAME:
                loadChoiceGame(this, 81 - b.getInt("clues", 1));
                //showInterstitial(NO_ACTION);
                break;
            default:
                loadStandardGame(this, b.getInt("difficulty", 1));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LinearLayout main_parent = (LinearLayout) findViewById(R.id.main_parent);
        //LinearLayout game_scr = (LinearLayout) findViewById(R.id.game_screen);
        //LinearLayout keypad = (LinearLayout) findViewById(R.id.keypad);
        if (newConfig.orientation == newConfig.ORIENTATION_PORTRAIT) {
            main_parent.setOrientation(LinearLayout.VERTICAL);
        } else {
            main_parent.setOrientation(LinearLayout.HORIZONTAL);
        }
    }

    @Override
    public void onDestroy() {
        mAd.destroy(this);
        super.onDestroy();
    }

    private void setDetails() {
        gameengine.getInstance().setDetails();
    }

    private void setStandardInterface() {
        gameengine.getInstance().setEventListener(new gameengine.Listener() {
            @Override
            public void onLevelChanged(int level) {
                levelText.setVisibility(View.GONE);
                //displayMessage("Level "+level,Snackbar.LENGTH_SHORT);
            }

            @Override
            public void onHintsChanged(int hints) {
                hintsText.setText("HP: " + hints + " | ");
            }

            @Override
            public void onViewErrorsChanged(int viewErrors) {
                //TextView v = (TextView) findViewById(R.id.view);
                //v.setText("Level: " + viewErrors + " | ");
            }

            @Override
            public void onTimeChanged(long time) {
                String timeToString = "[ " + convertToString(time) + " ]";
                timeText.setText(timeToString);
            }

            @Override
            public void onModeChanged(String mode) {
                mode = mode.equals("serial") ? "Serial" : "Standard";
                modeText.setText(mode + " | ");
            }

            @Override
            public void onDifficultyChanged(int difficulty) {
                diffText.setVisibility(View.VISIBLE);
                String diff = "";
                if (difficulty == gameengine.EASY_MODE) {
                    diff = "Easy";
                } else if (difficulty == gameengine.MED_MODE) {
                    diff = "Medium";
                } else if (difficulty == gameengine.HARD_MODE) {
                    diff = "Hard";
                } else if (difficulty == gameengine.VHARD_MODE) {
                    diff = "V. Hard";
                }
                diffText.setText(diff + " | ");
            }

            @Override
            public void onGameCompleted() {
                //stop timer
                pauseGameTime();
                int diff = gameengine.getInstance().getDifficulty();
                long time = getGameTime();

                showInterstitial(NO_ACTION);

                String m = "";

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (diff == gameengine.EASY_MODE) {
                    m = "e";
                } else if (diff == gameengine.MED_MODE) {
                    m = "m";
                } else if (diff == gameengine.HARD_MODE) {
                    m = "h";
                } else {
                    m = "v";
                }

                long first = prefs.getLong(m + "_score_1", 0);
                long second = prefs.getLong(m + "_score_2", 0);
                long third = prefs.getLong(m + "_score_3", 0);

                boolean hiscore = false;

                if (first == 0 || time < first) {
                    third = second;
                    second = first;
                    first = time;
                    hiscore = true;
                } else if (second == 0 || time < second) {
                    third = second;
                    second = time;
                    hiscore = true;
                } else if (third == 0 || time < third) {
                    third = time;
                    hiscore = true;
                }

                prefs.edit().putLong(m + "_score_1", first).putLong(m + "_score_2", second).putLong(m + "_score_3", third).apply();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.hi_score, null);
                mBuilder.setTitle("Congratulations..");
                TextView details = (TextView) mView.findViewById(R.id.text);
                if (hiscore) {
                    details.setText("Sudoku game solved. You have a new high score. You completed the game in " + secondsTotime(time) + ". \\o/\\o/");
                } else {
                    details.setText("Sudoku solved. You completed the game in " + secondsTotime(time));
                }


                mBuilder.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gameengine.getInstance().loadNextLevel();
                        //resetGameTime();

                    }
                });
                mBuilder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        showInterstitial(EXIT_ACT);
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.setCancelable(false).create();
                dialog.show();

            }

            @Override
            public void onGridError(String error) {
                displayMessage(error,Snackbar.LENGTH_LONG);
                finish();
            }
        });
    }

    private void setChoiceInterface() {
        gameengine.getInstance().setEventListener(new gameengine.Listener() {
            @Override
            public void onLevelChanged(int level) {
                levelText.setVisibility(View.VISIBLE);
                levelText.setText("Clues: " + (81 - level) + " | ");
                //displayMessage("Level "+level,Snackbar.LENGTH_SHORT);
            }

            @Override
            public void onHintsChanged(int hints) {
                hintsText.setText("HP: " + hints + " | ");
            }

            @Override
            public void onViewErrorsChanged(int viewErrors) {
                //TextView v = (TextView) findViewById(R.id.view);
                //v.setText("Level: " + viewErrors + " | ");
            }

            @Override
            public void onTimeChanged(long time) {
                String timeToString = "[ " + convertToString(time) + " ]";
                timeText.setText(timeToString);
            }

            @Override
            public void onModeChanged(String mode) {
                mode = mode.equals(gameengine.SERIAL_GAME) ? "Serial" : mode.equals(gameengine.STANDARD_GAME) ? "Standard" : "Choice";
                modeText.setText(mode + " | ");
            }

            @Override
            public void onDifficultyChanged(int difficulty) {
                diffText.setVisibility(View.GONE);
            }

            @Override
            public void onGameCompleted() {
                //stop timer
                pauseGameTime();
                int level = getCurrentLevel();
                long time = getGameTime();

                showInterstitial(NO_ACTION);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.hi_score, null);
                mBuilder.setTitle("Congratulations..");
                TextView details = (TextView) mView.findViewById(R.id.text);
                details.setText("Sudoku game of " + level + " clues was completed in " + secondsTotime(time) + ". \\o/\\o/");

                mBuilder.setPositiveButton("Play again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gameengine.getInstance().loadNextLevel();
                        //resetGameTime();

                    }
                });
                mBuilder.setNegativeButton("Back", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        showInterstitial(EXIT_ACT);
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.setCancelable(false).create();
                dialog.show();
            }

            @Override
            public void onGridError(String error) {
                displayMessage(error,Snackbar.LENGTH_LONG);
                finish();
            }
        });
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

    private void setSerialInterface() {
        gameengine.getInstance().setEventListener(new gameengine.Listener() {
            @Override
            public void onLevelChanged(int level) {
                levelText.setVisibility(View.VISIBLE);
                levelText.setText("Level: " + level + " | ");

                if (level % 6 == 0) {
                    loadRewardedVideoAd();
                }
                //displayMessage("Level "+level,Snackbar.LENGTH_SHORT);
            }

            @Override
            public void onHintsChanged(int hints) {
                hintsText.setText("HP: " + hints + " | ");
            }

            @Override
            public void onViewErrorsChanged(int viewErrors) {
                //TextView v = (TextView) findViewById(R.id.view);
                //v.setText("Level: " + viewErrors + " | ");
            }

            @Override
            public void onTimeChanged(long time) {
                String timeToString = "[ " + convertToString(time) + " ]";
                timeText.setText(timeToString);
            }

            @Override
            public void onModeChanged(String mode) {
                mode = mode.equals("serial") ? "Serial" : "Standard";
                modeText.setText(mode + " | ");
            }

            @Override
            public void onDifficultyChanged(int difficulty) {
                diffText.setVisibility(View.GONE);
            }

            @Override
            public void onGameCompleted() {
                int level = getCurrentLevel();
                long time = getGameTime();


                //stop timer
                //gameengine.getInstance().stopTime();
                String m = "se";

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                long firstT = prefs.getLong(m + "_T_score_1", 0);
                long secondT = prefs.getLong(m + "_T_score_2", 0);
                long thirdT = prefs.getLong(m + "_T_score_3", 0);

                int firstL = prefs.getInt(m + "_L_score_1", 0);
                int secondL = prefs.getInt(m + "_L_score_2", 0);
                int thirdL = prefs.getInt(m + "_L_score_3", 0);

                boolean hiscore = false;

                if (firstL == 0 || level >= firstL) {

                    if (level == firstL) {
                        if (time < firstT) {
                            thirdL = secondL;
                            secondL = firstL;
                            firstL = level;

                            thirdT = secondT;
                            secondT = firstT;
                            firstT = time;
                        } else {
                            thirdL = secondL;
                            secondL = level;

                            thirdT = secondT;
                            secondT = time;
                        }
                    } else {
                        thirdL = secondL;
                        secondL = firstL;
                        firstL = level;


                        thirdT = secondT;
                        secondT = firstT;
                        firstT = time;
                    }
                    hiscore = true;
                } else if (secondL == 0 || level >= secondL) {
                    if (level == secondL) {
                        if (time < secondT) {
                            thirdL = secondL;
                            secondL = level;

                            thirdT = secondT;
                            secondT = time;
                        } else {
                            thirdL = level;

                            thirdT = time;
                        }
                    } else {
                        thirdL = secondL;
                        secondL = level;


                        thirdT = secondT;
                        secondT = time;
                    }
                    hiscore = true;
                } else if (thirdL == 0 || level >= thirdL) {
                    if (level == thirdL) {
                        if (time < secondT) {
                            thirdL = level;

                            thirdT = time;
                        }
                    } else {
                        thirdL = level;

                        thirdT = time;
                    }
                    hiscore = true;
                }

                prefs.edit().putInt(m + "_L_score_1", firstL).putInt(m + "_L_score_2", secondL).putInt(m + "_L_score_3", thirdL).putLong(m + "_T_score_1", firstT).putLong(m + "_T_score_2", secondT).putLong(m + "_T_score_3", thirdT).apply();

                if (level >= 40) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.hi_score, null);
                    mBuilder.setTitle("Congratulations..");
                    TextView details = (TextView) mView.findViewById(R.id.text);
                    if (hiscore) {
                        details.setText("Sudoku game solved. You have a new high score. You have completed the Serial sudoku game in " + secondsTotime(time) + ". \\o/\\o/");
                    } else {
                        details.setText("You have completed the Serial sudoku game in " + secondsTotime(time) + ". \\o/\\o/\\o/");
                    }


                    mBuilder.setPositiveButton("PLAY AGAIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            loadSerialGame(GameActivity.this);
                        }
                    });
                    mBuilder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            showInterstitial(EXIT_ACT);
                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.setCancelable(false).create();
                    dialog.show();

                    return;
                }

                if (LEVEL_COUNTER % 6 == 0) {
                    showInterstitial(SERIAL_NEXT_LEVEL);
                } else {
                adAction = SERIAL_NEXT_LEVEL;
                performAdAction();
                }
                LEVEL_COUNTER++;
            }

            @Override
            public void onGridError(String error) {
                displayMessage(error,Snackbar.LENGTH_LONG);
                finish();
            }
        });
    }

    /**
     * Loads a new sudoku game in serial mode
     *
     * @param context The context the game should exist
     */
    private void loadSerialGame(Context context) {
        //creates a sudoku grid and displays ingame
        setSerialInterface();
        gameengine.getInstance().createSerialGame(context);
    }

    /**
     * Loads a new sudoku game in standard mode
     *
     * @param context    The context which the game exists
     * @param difficulty The difficulty of the game
     */
    private void loadStandardGame(Context context, int difficulty) {
        //creates a sudoku grid and displays ingame
        setStandardInterface();
        gameengine.getInstance().createStandardGame(context, difficulty);
    }

    /**
     * Loads a new sudoku game in standard mode
     *
     * @param context The context which the game exists
     * @param holes   The difficulty of the game
     */
    private void loadChoiceGame(Context context, int holes) {
        //creates a sudoku grid and displays ingame
        setChoiceInterface();
        gameengine.getInstance().createChoiceGame(context, holes);
    }

    @Override
    protected void onPause() {
        mAd.pause(this);
        //timer_on_pause = true;
        gameengine.getInstance().clearSolution();

        //save the game
        saveGame();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAd.resume(this);
        super.onResume();
        // if (timer_on_pause) {
        resumeGameTime();
        //     timer_on_pause = false;
        //  }
    }

    @Override
    public void onBackPressed() {
        //confirm game exit
        CoordinatorLayout screen = (CoordinatorLayout) findViewById(R.id.screen);
        Snackbar.make(screen, "Leave game?", Snackbar.LENGTH_SHORT).setAction("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial(EXIT_ACT);
            }
        }).setActionTextColor(Color.GREEN).show();
    }

    /**
     * This function saves the sudoku game to the app shared preferences
     * This is achieved by taking the grid, the inputed values and the solution arrays
     * and turning it into a string before saving in shared preferences
     */
    private void saveGame() {
        //get grid, values,solution,level,hints,view errors,time
        pauseGameTime();
        displayMessage("Saving game...", Snackbar.LENGTH_SHORT);
        if (gameengine.getInstance().getMode().equals("serial")) {
            saveSerialGame();
        } else if (gameengine.getInstance().getMode().equals(gameengine.CHOICE_GAME)) {
            saveChoiceGame();
        } else {
            saveStandardGame();
        }
    }

    private void saveSerialGame() {
        int[][] grid = gameengine.getInstance().getUnmodifiedGrid();
        int[][] values = gameengine.getInstance().getCurrentValues();
        int[][] solution = gameengine.getInstance().getCurrentSolution();

        int currentLevel = gameengine.getInstance().getLevel();
        int hints = gameengine.getInstance().getHints();
        long time = gameengine.getInstance().getTimer();
        // int difficulty = gameengine.getInstance().getDifficulty();

        //int diff=gameengine.getInstance().getDifficulty();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        // .putInt("s_difficulty", difficulty)
        mPrefs.edit().putString("s_level", encrypt(currentLevel + "")).putString("s_hints", encrypt(hints + "")).putString("s_time", encrypt(time + "")).apply();

        //the sudiku grid unmodified
        if (grid == null)
            return;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(grid[i][j]);
                if (i != 8 || j != 8) {
                    sb.append(",");
                }
            }
        }
        mPrefs.edit().putString("s_grid", encrypt(sb.toString())).apply();

        //the sudoku grid with user values
        if (values == null) {
            return;
        }

        sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(values[i][j]);
                if (i != 8 || j != 8) {
                    sb.append(",");
                }
            }
        }
        mPrefs.edit().putString("s_values", encrypt(sb.toString())).apply();

        //the solution for the puzzle
        if (solution == null) {
            return;
        }

        sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(solution[i][j]);
                if (i != 8 || j != 8) {
                    sb.append(",");
                }
            }
        }
        mPrefs.edit().putString("s_solution", encrypt(sb.toString())).apply();
    }

    private void saveStandardGame() {
        int[][] grid = gameengine.getInstance().getUnmodifiedGrid();
        int[][] values = gameengine.getInstance().getCurrentValues();
        int[][] solution = gameengine.getInstance().getCurrentSolution();

        // int currentLevel = gameengine.getInstance().getLevel();
        int hints = gameengine.getInstance().getHints();
        //int viewErrors = gameengine.getInstance().getViewErrors();
        long time = gameengine.getInstance().getTimer();
        int difficulty = gameengine.getInstance().getDifficulty();

        //int diff=gameengine.getInstance().getDifficulty();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //  .putString("level_st", encrypt(currentLevel+""))
        mPrefs.edit().putString("hints_st", encrypt(hints + "")).putString("time_st", encrypt(time + "")).putString("difficulty_st", encrypt(difficulty + "")).apply();

        //the sudiku grid unmodified
        if (grid == null)
            return;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(grid[i][j]);
                if (i != 8 || j != 8) {
                    sb.append(",");
                }
            }
        }
        String en = encrypt(sb.toString());
        mPrefs.edit().putString("grid_st", en).apply();

        //the sudoku grid with user values
        if (values == null) {
            return;
        }

        sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(values[i][j]);
                if (i != 8 || j != 8) {
                    sb.append(",");
                }
            }
        }
        mPrefs.edit().putString("values_st", encrypt(sb.toString())).apply();

        //the solution for the puzzle
        if (solution == null) {
            return;
        }

        sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(solution[i][j]);
                if (i != 8 || j != 8) {
                    sb.append(",");
                }
            }
        }
        mPrefs.edit().putString("solution_st", encrypt(sb.toString())).apply();
    }

    private void saveChoiceGame() {
        int[][] grid = gameengine.getInstance().getUnmodifiedGrid();
        int[][] values = gameengine.getInstance().getCurrentValues();
        int[][] solution = gameengine.getInstance().getCurrentSolution();

        int currentLevel = gameengine.getInstance().getLevel();
        int hints = gameengine.getInstance().getHints();
        // int viewErrors = gameengine.getInstance().getViewErrors();
        long time = gameengine.getInstance().getTimer();
        //int difficulty = gameengine.getInstance().getDifficulty();
        //  .putInt("difficulty_ch", difficulty)
        //int diff=gameengine.getInstance().getDifficulty();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        mPrefs.edit().putString("level_ch", encrypt(currentLevel + "")).putString("hints_ch", encrypt(hints + "")).putString("time_ch", encrypt(time + "")).apply();

        //the sudiku grid unmodified
        if (grid == null)
            return;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(grid[i][j]);
                if (i != 8 || j != 8) {
                    sb.append(",");
                }
            }
        }
        mPrefs.edit().putString("grid_ch", encrypt(sb.toString())).apply();

        //the sudoku grid with user values
        if (values == null) {
            return;
        }

        sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(values[i][j]);
                if (i != 8 || j != 8) {
                    sb.append(",");
                }
            }
        }
        mPrefs.edit().putString("values_ch", encrypt(sb.toString())).apply();

        //the solution for the puzzle
        if (solution == null) {
            return;
        }

        sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(solution[i][j]);
                if (i != 8 || j != 8) {
                    sb.append(",");
                }
            }
        }
        mPrefs.edit().putString("solution_ch", encrypt(sb.toString())).apply();
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

    private void loadRewardedVideoAd() {
        mAd.loadAd("ca-app-pub-1488497497647050/9208274722", new AdRequest.Builder().addTestDevice("E9C1DEC2F04C63973F213FD83AAEFEC8").build());
    }

    private void money() {
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        m_InterstitialAd = new InterstitialAd(this);
        m_InterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/6394409121");
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
                gameengine.getInstance().loadNextLevel();
                break;
            case SHOW_HINTS:
                if (!gameengine.getInstance().viewHint()) {
                    displayMessage("Help points exhausted. Use the GET HP button to get more. Select a cell before pressing HINT", Snackbar.LENGTH_LONG);
                }
                break;
            case VIEW_ERRORS:
                if (!gameengine.getInstance().viewSolution()) {
                    displayMessage("Help points exhausted. Use the GET HP button to get more.", Snackbar.LENGTH_LONG);
                }
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

    private int getCurrentHints() {
        return gameengine.getInstance().getHints();
    }

    private void setHints(int hints) {
        gameengine.getInstance().setHints(hints);
    }

    private int getCurrentLevel() {
        return gameengine.getInstance().getLevel();
    }

    private void setLevel(int level) {
        gameengine.getInstance().setLevel(level);
    }

    private long getGameTime() {
        return gameengine.getInstance().getTimer();
    }

    private void setTime(long time) {
        gameengine.getInstance().setTimer(time);
    }


    private void pauseGameTime() {
        gameengine.getInstance().stopTimer();
    }

    private void resumeGameTime() {
        gameengine.getInstance().startTimer();
    }

    private void resetGameTime() {
        gameengine.getInstance().resetTimer();
    }


    private String encrypt(String text) {
        String decryptedText = "";
        try {
            AesCbcWithIntegrity.CipherTextIvMac civ = AesCbcWithIntegrity.encrypt(text, key);
            decryptedText = civ.toString();
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
