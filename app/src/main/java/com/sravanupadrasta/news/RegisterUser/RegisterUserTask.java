package com.praveenupadrasta.news.RegisterUser;

import android.app.ProgressDialog;
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

public class RegisterUserTask extends AsyncTask<Void, Void, JSONObject>
{
    private ProgressDialog pd = null;
    private byte[] registerData = null;
    private RegisterUserActivity registerUserActivity = null;

    public RegisterUserTask(RegisterUserActivity registerUserActivity, byte[] registerData)
    {
        this.registerUserActivity = registerUserActivity;
        this.pd = new ProgressDialog(registerUserActivity);
        this.registerData = registerData;
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
            HttpURLConnection urlConnection = new HTTPConnection().doPost(Config.registerUser, registerData);
            OutputStream os = urlConnection.getOutputStream();
            os.write(registerData);
            os.close();
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
            if ((int) response.get("status_code") != 201) {
                registerUserActivity.updateView(response);
            }
            else
            {
                registerUserActivity.registerUserSuccessAction();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
