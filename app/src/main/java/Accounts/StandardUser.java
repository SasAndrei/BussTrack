package Accounts;

import android.os.Build;

import com.google.firebase.events.EventHandler;
import com.google.firebase.events.Subscriber;

import java.util.concurrent.Executor;

import Traffic.Route;
import Traffic.Station;

public class StandardUser extends User implements ISubscriber{
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

    @Override
    public void update() {
        //show popup
        System.out.println("Your bus is close");
    }
}
