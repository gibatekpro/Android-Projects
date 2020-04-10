package com.gibatekpro.sudoku;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gibatekpro.sudoku.engine.gameengine;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

/**
 * Created by Anthony Gibah on 6/7/2017.
 */
public class GameActivity extends AppCompatActivity {


    private String mode = "standard";
    private AdView mAdView;
    Context context;

    TextView levelText;
    TextView hintsText;
    TextView timeText;
    TextView modeText;
    TextView diffText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        MobileAds.initialize(this.getApplicationContext(),
                "ca-app-pub-1488497497647050~6742428320");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        context = getApplicationContext();

        // loads the solution activity on click


        levelText = (TextView) findViewById(R.id.level);
        hintsText = (TextView) findViewById(R.id.hints);
        timeText = (TextView) findViewById(R.id.time);
        modeText = (TextView) findViewById(R.id.mode);
        diffText = (TextView) findViewById(R.id.diff);

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

                String grid = mPrefs.getString("grid_st", "NaN");
                String values = mPrefs.getString("values_st", "NaN");
                String solution = mPrefs.getString("solution_st", "NaN");

                //int currentLevel = parseInt(decrypt(mPrefs.getString("level_st", "NaN")));
                // int viewErrors = mPrefs.getInt("viewErrors_st", 0);
                long time = parseLong(mPrefs.getString("time_st", "NaN"));
                int difficulty = parseInt(mPrefs.getString("difficulty_st", "NaN"));
                setStandardInterface();
                gameengine.getInstance().loadGrid(this, grid, values, solution,time, difficulty);

            return;
        }
        switch (mode) {

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

                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.setCancelable(false).create();
                dialog.show();

            }

            @Override
            public void onGridError(String error) {
                displayMessage(error, Snackbar.LENGTH_LONG);
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


    /**
     * Loads a new sudoku game in serial mode
     *
     * @param context The context the game should exist
     */


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


    @Override
    protected void onPause() {

        //timer_on_pause = true;
        gameengine.getInstance().clearSolution();

        //save the game
        saveGame();
        super.onPause();
    }

    @Override
    protected void onResume() {

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
                finish();
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


        saveStandardGame();

    }


    private void saveStandardGame() {
        int[][] grid = gameengine.getInstance().getUnmodifiedGrid();
        int[][] values = gameengine.getInstance().getCurrentValues();
        int[][] solution = gameengine.getInstance().getCurrentSolution();

        // int currentLevel = gameengine.getInstance().getLevel();

        //int viewErrors = gameengine.getInstance().getViewErrors();
        long time = gameengine.getInstance().getTimer();
        int difficulty = gameengine.getInstance().getDifficulty();

        //int diff=gameengine.getInstance().getDifficulty();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //  .putString("level_st", encrypt(currentLevel+""))
        mPrefs.edit().putString("time_st", time + "").putString("difficulty_st", difficulty + "").apply();

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

        String en = sb.toString();
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

        mPrefs.edit().putString("values_st", sb.toString()).apply();

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

        mPrefs.edit().putString("solution_st", sb.toString()).apply();
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



    /*
    *Method call for showing Interstitial ads
     */


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

    private long getGameTime() {
        return gameengine.getInstance().getTimer();
    }

    private void pauseGameTime() {
        gameengine.getInstance().stopTimer();
    }

    private void resumeGameTime() {
        gameengine.getInstance().startTimer();
    }

}

