package com.example.busstrack;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Traffic.Station;

public class LoadStations implements Runnable{
    Server fire = new Server();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        fire.<Station>getData("Station", Station.class);
        while (MapsActivity.stations.isEmpty())
        {

        }
        MapsActivity obj = new MapsActivity();
        obj.loadStationsOnMap(MapsActivity.stations);
        obj.loadStationInfo();
    }
}
