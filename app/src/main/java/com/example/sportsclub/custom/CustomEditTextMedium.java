package com.example.sportsclub.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditTextMedium extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditTextMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditTextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditTextMedium(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
            setTypeface(tf);
        }
    }

}