package com.example.sportsclub.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class CustomTextViewQuicksand extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextViewQuicksand(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewQuicksand(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewQuicksand(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/quicksand_regular.ttf");
            setTypeface(tf);
        }
    }

}
