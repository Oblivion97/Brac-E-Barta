package com.brac.bracebatra.ui.activity.HttpClient;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by palash on 10/24/2016.
 */
public class HttpClientManager {
    private static HttpClientManager wsm = null;
    private final AsyncHttpClient client;

    public static HttpClientManager getInstance(){
        if(wsm == null){
            wsm = new HttpClientManager();
        }
        return wsm;
    }

    private HttpClientManager(){
        client =  new AsyncHttpClient();
    }

    public AsyncHttpClient getClient(){
        return client;
    }

}
