package com.example.sportsclub.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class CustomTextViewLight2 extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextViewLight2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewLight2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewLight2(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Light.ttf");
            setTypeface(tf);
        }
    }

}
