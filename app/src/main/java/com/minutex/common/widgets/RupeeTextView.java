package com.minutex.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.minutex.R;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class RupeeTextView extends TextView {

    public RupeeTextView(Context context) {
        this(context, null);
    }

    public RupeeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public RupeeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int[] attrsArray = new int[] {
                android.R.attr.text,
                R.attr.strikeThrough,
                R.attr.rupeeText
        };

        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        setRupeeText(ta.getString(0));
        setRupeeText(ta.getString(2));


        if(ta.getBoolean(1, false)) {
            this.setPaintFlags(getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }


    public void setRupeeText(int resId) {
        setRupeeText(getResources().getString(resId));
    }

    public void setRupeeText(CharSequence text){
        setText(getResources().getString(R.string.rs) + text);
    }
}
