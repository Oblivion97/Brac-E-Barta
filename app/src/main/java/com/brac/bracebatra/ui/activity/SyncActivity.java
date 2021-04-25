package com.brac.bracebatra.ui.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.brac.bracebatra.R;
import com.brac.bracebatra.model.News;
import com.brac.bracebatra.model.Sync;
import com.brac.bracebatra.ui.adapter.NewsAdapter;
import com.brac.bracebatra.ui.adapter.SyncAdapter;
import com.brac.bracebatra.util.IOnSyncListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncActivity extends AppCompatActivity implements IOnSyncListener {
    List<Sync> syncList = new ArrayList<Sync>();
    RecyclerView recyclerView;
    SyncAdapter adapter;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysc);

        fab = (FloatingActionButton) findViewById(R.id.fab_sync);


        adapter = new SyncAdapter(syncList, getApplicationContext(),this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_sync);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setAdapter(adapter);

        addItem();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i =0 ;i< syncList.size();i++){
                    try {
                        Sync sync = syncList.get(i);
                        if(!sync.isSynced)
                            syncDataToWeb(sync,new JSONArray(syncList.get(i).attendence));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void addItem() {

        // ready from DB

        List<Sync> sync = new Select().from(Sync.class).execute();


        if (sync.size() > 0) {
           for (Sync a:sync)
           {

               syncList.add(a);

           }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void syncDataToWeb(final Sync sync, JSONArray jsonArray) {
        JSONArray sendArray=new JSONArray();
        try{
            Log.d("LoadData",jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject stdOb = jsonArray.getJSONObject(i);
                JSONObject obj=new JSONObject();
                obj.put("instituteId",stdOb.optString("instituteId"));
                obj.put("gradeId","47272800-4094-grad-b8a4-924ff12a4bfd");
                obj.put("studentId",stdOb.optString("studentId"));
                obj.put("attendDate",stdOb.optString("attendDate"));
                obj.put("attend",stdOb.optString("attend"));
                sendArray.put(obj);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonArrayRequest jreq = new JsonArrayRequest(Request.Method.POST,"http://bepmis.brac.net/rest/apps/attendance-report/save?uname=kholilur&passw=123456",sendArray,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Res",response.toString());
                        Toast.makeText(SyncActivity.this, "Updated"+response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley error","error"+error.getMessage());
                if(error.getMessage().contains("Successful"))
                {
                    try{
                        List<Sync> students = new Select().from(Sync.class).where("Title = ? AND date = ? AND schoolId = ?", sync.Title, sync.date,sync.schoolId).execute();
                        Long upateId=students.get(0).getId();
                        Sync sync = Sync.load(Sync.class, upateId);

                        sync.isSynced = true;
                        sync.save();
                        /*new Delete().from(Sync.class).where("schoolId = ?", sync.schoolId).execute();
                        sync.isSynced=true;
                        sync.save();*/
                        adapter.notifyDataSetChanged();
                        //Toast.makeText(SyncActivity.this, "Data Sync Sucssfull", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    
                }
                else
                {
                    Toast.makeText(SyncActivity.this, "Cannot send the requst to server", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(jreq);








    }
}
