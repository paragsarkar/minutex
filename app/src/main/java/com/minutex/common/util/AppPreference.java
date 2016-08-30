package com.minutex.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class AppPreference {
    private static SharedPreferences mSharedPreference;
    private static AppPreference mInstance = new AppPreference();

    public static AppPreference getInstance(Context mContext)
    {
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        return mInstance;
    }
    public void setString(String key,String value)
    {
        mSharedPreference.edit().putString(key,value).apply();
    }
    public String getString(String key)
    {
        return mSharedPreference.getString(key,null);
    }

}
