package Helper;

import java.net.HttpURLConnection;

/**
 * Created by praveenupadrasta on 18-05-2017.
 */

public interface HTTPInterface {
    public HttpURLConnection doGet(String requestUrl);
    public HttpURLConnection doPost(String requestUrl, byte[] urlParams);
    public HttpURLConnection doPut(String requestUrl, byte[] urlParams);
    public HttpURLConnection doDelete(String requestUrl);
}
