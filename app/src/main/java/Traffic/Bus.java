package Traffic;

import Utils.Coordinate;

public class Bus {
    private int BusID;
    private Coordinate Longitude;
    private Coordinate Latitude;

    public Bus(int busID, Coordinate longitude, Coordinate latitude)
    {
        BusID = busID;
        Longitude = longitude;
        Latitude = latitude;
    }
    public Bus()
    {

    }

    public int getBusID() {
        return BusID;
    }

    public void setBusID(int busID) {
        BusID = busID;
    }

    public Coordinate getLongitude() {
        return Longitude;
    }

    public void setLongitude(Coordinate longitude) {
        Longitude = longitude;
    }

    public Coordinate getLatitude() {
        return Latitude;
    }

    public void setLatitude(Coordinate latitude) {
        Latitude = latitude;
    }
}
