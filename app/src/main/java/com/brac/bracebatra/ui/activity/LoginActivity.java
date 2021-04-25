package com.brac.bracebatra.ui.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;


import com.activeandroid.query.Select;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Institute;
import com.brac.bracebatra.model.Student;
import com.brac.bracebatra.model.Sync;
import com.brac.bracebatra.model.User;
import com.brac.bracebatra.ui.activity.HttpClient.WSManager;
import com.brac.bracebatra.ui.activity.HttpClient.WSResponseListener;
import com.brac.bracebatra.util.util;
import com.google.gson.Gson;


import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements WSResponseListener {

    private static final int REQUEST_READ_CONTACTS = 0;

    EditText etUsername;
    EditText etPassword;

    String userName;
    String password;
    private ProgressDialog progress;
    List<Institute> institutes;

    WSManager wsManager = new WSManager(LoginActivity.this, this);
    private final Gson mGson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data ..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);


        etUsername = (EditText) findViewById(R.id.et_login_id);
        etPassword = (EditText) findViewById(R.id.et_login_password);

        // Set up the login form.

        setPreference("updateDone",false);

        findViewById(R.id.btn_login_submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                if (util.isNotEmpty(etUsername) && util.isNotEmpty(etPassword)) {

                    userName = etUsername.getText().toString();
                    password = etPassword.getText().toString();

                    loginRequest();
                    // attemptLogin();
                } else {
                    Toast.makeText(LoginActivity.this, "Please Enter User ID & Password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void loginRequest() {

        final String URL = "http://bepmis.brac.net/rest/apps/login";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("password", password);

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.get("message").equals("Successful")) {

                                // Save to SP
                                util.saveToPreference(util.STR_USER, userName, getApplicationContext());
                                util.saveToPreference(util.STR_PASS, password, getApplicationContext());
                                util.saveToPreference("user_logedin","yes",getApplicationContext());

                                //
                                util.CURRENT_PO = util.getFromPreference(util.STR_USER, "Nil", getApplicationContext());
                                util.CURRENT_PO_PASS = util.getFromPreference(util.STR_PASS, "Nil", getApplicationContext());

                                Toast.makeText(LoginActivity.this, "Login Successful !" + util.CURRENT_PO, Toast.LENGTH_SHORT).show();

                                SharedPreferences settings = getSharedPreferences("brac", 0);
                                boolean isUpdatedNeed=settings.getBoolean("IsUpdate", true);

                                setPreference("user_login",true);

                                if(isUpdatedNeed)
                                {
                                    InstRequest();
                                }
                                else
                                {
                                    progress.dismiss();

                                    Toast.makeText(LoginActivity.this, "You are updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, ProgramOrganizerActivity.class));
                                    finish();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        RequestQueue rq = Volley.newRequestQueue(LoginActivity.this);
        rq.add(request_json);

    }

    boolean getPreferrence(String key,boolean defult)
    {
        SharedPreferences settings = getSharedPreferences("brac", 0);
        return settings.getBoolean(key, defult);

    }
    public void InstRequest() {

        SharedPreferences settings = getSharedPreferences("brac", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("IsUpdate", false);
        editor.apply();

        String url = "http://bepmis.brac.net/rest/apps/institute?uname="+userName+"&passw="+password;

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);

                            Log.d("response INSTITUTE", response);
                            JSONArray jsonArray = jObject.getJSONArray("model");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Institute institute = new Institute();

                                institute.setName(jsonObject.getString("name"));
                                institute.setEducationType(jsonObject.getString("instituteTypeName"));
                                institute.setPoId(jsonObject.getString("poUserName"));
                                institute.setTotalStudent(jsonObject.getString("totalStudent"));
                                institute.setInsId(jsonObject.getString("id"));

                                institute.save();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        institutes = new Select().from(Institute.class).where("poId = ?", "kholilur").execute();
                        for (int i = 0; i < institutes.size(); i++) {
                            SchoolRequest(institutes.get(i).getInsId().toString(),i);
                        }
                        Thread thread=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(true)
                                {
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if(getPreferrence("update_done",false))
                                    {
                                        progress.dismiss();
                                        startActivity(new Intent(LoginActivity.this, ProgramOrganizerActivity.class));
                                        finish();
                                        break;
                                    }


                                }
                            }
                        });
                        thread.start();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue rq = Volley.newRequestQueue(LoginActivity.this);
        rq.add(sr);
    }

    public void SchoolRequest(final String schoolId , final int instituteSize) {

        String url = "http://bepmis.brac.net/rest/apps/student?uname="+userName+"&passw="+password +
                "&instituteId=" + schoolId;

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            JSONArray jsonArray = jObject.getJSONArray("model");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Student student = new Student();

                                if (!jsonObject.isNull("id")) {
                                    student.setSid(jsonObject.getString("id"));
                                }
                                if (!jsonObject.isNull("instituteId")) {
                                    student.setInstituteId(jsonObject.getString("instituteId"));
                                }
                                if (!jsonObject.isNull("fullName")) {
                                    student.setStudentFirstName(jsonObject.getString("fullName"));
                                }
                                if (!jsonObject.isNull("studentId")) {
                                    student.setStudentId(jsonObject.getString("studentId"));
                                }
                                if (!jsonObject.isNull("gradeId")) {
                                    student.setStudentId(jsonObject.getString("gradeId"));
                                }
                                student.setQueryInstituteId(schoolId);

                                student.save();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        if(instituteSize==institutes.size()-1)
                        {
                            setPreference("update_done",true);
                            Log.d("Updated","Update Done");
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue rq = Volley.newRequestQueue(LoginActivity.this);
        rq.add(sr);
    }

    void setPreference(String key,boolean val)
    {
        SharedPreferences settings = getSharedPreferences("brac", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
   /* private void attemptLogin() {
        try {

            //showProgress(true);
            User user = new User();
            user.setUsername(etUsername.getText().toString());
            user.setPassword(etPassword.getText().toString());
            StringEntity entity = new StringEntity(mGson.toJson(user));
            //Log.d("response",mGson.toJson(user));
            wsManager.post(util.URL_LOGIN, null, null, null, entity);
*//*
            user.setUsername("kholilur");
            user.setPassword("123456");*//*


        } catch (Throwable t) {

            t.printStackTrace();
        }
    }*/


    @Override
    public void success(String response, String url) {
        try {

            if (url.equalsIgnoreCase(util.URL_INSTITUTE)) {


            }
            if (url.equalsIgnoreCase(util.URL_LOGIN)) {

            }
            if (url.equalsIgnoreCase(util.URL_STUDENT)) {
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();

                /*JSONObject jObject = new JSONObject(response);
                JSONArray jsonArray = jObject.getJSONArray("model");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Student student = new Student();

                    if (!jsonObject.isNull("instituteId")) {
                        student.setInstituteId(jsonObject.getString("instituteId"));
                    }
                    if (!jsonObject.isNull("fullName")) {
                        student.setStudentFirstName(jsonObject.getString("fullName"));
                    }
                    if (!jsonObject.isNull("studentId")) {
                        student.setStudentId(jsonObject.getString("studentId"));
                    }

                    student.save();
                }*/

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void failure(int statusCode, Throwable error, String content) {

    }
}


