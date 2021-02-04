package com.u2tzjtne.activitylauncher;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 *
 * @author JK
 * @date 2017/10/9
 */
public class SPUtils {

    private static final SharedPreferences SP = App.getContext()
            .getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
    private static final SharedPreferences.Editor EDIT = SP.edit();

    public static void putBoolean(String key, boolean value) {
        EDIT.putBoolean(key, value);
        EDIT.apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return SP.getBoolean(key, defValue);
    }

    public static void putInt(String key, int value) {
        EDIT.putInt(key, value);
        EDIT.apply();
    }

    public static int getInt(String key, int defValue) {
        return SP.getInt(key, defValue);
    }

    public static void putString(String key, String value) {
        EDIT.putString(key, value);
        EDIT.apply();
    }

    public static String getString(String key, String defValue) {
        return SP.getString(key, defValue);
    }
}
