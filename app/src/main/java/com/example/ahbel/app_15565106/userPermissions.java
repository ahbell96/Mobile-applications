package com.example.ahbel.app_15565106;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class userPermissions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_permissions);


    }
    public void addLocation(View view) {
        ActivityCompat.requestPermissions(userPermissions.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
        // requesting permission to access the fine location within the manifest file, for the userPermissions activity (this activity.), then put it into a string array?
        // this gives me access to the GPS level location data.


    // from workshop 2.
        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case 1: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the work!
                        // display short notification stating permission granted
                        Toast.makeText(userPermissions.this, "Permission granted to access location!", Toast.LENGTH_SHORT).show();

                    } else {

                        // permission denied, add code to deal with this!
                    }
                    return;
                }
            }
        }

    public void cameraPermissions(View view) {

        ActivityCompat.requestPermissions(userPermissions.this,
                new String[]{Manifest.permission.CAMERA},
                1);
    }
}
