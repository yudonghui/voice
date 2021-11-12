package com.ydh.voicechange;

import android.app.Application;
import android.content.Context;

/**
 * Created by ydh on 2021/11/11
 */
public class BaseApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static Context getContext() {
        return context;
    }
}
