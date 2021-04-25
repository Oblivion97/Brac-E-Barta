package com.brac.bracebatra.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.brac.bracebatra.R;
import com.brac.bracebatra.util.util;

public class MainActivity extends AppCompatActivity {

    boolean getPreferrence(String key,boolean defult)
    {
        SharedPreferences settings = getSharedPreferences("brac", 0);
        return settings.getBoolean(key, defult);

    }
    public void checkPermissions(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions(120, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                util.CURRENT_PO = util.getFromPreference(util.STR_USER, "Nil", getApplicationContext());
                util.CURRENT_PO_PASS = util.getFromPreference(util.STR_PASS, "Nil", getApplicationContext());

                if (util.getFromPreference("Login", "Nil", getApplicationContext()).equals("Nil")) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, ProgramOrganizerActivity.class));
                }



            }
        });
    }

    public void onAbout(View view) {
    }

    public void onContact(View view) {
    }

    public void onGalery(View view) {
    }

    public void onNews(View view) {
        startActivity(new Intent(MainActivity.this, NewsActivity.class));
    }

    public void onEvents(View view) {
        startActivity(new Intent(MainActivity.this, EventActivity.class));
    }
}
