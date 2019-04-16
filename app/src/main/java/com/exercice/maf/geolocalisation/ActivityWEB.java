package com.exercice.maf.geolocalisation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class ActivityWEB extends AppCompatActivity {

    private WebView browser;
    private String latitude;
    private String longitude;
    private String ville;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_ville);
        Intent thisIntent = getIntent();
        latitude = thisIntent.getExtras().getString("latitude");
        longitude=thisIntent.getExtras().getString("longitude");
        ville=thisIntent.getExtras().getString("ville");
        url=thisIntent.getExtras().getString("url");
        browser= (WebView) findViewById(R.id.webViewGoogleVille);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setUseWideViewPort(true);


        browser.loadUrl(url);


    }
}
