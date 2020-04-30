package com.wishhard.nf.numbersfacts.settings;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.wishhard.nf.numbersfacts.R;

public class CustomSwitchPref extends SwitchPreferenceCompat {

    private Context mContext;

    public CustomSwitchPref(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomSwitchPref(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSwitchPref(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitchPref(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);


        final PrefTitleAndSummary titleView = (PrefTitleAndSummary) holder.findViewById(R.id.title_p);

        if(titleView != null) {
            final CharSequence title = getTitle();
            titleView.setText(title);
            titleView.setVisibility(View.VISIBLE);
        } else {
            titleView.setVisibility(View.GONE);
        }

        final PrefTitleAndSummary summaryView = (PrefTitleAndSummary) holder.findViewById(R.id.summary_p);
        if(summaryView != null) {
            final CharSequence summary = getSummary();
            if(!TextUtils.isEmpty(summary)) {
                summaryView.setText(summary);
                summaryView.setVisibility(View.VISIBLE);
            }
        } else {
            summaryView.setVisibility(View.GONE);
        }

        View switchView = holder.findViewById(R.id.switchWidget);

        if(switchView instanceof SwitchCompat) {
            customThumbAndTracker((SwitchCompat) switchView);
        }
    }

    private void customThumbAndTracker(SwitchCompat v) {
        int mediumSandy = ContextCompat.getColor(mContext, R.color.medium_sandy);
        int mediumGrey = ContextCompat.getColor(mContext, R.color.medium_grey);
        int alphaMediumYellow = Color.argb(127, Color.red(mediumSandy), Color.green(mediumSandy), Color.blue(mediumSandy));
        int alphaMediumGrey = Color.argb(127, Color.red(mediumGrey), Color.green(mediumGrey), Color.blue(mediumGrey));

        DrawableCompat.setTintList(v.getThumbDrawable(), new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        mediumSandy,
                        ContextCompat.getColor(getContext(), R.color.light_grey)
                }));

        DrawableCompat.setTintList(v.getTrackDrawable(), new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        alphaMediumYellow,
                        alphaMediumGrey
                }));

    }


}
