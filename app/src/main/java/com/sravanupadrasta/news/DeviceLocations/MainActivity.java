package com.praveenupadrasta.news.DeviceLocations;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.praveenupadrasta.news.FCM.FCMTokenRegistrationTask;
import com.praveenupadrasta.news.Login.LoginActivity;
import com.praveenupadrasta.news.Logout.LogoutTask;
import com.praveenupadrasta.news.R;
import com.praveenupadrasta.news.DetectionAndHistory.TabbedActivity;
import com.praveenupadrasta.news.UserProfile.UserProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

import Miscellaneous.Utils;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap = null;
    private MarkerOptions markerOptions = null;
    private LatLng latLng = null;

    private String locationName = null;
    private ImageView imgDetection = null;
    private ImageView imgHistory = null;
    private AutoCompleteTextView etLocation = null;
    private ArrayList<String> places = null;
    private ArrayList<HashMap<String,String>> placeLatLongList = null;
    private ArrayAdapter<String> adapter = null;
    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        String fcm_token = FirebaseInstanceId.getInstance().getToken();

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Utils.isNetworkAvailable(conMgr)) {
            initViews();
            new FCMTokenRegistrationTask(context, fcm_token, new LocationModel(context).getParams(fcm_token)).execute();
            new PinpointLocationsTask(MainActivity.this , context).execute();
        }
        else  {
            Toast.makeText(this,"No internet connection available", Toast.LENGTH_LONG).show();
        }
    }

    public void initViews()
    {
        context = getApplicationContext();
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        imgDetection = (ImageView)findViewById(R.id.imgDetection);
        imgHistory = (ImageView) findViewById(R.id.imgHistory);

        imgDetection.setOnClickListener(imgDetectionClickListener);
        imgHistory.setOnClickListener(imgHistoryClickListener);
        etLocation = (AutoCompleteTextView) findViewById(R.id.et_location);
        etLocation.setOnItemClickListener(etLocation_onItemClickListener);
        places = new ArrayList<String>();
        placeLatLongList = new ArrayList<HashMap<String,String>>();
    }

    AdapterView.OnItemClickListener etLocation_onItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            showLocation(adapterView.getItemAtPosition(i).toString());
        }

        private void showLocation(String placeName)
        {
            for(int i=0;i<placeLatLongList.size();i++)
            {
                HashMap<String, String> tempMap = placeLatLongList.get(i);
                if(tempMap.containsKey(placeName))
                {
                    String coord[] = tempMap.get(placeName).split(",");
                    double latitude = Double.parseDouble(coord[0]);
                    double longitude = Double.parseDouble(coord[1]);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(latitude, longitude), 10));

                }
            }
        }
    };

    OnClickListener imgDetectionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, TabbedActivity.class);
            intent.putExtra("location", locationName);
            intent.putExtra("tab",0);
            intent.putExtra("isNotification", false);
            startActivity(intent);
        }
    };

    OnClickListener imgHistoryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, TabbedActivity.class);
            intent.putExtra("location", locationName);
            intent.putExtra("tab",1);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.item_profile:
                Intent showProfile = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(showProfile);
                return true;
            case R.id.item_logout:
                new LogoutTask(MainActivity.this, context).execute();
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }

    public void updateView(String data)
    {
        try {
            JSONObject jsonObj = new JSONObject(data);
            JSONArray details = jsonObj.getJSONArray("details");
            double tempLatitude = 0, tempLongitude = 0;
            for(int i = 0; i < details.length(); i++)
            {
                JSONObject obj = details.getJSONObject(i);
                String name = obj.getString("name");
                String state = obj.getString("state");
                String country = obj.getString("country");
                double latitude = Double.parseDouble(obj.getString("latitude"));
                double longitude = Double.parseDouble(obj.getString("longitude"));
                HashMap<String,String> temp = new HashMap<>();
                temp.put(name,latitude+","+longitude);

                placeLatLongList.add(temp);
                places.add(name);
                tempLatitude += latitude;
                tempLongitude += longitude;

                latLng = new LatLng(latitude, longitude);

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(name + ", " + state + ", " + country);

                googleMap.addMarker(markerOptions).showInfoWindow();
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        locationName = marker.getTitle().split(",")[0];
                        marker.showInfoWindow();
                        LinearLayout linearLayoutOptions = (LinearLayout)findViewById(R.id.linLayout_options);
                        if(linearLayoutOptions.getVisibility()== View.VISIBLE) {
                            linearLayoutOptions.setVisibility(View.INVISIBLE);
                        }
                        else {
                            linearLayoutOptions.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(tempLatitude/details.length(), tempLongitude/details.length()), 5));

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, places);
            etLocation.setAdapter(adapter);
            etLocation.setThreshold(1);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void logoutUser()
    {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("access_token", null);
        editor.apply();
        Intent loginIntent = new Intent(context, LoginActivity.class);
        startActivity(loginIntent);
    }
}