package com.brac.bracebatra.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Event;
import com.brac.bracebatra.model.Institute;
import com.brac.bracebatra.model.News;
import com.brac.bracebatra.ui.adapter.NewsAdapter;
import com.brac.bracebatra.ui.adapter.SchoolAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    List<News> newsList = new ArrayList<News>();
    RecyclerView recyclerView;
    NewsAdapter adapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                newsDeleteAndFillRequest();
                newsList.clear();
                adapter.notifyDataSetChanged();


                break;
            default:
                break;
        }

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        adapter = new NewsAdapter(newsList, getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.rv_news);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setAdapter(adapter);

        addData();
    }

    private void addData() {

        List<News> events = new Select().from(News.class).execute();

        newsList.addAll(events);
        adapter.notifyDataSetChanged();
    }

    public void newsDeleteAndFillRequest() {

        //deleteing event in refresh
        new Delete().from(News.class).execute();

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
                                event.save();

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addData();
                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue rq = Volley.newRequestQueue(NewsActivity.this);
        rq.add(sr);
    }
}
