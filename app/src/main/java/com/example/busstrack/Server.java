package com.example.busstrack;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Accounts.Admin;
import Accounts.StandardUser;
import Accounts.User;
import Traffic.Bus;
import Traffic.Route;
import Traffic.Station;

public class Server {

    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public <T> void getData(MainActivity instance) {

        reference = rootNode.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if("StationCount".equals(snapshot.getKey()))
                    {
                        MapsActivity.stationCount = snapshot.getValue(Integer.class);
                    }
                    if("Station".equals(snapshot.getKey()))
                    {
                        //put stations into list
                        for(DataSnapshot stationAsSnapshot : snapshot.getChildren())
                        {
                            Station station = stationAsSnapshot.getValue(Station.class);
                            MapsActivity.stations.add(station);
                        }
                    }
                    if("AccountsCount".equals(snapshot.getKey()))
                    {
                        MainActivity.userCount = snapshot.getValue(Integer.class);
                    }
                    if("Accounts".equals(snapshot.getKey()))
                    {
                        //put accounts into list
                        for(DataSnapshot accountAsSnapshot : snapshot.getChildren())
                        {
                            User user;

                            String username = getUsername(accountAsSnapshot);
                            assert username != null;
                            if(username.contains("admin"))
                            {
                                user = accountAsSnapshot.getValue(Admin.class);
                            }
                            else
                            {
                                user = accountAsSnapshot.getValue(StandardUser.class);

                            }
                            instance.usersWithNotification.add(user);
                        }
                    }
                    if("BussesCount".equals(snapshot.getKey()))
                    {
                        MapsActivity.busCount = snapshot.getValue(Integer.class);
                    }
                    if("Busses".equals(snapshot.getKey()))
                    {
                        for(DataSnapshot busAsSnapshot : snapshot.getChildren())
                        {
                            Bus bus = busAsSnapshot.getValue(Bus.class);
                            MapsActivity.busses.add(bus);
                        }
                    }
                    if("RoutesCount".equals(snapshot.getKey()))
                    {
                        MapsActivity.routeCount = snapshot.getValue(Integer.class);
                    }
                    if("Routes".equals(snapshot.getKey()))
                    {
                        for(DataSnapshot routeAsSnapshot : snapshot.getChildren())
                        {
                            Route route = routeAsSnapshot.getValue(Route.class);
                            MapsActivity.routes.add(route);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getUsername(DataSnapshot accountAsSnapshot) {
        for(DataSnapshot fieldOfUser : accountAsSnapshot.getChildren())
        {
            if("username".equals(fieldOfUser.getKey()))
            {
                return fieldOfUser.getValue(String.class);
            }
        }
        return null;
    }


    public <T> void pushData(String place, ArrayList<T> listToAdd) {

        reference = rootNode.getReference();
        int idToAdd = 0;
        if(place.equals("Station"))
        {
            idToAdd = MapsActivity.stationCount + 1;
            MapsActivity.stationCount += listToAdd.size();
            reference.child("StationCount").setValue(MapsActivity.stationCount);
        }
        if(place.equals("Accounts"))
        {
            idToAdd = MainActivity.userCount + 1;
            MainActivity.userCount += listToAdd.size();
            reference.child("AccountsCount").setValue(MainActivity.userCount);
        }
        if(place.equals("Routes"))
        {
            idToAdd = MapsActivity.routeCount + 1;
            MapsActivity.routeCount += listToAdd.size();
            reference.child("RoutesCount").setValue(MapsActivity.routeCount);
        }
        if(place.equals("Busses"))
        {
            idToAdd = MapsActivity.busCount + 1;
            MapsActivity.busCount += listToAdd.size();
            reference.child("BussesCount").setValue(MapsActivity.busCount);
        }
        reference = rootNode.getReference(place);
        for (T element : listToAdd) {
            reference.child(String.valueOf(idToAdd)).setValue(element);
            idToAdd++;
        }
    }

}
