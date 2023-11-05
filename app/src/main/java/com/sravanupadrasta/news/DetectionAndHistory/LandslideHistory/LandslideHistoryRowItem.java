package com.praveenupadrasta.news.DetectionAndHistory.LandslideHistory;

/**
 * Created by praveenupadrasta on 23-06-2017.
 */

public class LandslideHistoryRowItem {
    private String dateTime;
    private String latitude;
    private String longitude;
    private String rainfall;
    private double moistureLevel;
    private double magnitude;

    public LandslideHistoryRowItem(String dateTime, String latitude, String longitude,
                                    String rainfall, double moistureLevel, double magnitude)
    {
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rainfall = rainfall;
        this.moistureLevel = moistureLevel;
        this.magnitude = magnitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRainfall() {
        return rainfall;
    }

    public void setRainfall(String rainfall) {
        this.rainfall = rainfall;
    }

    public Double getMoistureLevel() {
        return moistureLevel;
    }

    public void setMoistureLevel(double moistureLevel) {
        this.moistureLevel = moistureLevel;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }
}
