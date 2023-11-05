package com.praveenupadrasta.news.UserProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;

/**
 * Created by praveenupadrasta on 06-07-2017.
 */

public class GetUserProfileTask extends AsyncTask<Void, Void, JSONObject>
{
    private ProgressDialog pd = null;
    private Context context = null;
    private UserProfileActivity userProfileActivity = null;

    public GetUserProfileTask(UserProfileActivity userProfileActivity, Context context)
    {
        this.userProfileActivity = userProfileActivity;
        this.pd = new ProgressDialog(userProfileActivity);
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        pd.setMessage("Loading");
        pd.show();
    }

    @Override
    protected JSONObject doInBackground(Void... params)
    {
        JSONObject response = null;
        try {
            HttpURLConnection urlConnection = new HTTPConnection().doGet(Config.getProfile);
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
    protected void onPostExecute(JSONObject response)
    {
        pd.dismiss();
        try {
            if((int)response.get("status_code") == 200) {
                userProfileActivity.updateUserProfileView(response.getJSONObject("details"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}