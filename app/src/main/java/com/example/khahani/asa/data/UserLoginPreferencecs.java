package com.example.khahani.asa.data;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLoginPreferencecs {

    public static final String PREF_FILE_NAME = "userlogin";
    public static final String PREF_USERNAME = "userlogin";
    public static final String PREF_PERSIAN_NAME = "perisan_name";
    public static final String PREF_PANEL_RESERVE_EXTRA= "panel_reserve_extra";



    public static void write(Context context, String username, String persian_name, String panel_reserve_extra,
                             SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener){

        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().commit();
        sharedPref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        editor.putString(PREF_USERNAME, username);
        editor.putString(PREF_PERSIAN_NAME, persian_name);
        editor.putString(PREF_PANEL_RESERVE_EXTRA, panel_reserve_extra);

        editor.apply();

    }

    public static String read(Context context, String itemName){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(itemName, "");
    }

}
