package Traffic;

import com.example.busstrack.MapsActivity;

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
    @Override
    public boolean equals(Object o)
    {
        Station compared = (Station) o;
        if (!Name.equals(compared.Name))
            return false;
        if (!(Longitude.asDouble() ==compared.Longitude.asDouble()))
            return false;
        if (!(Latitude.asDouble() == compared.Latitude.asDouble()))
            return false;
        return true;
    }
    @Override
    public String toString()
    {
        return "Name of station: " + Name + ", Longitude and Latitude";
    }

    public void checkRoutes() {
        ArrayList<Route> routes = MapsActivity.routes;
        routesThatPassThrough.clear();
        for (Route r : routes) {
            for (Station s : r.stations) {
                if (s.equals(this))
                    routesThatPassThrough.add(r);
            }
        }
    }
}
