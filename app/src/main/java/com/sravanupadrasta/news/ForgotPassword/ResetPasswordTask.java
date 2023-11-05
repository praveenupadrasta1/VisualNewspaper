package com.praveenupadrasta.news.ForgotPassword;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;

import com.praveenupadrasta.news.R;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.DialogBox;
import Miscellaneous.Utils;

/**
 * Created by praveenupadrasta on 06-07-2017.
 */

public class ResetPasswordTask extends AsyncTask<Void, Void, JSONObject>
{
    private ProgressDialog pd = null;
    private ForgotPasswordActivity forgotPasswordActivity = null;
    public ResetPasswordTask(ForgotPasswordActivity forgotPasswordActivity)
    {
        this.forgotPasswordActivity = forgotPasswordActivity;
        this.pd = new ProgressDialog(forgotPasswordActivity);
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
        try{
            byte[] forgotPasswordData = forgotPasswordActivity.getForgotPasswordData();
            HttpURLConnection urlConnection = new HTTPConnection().doPut(Config.forgotPassword, forgotPasswordData);
            OutputStream os = urlConnection.getOutputStream();
            os.write(forgotPasswordData);
            os.close();
            urlConnection.connect();
            response = Utils.readResponse(urlConnection);
            response.put("status_code", urlConnection.getResponseCode());
        }
        catch(Exception e)
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
            if ((int) response.get("status_code") != 200) {
                forgotPasswordActivity.updateView(response);
            }
            else
            {
                forgotPasswordActivity.resetPasswordSuccessAction(response.get("details").toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
