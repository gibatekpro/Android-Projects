package com.gibatekpro.sudokusolverandcreator.engine;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Anthony Gibah on 3/14/2017.
 */
public class gameengine {
    private gamegrid grid = null;
    private int selectedPosX = -1, selectedPosY = -1;
    private static gameengine instance;

    private Listener listener;

    private Solvegame solve;

    private gameengine() {
    }

    public static gameengine getInstance() {
        if (instance == null) {
            instance = new gameengine();

        }
        return instance;
    }


    private void createGrid(Context context, int difficulty) {

        if (sudokuSolverIsRunning()) {
            listener.onErrorCreatingGrid("Cannot create a new game while one is being solved.");
            return;
        }
        int[][] sudoku;
        try {
            sudoku = sudokugenerator.getInstance().generategrid();
        } catch (RuntimeException re) {
            listener.onErrorCreatingGrid(re.getMessage());
            return;
        }
        //setSolution(sudoku);
        sudoku = sudokugenerator.getInstance().removeElements(sudoku, difficulty);
        if (grid == null) {
            grid = new gamegrid(context);
        }
        grid.setGrid(sudoku);
        listener.onSudokuGenerated();
        //setUnmodifiedGrid(sudoku);
    }

    public gamegrid getGrid() {
        return grid;
    }

    public void setSelectedPosition(int x, int y) {
        selectedPosX = x;
        selectedPosY = y;
    }

    public void setNumber(int Number) {
        if (Number < 1 || Number > 9) {
            Number = 0;
        }
        if ((selectedPosX != -1) && (selectedPosY != -1)) {
            grid.setItem(selectedPosX, selectedPosY, Number);
        }
    }

    public boolean checkGame() {
        if (sudokuSolverIsRunning()) {
            listener.onErrorCreatingGrid("Cannot validate while game is being solved.");
            return false;
        }
        return grid.checkGame();
    }


    public void createChoiceGame(Context context, int holes) {
        createGrid(context, holes);
    }

    public void createEmptyGrid(Context context) {
        if (sudokuSolverIsRunning()) {
            listener.onErrorCreatingGrid("Cannot clear grid while game is being solved.");
            return;
        }
        int[][] sudoku = new int[9][9];
        if (grid == null) {
            grid = new gamegrid(context);
        }
        grid.setGrid(sudoku);
    }

    public void createEmptyGrid(Context context, Listener listener) {
        setEventListener(listener);
        createEmptyGrid(context);
    }

    public void SolveSudoku(Context context) {
        if (solve == null) {
            solve = new Solvegame();
            solve.execute(context);
        }else if(sudokuSolverIsRunning()){
            solve.cancel(true);
        }
    }

    public void cancelSolveGame() {
        if (sudokuSolverIsRunning()) {
            solve.cancel(true);
        }
    }

    public boolean sudokuSolverIsRunning() {
        return solve != null && solve.getStatus() == AsyncTask.Status.RUNNING;
    }


    /**
     * An async task that solves the sudoku puzzle in the background
     */
    private class Solvegame extends AsyncTask<Context, Integer, int[][]> {


        private ArrayList<ArrayList<Integer>> Available = new ArrayList<>();
        private Random rand = new Random();

        int[][] sudGrid = new int[9][9];

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sudGrid = new int[9][9];
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    int val = grid.getItem(x, y).getValue();
                    sudGrid[x][y] = val;
                    if (val != 0) {
                        if(checkConflict(sudGrid,x,y,val)){
                            listener.onErrorCreatingGrid("Invalid entry. Conflict detected.");
                            this.cancel(true);
                            return;
                        }
                    }
                }
            }

            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    int val = grid.getItem(x, y).getValue();
                    if (val != 0) {
                        grid.getItem(x, y).setModifiable(false);
                    }
                }
            }
        }

        @Override
        protected int[][] doInBackground(Context... context) {
//            if (grid == null) {
//                grid = new gamegrid(context[0]);
//            }
            return solvegrid(sudGrid);
        }

        @Override
        protected void onCancelled(int[][] ints) {
            listener.onSudokuSolveCanceled();
            //listener.onErrorCreatingGrid("Sudoku solver aborted by user.");
            super.onCancelled(ints);
            solve = null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int xpos = values[0];
            int ypos = values[1];
            int digit = values[2];
            grid.getGrid()[xpos][ypos].setValue(digit);
            //super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(int[][] ints) {
            listener.onErrorCreatingGrid("Finished.");
            listener.onSudokuSolved();
            super.onPostExecute(ints);
            solve = null;
        }

        private int[][] solvegrid(int[][] arrayGrid) throws RuntimeException {

            int[][] sudoku = new int[9][9];
            int setposition = 0;
            emptygrid(sudoku, arrayGrid);

            while (setposition < 81 && !isCancelled()) {
                if (setposition < 0)
                    setposition = 0;
                int xpos = setposition / 9;
                int ypos = setposition % 9;

                if (arrayGrid[xpos][ypos] == 0) {
                    if (Available.get(setposition).size() != 0) {

                        if(isCancelled())
                            return new int[9][9];

                        int i = rand.nextInt(Available.get(setposition).size());
                        int digit = Available.get(setposition).get(i);
                        if (!checkConflict(sudoku, setposition, digit)) {
                            sudoku[xpos][ypos] = digit;
                            publishProgress(xpos, ypos, digit);
                            Available.get(setposition).remove(i);
                            setposition++;

                        } else {
                            Available.get(setposition).remove(i);
                        }
                    } else {
                        for (int i = 1; i <= 9; i++) {
                            Available.get(setposition).add(i);
                        }
                        setposition--;
                        while (setposition >= 1) {

                            if(isCancelled())
                                return new int[9][9];

                            xpos = setposition / 9;
                            ypos = setposition % 9;
                            if (arrayGrid[xpos][ypos] != 0) {
                                setposition--;
                            } else {
                                sudoku[xpos][ypos] = -1;
                                publishProgress(xpos, ypos, 0);
                                break;
                            }
                        }
                    }
                } else {
                    setposition++;
                }
            }
            return sudoku;
        }

        private void emptygrid(int[][] sudoku, int[][] available) {
            Available.clear();
            int val;
            for (int y = 0; y < 9; y++) {
                for (int x = 0; x < 9; x++) {
                    val = available[x][y];
                    if (val != 0) {
                        sudoku[x][y] = val;
                    } else {
                        sudoku[x][y] = -1;
                    }
                }
            }
            for (int x = 0; x < 81; x++) {
                Available.add(new ArrayList<Integer>());
                int xpos = x / 9;
                int ypos = x % 9;
                if (available[xpos][ypos] != 0) {
                    Available.get(x).add(available[xpos][ypos]);
                } else {
                    for (int i = 1; i <= 9; i++) {
                        Available.get(x).add(i);
                    }
                }
            }
        }

        private boolean checkConflict(int[][] sudoku, int setposition, final int digit) {
            int xpos = setposition / 9;
            int ypos = setposition % 9;

            return checkRegionConflict(sudoku, xpos, ypos, digit) || checkHorizontalConflict(sudoku, xpos, ypos, digit) || checkVerticalConflict(sudoku, xpos, ypos, digit);
        }

        private boolean checkConflict(int[][] sudoku, int xpos,int ypos, final int digit) {
            return checkRegionConflict(sudoku, xpos, ypos, digit) || checkHorizontalConflict(sudoku, xpos, ypos, digit) || checkVerticalConflict(sudoku, xpos, ypos, digit);
        }

        /**
         * Checks for conflict on the x-axis
         * @param sudoku    The sudoku game to check
         * @param xpos  the x-axis coordinat
         * @param ypos  The y-axis coordinate
         * @param digit The digit being checked for conflict
         * @return  Boolean True if exists, otherwise false.
         */
        private boolean checkHorizontalConflict(final int[][] sudoku, final int xpos, final int ypos, final int digit) {
            for (int x = 8; x >= 0; x--) {
                if (digit == sudoku[x][ypos] && x != xpos) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Checks for conflict on the y-axis
         * @param sudoku    The sudoku game to check
         * @param xpos  the x-axis coordinat
         * @param ypos  The y-axis coordinate
         * @param digit The digit being checked for conflict
         * @return  Boolean True if exists, otherwise false.
         */
        private boolean checkVerticalConflict(final int[][] sudoku, final int xpos, final int ypos, final int digit) {
            for (int y = 8; y >= 0; y--) {
                if (digit == sudoku[xpos][y] && y != ypos) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Checks for conflict on the 3x3 region
         * @param sudoku    The sudoku game to check
         * @param xpos  the x-axis coordinat
         * @param ypos  The y-axis coordinate
         * @param digit The digit being checked for conflict
         * @return  Boolean True if exists, otherwise false.
         */
        private boolean checkRegionConflict(final int[][] sudoku, final int xpos, final int ypos, final int digit) {
            int xregion = xpos / 3;
            int yregion = ypos / 3;
            for (int x = xregion * 3; x < xregion * 3 + 3; x++) {
                for (int y = yregion * 3; y < yregion * 3 + 3; y++) {
                    if ((x != xpos || y != ypos) && digit == sudoku[x][y]) {
                        return true;
                    }
                }
            }
            return false;
        }
    }


    /**
     * An interface that updates the game activity ui about in game changes
     */
    public interface Listener {
        void onSudokuGenerated();

        void onSudokuSolved();

        void onErrorCreatingGrid(String error);

        void onSudokuSolveCanceled();
    }

    /**
     * Sets the event listener for notifying in-game changes to UI
     *
     * @param listener  The listener to ba attached
     */
    private void setEventListener(Listener listener) {
        this.listener = listener;
    }
}
