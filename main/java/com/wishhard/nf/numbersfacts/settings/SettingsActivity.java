package com.wishhard.nf.numbersfacts.settings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.wishhard.nf.numbersfacts.R;
import com.wishhard.nf.numbersfacts.ShareFactActivity;

public class SettingsActivity extends AppCompatActivity {
    public static final String FACT_SCREEN_ORIENTATION_KEY = "com.wishhard.settings_fact_screen_orientation";
    public static final String SHOW_FACT_WITH_IMG_KEY = "com.wishhard.settings_show_fact_with_image";

    private ActionBar mActionBar;
    private ColorDrawable actionBarColor = new ColorDrawable(Color.parseColor("#616161"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActionBar = getSupportActionBar();
        actionBarSettings();

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFrag()).commit();


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,ShareFactActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
       // finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actionBarSettings() {
        mActionBar.setTitle("Setting");
        mActionBar.setBackgroundDrawable(actionBarColor);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_up_action);
        mActionBar.setHomeButtonEnabled(true);
    }


    public static class SettingsFrag extends PreferenceFragmentCompat {
        private CustomSwitchPref bgMusicPref,soundEffectsPref;
        private ContextThemeWrapper themeWrapper;




        @Override
        public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
            Context activityContext = getActivity();

            PreferenceScreen prefScreen = getPreferenceManager().createPreferenceScreen(activityContext);
            setPreferenceScreen(prefScreen);


            TypedValue themeTypedValue = new TypedValue();
            activityContext.getTheme().resolveAttribute(R.attr.preferenceTheme, themeTypedValue, true);

            themeWrapper = new ContextThemeWrapper(activityContext, themeTypedValue.resourceId);

            bgMusicPref = new CustomSwitchPref(themeWrapper);
            bgMusicPref.setKey(FACT_SCREEN_ORIENTATION_KEY);
            bgMusicPref.setTitle(R.string.fact_screen_orientation_pref_title);
            bgMusicPref.setDefaultValue(false);
            bgMusicPref.setLayoutResource(R.layout.pref_layout);

            soundEffectsPref = new CustomSwitchPref(themeWrapper);
            soundEffectsPref.setKey(SHOW_FACT_WITH_IMG_KEY);
            soundEffectsPref.setTitle(R.string.show_img_with_fact_pref_title);
            soundEffectsPref.setDefaultValue(false);
            soundEffectsPref.setLayoutResource(R.layout.pref_layout);

            prefScreen.addPreference(bgMusicPref);

            bgMusicPref.setSummary(prefSummary(FACT_SCREEN_ORIENTATION_KEY, R.string.fso_pref_checked_sumary,
                    R.string.fso_pref_unchecked_sumary));

            prefScreen.addPreference(soundEffectsPref);
            soundEffectsPref.setSummary(prefSummary(SHOW_FACT_WITH_IMG_KEY,R.string.siwf_pref_checked_sumary,
                    R.string.siwf_pref_unchecked_sumary));


        }

        @Override
        public RecyclerView onCreateRecyclerView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            RecyclerView r = (RecyclerView) inflater.inflate(R.layout.pref_recycler_view_layout,parent,false);
            r.setLayoutManager(onCreateLayoutManager());
            r.addItemDecoration(new DividerDecoration(getActivity()));
            return r;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            getListView().setBackgroundColor(Color.BLACK);
        }

        @Override
        public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
            super.onViewStateRestored(savedInstanceState);

             bgMusicPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                 @Override
                 public boolean onPreferenceChange(Preference preference, Object o) {
                     spUpdate(FACT_SCREEN_ORIENTATION_KEY,o);
                     preference.setSummary(prefSummary(FACT_SCREEN_ORIENTATION_KEY,R.string.fso_pref_checked_sumary,
                             R.string.fso_pref_unchecked_sumary));
                     return true;
                 }
             });

             soundEffectsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                 @Override
                 public boolean onPreferenceChange(Preference preference, Object o) {
                     spUpdate(SHOW_FACT_WITH_IMG_KEY,o);
                     preference.setSummary(prefSummary(SHOW_FACT_WITH_IMG_KEY,R.string.siwf_pref_checked_sumary,
                             R.string.siwf_pref_unchecked_sumary));
                     return true;
                 }
             });

        }

        private int prefSummary(String key, int checkedSummaryRes, int unCheckedSummaryRes) {

            Preference p = findPreference(key);
            if(p.getSharedPreferences().getBoolean(key,false)) {
                return checkedSummaryRes;
            }
            return unCheckedSummaryRes;

        }

        private void spUpdate(String prefKey,Object nv) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(prefKey, (Boolean) nv);
            editor.commit();
        }
    }
}
