package com.wishhard.nf.numbersfacts.menupojo;

import android.graphics.drawable.Drawable;

public class PicAndTextPowerMenu {
    private Drawable icon;
    private String title;

    public PicAndTextPowerMenu(Drawable icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
