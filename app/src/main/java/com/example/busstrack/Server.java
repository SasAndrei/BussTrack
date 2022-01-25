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
                    if("Station".equals(snapshot.getKey()))
                    {
                        //put stations into list
                        for(DataSnapshot stationAsSnapshot : snapshot.getChildren())
                        {
                            Station station = stationAsSnapshot.getValue(Station.class);
                            MapsActivity.stations.add(station);
                        }
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

//
//    public <T> void pushData(String place, ArrayList<T> tArrayList) {
//
//        reference = rootNode.getReference("Station");
//        int id = 0;
//        while(reference.child(String.valueOf(id)) != null)
//        {
//            id++;
//        }
//        System.out.println("HELLLLOOOO" + id);
//        for (T element : tArrayList) {
//            id++;
//
//            reference.child(String.valueOf(id)).setValue(element);
//        }
//    }

//


//    public <T> void pushData(String place, ArrayList<T> tArrayList) {
//
//        reference = rootNode.getReference(place);
//        int id = 0;
//        for (T element : tArrayList) {
//            id++;
//            reference.child(String.valueOf(id)).setValue(element);
//        }
//    }

}
