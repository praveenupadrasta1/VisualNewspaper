package com.praveenupadrasta.news.FCM;

import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import Helper.HTTPConnection;
import Miscellaneous.Utils;

/**
 * Created by praveenupadrasta on 18-04-2017.
 */
public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
    }


}
