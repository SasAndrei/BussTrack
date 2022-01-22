package com.example.busstrack;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.busstrack.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import Traffic.Route;
import Traffic.Station;
import Utils.Coordinate;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME = 1000; // 1 second
    private final long MIN_DIST = 5; // 5 Meters

    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference;

    public static ArrayList<Station> stations = new ArrayList<>();
    ArrayList<Route> routes = new ArrayList<>();
    Dialog dialog;



    private static LatLng latLng;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        binding.runOnUIThread(new Runnable(){
//            public void run(){
//
//            }
//        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dialog = new Dialog(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near cluj, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setMinZoomPreference(13.0f);
        mMap.setMaxZoomPreference(20.0f);


        LatLng clujCenter = new LatLng(46.772483, 23.595355);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(clujCenter));
        //load Stations
//        System.out.println("HEREEEEEEEEE1");
//        System.out.println("Station is empty: " +  stations.isEmpty());
//        for (Station station: stations)
//        {
//            System.out.println(station);
//        }



//        reference = rootNode.getReference("Station");

//
//        int stationNumber = 0;
//        for (Station station: stations)
//        {
//            stationNumber++;
//            reference.child(String.valueOf(stationNumber)).setValue(station);
//        }


        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        System.out.println("HEREEEEEEEEE2");



        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                try {
                    mMap.clear();
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());

//                    fire.<Station>getData("Station", Station.class);
//                    loadStationsOnMap(stations);
//                    loadStationInfo();

                    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.profile_undefined);
                    image = Bitmap.createScaledBitmap(image, 70, 70, false);
                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(image)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    new GoogleMap.OnMarkerClickListener() {

                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            return false;
                        }
                    };
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }


    public void updateDatabase() {
        //reference = rootNode.getReference("Station");
       // reference.child("1").removeValue();
    }

    public void getStations() {

        reference = rootNode.getReference("Station");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stations.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Station station = snapshot.getValue(Station.class);
                    stations.add(station);
                    loadStationOnMap(station);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void loadStationsOnMap(ArrayList<Station> stations)
    {
        for (Station station: stations)
        {
            loadStationOnMap(station);
        }
    }
    public void loadStationOnMap(Station station) {
        latLng = new LatLng(station.Longitude.asDouble(), station.Latitude.asDouble());
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.bus_marker);
        image = Bitmap.createScaledBitmap(image, 70, 70, false);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(image)));
        //marker.setInfoWindowAnchor(20, 30);

    }

    public void loadStationInfo()
    {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {

                dialog.setContentView(R.layout.station_popup);
                dialog.show();
                return true;
            }
        });
    }
}