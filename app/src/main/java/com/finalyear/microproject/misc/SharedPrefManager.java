package com.finalyear.microproject.misc;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    static SharedPreferences sharedPreferences;
    static Context mContext;

    static int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "CartId";

    static SharedPreferences.Editor editor;

    public SharedPrefManager (Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void saveUUID(Context context, String uid){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uuid", uid);
        editor.commit();
    }

    public String getUUID(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("uuid", "");
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}