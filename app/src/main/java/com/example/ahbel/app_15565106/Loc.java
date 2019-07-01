package com.example.ahbel.app_15565106;

import java.text.DecimalFormat;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.*;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class Loc extends AppCompatActivity {

    public double lati, longi;
    String Filename = "Location_Data";
    String locationProvider;
    String Uri;
    public TextView tv1;
    public LocationManager LM;
    public Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // workshop material
        tv1 = new TextView(this);
        // creating an instance of textview.
        tv1.setTextSize(45);
        setContentView(tv1);

        LM = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationProvider = LocationManager.GPS_PROVIDER;

        lastKnownLocation = LM.getLastKnownLocation(locationProvider);

        if(lastKnownLocation == null) {
            lati = 0;
            longi = 0;
        }
        else{
            lati = lastKnownLocation.getLatitude();
            longi = lastKnownLocation.getLongitude();
        }

        // adding decimal formatting to latitude and longitude coordinates.
        DecimalFormat DecFor = new DecimalFormat("#0.00");
        Uri = "Lat:\n" + DecFor.format(lati) + "\nLong:\n" + DecFor.format(longi);
        tv1.setText(Uri);

    }
}
