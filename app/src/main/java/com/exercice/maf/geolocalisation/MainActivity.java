package com.exercice.maf.geolocalisation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Reader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText longitude;
    private EditText latitude;
    private TextView resultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        longitude= (EditText) findViewById(R.id.editTxtLongitude);
        latitude=(EditText) findViewById(R.id.editTxtLatitude);
        resultat=(TextView) findViewById(R.id.EditTxtResultat);
    }

    public void chercherLieu (View v){

        String txtLongitude= longitude.getText().toString();
        String txtLatitude=latitude.getText().toString();

        AsyncTaskGeoSearch asyncTaskGeoSearch = new AsyncTaskGeoSearch(this,txtLongitude,txtLatitude);
        asyncTaskGeoSearch.execute();




    }
}
