package Helper;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by praveenupadrasta on 18-05-2017.
 */

public class HTTPConnection implements HTTPInterface {

    public HttpURLConnection doGet(String requestUrl)
    {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return urlConnection;
    }

    public HttpURLConnection doPost(String requestUrl, byte[] urlParams)
    {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setFixedLengthStreamingMode(urlParams.length);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            urlConnection.setRequestProperty("Accept", "*/*");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return urlConnection;
    }

    public HttpURLConnection doPut(String requestUrl, byte[] urlParams)
    {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setFixedLengthStreamingMode(urlParams.length);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            urlConnection.setRequestProperty("Accept", "*/*");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return urlConnection;
    }

    public HttpURLConnection doDelete(String requestUrl)
    {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
//            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
            urlConnection.setRequestMethod("DELETE");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return urlConnection;
    }
}
