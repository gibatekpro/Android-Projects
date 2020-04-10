package com.gibatekpro.sudokuplusplus.engine;

import android.content.Context;
import android.os.Handler;

import com.gibatekpro.sudokuplusplus.sudokugrid.sudokucell;

/**
 * Created by Anthony Gibah on 3/15/2017.
 */
public class gamegrid {

    private sudokucell[][] Sudoku = new sudokucell[9][9];
    private Context context;
    private int hintPosX = -1, hintPosY = -1;
    private boolean hinting;

    //a timer used to display the incorrect cells for a particular amount of time
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            clearSolution();
            clearHint(hintPosX, hintPosY);
        }
    };
    private boolean hinting1;

    /*below sets up the sudoku cell but it is not seen*/
    public gamegrid(Context context) {
        this.context = context;

        //used to apply shading to every other 3x3 grid
        boolean x_shade = false;
        boolean y_shade = false;
        for (int x = 0; x < 9; x++) {
            switch (x + 1) {
                case 4:
                case 5:
                case 6:
                    x_shade = true;
                    break;
                default:
                    x_shade = false;
            }
            for (int y = 0; y < 9; y++) {
                if (!x_shade) {
                    switch (y + 1) {
                        case 4:
                        case 5:
                        case 6:
                            y_shade = true;
                            break;
                        default:
                            y_shade = false;
                    }
                } else {
                    switch (y + 1) {
                        case 4:
                        case 5:
                        case 6:
                            y_shade = false;
                            break;
                        default:
                            y_shade = true;
                    }
                }
                Sudoku[x][y] = new sudokucell(context, y_shade);
            }
        }
    }

    /*below will fill it up with integers*/
    public void setGrid(int[][] grid) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Sudoku[x][y].setModifiable(true);
                Sudoku[x][y].setInitValue(grid[x][y]);
                if (grid[x][y] != 0) {
                    Sudoku[x][y].setModifiable(false);
                }
            }
        }
    }
    /*below will fill it up with integers*/
    public void clearGrid() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Sudoku[x][y].clearCell();
            }
        }
    }

    public void viewHint(int selectedPosX, int selectedPosY, int value) {
        if (hinting || selectedPosX == -1 || selectedPosY == -1)
            return;

        hinting = true;
        Sudoku[selectedPosX][selectedPosY].setValue(value);
        //start the timer that stops displaying incorrect cells
        hintPosX = selectedPosX;
        hintPosY = selectedPosY;
        timerHandler.postDelayed(timerRunnable, 3000);
    }

    /**
     * Sets the correct value property of the sudoku cells which can be used
     * later to display incorrect cells for a particular amount of time
     *
     * @param solution The correct values of the sudoku grid
     */
    public void viewSolution(int[][] solution) {
        if (solution != null) {
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    //Sudoku[x][y].setInitValue(grid[x][y]);
                    Sudoku[x][y].setCorrectValue(solution[x][y]);
                }
            }
        }
        //start the timer that stops displaying incorrect cells
        timerHandler.postDelayed(timerRunnable, 3000);
    }

    /**
     * Stops the display of incorrect cells
     */
    public void clearSolution() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Sudoku[x][y].setCorrectValue(-1);
            }
        }
        timerHandler.removeCallbacks(timerRunnable);
    }

    /**
     * Clear the hint that was displayed
     *
     * @param hintPosX The row of the hint
     * @param hintPosY The column of the hint
     */
    private void clearHint(int hintPosX, int hintPosY) {
        if (!hinting || hintPosX == -1 || hintPosY == -1)
            return;

        Sudoku[hintPosX][hintPosY].setValue(0);
        hinting = false;
    }


    public sudokucell[][] getGrid() {

        return Sudoku;
    }

    public sudokucell getItem(int x, int y) {
        return Sudoku[x][y];
    }

    /**
     * Gets a particy=ular cell from the array of cells
     *
     * @param position The position of the cell in the array
     * @return The cell
     */
    public sudokucell getItem(int position) {
        int x = position / 9;
        int y = position % 9;
        return Sudoku[x][y];
    }

    public void setItem(int x, int y, int Number) {
        Sudoku[x][y].setValue(Number);
    }

    public boolean checkGame() {
        int[][] sudGrid = new int[9][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                sudGrid[x][y] = getItem(x, y).getValue();
            }
        }
        if (SudokuChecker.getInstance().checkSudoku(sudGrid)) {

            return true;
        }
        return false;
    }
}
