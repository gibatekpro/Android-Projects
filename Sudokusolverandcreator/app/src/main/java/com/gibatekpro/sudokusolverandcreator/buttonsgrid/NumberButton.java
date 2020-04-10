package com.gibatekpro.sudokusolverandcreator.buttonsgrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.gibatekpro.sudokusolverandcreator.engine.gameengine;


/**
 * Created by Anthony Gibah on 3/15/2017.
 */
public class NumberButton extends androidx.appcompat.widget.AppCompatButton implements OnClickListener {
    private int Number;
    private Context context;
    public NumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
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
       // ((Activity)context).itemClicked();
    }
}
