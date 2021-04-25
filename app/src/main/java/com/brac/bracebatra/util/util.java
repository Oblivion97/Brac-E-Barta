package com.brac.bracebatra.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;

/**
 * Created by hhson on 8/11/2017.
 */
public class util {

    public static String URL_LOGIN = "http://bepmis.brac.net/rest/auth/login";
    public static String URL_STUDENT = "http://bepmis.brac.net/rest/student?start=0";
    public static String URL_INSTITUTE = "http://bepmis.brac.net/rest/institute?start=1";
    public static String CURRENT_PO ;
    public static String CURRENT_PO_PASS ;
    public static String STR_USER = "currentUser" ;
    public static String STR_PASS = "currentPassword";

    public static void saveToPreference(String tag , String value , Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(tag, value);
        editor.commit();
    }

    public static String getFromPreference(String tag , String value , Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  preferences.getString(tag, value);
    }
    public static boolean isNotEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return true;

        return false;
    }
}
