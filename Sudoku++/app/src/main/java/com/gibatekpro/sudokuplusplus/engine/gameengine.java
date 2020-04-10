package com.gibatekpro.sudokuplusplus.engine;

import android.content.Context;
import android.os.Handler;

import java.util.Random;

/**
 * Created by Anthony Gibah on 3/14/2017.
 */
public class gameengine {
    private gamegrid grid = null;
    private int selectedPosX = -1, selectedPosY = -1;
    private static gameengine instance;
    public int[][] sudokuSolution = new int[9][9];
    public int[][] unmodifiedGrid = new int[9][9];

    private Context context;

    private Listener listener;

    private String mode = "standard";
    private int level = 1;
    private int hints = 0;
    private int viewErrors = 0;
    private long timer = 0;
    private int difficulty = 0;

    public boolean TIMER_ISRUNNING = false;

    public static final int EASY_MODE = 1;
    public static final int MED_MODE = 2;
    public static final int HARD_MODE = 3;
    public static final int VHARD_MODE = 4;

    public static final String SERIAL_GAME = "serial";
    public static final String STANDARD_GAME = "standard";
    public static final String CHOICE_GAME = "choice";


    //a timer used to display the incorrect cells for a particular amount of time
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            setTimer(getTimer() + 1);
            timerHandler.postDelayed(timerRunnable, 1000);
        }
    };

    private gameengine() {
    }

    public static gameengine getInstance() {
        if (instance == null) {
            instance = new gameengine();

        }
        return instance;
    }


    public void createGrid(Context context, int difficulty) {
        int[][] sudoku;
        try {
            sudoku = sudokugenerator.getInstance().generategrid();
        } catch (RuntimeException re) {
            listener.onGridError(re.toString());
            return;
        }
        setSolution(sudoku);
        int holes_to_make = 0;
        switch (mode) {
            case SERIAL_GAME:
                holes_to_make = level * 2;
                break;
            case CHOICE_GAME:
                holes_to_make = level;
                break;
            default:
                Random bar = new Random();
                switch (difficulty) {
                    case 2:
                        holes_to_make = bar.nextInt(3) + 50;
                        break;
                    case 3:
                        holes_to_make = bar.nextInt(4) + 54;
                        break;
                    case 4:
                        holes_to_make = bar.nextInt(3) + 59;
                        break;
                    default:
                        holes_to_make = bar.nextInt(8) + 42;
                        break;
                }
                break;
        }
        sudoku = sudokugenerator.getInstance().removeElements(sudoku, holes_to_make);
        if (grid == null) {
            grid = new gamegrid(context);
        }
        grid.setGrid(sudoku);
        setUnmodifiedGrid(sudoku);
    }

    public boolean viewSolution() {
        if (getHints() > 0) {
            grid.viewSolution(sudokuSolution);
            setHints(getHints() - 1);
            return true;
        }
        return false;
    }

    public boolean viewHint() {
        if (getHints() > 0 && selectedPosX > -1 && selectedPosX > -1) {
            grid.viewHint(selectedPosX, selectedPosY, sudokuSolution[selectedPosX][selectedPosY]);
            setHints(getHints() - 1);
            return true;
        }
        return false;
    }

    private void setSolution(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(sudoku[i], 0, sudokuSolution[i], 0, 9);
        }
    }

    private void setUnmodifiedGrid(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(sudoku[i], 0, unmodifiedGrid[i], 0, 9);
        }
    }

    public gamegrid getGrid() {
        return grid;
    }

    public void setSelectedPosition(int x, int y) {
        selectedPosX = x;
        selectedPosY = y;
    }

    public void setNumber(int Number) {
        if ((selectedPosX != -1) && (selectedPosY != -1)) {
            grid.setItem(selectedPosX, selectedPosY, Number);
        }

        if (checkGame()) {
            listener.onGameCompleted();
            //loadNextLevel();
        }
        ;
        //
    }

    public void loadNextLevel() {
        switch (getMode()) {
            case SERIAL_GAME:
                setLevel(getLevel() + 1);
//                if (getLevel() % 11 == 0) {
//                    setHints(getHints() + 1);
//                    setViewErrors(getViewErrors() + 1);
//                }

                createGrid(getContext(), getDifficulty());
                break;
            case CHOICE_GAME:
                createChoiceGame(getContext(), getLevel());
                break;
            default:
                createStandardGame(getContext(), getDifficulty());
                break;
        }
    }

    private void clearCurrentGame() {
        grid.clearGrid();
    }

    public boolean checkGame() {
        return grid.checkGame();
    }

    public void solutionGrid(Context context) {
        grid = new gamegrid(context);
        // grid.setGrid(sudokuSolution,null);
    }

    public void clearSolution() {
        grid.clearSolution();
    }

    /**
     * Gets the current values of the sudoku game.
     *
     * @return an array of the values
     */
    public int[][] getCurrentValues() {
        int[][] currentValue = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                currentValue[i][j] = grid.getItem(i, j).getValue();
            }
        }
        return currentValue;
    }

    /**
     * returns the soltion of the sudoku game
     *
     * @return an array of the solution
     */
    public int[][] getCurrentSolution() {
        return sudokuSolution;
    }

    /**
     * Returns a newly created grid with holes
     *
     * @return The newly created grid
     */
    public int[][] getUnmodifiedGrid() {
        return unmodifiedGrid;
    }

    public void loadGrid(Context context, String grid, String values, String solution, int hints, long time, int difficulty) {
        int level = 1;
        setMode("standard");
        setLevel(level);
        setHints(hints);
        // setViewErrors(viewErrors);
        setTimer(time);
        setDifficulty(difficulty);
        //if there is no saved game, create a new grid from scratch
        if (grid.equals("NaN")) {
            //set values
            createGrid(context, difficulty);
            return;
        }


        //get the saved grid (string) and convert to 2D int array
        String[] string_grid = grid.split(",");
        int[][] finalGrid = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                finalGrid[i][j] = Integer.parseInt(string_grid[i * 9 + j]);
            }
        }

        createContinueGrid(context, finalGrid, difficulty);

        //if there is no saved solution, exit function
        if ("NaN".equals(solution)) {
            createGrid(context, difficulty);
            return;
        }

        //get the saved grid (string) and convert to 2D int array
        String[] string_solution = solution.split(",");
        int[][] finalSolution = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                finalSolution[i][j] = Integer.parseInt(string_solution[i * 9 + j]);
            }
        }

        //set solution if its not null
        if (finalSolution != null)
            setSolution(finalSolution);

        //if there is no saved values, rexit function
        if ("NaN".equals(values)) {
            createGrid(context, difficulty);
            return;
        }

        //get the saved values (string) and convert to 2D int array
        String[] string_values = values.split(",");
        int[][] finalValues = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                finalValues[i][j] = Integer.parseInt(string_values[i * 9 + j]);
            }
        }

        //set values of continue grid if not null
        if (finalValues != null)
            setContinueValues(finalValues);
    }

    public void loadChoiceGrid(Context context, String grid, String values, String solution, int level, int hints, long time) {
        int difficulty = 0;
        setMode(CHOICE_GAME);
        setLevel(level);
        setHints(hints);
        //setViewErrors(viewErrors);
        setTimer(time);
        setDifficulty(difficulty);
        //if there is no saved game, create a new grid from scratch
        if (grid.equals("NaN")) {
            //set values
            createGrid(context, difficulty);
            return;
        }


        //get the saved grid (string) and convert to 2D int array
        String[] string_grid = grid.split(",");
        int[][] finalGrid = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                finalGrid[i][j] = Integer.parseInt(string_grid[i * 9 + j]);
            }
        }

        createContinueGrid(context, finalGrid, difficulty);

        //if there is no saved solution, exit function
        if ("NaN".equals(solution)) {
            createGrid(context, difficulty);
            return;
        }

        //get the saved grid (string) and convert to 2D int array
        String[] string_solution = solution.split(",");
        int[][] finalSolution = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                finalSolution[i][j] = Integer.parseInt(string_solution[i * 9 + j]);
            }
        }

        //set solution if its not null
        if (finalSolution != null)
            setSolution(finalSolution);

        //if there is no saved values, rexit function
        if ("NaN".equals(values)) {
            createGrid(context, difficulty);
            return;
        }

        //get the saved values (string) and convert to 2D int array
        String[] string_values = values.split(",");
        int[][] finalValues = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                finalValues[i][j] = Integer.parseInt(string_values[i * 9 + j]);
            }
        }

        //set values of continue grid if not null
        if (finalValues != null)
            setContinueValues(finalValues);
    }

    private void setContinueValues(int[][] finalValues) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid.getItem(i, j).setValue(finalValues[i][j]);
            }
        }
    }

    public void createContinueGrid(Context context, int[][] sudoku, int difficulty) {
        grid = new gamegrid(context);
        grid.setGrid(sudoku);
        // mCallback.saveGrid(sudoku, difficulty, 1);
    }

    public void createSerialGame(Context context) {
        setContext(context);
        setMode("serial");
        setHints(0);
        setViewErrors(0);
        setLevel(1);
        setDifficulty(0);

        createGrid(context, 0);

        resetTimer();
    }

    /**
     * Creates a sudoku game in standard mode
     *
     * @param context    The context the game exists
     * @param difficulty The difficulty of the game
     */
    public void createStandardGame(Context context, int difficulty) {
        setContext(context);
        setMode("standard");
        setDifficulty(difficulty);

        switch (difficulty) {
            case 2:
                setHints(2);
                setViewErrors(4);
                break;
            case 3:
                setHints(3);
                setViewErrors(5);
                break;
            case 4:
                setHints(3);
                setViewErrors(5);
                break;
            default:
                setHints(2);
                setViewErrors(3);
                break;
        }
        setLevel(1);

        createGrid(context, difficulty);

        resetTimer();
    }

    public void createChoiceGame(Context context, int holes) {
        setContext(context);
        setMode(CHOICE_GAME);
        setDifficulty(difficulty);
        setHints(1);
        setViewErrors(3);

        setLevel(holes);

        createGrid(context, difficulty);

        resetTimer();
    }

    /**
     * Gets the context of the current game
     *
     * @return The context of the game
     */
    public Context getContext() {
        return context;
    }

    /**
     * Sets the context for the current game
     *
     * @param context the context
     */
    public void setContext(Context context) {
        this.context = context;
    }


    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        listener.onDifficultyChanged(difficulty);
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
        listener.onTimeChanged(timer);
    }

    public int getViewErrors() {
        return viewErrors;
    }

    public void setViewErrors(int viewErrors) {
        this.viewErrors = viewErrors;
        listener.onViewErrorsChanged(viewErrors);
    }

    public int getHints() {
        return hints;
    }

    public void setHints(int hints) {
        this.hints = hints;
        listener.onHintsChanged(hints);
    }

    /**
     * Gets the current level of the current game
     *
     * @return The current level
     */
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        listener.onLevelChanged(level);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
        listener.onModeChanged(mode);
    }

    /**
     * Starts the in-game timer
     */
    public void startTimer() {
        if (!TIMER_ISRUNNING) {
            timerHandler.postDelayed(timerRunnable, 1000);
        }
        TIMER_ISRUNNING = true;
    }

    /**
     * Sops the game timer
     */
    public void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
        TIMER_ISRUNNING = false;
    }

    public void resetTimer() {
        setTimer(0);
        if (!TIMER_ISRUNNING) {
            timerHandler.postDelayed(timerRunnable, 1000);
            TIMER_ISRUNNING = true;
        }
    }

    public void loadSerialGrid(Context context, String grid, String values, String solution, int currentLevel, int hints, long time) {
        setMode("serial");
        setLevel(currentLevel);
        setDifficulty(1);
        setHints(hints);
        setTimer(time);
        //if there is no saved game, create a new grid from scratch
        if (grid.equals("NaN")) {
            //set values
            createGrid(context, 1);
            return;
        }


        //get the saved grid (string) and convert to 2D int array
        String[] string_grid = grid.split(",");
        int[][] finalGrid = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                finalGrid[i][j] = Integer.parseInt(string_grid[i * 9 + j]);
            }
        }

        createContinueGrid(context, finalGrid, 1);

        //if there is no saved solution, exit function
        if ("NaN".equals(solution)) {
            createGrid(context, 1);
            return;
        }

        //get the saved grid (string) and convert to 2D int array
        String[] string_solution = solution.split(",");
        int[][] finalSolution = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                finalSolution[i][j] = Integer.parseInt(string_solution[i * 9 + j]);
            }
        }

        //set solution if its not null
        if (finalSolution != null)
            setSolution(finalSolution);

        //if there is no saved values, rexit function
        if ("NaN".equals(values)) {
            createGrid(context, 1);
            return;
        }

        //get the saved values (string) and convert to 2D int array
        String[] string_values = values.split(",");
        int[][] finalValues = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                finalValues[i][j] = Integer.parseInt(string_values[i * 9 + j]);
            }
        }

        //set values of continue grid if not null
        if (finalValues != null)
            setContinueValues(finalValues);
    }

    public void setDetails() {
        setMode(getMode());

        setLevel(getLevel());

        setDifficulty(getDifficulty());

        setHints(getHints());

        setTimer(getTimer());
    }

    /**
     * An interface that updates the game activity ui about in game changes
     */
    public interface Listener {
        /**
         * Game level has changed
         *
         * @param level The new level
         */
        void onLevelChanged(int level);

        /**
         * Game number of hints have changed
         *
         * @param hints The number of hints
         */
        void onHintsChanged(int hints);

        /**
         * Number of views for errors have changed
         *
         * @param viewErrors the new value
         */
        void onViewErrorsChanged(int viewErrors);

        /**
         * Game difficulty has changed
         *
         * @param difficulty the new value
         */
        void onDifficultyChanged(int difficulty);

        /**
         * In-game time has changed
         *
         * @param time The new time
         */
        void onTimeChanged(long time);

        /**
         * Game mode has changed
         *
         * @param mode The new mode
         */
        void onModeChanged(String mode);

        void onGameCompleted();

        void onGridError(String error);
    }

    /**
     * Sets the event listener for notifying in-game changes to UI
     *
     * @param listener
     */
    public void setEventListener(Listener listener) {
        this.listener = listener;
    }
}
