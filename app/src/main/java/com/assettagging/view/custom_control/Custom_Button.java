package com.assettagging.view.custom_control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class Custom_Button extends Button {

    public Custom_Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTransformationMethod(null);
    }
}
