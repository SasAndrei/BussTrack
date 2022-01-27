package Accounts;

import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.busstrack.MainActivity;
import com.example.busstrack.MapsActivity;
import com.example.busstrack.R;
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
        Dialog notification = MapsActivity.notification;
        notification.setContentView(R.layout.bus_notification);
        notification.show();
        Button button = notification.findViewById(R.id.btnClose);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("click");
                notification.dismiss();
            }
        });
        System.out.println("Your bus is close");
    }
}
