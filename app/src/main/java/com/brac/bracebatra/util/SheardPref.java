package com.brac.bracebatra.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BlackFlag on 1/8/2018.
 */

public class SheardPref {

    public  static void putKey(Context context, String key, boolean val)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("BarcPref1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key,val);
        editor.commit();
    }
    public static  boolean getBoolenKey(Context context,String key)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("BarcPref1", Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(key, false); // getting String
    }
    public  static void putKey(Context context, String key, String val)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("BarcPref1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key,val);
        editor.commit();
    }
    public static  String getKey(Context context,String key)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("BarcPref1", Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, ""); // getting String
    }
}
