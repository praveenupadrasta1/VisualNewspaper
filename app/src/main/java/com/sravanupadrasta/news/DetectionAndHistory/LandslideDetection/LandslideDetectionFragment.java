package com.praveenupadrasta.news.DetectionAndHistory.LandslideDetection;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.praveenupadrasta.news.R;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;
import pl.pawelkleczkowski.customgauge.CustomGauge;

public class LandslideDetectionFragment extends Fragment implements OnMapReadyCallback {

    private String locationName;
    private CustomGauge moistureValue;
    private CustomGauge magnitude;
    private TextView txtDateTime;
    private TextView txtRainfall;
    private TextView txtMoistureValue;
    private TextView txtMagnitudeValue;
    private Context context;
    private FragmentActivity myContext;
    private String access_token;
    private GoogleMap googleMap;
    private LatLng latLng;
    private MarkerOptions markerOptions;
    MapView mapView;
    private boolean isNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        isNotification = (Boolean)getArguments().get("isNotification");
        locationName = getArguments().get("location").toString();

        View rootView = inflater.inflate(R.layout.fragment_landslide_detection2, container, false);
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
        initView(rootView);

        return rootView;
    }

    private void populateDataOnView(){
        try {
            Bundle bundle = getArguments();
            String dateTime = bundle.get("date_time").toString();
            String isRainfall = bundle.get("rainfall").toString();
            double accelerometer_level = (double) bundle.get("accelerometer_level");
            String latitude = bundle.get("latitude").toString();
            String longitude = bundle.get("longitude").toString();
            double moistureLevel = (double) bundle.get("moisture_level");

            txtDateTime.setText(dateTime + " UTC");
            txtMagnitudeValue.setText(String.format("%.2f", accelerometer_level));
            txtMoistureValue.setText(String.format("%.2f", moistureLevel));
            txtRainfall.setText(isRainfall);
            magnitude.setValue((int) (accelerometer_level * 1000));
            moistureValue.setValue((int) (moistureLevel * 1000));

            latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            markerOptions.position(latLng);
            markerOptions.title(latitude + ", " + longitude + " - " + locationName);
            googleMap.addMarker(markerOptions).showInfoWindow();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initView(View rootView)
    {
        context = getContext();
        txtDateTime = (TextView) rootView.findViewById(R.id.lblDateTime);
        txtRainfall = (TextView) rootView.findViewById(R.id.lblRainfallValue);
        txtMoistureValue = (TextView) rootView.findViewById(R.id.lblMoistureValue);
        txtMagnitudeValue = (TextView) rootView.findViewById(R.id.lblMagnitudeValue);

        magnitude = (CustomGauge) rootView.findViewById(R.id.gaugeMagnitudeLevel);
        magnitude.setStartValue(0);
        magnitude.setEndValue(2000);
        magnitude.setStartAngle(180);
        magnitude.setPointStartColor(Color.GREEN);
        magnitude.setPointEndColor(Color.RED);

        moistureValue = (CustomGauge) rootView.findViewById(R.id.gaugeMoistureLevel);
        moistureValue.setStartValue(0);
        moistureValue.setEndValue(1000);
        moistureValue.setStartAngle(180);
        moistureValue.setPointStartColor(Color.GREEN);
        moistureValue.setPointEndColor(Color.RED);

        markerOptions = new MarkerOptions();
    }

    private void getLatestHistoryData()
    {
        new GetLatestHistoryTask(LandslideDetectionFragment.this, context, locationName).execute();
    }

    public void updateView(JSONObject response)
    {
        try
        {
            if((int)response.get("status_code") == 200) {
                JSONObject temp = (JSONObject) response.get("details");
                String dateTime = temp.get("date_time").toString();
                String isRainfall = (boolean) temp.get("rainfall")? "Yes":"No";
                double accelerometer_level = (double) temp.get("accelerometer_level");
                String latitude = temp.get("latitude").toString();
                String longitude = temp.get("longitude").toString();
                double moistureLevel = (double) temp.get("moisture_level");

                txtDateTime.setText(dateTime.split("T")[0]+" "+
                        dateTime.split("T")[1].substring(0,dateTime.split("T")[1].length()-1)+" UTC");
                txtMagnitudeValue.setText(String.format("%.2f",accelerometer_level));
                txtMoistureValue.setText(String.format("%.2f",moistureLevel));
                txtRainfall.setText(isRainfall);
                magnitude.setValue((int)(accelerometer_level * 1000));
                moistureValue.setValue((int)(moistureLevel * 1000));

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                markerOptions.position(latLng);
                markerOptions.title(latitude+", "+longitude+" - "+locationName);
                googleMap.addMarker(markerOptions).showInfoWindow();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if(!isNotification)
            getLatestHistoryData();
        else
            populateDataOnView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
