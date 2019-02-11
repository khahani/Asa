package com.example.khahani.asa;

import android.app.Application;
import android.os.Handler;

import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AsaApplication extends Application {

    private static AsaApplication instance;
    private Handler mHandler;
    private IDAL idal;

    public static AsaApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        mHandler = new Handler();

        try {
            idal = NeptuneLiteUser.getInstance().getDal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iransans_farnum.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public void runOnUiThread(Runnable r) {
        mHandler.post(r);
    }

    public void runOnUiThreadDelayed(Runnable r, long millis) {
        mHandler.postDelayed(r, millis);
    }

    public IDAL getIdal() {
        return idal;
    }
}
