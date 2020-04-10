package com.gibatekpro.sudoku.sudokugrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.gibatekpro.sudoku.engine.gameengine;

/**
 * Created by Anthony Gibah on 6/7/2017.
 */
public class sudokugridview extends GridView {
    private final Context context;

    public sudokugridview(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        sudokugridviewadapter gridviewadapter = new sudokugridviewadapter(context);

        setAdapter(gridviewadapter);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                int x = position / 9;
                int y = position % 9;

                gameengine.getInstance().setSelectedPosition(x,y);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    class sudokugridviewadapter extends BaseAdapter {

        private Context context;

        public sudokugridviewadapter(Context context) {

            this.context = context;

        }

        @Override
        public int getCount() {
            return 81;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return gameengine.getInstance().getGrid().getItem(position);
        }
    }
}

