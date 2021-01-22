package com.example.sportsclub.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class CustomTextViewItalic extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextViewItalic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewItalic(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Italic.ttf");
            setTypeface(tf);
        }
    }

}
