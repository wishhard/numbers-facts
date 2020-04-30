package com.wishhard.nf.numbersfacts.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class StateListDrawableBuilder {


    private static final int[] STATE_PRESSED = new int[]{android.R.attr.state_pressed};
    private static final int[] STATE_DISABLED = new int[]{-android.R.attr.state_enabled};
    private static final int[] STATE_ENABLED = new int[]{android.R.attr.state_enabled};

    private static final int[] STATE_SELECTED = new int[]{android.R.attr.state_selected};
    private static final int[] STATE_NOT_SELECTED = new int[]{-android.R.attr.state_selected};

    private static final int[] STATE_ACTIVATED = new int[]{android.R.attr.state_activated};
    private static final int[] STATE_NOT_ACTIVATED = new int[]{-android.R.attr.state_selected};

    private Drawable pressedDrawable;
    private Drawable enabledDrawable;
    private Drawable disabledDrawable;

    private Drawable selectedDrawable;
    private Drawable notSelectedDrawable;

    private Drawable activatedDrawable;
    private Drawable notActivatedDrawable;


    public StateListDrawableBuilder setActivatedDrawable(Drawable activatedDrawable) {
        this.activatedDrawable = activatedDrawable;
        return this;
    }

    public StateListDrawableBuilder setNotActivatedDrawable(Drawable notActivatedDrawable) {
        this.notActivatedDrawable = notActivatedDrawable;
        return this;
    }


    public StateListDrawableBuilder setSelectedDrawable(Drawable selectedDrawable) {
        this.selectedDrawable = selectedDrawable;
        return this;
    }

    public StateListDrawableBuilder setNotSelectedDrawable(Drawable notSelectedDrawable) {
        this.notSelectedDrawable = notSelectedDrawable;
        return this;
    }

    public StateListDrawableBuilder setPressedDrawable(Drawable pressedDrawable) {
        this.pressedDrawable = pressedDrawable;
        return this;
    }

    public StateListDrawableBuilder setDisabledDrawable(Drawable disabledDrawable) {
        this.disabledDrawable = disabledDrawable;
        return this;
    }

    public StateListDrawableBuilder setEnableDrawable(Drawable enabledDrawable) {
        this.enabledDrawable = enabledDrawable;
        return this;
    }

    public StateListDrawable build() {
        StateListDrawable listDrawable = new StateListDrawable();

        if(this.activatedDrawable != null) {
            listDrawable.addState(STATE_ACTIVATED, activatedDrawable);
        }

        if(this.notActivatedDrawable != null) {
            listDrawable.addState(STATE_NOT_ACTIVATED, notActivatedDrawable);
        }

        if(this.selectedDrawable != null) {
            listDrawable.addState(STATE_SELECTED, selectedDrawable);
        }

        if(this.notSelectedDrawable != null) {
            listDrawable.addState(STATE_NOT_SELECTED, notSelectedDrawable);
        }

        if(this.pressedDrawable != null) {
            listDrawable.addState(STATE_PRESSED,pressedDrawable);
        }

        if(this.disabledDrawable != null) {
            listDrawable.addState(STATE_DISABLED,disabledDrawable);
        }

        if(this.enabledDrawable != null) {
            listDrawable.addState(STATE_ENABLED,enabledDrawable);
        }

        return listDrawable;

    }



}
