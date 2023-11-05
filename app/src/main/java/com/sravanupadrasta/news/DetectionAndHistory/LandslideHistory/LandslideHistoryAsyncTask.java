package com.praveenupadrasta.news.DetectionAndHistory.LandslideHistory;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.praveenupadrasta.news.DetectionAndHistory.LandslideDetection.LandslideDetectionFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import Adapters.LandslideHistoryBaseAdapter;
import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;

/**
 * Created by praveenupadrasta on 06-07-2017.
 */

public class LandslideHistoryAsyncTask extends AsyncTask<Void, Void, JSONObject> {
    private ProgressDialog pd = null;
    private String locationName = null;
    private Context context = null;
    private LandslideHistoryFragment landslideHistoryFragment = null;
    public LandslideHistoryAsyncTask(LandslideHistoryFragment landslideDetectionFragment, Context context)
    {
        this.context = context;
        this.landslideHistoryFragment = landslideDetectionFragment;
        pd = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        pd.setMessage("Loading");
        pd.show();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject response = null;
        try {
            HttpURLConnection urlConnection = new HTTPConnection().doGet(Config.getHistory + locationName);
            urlConnection.setRequestProperty("Auth", Utils.getAccessToken(context));
            urlConnection.connect();
            response = Utils.readResponse(urlConnection);
            response.put("status_code", urlConnection.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        pd.dismiss();
        landslideHistoryFragment.updateView(response);
    }
}