package com.example.busstrack;

import androidx.annotation.NonNull;

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

    public <T> void getData(String place, ArrayList<T> tArrayList) {

        reference = rootNode.getReference(place);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    T element = (T) snapshot.getValue();
                    tArrayList.add(element);
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
