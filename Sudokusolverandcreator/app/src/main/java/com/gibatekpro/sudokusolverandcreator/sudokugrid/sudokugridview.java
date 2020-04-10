package com.gibatekpro.sudokusolverandcreator.sudokugrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.gibatekpro.sudokusolverandcreator.engine.gameengine;


/**
 * Created by Anthony Gibah on 3/14/2017.
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
            }
        });
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
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
//    }

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
