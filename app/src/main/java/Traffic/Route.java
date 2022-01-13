package Traffic;

import android.renderscript.ScriptIntrinsicYuvToRGB;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.LinkedList;

import Accounts.StandardUser;

public class Route {
    public String name;
    public ArrayList<Bus> busses = new ArrayList<Bus>();
    public LinkedList<Station> stations = new LinkedList<Station>();
    //public Dictionary<StandardUser, Station> busRequests = new Dictionary<StandardUser, Station>();

    public Route(String name, ArrayList<Bus> busses, LinkedList<Station> stations)
    {
        this.name = name;
        this.busses = busses;
        this.stations = stations;
    }

    public Route(String name, LinkedList<Station> stations)
    {
        this.name = name;
        this.stations = stations;
    }

}
