package com.praveenupadrasta.news.DetectionAndHistory.LandslideDetection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import Adapters.LandslideHistoryBaseAdapter;
import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;

/**
 * Created by praveenupadrasta on 06-07-2017.
 */

public class GetLatestHistoryTask extends AsyncTask<Void, Void, JSONObject>
{
    private ProgressDialog pd = null;
    private String locationName = null;
    private Context context = null;
    private LandslideDetectionFragment landslideDetectionFragment = null;
    public GetLatestHistoryTask(LandslideDetectionFragment landslideDetectionFragment, Context context, String locationName)
    {
        this.landslideDetectionFragment = landslideDetectionFragment;
        this.context = context;
        this.pd = new ProgressDialog(context);
        this.locationName = locationName;
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
            HttpURLConnection urlConnection = new HTTPConnection().doGet(Config.getLatestHistory+locationName);
            urlConnection.setRequestProperty("Auth", Utils.getAccessToken(context));
            urlConnection.connect();
            response = Utils.readResponse(urlConnection);
            response.put("status_code", urlConnection.getResponseCode());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        pd.dismiss();
        landslideDetectionFragment.updateView(response);
    }
}