package com.example.geolocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final private int REQUEST_GEO_CODE = 34;
    private Button geoButton;
    private TextView position;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        position = findViewById(R.id.position);
//        position.setVisibility(View.INVISIBLE);
        geoButton = findViewById(R.id.getGeo);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        geoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPermissions()) {
                    getGeolocation();
                }
            }
        });
    }

    public boolean getPermissions() {
        String geo_perm = Manifest.permission.ACCESS_FINE_LOCATION;
        if(ActivityCompat.checkSelfPermission(
                this, geo_perm) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{geo_perm}, REQUEST_GEO_CODE);
            return false;
        }
        return true;
    }

    private void getGeolocation(){
        if (position.getVisibility() == View.INVISIBLE){
            position.setVisibility(View.VISIBLE);
        }
        try {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new MyLoccationListener(), null);
        }catch (SecurityException e){}
    }

    private class MyLoccationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            position.setText(
                    "Lat: " + location.getLatitude() + "\nLon: " + location.getLongitude());
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_GEO_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Location is accessible", Toast.LENGTH_LONG).show();
                }
                else{
                    geoButton.setVisibility(View.INVISIBLE);
                    new CountDownTimer(10000, 1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            position.setText("" + (millisUntilFinished / 1000) + "!");
                        }

                        @Override
                        public void onFinish() {
                            geoButton.setVisibility(View.VISIBLE);
                            position.setText("");
                        }
                    }.start();
                }
            }
        }
}