package com.example.busstrack;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Accounts.Admin;
import Accounts.User;
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
                ArrayList<T> tArrayList = new ArrayList<>();
                T element = null;
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
                            Admin user = accountAsSnapshot.getValue(Admin.class);
                            instance.usersWithNotification.add(user);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        reference = rootNode.getReference(place);
        for (T element : listToAdd) {
            reference.child(String.valueOf(idToAdd)).setValue(element);
            idToAdd++;
        }
    }

}
