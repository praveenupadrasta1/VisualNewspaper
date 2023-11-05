package com.praveenupadrasta.news.Login;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

/**
 * Created by praveenupadrasta on 06-07-2017.
 */

public class LoginModel {

    private Context context = null;

    public LoginModel(Context context)
    {
        this.context = context;
    }

    public void storeAccessToken(JSONObject response)
    {
        try {
            SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("access_token", response.get("access_token").toString());
            editor.apply();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
