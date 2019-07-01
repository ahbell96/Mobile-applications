package com.example.ahbel.app_15565106;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView tvTemp, tvCity, tvBrief, tvCurDate, tvLat, tvLong;
    SimpleDateFormat datetime;
    public double lati, longi;
    String locationProvider;
    public LocationManager LM;
    public Location lastKnownLocation;
    Button read, write;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTemp = (TextView) findViewById(R.id.textTemp);
        tvCity = (TextView) findViewById(R.id.textCity);
        tvBrief = (TextView) findViewById(R.id.textDesc);
        tvCurDate = (TextView) findViewById(R.id.textDate);
        tvLat = (TextView) findViewById(R.id.textLat);
        tvLong = (TextView) findViewById(R.id.textLong);
        read = (Button) findViewById(R.id.getData);
        write = (Button) findViewById(R.id.saveButton);

        getWeather();
    }

    public void readData (View view) {
        try {
            FileInputStream fileInputStream = openFileInput("Saved_Weather_Data.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String Lines;
            while((Lines=bufferedReader.readLine())!=null) {
                stringBuffer.append(Lines + "\n");
            }

            String path = getFilesDir().getAbsolutePath();
            Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData(View view) {
        try {
            String Data = tvTemp.getText().toString() + "\n" + tvCity.getText().toString() + "\n" + tvBrief.getText().toString()
                    + "\n" + tvCurDate.getText().toString() + "\n" + tvLong.getText().toString() + "\t" + tvLat.getText().toString();
            FileOutputStream fileOutputStream = openFileOutput("Saved_Weather_Data.txt", MODE_PRIVATE);
            fileOutputStream.write(Data.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Weather Data Saved!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    //public void clearData(View view) {
     //   try {
      //      PrintWriter printWriter = new PrintWriter("Saved_Weather_Data.txt");
       //     printWriter.print("");
        //    printWriter.close();
        //    Toast.makeText(getApplicationContext(), "Data Cleared!", Toast.LENGTH_SHORT).show();
       // }
       // catch(FileNotFoundException e) {
       //     e.printStackTrace();
       // }  catch (IOException e) {
       //     e.printStackTrace();
       // }
   // }

    public void getWeather() {
        requestLocation();
        // getting the latitude and longitude URL from openweathermap API.
        String LatLongURL = "http://api.openweathermap.org/data/2.5/weather?lat="+lati+"&lon="+longi+"&appid=b92f000d49babe439a419d0164c08c0b&units=Metric";
        JsonObjectRequest JSOR = new JsonObjectRequest(Request.Method.GET, LatLongURL, null, new Response.Listener<JSONObject>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // retrieving all the key information from the API
                    // including the main, the coordinates, the weather, temperature etcetera.
                    // adapts according to the latitude and longitude coordinates given to it.
                    JSONObject mainobject = response.getJSONObject("main");
                    JSONObject coordObject = response.getJSONObject("coord");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(mainobject.getDouble("temp"));
                    String description = object.getString("description");
                    String city = response.getString("name");
                    String Latitude, Longitude;
                    Longitude = String.valueOf(coordObject.getDouble("lon"));
                    Latitude = String.valueOf(coordObject.getDouble("lat"));

                    // providing a seperate date and time, in relation to the location of the user, not the location.
                    datetime = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    String cDateAndTime = datetime.format(new Date());

                    // simply displaying the wanted information through the text views.
                    tvTemp.setText(temp + "c");
                    tvCity.setText(city);
                    tvBrief.setText(description);
                    tvCurDate.setText(cDateAndTime);
                    tvLong.setText(String.valueOf(Longitude));
                    tvLat.setText(String.valueOf(Latitude));
                    // convert the temperature into double.

                    double tInt = Double.parseDouble(temp);
                    tvTemp.setText(String.valueOf(tInt));

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(JSOR);
    }

    public void requestLocation() {

        // Workshop material - (Workshop 2)
        LM = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationProvider = LocationManager.GPS_PROVIDER;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastKnownLocation = LM.getLastKnownLocation(locationProvider);

        if(lastKnownLocation == null) {
            lati = 0;
            longi = 0;
        }
        else{
            lati = lastKnownLocation.getLatitude();
            longi = lastKnownLocation.getLongitude();
        }
    }

    public void getLocation(View view) {
        // create an intent to start the activity called TestActivity
        Intent intent = new Intent(this, Loc.class);
        startActivity(intent);
    }

    public void addPermissions(View view) {
        Intent intent = new Intent(this, userPermissions.class);
        startActivity(intent);
    }

    public void cameraIntent(View view) {
        int REQUEST_IMAGE_CAPTURE = 1;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void gotoGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse
                ("content://media/internal/images/media"));
        startActivity(intent);
    }

    public void openGMaps(View view) {

        // used to open google maps to find the wanted longitude and latitude, especially of where the user is located.
        // this is error checked, to see if the user is connected to either Mobile network or wifi.
        // if the user is using at least one of these, the can continue and use the google maps link.
        // if not, the user has no access.

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_LONG).show();
            getWeather();
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("geo:"+lati+","+longi));
            startActivity(intent);
        }
        else{
            connected = false;
        Toast.makeText(MainActivity.this, "Connection Failed!", Toast.LENGTH_LONG).show();
        }
    }
}
