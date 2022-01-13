package Traffic;

import java.util.ArrayList;

import Utils.Coordinate;

public class Station {
    public String Name;
    public Coordinate Longitude;
    public Coordinate Latitude;
    public ArrayList<Route> routesThatPassThrough = new ArrayList<>();

    public Station(String name, Coordinate longitude, Coordinate latitude)
    {
        Name = name;
        Longitude = longitude;
        Latitude = latitude;
    }
    public Station()
    {
    }
}
