package Deserializers;

/**
 * Created by praveenupadrasta on 09-04-2017.
 */
public class LocationDeserializer {

    private String name;
    private String state;
    private String country;
    private double latitude;
    private double longitude;

//    LocationDeserializer(String name, String state, String country, double latitude, double longitude)
//    {
//        this.locName = name;
//        this.locState = state;
//        this.locCountry = country;
//        this.locLatitude = latitude;
//        this.locLongitude = longitude;
//    }

    public String getName()
    {  return this.name;    }

    public String getState()
    { return this.state;   }

    public String getCountry()
    { return this.country; }

    public double getLatitude()
    { return this.latitude; }

    public double getLongitude()
    { return this.longitude; }


}
