package com.gibatekpro.sudoku.buttonsgrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.gibatekpro.sudoku.engine.gameengine;

/**
 * Created by Anthony Gibah on 6/7/2017.
 */
public class NumberButton extends Button implements View.OnClickListener {
    private int Number;
    public NumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        gameengine.getInstance().setNumber(Number);
    }
    public void setNumber(int Number){
        if (Number == 10){
        }
        this.Number = Number;
    }
}
