package com.praveenupadrasta.news.Logout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.praveenupadrasta.news.DeviceLocations.MainActivity;
import com.praveenupadrasta.news.Login.LoginActivity;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;

/**
 * Created by praveenupadrasta on 06-07-2017.
 */

public class LogoutTask extends AsyncTask<Void, Void, JSONObject>
{
    private ProgressDialog pd = null;
    private Context context = null;
    private MainActivity mainActivity = null;

    public LogoutTask(MainActivity mainActivity, Context context)
    {
        this.mainActivity = mainActivity;
        this.pd = new ProgressDialog(mainActivity);
        this.context = context;
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
            HttpURLConnection urlConnection = new HTTPConnection().doDelete(Config.logout);
            urlConnection.setRequestProperty("Auth", Utils.getAccessToken(context));

            urlConnection.connect();
            response = Utils.readResponse(urlConnection);
            response.put("status_code", urlConnection.getResponseCode());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        pd.cancel();
        try {
            if ((int)response.get("status_code") == 200)
            {
                mainActivity.logoutUser();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}