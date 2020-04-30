package com.wishhard.nf.numbersfacts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.leinardi.android.speeddial.SpeedDialView;
import com.wishhard.nf.numbersfacts.adapters.SectionsPageAdapter;
import com.wishhard.nf.numbersfacts.interfaces.TabDataListener;
import com.wishhard.nf.numbersfacts.pref.FactPref;
import com.wishhard.nf.numbersfacts.services.AppStickyService;
import com.wishhard.nf.numbersfacts.tabfrags.InfoFrag;
import com.wishhard.nf.numbersfacts.tabfrags.MathTabFrag;
import com.wishhard.nf.numbersfacts.tabfrags.MonthTabFrag;
import com.wishhard.nf.numbersfacts.tabfrags.YearTabFrag;
import com.wishhard.nf.numbersfacts.utils.KeyboardUtil;
import com.wishhard.nf.numbersfacts.utils.ScreenUtility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class NumberFactActivity extends AppCompatActivity implements TabDataListener {

    public static final String FOR_SHARE_TITLE = "fact_title_from_fact_screen";
    public static final String FOR_SHARE_FACT = "fact_from_fact_screen";

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    private InterstitialAd mInterstitialAd;

    private Toolbar toolbar;

    private String title,fact;

    private SpeedDialView fab;
    private FrameLayout mf;

    private FactPref pref;

    private InfoFrag infof;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_fact);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mViewPager = findViewById(R.id.container);
        setUpViewPager(mViewPager);

        pref = FactPref.getInstance(this);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);

        infof = new InfoFrag();

        mf = findViewById(R.id.info_cont);


        ScreenUtility.initContext(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MobileAds.initialize(this, "ca-app-pub-3133582463179859~6077279804");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.nf_au1));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });




        fab = findViewById(R.id.speedDial);
        fab.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                if(!title.isEmpty()) {

                    Intent i = new Intent(NumberFactActivity.this, ShareFactActivity.class);
                    i.putExtra(FOR_SHARE_TITLE, title);
                    i.putExtra(FOR_SHARE_FACT, fact);
                    startActivity(i);

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }



                }
                return false;
            }

            @Override
            public void onToggleChanged(boolean isOpen) {

            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                showHideFab(false);
                showHideInfo(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Intent i = new Intent(this,AppStickyService.class);
        startService(i);
    }

    @Override
    public void onBackPressed() {

        if(infof.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(infof).commit();
        } else {
            pref.clear();
            try {
                deleteCaches();
            } catch (IOException e) {

            }
            Intent i = new Intent(this, AppStickyService.class);
            stopService(i);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_info) {
            showHideInfo(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpViewPager(ViewPager viewPager) {
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mSectionsPageAdapter.addFragment(new MathTabFrag(),getString(R.string.mathTab));
        mSectionsPageAdapter.addFragment(new MonthTabFrag(),getString(R.string.monthTab));
        mSectionsPageAdapter.addFragment(new YearTabFrag(),getString(R.string.yearTab));
        viewPager.setAdapter(mSectionsPageAdapter);
    }

    public void showHideFab(boolean show) {
        if (show) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    private void deleteCaches() throws IOException {
        File dir = this.getCacheDir();
        List<File> children = (List<File>) FileUtils.listFilesAndDirs(dir,
                TrueFileFilter.INSTANCE,TrueFileFilter.INSTANCE);
        for (int i = children.size()-1; i >= 0;i--) {
            if(i == 0) {
                break;
            }
            FileUtils.deleteQuietly(children.get(i));
        }
    }

    @Override
    public void currentTabData(int pos, String title, String fact) {

        if(!title.isEmpty()) {
            this.title = title;
            this.fact = fact;
            showHideFab(true);
        } else {
            this.title = "";
            this.fact = "";
            showHideFab(false);
        }
    }


    public void showHideInfo(boolean show) {

        if(show) {
            if(!infof.isAdded()) {
                getSupportFragmentManager().beginTransaction().add(mf.getId(), infof).commit();
                KeyboardUtil.hideSoftKeyboard(this);
            }
        } else {
            if(infof.isAdded()) {
                getSupportFragmentManager().beginTransaction().remove(infof).commit();
            }
        }
    }

    public void clearPrefs() {
        pref.clear();
    }
}
