package com.iconanalytics.petcare;

import android.os.AsyncTask;

import com.iconanalytics.petcare.AsyncResponse;

public class ServerFetch extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;


    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.query(strings);

    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}