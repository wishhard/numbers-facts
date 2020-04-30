package com.wishhard.nf.numbersfacts.tabfrags;

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
import android.widget.RelativeLayout;


import com.androidmads.amsnackbarlib.AMSnackbar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.infideap.blockedittext.BlockEditText;
import com.wishhard.nf.numbersfacts.NumberFactActivity;
import com.wishhard.nf.numbersfacts.R;
import com.wishhard.nf.numbersfacts.interfaces.TabDataListener;
import com.wishhard.nf.numbersfacts.pojos.NpItem;
import com.wishhard.nf.numbersfacts.services.NumbersApiService;
import com.wishhard.nf.numbersfacts.utils.Connectivity;
import com.wishhard.nf.numbersfacts.utils.KeyboardUtil;
import com.wishhard.nf.numbersfacts.utils.NumbersApiUrlHelper;
import com.wishhard.nf.numbersfacts.utils.ScreenUtility;
import com.wishhard.nf.numbersfacts.views.FactSearchButton;

import org.apache.commons.lang3.StringUtils;

public class MathTabFrag extends Fragment {

    private RelativeLayout mLayout;
    private FrameLayout loaging;
    private BlockEditText mBlockEditText;
    private AppCompatTextView factTv,searchTitleTv;

    private TabDataListener mTabDataListener;

    private InterstitialAd mInterstitialAd;

    private  String sq;

    private final BroadcastReceiver mReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NpItem npItem = intent.getParcelableExtra(NumbersApiService.ACTION_NUMBER_API_PAYLOAD);
            String errror = intent.getStringExtra(NumbersApiService.ACTION_ERROR_FROM_NNMB_API);

            showHideLoading(false);
            if(npItem == null) {
                if(searchTitleTv.getText().toString().isEmpty() && factTv.getText().toString().isEmpty()
                        && errror.equals(NumbersApiService.ERROR_FROM_TAB[0])) {
                    factTv.setText("Error!");
                }
            } else {
                if (npItem.isFound() && npItem.getType().equals("math")) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    searchTitleTv.setText(sq);
                    factTv.setText(npItem.getFact());
                    upDateTabListener();
                } else if (!npItem.isFound() && npItem.getType().equals("math")) {
                    searchTitleTv.setText("");
                    factTv.setText("No fact found for " + sq);
                    upDateTabListener();
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            mLayout = (RelativeLayout) inflater.inflate(R.layout.math_and_month_tab_layout,container,false);
        }

        loaging = mLayout.findViewById(R.id.loading_frame);
        loaging.setOnClickListener(null);

        mBlockEditText = mLayout.findViewById(R.id.blockEditText_year_math);
        mBlockEditText.setDefaultLength(6);
        mBlockEditText.setNumberOfBlock(1);
        mBlockEditText.setHint(getActivity().getResources().getString(R.string.math_tab_hint));

        FactSearchButton b = mLayout.findViewById(R.id.button);

        searchTitleTv = mLayout.findViewById(R.id.searchTitle);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,setMargins(2.6f),0,0);
        lp.addRule(RelativeLayout.BELOW,R.id.input_sesion);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        searchTitleTv.setLayoutParams(lp);
        factTv = mLayout.findViewById(R.id.factTv);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(0,setMargins(4.5f),0,0);
        lp2.addRule(RelativeLayout.BELOW,R.id.searchTitle);
        lp2.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        factTv.setLayoutParams(lp2);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sq = mBlockEditText.getText();

                if(Connectivity.isConnected(getActivity())) {

                    if (StringUtils.isAllEmpty(sq) || StringUtils.isAllBlank(sq)) {

                    } else {

                        ((NumberFactActivity)getActivity()).clearPrefs();

                        deleteFirstZeros();

                        searchTitleTv.setText("");
                        factTv.setText("");
                        ((NumberFactActivity) getActivity()).showHideFab(false);

                        NumbersApiUrlHelper numbHelper = new NumbersApiUrlHelper();
                        numbHelper.setType(NumbersApiUrlHelper.TYPE_MATH);
                        numbHelper.setMath(Integer.parseInt(sq));

                        Intent i = new Intent(getActivity(), NumbersApiService.class);
                        i.putExtra(NumbersApiService.NUMBERS_API_URI_ACTION, numbHelper);
                        i.putExtra(NumbersApiService.ACTION_ERROR_FROM_NNMB_API,NumbersApiService.ERROR_FROM_TAB[0]);
                        getActivity().startService(i);
                        KeyboardUtil.hideSoftKeyboard(getActivity());
                        showHideLoading(true);
                    }
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
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        KeyboardUtil.hideSoftKeyboard(getActivity());
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

    private void upDateTabListener() {
        if(mTabDataListener != null) {
            mTabDataListener.currentTabData(0,searchTitleTv.getText().toString(),
                    factTv.getText().toString());
        }
    }

    private String deleteFirstZeros() {

        if(sq.length() > 0) {
            int s = 0;
            while (s < sq.length()) {
                if (sq.charAt(s) != '0' || s == sq.length() - 1) {
                    break;
                }
                s++;
            }

            return sq = StringUtils.substring(sq, s, sq.length());
        }

        return sq;
    }

    private int setMargins(float p) {
        return (int) ((ScreenUtility.getWidthInPx()*p)/100.0f);
    }
}
