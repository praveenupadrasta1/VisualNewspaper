package Miscellaneous;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.praveenupadrasta.news.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by praveenupadrasta on 18-05-2017.
 */

public class Utils {

    public static JSONObject readResponse(HttpURLConnection urlConnection)
    {
        String response = "";
        JSONObject responseJson = null;
        try {
            InputStream is;
            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201)
                is = urlConnection.getInputStream();
            else
                is = urlConnection.getErrorStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = rd.readLine()) != null) {
                response += line;
            }
            rd.close();
            responseJson = new JSONObject(response);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return responseJson;
    }

    public static ArrayList<Integer> getEmptyFields(Window v, Object... id)
    {
        ArrayList<Integer> idList = new ArrayList<Integer>();
        for (Object i: id)
        {
            EditText e = (EditText) v.findViewById((Integer)i);
            if(e.getText().toString().equals(""))
                idList.add((Integer)i);
        }
        return idList;
    }

    public static void showErrorMsg(Window v, String msg, int lblErrorId, ArrayList<Integer> idList)
    {
        for(Integer id: idList)
        {
            EditText e=(EditText) v.findViewById(id);
            e.setBackgroundResource(R.drawable.edittext_error_background);
        }
        TextView lbl_error = (TextView) v.findViewById(lblErrorId);
        lbl_error.setText(msg);
        lbl_error.setVisibility(View.VISIBLE);
    }

    public static boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getAccessToken(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", 0);
        return prefs.getString("access_token", null);
    }
}
