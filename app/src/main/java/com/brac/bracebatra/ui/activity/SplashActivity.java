package com.brac.bracebatra.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.brac.bracebatra.R;
import com.brac.bracebatra.app.Config;
import com.brac.bracebatra.model.Event;
import com.brac.bracebatra.model.Institute;
import com.brac.bracebatra.model.News;
import com.brac.bracebatra.model.Student;
import com.brac.bracebatra.model.Sync;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(Institute.class , Student.class, Sync.class , News.class , Event.class);
        ActiveAndroid.initialize(config.create());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        newsRequest();
        eventRequest();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));

            }
        }, 600);
    }

    public void eventRequest() {

        String url = "http://bepmis.brac.net/rest/apps/events/list?uname=kholilur&passw=123456";

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jObject = new JSONObject(response);

                            Log.d("response INSTITUTE", response);
                            JSONArray jsonArray = jObject.getJSONArray("model");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Event event = new Event();

                                event.setEventId(jsonObject.getString("id"));
                                event.setTitle(jsonObject.getString("title"));
                                event.setDetails(jsonObject.getString("description"));
                                event.setLocation(jsonObject.getString("eventPlace"));
                                event.setDateTime(jsonObject.getString("eventDate"));
                                event.setOrgBy(jsonObject.getString("eventOrganizer"));
                                event.setKeynoteSpeaker(jsonObject.getString("keynoteSpeaker"));

                                List<Event> events = new Select().from(Event.class)
                                        .where("eventId = ?",jsonObject.getString("id")).execute();
                                if(events.size()==0){
                                    event.save();
                                }

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue rq = Volley.newRequestQueue(SplashActivity.this);
        rq.add(sr);
    }

    public void newsRequest() {

        String url = "http://bepmis.brac.net/rest/apps/news/list?uname=kholilur&passw=123456";

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jObject = new JSONObject(response);

                            Log.d("response INSTITUTE", response);
                            JSONArray jsonArray = jObject.getJSONArray("model");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                News event = new News();

                                event.setnTitle(jsonObject.getString("title"));
                                event.setnBody(jsonObject.getString("description"));
                                event.setnDate(jsonObject.getString("publishedDate"));
                                event.setnPostBy(jsonObject.getString("tag"));
                                event.setnId(jsonObject.getString("id"));
                                List<News> events = new Select().from(News.class)
                                        .where("nId = ?",jsonObject.getString("id")).execute();
                                if(events.size()==0){
                                    event.save();
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue rq = Volley.newRequestQueue(SplashActivity.this);
        rq.add(sr);
    }
}
