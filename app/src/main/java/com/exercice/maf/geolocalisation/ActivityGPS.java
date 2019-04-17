package com.exercice.maf.geolocalisation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import static android.location.LocationManager.*;

public class ActivityGPS extends AppCompatActivity implements LocationListener {

    private TextView latitude;
    private TextView longitude;
    private TextView altitude;
    private LocationManager lm;
    private String choix_source="GPS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        altitude = findViewById(R.id.editTxtAltitudeGPS);
        latitude = findViewById(R.id.editTxtLatitudeGPS);
        longitude = findViewById(R.id.editTxtLongitudeGPS);

        //On récupère le service de localisation
        lm = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }


    public void chercherAdresse (View v){

        String txtLongitude= longitude.getText().toString();
        String txtLatitude=latitude.getText().toString();

        AsyncTaskGeoSearch asyncTaskGeoSearch = new AsyncTaskGeoSearch(this,txtLongitude,txtLatitude,"adresse");
        asyncTaskGeoSearch.execute();


    }

    public void afficherGoogleMap(View v){
        String txtLongitude= longitude.getText().toString();
        String txtLatitude=latitude.getText().toString();
        Intent intentGoogleMap = new Intent(this,MapsActivity.class);
        intentGoogleMap.putExtra("longitude",txtLongitude);
        intentGoogleMap.putExtra("latitude",txtLatitude);
        startActivity(intentGoogleMap);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        Log.d("testGeo","latitude: "+lat);
        Log.d("testGeo", "longitude: "+lon);


        latitude.setText(String.valueOf(lat));
        longitude.setText(String.valueOf(lon));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void afficherSource (View v){
        List<String> providers = lm.getProviders(true);
        final String[] sources = new String[providers.size()];
        int i =0;
        //on stock le nom de ces source dans un tableau de string
        for(String provider : providers) {
            sources[i++] = provider;
            Log.d("testGPS", provider);
        }


        new AlertDialog.Builder(this).setItems(sources,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        findViewById(R.id.btnSource).setEnabled(true);
                    //on stock le choix de la source choisi
                        choix_source = sources[which];
                    //on modifie la barre de titre de l'application
                        setTitle(String.format("%s - %s",
                                getString(R.string.app_name), choix_source));

                    }
                }).create().show();
    }


}
