package com.example.lad.myapplication;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    private void initLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();


        assert(!locationManager.getAllProviders().isEmpty());
        for (Iterator<String> i = locationManager.getAllProviders().iterator();i.hasNext();) {
            Log.i(TAG, "Provider: " + i.next());
        }
        String provider = locationManager.getBestProvider(criteria, true);


        if // (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                (  ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i(TAG, "No permission ....");
            return;
        }
        Log.i(TAG, "Provider:" + provider);
        this.location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        assert(location != null);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        initLocation();
        // Getting the name of the best provider

        String myAdr = "";
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 10);

            for (Address a : addresses) {
                for(int i = 0; i < a.getMaxAddressLineIndex(); i++) {
                    myAdr = myAdr + a.getAddressLine(i) + "\n";
                   // Log.i(TAG,"My adress: " + a.getAddressLine(i));
                }
            }

        } catch (IOException e) {
            Log.e(TAG,"Error: " + e.getMessage());
        }

        LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        // LatLng vienna = new LatLng(48, 16);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(currentPos).title(myAdr));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
    }
}
