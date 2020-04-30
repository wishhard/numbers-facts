package com.wishhard.nf.numbersfacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.androidmads.amsnackbarlib.AMSnackbar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.wishhard.nf.numbersfacts.adapters.ImgListAdopter;
import com.wishhard.nf.numbersfacts.pojos.ImgLinksPojo;
import com.wishhard.nf.numbersfacts.services.PixaBayService;
import com.wishhard.nf.numbersfacts.utils.Connectivity;
import com.wishhard.nf.numbersfacts.utils.KeyboardUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PixelBayActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener{

    private List<ImgLinksPojo> items = new ArrayList<>();

    private MaterialSearchBar searchBar;

    private FrameLayout loading;

    private RecyclerView recyclerView;
    private ImgListAdopter adopter;

    private InterstitialAd mInterstitialAd;

    private final BroadcastReceiver mReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ImgLinksPojo[] ia = (ImgLinksPojo[]) intent.getParcelableArrayExtra(PixaBayService.ACTION_PIXABAY_PAYLOAD);
            String error = intent.getStringExtra(PixaBayService.ACTION_PIXABAY_EROR);

            if(ia.length == 0 && error != null) {
                showHideLoading(false);
                Toast.makeText(PixelBayActivity.this, "Error!", Toast.LENGTH_LONG).show();
            } else {
                if(ia.length == 0) {
                    showHideLoading(false);
                    Toast.makeText(PixelBayActivity.this, "Sorry found nothing.", Toast.LENGTH_LONG).show();
                } else {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    items = Arrays.asList(ia);
                    showHideLoading(false);
                    setAdopter();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_bay);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        searchBar =  findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);

        recyclerView = findViewById(R.id.cycle);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.nf_au1));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        loading = findViewById(R.id.loading_frame);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver,
                        new IntentFilter(PixaBayService.MY_SERVICE_MESSAGE));


    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        String sq = text.toString();

        if(Connectivity.isConnected(this)) {
            if (StringUtils.isAllEmpty(sq) || StringUtils.isAllBlank(sq)) {

            } else {
                KeyboardUtil.hideSoftKeyboard(PixelBayActivity.this);
                startPixabayService(sq);
            }
        } else {
            AMSnackbar amSnackbar = new AMSnackbar.Builder()
                    .make(findViewById(android.R.id.content))
                    .message("No network connection")
                    .messageTextColor(Color.WHITE)
                    .backgroundColor(Color.RED)
                    .duration(Snackbar.LENGTH_SHORT)
                    .build();
            amSnackbar.show();
        }
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mReceiver);
    }


    private void setAdopter() {
        adopter = new ImgListAdopter(this, items);
        recyclerView.setAdapter(adopter);
    }



    private void startPixabayService(String sq) {
        sq = StringUtils.normalizeSpace(sq);
        try {
            sq = URLEncoder.encode(sq,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(this, PixaBayService.class);
        i.putExtra(PixaBayService.ACTION_PB_SQ,sq);
        startService(i);
        showHideLoading(true);
    }

    private void showHideLoading(boolean show) {
        if(show) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }
}
