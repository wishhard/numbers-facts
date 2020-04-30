package com.wishhard.nf.numbersfacts.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.wishhard.nf.numbersfacts.R;
import com.wishhard.nf.numbersfacts.utils.StateListDrawableBuilder;


public class FactSearchButton extends AppCompatImageButton {


   private Drawable normal, pressed;

    public FactSearchButton(Context context) {
        super(context);
        init(context);
    }

    public FactSearchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        normal = AppCompatResources.getDrawable(context, R.drawable.search_btn_n);
        pressed = AppCompatResources.getDrawable(context,R.drawable.search_btn_c);
        setStateListDrawable();
    }

    private void setStateListDrawable() {
        StateListDrawable listDrawable = new StateListDrawableBuilder()
                .setPressedDrawable(pressed)
                .setEnableDrawable(normal)
                .build();

        setBackground(listDrawable);
    }

}
