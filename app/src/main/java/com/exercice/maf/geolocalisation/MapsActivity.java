package com.exercice.maf.geolocalisation;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent thisIntent= getIntent();
        longitude = Double.parseDouble(thisIntent.getExtras().getString("longitude"));
        latitude= Double.parseDouble(thisIntent.getExtras().getString("latitude"));


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("testMap","on passe dans onMapready()");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude,longitude), 15));
       /*googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Coucou")
        );*/
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
