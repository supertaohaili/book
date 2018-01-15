package com.thl.reader;

import android.content.Context;

import com.thl.reader.util.PageFactory;

import org.litepal.LitePalApplication;


public class AppContext extends LitePalApplication {
    public static volatile Context applicationContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        LitePalApplication.initialize(this);
        Config.createConfig(this);
        PageFactory.createPageFactory(this);
    }

}
