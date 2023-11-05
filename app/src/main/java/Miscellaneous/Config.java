package Miscellaneous;

/**
 * Created by praveenupadrasta on 09-04-2017.
 */
public class Config {

    public static String baseUrl = "http://praveenupadrasta.pythonanywhere.com";
    public static String getLocations = baseUrl + "/location/getLocations/";
    public static String login = baseUrl + "/user/login/";
    public static String registerUser = baseUrl + "/user/register/";
    public static String forgotPassword = baseUrl + "/user/forgot_password/";
    public static String getProfile = baseUrl + "/user/getProfile/";
    public static String changePassword = baseUrl + "/user/change_password/";
    public static String editProfile = baseUrl + "/user/editProfile/";
    public static String logout = baseUrl + "/user/logout/";
    public static String saveFCMToken = baseUrl + "/fcm/save_registration_token/";
    public static String getHistory = baseUrl + "/history/get_history?location=";
    public static String getLatestHistory = baseUrl + "/history/get_latest_history?location=";
}
