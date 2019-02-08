package com.example.khahani.asa;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AsaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iransans_farnum.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
