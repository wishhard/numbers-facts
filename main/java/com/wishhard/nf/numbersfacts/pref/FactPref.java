package com.wishhard.nf.numbersfacts.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class FactPref {

    private static final String TIME_PREF_KEY = "com.wishhard.cm.clickermania.pref_time_pref_key";

    private static FactPref mGamePref;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    private FactPref(Context context) {
        mContext = context;

        mSharedPreferences = mContext.getSharedPreferences(TIME_PREF_KEY,Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public static synchronized FactPref getInstance(Context context) {

        if(mGamePref == null) {
            mGamePref =  new FactPref(context.getApplicationContext());
        }
        return mGamePref;
    }

    public void setValue(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.commit();
    }

    public String getStringValue(String key,String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public void clear() {
        mSharedPreferencesEditor.clear().commit();
    }

}
