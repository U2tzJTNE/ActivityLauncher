package com.u2tzjtne.activitylauncher;

import android.app.Application;
import android.content.Context;

/**
 * @author u2tzjtne@gmail.com
 * @date 2020/6/3
 */
public class App extends Application {
    private static App instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
            context = getApplicationContext();
        }
    }

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }
}
