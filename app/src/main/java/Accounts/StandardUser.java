package Accounts;

import android.os.Build;

import Traffic.Route;
import Traffic.Station;

public class StandardUser extends User{

    public StandardUser(String user, String pass) {
        super(user,pass);
    }
    public StandardUser(String user, String pass, String email) {
        super(user,pass, email);
    }

    public StandardUser() {
    }
    @Override
    public void login() {

    }

    public void getStationInfo()
    {

    }

    public void getLineData()
    {

    }

    public void takeRoute(Route routeIWantToTake, Station stationIWantToTakeTheBusFrom)
    {

    }

    public void notifyBusIsClose()
    {

    }


}
