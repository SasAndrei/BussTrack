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

import Traffic.Station;

public class Server {

    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public <T> void getData(String place, Class<T> typeClass) {

        reference = rootNode.getReference(place);
        System.out.println("We are in getData");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<T> tArrayList = new ArrayList<>();
                T element = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    element = snapshot.getValue(typeClass);
                    tArrayList.add(element);
                }
                if(element instanceof Station)
                {
                    MapsActivity.stations = (ArrayList<Station>) tArrayList;
                    System.out.println("We are in onDataChange");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public <T> void pushData(String place, ArrayList<T> tArrayList) {

        reference = rootNode.getReference(place);
        int id = 0;
        for (T element : tArrayList) {
            id++;

            reference.child(String.valueOf(id)).setValue(element);
        }
    }

}
