package com.praveenupadrasta.news.UserProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;

/**
 * Created by praveenupadrasta on 06-07-2017.
 */

public class ChangePasswordTask extends AsyncTask<Void, Void, JSONObject>
{
    private ProgressDialog pd = null;
    private byte[] urlParams = null;
    private UserProfileActivity userProfileActivity = null;
    private Context context = null;

    public ChangePasswordTask(UserProfileActivity userProfileActivity, Context context, byte[] urlParams)
    {
        this.userProfileActivity = userProfileActivity;
        this.pd = new ProgressDialog(userProfileActivity);
        this.urlParams = urlParams;
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        pd.setMessage("Loading");
        pd.show();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject response = null;
        try {
            HttpURLConnection urlConnection = new HTTPConnection().doPost(Config.changePassword, urlParams);
            urlConnection.setRequestProperty("Auth", Utils.getAccessToken(context));
            OutputStream os = urlConnection.getOutputStream();
            os.write(urlParams);
            os.close();
            urlConnection.connect();
            int resCode = urlConnection.getResponseCode();
            response = Utils.readResponse(urlConnection);
            response.put("status_code", resCode);
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
            int resCode = (int)response.get("status_code");
            userProfileActivity.updateView(resCode, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
