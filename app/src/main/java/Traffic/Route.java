package Traffic;

import android.renderscript.ScriptIntrinsicYuvToRGB;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;

import Accounts.ISubscriber;
import Accounts.StandardUser;
import Accounts.User;

public class Route implements IPublisher {
    public String name;
    public ArrayList<Bus> busses = new ArrayList<Bus>();
    public ArrayList<Station> stations = new ArrayList<>();
    public HashMap<ISubscriber, Station> busRequests = new HashMap<ISubscriber, Station>();
    public int nearbyDistance = 200000;


    public void checkIfBussesAreNearby()
    {
        for (Station station : busRequests.values()) {
            //check for each station if there are busses nearby
            for (Bus bus : busses) {
                if(distance(bus.getLatitude().asDouble(), station.Latitude.asDouble(), bus.getLongitude().asDouble(), station.Longitude.asDouble(), 0,0) < nearbyDistance)
                {
                    notifySubscribers(station);
                    return;
                }

            }
        }
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Bus> getBusses() {
        return busses;
    }

    public void setBusses(ArrayList<Bus> busses) {
        this.busses = busses;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    public Route(String name, ArrayList<Bus> busses, ArrayList<Station> stations)
    {
        this.name = name;
        this.busses = busses;
        this.stations = stations;
    }
    public Route()
    {

    }

    public Route(String name, ArrayList<Station> stations)
    {
        this.name = name;
        this.stations = stations;
    }

    @Override
    public void subscribe(ISubscriber subscriber, Station station) {
        busRequests.put(subscriber, station);
    }

    @Override
    public void unSubscribe(ISubscriber subscriber) {
        busRequests.remove(subscriber);
    }

    @Override
    public void notifySubscribers(Station station) {
        for (ISubscriber subscriber : busRequests.keySet()) {
            Station stationSubscriberIsAt = busRequests.get(subscriber);
            if(station.equals(stationSubscriberIsAt))
            {
                subscriber.update();
                unSubscribe(subscriber);
            }

        }
        checkIfBussesAreNearby();
    }
}
