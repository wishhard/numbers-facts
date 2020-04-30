package com.wishhard.nf.numbersfacts.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wishhard.nf.numbersfacts.R;


public class DividerDecoration extends RecyclerView.ItemDecoration {

         private Context mContext;
         private Drawable mDivider;
         private final Rect mBounds = new Rect();



    public DividerDecoration(Context mContext) {
        this.mContext = mContext;

        mDivider = ContextCompat.getDrawable(mContext, R.drawable.the_divider);

    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //super.onDraw(c, parent, state);

        vDram(c,parent);
    }



    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
           outRect.set(0,0,0,mDivider.getIntrinsicHeight());
    }


    @SuppressLint("NewApi")
    private void vDram(Canvas c,RecyclerView parent) {
            c.save();
            final int left;
            final int right;

            if(parent.getClipToPadding()) {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                c.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
            } else {
                left = 0;
                right = parent.getWidth();
            }

            final int childCount = parent.getChildCount();
            for(int i = 0; i < childCount;i++) {
                final  View child= parent.getChildAt(i);
                 parent.getDecoratedBoundsWithMargins(child,mBounds);
                final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
                 final int top = bottom - mDivider.getIntrinsicHeight();
                 mDivider.setBounds(left,top,right,bottom);
                 mDivider.draw(c);
            }
        c.restore();
    }

}
