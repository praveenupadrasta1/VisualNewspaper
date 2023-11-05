package com.praveenupadrasta.news.FCM;

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

public class FCMTokenRegistrationTask extends AsyncTask<Void, Void, Void>
{
    private String token = "";
    private byte[] urlParams = null;
    private Context context = null;
    public FCMTokenRegistrationTask(Context context, String token, byte[] urlParams)
    {
        this.context = context;
        this.token = token;
        this.urlParams = urlParams;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        try {
            HttpURLConnection urlConnection = new HTTPConnection().doPost(Config.saveFCMToken, urlParams);
            urlConnection.setRequestProperty("Auth", Utils.getAccessToken(context));
            OutputStream os = urlConnection.getOutputStream();
            os.write(urlParams);
            os.close();

            urlConnection.connect();
            JSONObject response = Utils.readResponse(urlConnection);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}