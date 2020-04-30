package com.wishhard.nf.numbersfacts.tabfrags;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidmads.amsnackbarlib.AMSnackbar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wishhard.nf.numbersfacts.NumberFactActivity;
import com.wishhard.nf.numbersfacts.R;
import com.wishhard.nf.numbersfacts.constpack.MonthsAndDaysConst;
import com.wishhard.nf.numbersfacts.interfaces.TabDataListener;
import com.wishhard.nf.numbersfacts.pojos.NpItem;
import com.wishhard.nf.numbersfacts.services.NumbersApiService;
import com.wishhard.nf.numbersfacts.utils.Connectivity;
import com.wishhard.nf.numbersfacts.utils.KeyboardUtil;
import com.wishhard.nf.numbersfacts.utils.NumbersApiUrlHelper;
import com.wishhard.nf.numbersfacts.utils.ScreenUtility;
import com.wishhard.nf.numbersfacts.views.FactSearchButton;
import com.wishhard.nf.numbersfacts.views.Wheel;

import java.util.Arrays;

public class MonthTabFrag extends Fragment {

    private RelativeLayout mLayout;
    private AppCompatTextView factTv,searchTitleTv;

    private LinearLayout monthbox;

    private Wheel monthWheel,dayWheel;
    private FrameLayout loaging;
    private TabDataListener mTabDataListener;
    private   FactSearchButton b;

    private InterstitialAd mInterstitialAd;

    int month,day;

    private final BroadcastReceiver mReceiver =new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            NpItem npItem = intent.getParcelableExtra(NumbersApiService.ACTION_NUMBER_API_PAYLOAD);
            String errror = intent.getStringExtra(NumbersApiService.ACTION_ERROR_FROM_NNMB_API);

            showHideLoading(false);
            if(npItem == null) {
                if(searchTitleTv.getText().toString().isEmpty() && factTv.getText().toString().isEmpty()
                        && errror.equals(NumbersApiService.ERROR_FROM_TAB[1])) {
                    factTv.setText("Error!");
                }
            } else {
                if (npItem.isFound() && npItem.getType().equals("date")) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    searchTitleTv.setText(monthWheel.getSelectedItem() + " " + dayWheel.getSelectedItem());
                    factTv.setText(npItem.getFact());
                    upDateTabListener();
                } else if (!npItem.isFound() && npItem.getType().equals("date")) {
                    searchTitleTv.setText("");
                    factTv.setText("No fact found for " + monthWheel.getSelectedItem() + " " + dayWheel.getSelectedItem());
                    upDateTabListener();
                }

            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ScreenUtility.initContext(getActivity());

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.nf_au2));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if(mLayout == null) {
            mLayout = (RelativeLayout) inflater.inflate(R.layout.month_tab_layout,container,false);
        }

        loaging = mLayout.findViewById(R.id.loading_frame);
        loaging.setOnClickListener(null);

        monthbox = mLayout.findViewById(R.id.monthbox);
        setMonthBox();

        monthWheel = mLayout.findViewById(R.id.monthwheel);
        monthWheel.setItems(Arrays.asList(MonthsAndDaysConst.MONTH_PARAM));
        monthWheel.setWheelListener(new Wheel.WheelListener() {
            @Override
            public void onSelected(String item, int index) {
                month = index+1;
                dayWheel.updateDaysInMonth(item);
            }
        });

        dayWheel = mLayout.findViewById(R.id.daywheel);
        dayWheel.setItems(Arrays.asList(MonthsAndDaysConst.DAY_PARAM));
        dayWheel.setWheelListener(new Wheel.WheelListener() {
            @Override
            public void onSelected(String item, int index) {
                day = index+1;
            }
        });

        searchTitleTv = mLayout.findViewById(R.id.searchTitle);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,setMargins(2.6f),0,0);
        lp.addRule(RelativeLayout.BELOW,R.id.monthbox);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        searchTitleTv.setLayoutParams(lp);
        factTv = mLayout.findViewById(R.id.factTv);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(0,setMargins(4.5f),0,0);
        lp2.addRule(RelativeLayout.BELOW,R.id.searchTitle);
        lp2.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        factTv.setLayoutParams(lp2);


        b = mLayout.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connectivity.isConnected(getActivity())) {

                    ((NumberFactActivity)getActivity()).clearPrefs();

                    searchTitleTv.setText("");
                    factTv.setText("");
                    ((NumberFactActivity) getActivity()).showHideFab(false);

                    b.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent i = new Intent(getActivity(), NumbersApiService.class);
                            NumbersApiUrlHelper numbersApiUrlHelper = new NumbersApiUrlHelper();
                            numbersApiUrlHelper.setType(NumbersApiUrlHelper.TYPE_DATE);
                            month = (month == 0)? 1:month;
                            day = (day == 0)? 1:day;
                            numbersApiUrlHelper.setMonthAndDay(month, day);
                            i.putExtra(NumbersApiService.NUMBERS_API_URI_ACTION, numbersApiUrlHelper);
                            i.putExtra(NumbersApiService.ACTION_ERROR_FROM_NNMB_API,NumbersApiService.ERROR_FROM_TAB[1]);
                            getActivity().startService(i);
                        }
                    },800);
                    showHideLoading(true);
                } else {
                    upDateTabListener();
                    AMSnackbar amSnackbar = new AMSnackbar.Builder()
                            .make(mLayout)
                            .message("No network connection")
                            .messageTextColor(Color.WHITE)
                            .backgroundColor(Color.RED)
                            .duration(Snackbar.LENGTH_SHORT)
                            .build();
                    amSnackbar.show();
                }
            }
        });

        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mReceiver,
                        new IntentFilter(NumbersApiService.MY_SERVICE_MESSAGE));

        return mLayout;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            upDateTabListener();
            if (!isVisibleToUser) {
                KeyboardUtil.hideSoftKeyboard(getActivity());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext())
                .unregisterReceiver(mReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTabDataListener = (TabDataListener) context;
    }

    @Override
    public void onDetach() {
        mTabDataListener = null;
        super.onDetach();
    }


    private void showHideLoading(boolean show) {
        if(show) {
            loaging.setVisibility(View.VISIBLE);
        } else {
            loaging.setVisibility(View.GONE);
        }
    }

    private void setMonthBox() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,setMargins(3f),0,0);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        monthbox.setLayoutParams(lp);
    }


    private void upDateTabListener() {
        if(mTabDataListener != null) {
            mTabDataListener.currentTabData(1,searchTitleTv.getText().toString(),
                    factTv.getText().toString());
        }
    }

    private int setMargins(float p) {
        return (int) ((ScreenUtility.getWidthInPx()*p)/100.0f);
    }
}
