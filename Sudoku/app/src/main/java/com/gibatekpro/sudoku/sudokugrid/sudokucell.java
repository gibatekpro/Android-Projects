package com.gibatekpro.sudoku.sudokugrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Anthony Gibah on 6/7/2017.
 */
public class sudokucell extends basesudokucell {

    private Paint npaint;
    //determines if the cell should be shaded
    private Boolean shade = false;

    public sudokucell(Context context, Boolean shade) {
        super(context);
        this.shade = shade;
        npaint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLines(canvas);
        drawDigit(canvas);
    }

    /**
     * Draws the digit that is contained in the sudoku cell
     * @param canvas The canvas on which the digit is drawn
     */
    private void drawDigit(Canvas canvas) {
        //check if the cell is modifiable.
        if (getModifiable()) {
            //check if the value in the cell is correct and color code it (red= incorrect, blue=correct)
            if (getCorrectValue() == getValue() || getCorrectValue() == -1) {
                npaint.setColor(Color.BLUE);
            } else {
                npaint.setColor(Color.RED);
            }

        } else {
            npaint.setColor(Color.BLACK);
        }
        npaint.setTextSize(50);
        npaint.setStyle(Paint.Style.FILL);

        Rect bounds = new Rect();
        npaint.getTextBounds(String.valueOf(getValue()), 0, String.valueOf(getValue()).length(), bounds);

        /*the if below displays only non zero numbers. get value() gets the value of the generated numbers*/
        if (getValue() != 0) {
            canvas.drawText(String.valueOf(getValue()), (getWidth() - bounds.width()) / 2, (getHeight() + bounds.height()) / 2, npaint);
        }
    }

    /**
     * Draws the walls of a sudoku cell.
     * @param canvas The canvas on which the cell walls and digits are drawn
     */
    private void drawLines(Canvas canvas) {
        /*below sets the color, style and size of the stroke cells*/


        if (shade) {
            npaint.setColor(Color.rgb(220, 220, 220));
            npaint.setStyle(Paint.Style.FILL);

            canvas.drawRect(0, 0, getWidth(), getHeight(), npaint);
        }
        npaint.setColor(Color.BLACK);
        npaint.setStrokeWidth(3);
        npaint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(0, 0, getWidth(), getHeight(), npaint);
    }
}

