package com.example.khahani.asa.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.example.khahani.asa.AsaApplication;

/**
 * Created by huangmuhua on 2017/11/5.
 */

public class FontManager {

    private Typeface persianFont;
    private Typeface persianFontBold;
    private Typeface persianFontNumber;

    private FontManager() {
        AssetManager assetManager = AsaApplication.getInstance().getAssets();
        persianFont = Typeface.createFromAsset(assetManager, "fonts/IRANSans-Bold-web.ttf");
        persianFontBold = Typeface.createFromAsset(assetManager, "fonts/IRANSans-Bold-web.ttf");
        persianFontNumber = Typeface.createFromAsset(assetManager, "fonts/vazir_persian_digits.ttf");
    }

    private static FontManager sInstance = new FontManager();

    public static FontManager getInstance() {
        return sInstance;
    }

    public Typeface getPersianFont() {
        return persianFont;
    }

    public Typeface getPersianFontBold() {
        return persianFontBold;
    }

    public Typeface getPersianFontNumber() {
        return persianFontNumber;
    }
}
