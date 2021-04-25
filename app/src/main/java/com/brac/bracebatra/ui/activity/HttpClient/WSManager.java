package com.brac.bracebatra.ui.activity.HttpClient;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.brac.bracebatra.util.util;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

public class WSManager
{
	private WSResponseListener wsResponseListener;
	private Context context;
	private static final String TAG = "WSManager";
	public static final String WS_POST_JSON_CONTENT_TYPE = "application/json";
	private HttpClientManager wsm;

	public WSManager(Context _context, WSResponseListener _wsResponseListener){
		context = _context;
		wsResponseListener = _wsResponseListener;
		wsm = HttpClientManager.getInstance();
	}

	public void post(final String url, String user, String password, String requester,StringEntity entity){
		//client.addHeader(IConstant.WS_USERNAME, user);
		wsm.getClient().setTimeout(600000);
		wsm.getClient().post(context, url, entity, WS_POST_JSON_CONTENT_TYPE, new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(String response) 
			{
				Log.d("Login response ","Success");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.get("message").equals("Successful")){
                        get(util.URL_INSTITUTE);
                        wsResponseListener.success(response, url);
						//get("http://bepmis.brac.net/rest/student?start=0");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

			//	get("http://bepmis.brac.net/rest/teacher/listAllTeacher?start=1");
				//get("http://bepmis.brac.net/rest/teacher/listAllTeacher?start=2");


			}
			@Override
			public void onFailure(int statusCode, Throwable error, String content) 
			{
				wsResponseListener.failure(statusCode, error, content);
			}
		});		
	}
	
	public void get(final String url){
		wsm.getClient().setTimeout(600000);
		wsm.getClient().get(context, url, new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(String response)
			{
				wsResponseListener.success(response, url);
			}
			@Override
            public void onFailure(int statusCode, Throwable error, String content) 
			{

				wsResponseListener.failure(statusCode, error, content);
            }				
		});
	}
}
