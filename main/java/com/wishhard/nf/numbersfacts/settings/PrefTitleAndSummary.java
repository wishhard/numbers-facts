package com.wishhard.nf.numbersfacts.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.wishhard.nf.numbersfacts.R;
import com.wishhard.nf.numbersfacts.utils.ScreenUtility;


public class PrefTitleAndSummary extends AppCompatTextView {

    private static final int DEFUALT_VALUE = 0;
    private static final int TITLE_VALUE = 1;
    private static final int SUMMARY_VALUE = 2;

    private Context mContext;

    private int titleOrSummary;


    public PrefTitleAndSummary(Context context) {
        super(context);
        init(context);
    }

    public PrefTitleAndSummary(Context context, AttributeSet attrs) {
        super(context, attrs);
        prefTitleAndSummaryAttrValues(attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        ScreenUtility.initContext(mContext);

        setIncludeFontPadding(false);
        setTitleOrSummary();

    }

    private void prefTitleAndSummaryAttrValues(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PrefTitleAndSummary,0,0);

        try {
            titleOrSummary = a.getInt(R.styleable.PrefTitleAndSummary_title_or_summary,DEFUALT_VALUE);

            if(titleOrSummary < DEFUALT_VALUE || titleOrSummary > SUMMARY_VALUE) {
                titleOrSummary = DEFUALT_VALUE;
            }

        } finally {
             a.recycle();
        }
    }

    private void setTitleOrSummary() {
        if(titleOrSummary != DEFUALT_VALUE) {

            setTypeface((titleOrSummary == TITLE_VALUE)? Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/titillium_web_bold.ttf"): Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/titillium_web_light.ttf"));

            setTextColor((titleOrSummary == TITLE_VALUE)? ContextCompat.getColor(mContext, R.color.title_color_pref)
                    :ContextCompat.getColor(mContext, R.color.summary_color_pref));

            if(titleOrSummary == TITLE_VALUE) {
                setHorizontalFadingEdgeEnabled(true);
                setSingleLine();
                setEllipsize(TextUtils.TruncateAt.MARQUEE);
                if(ScreenUtility.hasNormalScreenSize()) {
                    setTextSize(20);
                } else {
                    setTextSize(24);
                }
            } else {
                setMaxLines(4);
                setTextSize(14);
            }
        }
    }

}
