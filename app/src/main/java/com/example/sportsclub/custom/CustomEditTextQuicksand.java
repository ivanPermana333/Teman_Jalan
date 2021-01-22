package com.example.sportsclub.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditTextQuicksand extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditTextQuicksand(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditTextQuicksand(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditTextQuicksand(Context context) {
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