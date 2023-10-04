package com.gharpeshiksha.tutorapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefConfig {


    private final SharedPreferences preferences;
    private final Context context;

    public PrefConfig(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status), status);
        editor.apply();
    }

    public boolean readLoginStatus() {
        return preferences.getBoolean(context.getString(R.string.pref_login_status), true);
    }


    public void writeSort(String sort) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_sort), sort);
        editor.apply();
    }

    public String readSort() {
        return preferences.getString(context.getString(R.string.pref_sort), "recency");
    }
}
