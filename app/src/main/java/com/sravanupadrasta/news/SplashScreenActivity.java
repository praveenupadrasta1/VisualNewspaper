package com.praveenupadrasta.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.praveenupadrasta.news.DeviceLocations.MainActivity;
import com.praveenupadrasta.news.Login.LoginActivity;

import java.net.HttpURLConnection;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;

public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if (Utils.isNetworkAvailable(conMgr)) {
                    new OpenActivity().execute();
                }
                else
                {
                    Toast.makeText(context, "No internet connection available", Toast.LENGTH_LONG).show();
                }
            }
        }, SPLASH_TIME_OUT);

    }

    private class OpenActivity extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... param){
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("app_prefs", MODE_PRIVATE);
            String access_token = prefs.getString("access_token", null);
            if(access_token == null) {
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
            }
            else
            {
                try {
                    HttpURLConnection urlConnection = new HTTPConnection().doGet(Config.getLocations);
                    urlConnection.setRequestProperty("Auth", access_token);
                    urlConnection.connect();

                    if (urlConnection.getResponseCode() == 200) {
                        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}