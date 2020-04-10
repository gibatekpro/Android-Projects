package com.gibatekpro.sudokusolverandcreator.engine;

/**
 * Created by Anthony Gibah on 3/14/2017.
 */

import java.util.ArrayList;
import java.util.Random;

public class sudokugenerator {
    private static sudokugenerator instance;
    private ArrayList<ArrayList<Integer>> Available = new ArrayList<>();
    private Random rand = new Random();
    private int RETRY = 5;

    private sudokugenerator() {

    }

    public static sudokugenerator getInstance() {
        if (instance == null) {
            instance = new sudokugenerator();
        }
        return instance;
    }

    public int[][] generategrid() throws RuntimeException {

        int[][] sudoku = new int[9][9];
        int setposition = 0;
        emptygrid(sudoku);
        RETRY = 5;
        while (Available.size() != 81) {
            if (RETRY == 0)
                throw new RuntimeException("Array out of bounds");
            else {
                emptygrid(sudoku);
            }
            RETRY--;
        }

        while (setposition < 81) {
            if (setposition < 0)
                setposition = 0;
            if (Available.get(setposition).size() != 0) {
                int i = rand.nextInt(Available.get(setposition).size());
                int digit = Available.get(setposition).get(i);

                if (!checkConflict(sudoku, setposition, digit)) {
                    int xpos = setposition / 9;
                    int ypos = setposition % 9;

                    sudoku[xpos][ypos] = digit;
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
            }
        }
        return sudoku;
    }
    /*private void savesolution(int[][] Sudoku) {
        // TODO Auto-generated method stub
        int [] [] solution = new int [9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solution[i][j] = Sudoku[x][y];
            }
        }
    }*/


    public int[][] removeElements(int[][] sudoku, int holesToMake) {
        /*
         * We define difficulty as follows: Easy: 32+ clues (49 or fewer holes)
		 * Medium: 27-31 clues (50-54 holes) Hard: 26 or fewer clues (54+ holes)
		 * This is human difficulty, not algorighmically (though there is some
		 * correlation)
		 */
        double remainingSquares = 81;
        double remainingHoles = (double) holesToMake;

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                double holeChance = remainingHoles / remainingSquares;
                if (Math.random() <= holeChance) {
                    sudoku[i][j] = 0;
                    remainingHoles--;
                }
                remainingSquares--;
            }
        return sudoku;
    }


    private void emptygrid(int[][] sudoku) {
        Available.clear();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                sudoku[x][y] = -1;
            }
        }
        for (int x = 0; x < 81; x++) {
            Available.add(new ArrayList<Integer>());
            for (int i = 1; i <= 9; i++) {
                Available.get(x).add(i);
            }
        }
    }

    private boolean checkConflict(int[][] sudoku, int setposition, final int digit) {
        int xpos = setposition / 9;
        int ypos = setposition % 9;


        if (checkRegionConflict(sudoku, xpos, ypos, digit) || checkHorizontalConflict(sudoku, xpos, ypos, digit) || checkVerticalConflict(sudoku, xpos, ypos, digit)) {
            return true;
        }


        return false;
    }

    /**
     * return true if there is a conflict
     *
     * @param sudoku
     * @param xpos
     * @param ypos
     * @param digit
     * @return
     */
    private boolean checkHorizontalConflict(final int[][] sudoku, final int xpos, final int ypos, final int digit) {
        for (int x = xpos - 1; x >= 0; x--) {
            if (digit == sudoku[x][ypos]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalConflict(final int[][] sudoku, final int xpos, final int ypos, final int digit) {
        for (int y = ypos - 1; y >= 0; y--) {
            if (digit == sudoku[xpos][y]) {
                return true;
            }
        }
        return false;
    }

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
