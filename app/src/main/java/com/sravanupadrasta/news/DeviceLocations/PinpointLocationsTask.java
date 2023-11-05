package com.praveenupadrasta.news.DeviceLocations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.praveenupadrasta.news.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;

/**
 * Created by praveenupadrasta on 06-07-2017.
 */

public class PinpointLocationsTask extends AsyncTask<Void, Void, String> {

    private Context context = null;
    private MainActivity mainActivity = null;

    public PinpointLocationsTask(MainActivity mainActivity, Context context)
    {
        this.mainActivity = mainActivity;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... param) {
        String jsonString = "",data="";
        try {
            String access_token = Utils.getAccessToken(context);

            HttpURLConnection urlConnection = new HTTPConnection().doGet(Config.getLocations);
            urlConnection.setRequestProperty ("Auth", access_token);
            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            while ((data = reader.readLine()) != null){
                jsonString += data;
            }
            urlConnection.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonString;
    }

    @Override
    protected void onPostExecute(String data) {
        mainActivity.updateView(data);
    }
}