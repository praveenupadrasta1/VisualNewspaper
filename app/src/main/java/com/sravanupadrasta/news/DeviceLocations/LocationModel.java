package com.praveenupadrasta.news.DeviceLocations;

import android.content.Context;
import android.provider.Settings;

import org.json.JSONObject;

/**
 * Created by praveenupadrasta on 06-07-2017.
 */

public class LocationModel {

    private Context context = null;
    public LocationModel(Context context)
    {
        this.context = context;
    }

    public byte[] getParams(String fcm_token)
    {
        JSONObject params = null;
        try {
            params = new JSONObject();
            params.put("device_id", Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID));
            params.put("fcm_token", fcm_token);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return params.toString().getBytes();
    }

}
