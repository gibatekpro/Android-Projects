package com.gibatekpro.sudoku.buttonsgrid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.gibatekpro.sudoku.R;
import com.gibatekpro.sudoku.engine.gameengine;

/**
 * Created by Anthony Gibah on 6/7/2017.
 */
public class buttongridview extends GridView {
    public buttongridview(Context context, AttributeSet attrs) {
        super(context, attrs);
        buttongridviewAdapter gridviewadapter = new buttongridviewAdapter(context);
        setAdapter(gridviewadapter);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // Calculate entire height by providing a very large height hint.
        // View.MEASURED_SIZE_MASK represents the largest height possible.
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }

    class buttongridviewAdapter extends BaseAdapter {

        private Context context;

        public buttongridviewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 11;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                v = inflater.inflate(R.layout.button, parent, false);

                NumberButton btn = (NumberButton) v;
                btn.setId(position);
                if (position < 9) {
                    btn.setText(String.valueOf(position + 1));
                    btn.setNumber(position + 1);

                }
                if (position == 10) {
                    btn.setText("Cancel");
                    btn.setTextColor(Color.parseColor("#FFFFFF"));
                    btn.setBackgroundColor(Color.parseColor("#D50000"));

                    /*Delete button is going to set the number to zero which is blank space*/
                    btn.setNumber(0);
                }
                if (position == 9) {
                    btn.setText("Check");

                    btn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gameengine.getInstance().viewSolution();
                        }
                    });

                }

                return btn;
            }
            return v;
        }
    }

    /**
     * Created by Anthony Gibah on 3/17/2017.
     */
}

