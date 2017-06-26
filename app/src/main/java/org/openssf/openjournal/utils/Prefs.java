package org.openssf.openjournal.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context _context;

    // Mode
    private final int PRIVATE_MODE = 0;

    // File Name
    private static final String PREF_NAME = "app";
    private static final String IS_FIRST_TIME = "IsFirstTime";

    @SuppressLint("CommitPrefEdits") // Required as per function structure
    public Prefs(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME, true);
    }

}
