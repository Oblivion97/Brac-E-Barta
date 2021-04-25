package com.brac.bracebatra.util;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


import com.brac.bracebatra.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by BlackFlag on 1/21/2018.
 */

public class CalendarContentResolver {
    public static final String[] FIELDS = {
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_COLOR,
            CalendarContract.Calendars.VISIBLE
    };

    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");

    ContentResolver contentResolver;
    Map<String,String> calendars = new HashMap<String,String>();

    public CalendarContentResolver(Context ctx) {
        contentResolver = ctx.getContentResolver();
    }



    public Map<String,String> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        calendars.clear();

        Log.d("cnameslength",""+CNames.length);
        if (CNames.length==0)
        {
            Toast.makeText(context,"No event exists in calendar",Toast.LENGTH_LONG).show();
        }
        for (int i = 0; i < CNames.length; i++) {

            calendars.put(cursor.getString(1),"added");
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();



        }
        return calendars;
    }



}
