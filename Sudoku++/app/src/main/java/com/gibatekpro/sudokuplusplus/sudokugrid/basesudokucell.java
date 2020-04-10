package com.gibatekpro.sudokuplusplus.sudokugrid;

import android.content.Context;
import android.view.View;

/**
 * Created by Anthony Gibah on 3/15/2017.
 */
public class basesudokucell extends View {
    private int value;
    private boolean modifiable = true;
    private int correctValue = -1;
    private boolean selected = false;

    public basesudokucell(Context context) {
        super(context);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void setInitValue(int value) {
        this.value = value;
        invalidate();
    }

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    public void setValue(int value) {
        if (modifiable) {
            this.value = value;
        }
        invalidate();
    }

    public boolean getModifiable() {
        return modifiable;
    }

    public int getValue() {
        return value;
    }

    public int getCorrectValue() {
        return correctValue;
    }

    public void setCorrectValue(int correctValue) {
        if (modifiable) {
            this.correctValue = correctValue;
        }
        invalidate();
    }

    public void clearCell() {
        invalidate();
    }

}
