package com.brac.bracebatra.ui.activity.HttpClient;

public interface WSResponseListener {
	public void success(String response, String url);
    public void failure(int statusCode, Throwable error, String content);
}

