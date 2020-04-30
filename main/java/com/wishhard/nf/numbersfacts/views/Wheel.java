package com.wishhard.nf.numbersfacts.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.wishhard.nf.numbersfacts.constpack.MonthsAndDaysConst;
import com.wishhard.nf.numbersfacts.utils.ScreenUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wheel extends ScrollView implements Runnable {


    private WheelListener wheelListener;

    private LinearLayout mContainer;
    private List<String> items;

    private Context mContext;

    private int displayItemViewCount = 1;
    int selectedIndex = 0;

    private int itemHeight = 0;

    private int initialY;

    private boolean outSide = false;
    private Rect viewBoundry;


    public Wheel(Context context) {
        super(context);
        this.mContext = context;
        init(mContext);
    }

    public Wheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(mContext);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        ScreenUtility.initContext(context);

        setVerticalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);

        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.VERTICAL);
        addView(mContainer);


        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    startTaxt();
                }

                return false;
            }
        });

    }

    @Override
    public void fling(int velocityY) {
        super.fling((int) (velocityY / 2f));
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setItems(List<String> list) {

        if(items == null) {
            items = new ArrayList<>();
        }
        items.clear();
        items.addAll(list);

        initializeData();

    }


    public void setWheelListener(WheelListener wheelListener) {
        this.wheelListener = wheelListener;
    }

    public String getSelectedItem() {
        return items.get(selectedIndex);
    }


    public void updateDaysInMonth(String month) {

            switch (month) {
                case MonthsAndDaysConst.FEB:
                    mContainer.removeAllViews();
                    setItems(Arrays.asList(MonthsAndDaysConst.DAY_PARAM_29));
                    selectedIndex = (selectedIndex >= items.size())?  items.size()-1:selectedIndex;
                    break;
                case MonthsAndDaysConst.JAN:
                case MonthsAndDaysConst.MAR:
                case MonthsAndDaysConst.MAY:
                case MonthsAndDaysConst.JUL:
                case MonthsAndDaysConst.AUG:
                case MonthsAndDaysConst.OCT:
                case MonthsAndDaysConst.DEC:
                    mContainer.removeAllViews();
                    setItems(Arrays.asList(MonthsAndDaysConst.DAY_PARAM));
                    break;
                case MonthsAndDaysConst.APR:
                case MonthsAndDaysConst.JUN:
                case MonthsAndDaysConst.SEP:
                case MonthsAndDaysConst.NOV:
                    mContainer.removeAllViews();
                    setItems(Arrays.asList(MonthsAndDaysConst.DAY_PARAM_30));
                    selectedIndex = (selectedIndex >= items.size())?  items.size()-1:selectedIndex;
                    break;
            }


    }


    private TextView createTv(String item) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/leagueGothic_regular.otf");

            TextView tv = new TextView(mContext);
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setSingleLine(true);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
            tv.setIncludeFontPadding(false);
            tv.setTypeface(tf);
            tv.setTextColor(Color.BLACK);
            tv.setText(item);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(dp2pix(10f), dp2pix(12f), dp2pix(10f), dp2pix(5f));

            if(itemHeight == 0) {
                itemHeight = getMeasuredViewHeight(tv);
                mContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        itemHeight * displayItemViewCount));

                this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        itemHeight * displayItemViewCount));
            }

            return tv;

    }

    private void initializeData() {

        for (String s:items) {
            mContainer.addView(createTv(s));
        }

    }



    private void onSelectedCallBlack() {
        if(wheelListener != null) {
            wheelListener.onSelected(items.get(selectedIndex), selectedIndex);
        }
    }


    private void startTaxt() {
        initialY = getScrollY();
        this.postDelayed(this,50);
    }

    private int getMeasuredViewHeight(View v) {
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int expand = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        v.measure(w,expand);
        return v.getMeasuredHeight();
    }


    private int dp2pix(float value) {
        return (int) (value * ScreenUtility.getDensity() + 0.5f);
    }


    @Override
    public void run() {
         int newY = getScrollY();

         if(initialY - newY == 0) {
             final int remainder = initialY % itemHeight;
             final int divided = initialY / itemHeight;


             if(remainder == 0) {
                 selectedIndex = divided;

                 onSelectedCallBlack();
             } else {
                 if(remainder > itemHeight/2) {
                      Wheel.this.post(new Runnable() {
                          @Override
                          public void run() {

                              Wheel.this.smoothScrollTo(0,initialY - remainder + itemHeight);
                              selectedIndex = divided +1;

                              onSelectedCallBlack();
                          }
                      });

                 } else {
                      Wheel.this.post(new Runnable() {
                          @Override
                          public void run() {
                              Wheel.this.smoothScrollTo(0,initialY - remainder);
                              selectedIndex = divided;

                              onSelectedCallBlack();
                          }
                      });

                 }
             }
         } else {
             initialY =  getScrollY();
             Wheel.this.postDelayed(this,50);
         }
    }

    public interface WheelListener {
        void onSelected(String item, int index);
    }
}
