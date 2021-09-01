package com.example.myvehiclesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;

public class DashActivity extends AppCompatActivity {

    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;

    Button logout , showMap;

    static DashActivity instance;
    public static DashActivity getInstance(){
        return instance;
    }
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        instance = this;

        mLatitudeTextView = (TextView) findViewById((R.id.latitude));
        mLongitudeTextView = (TextView) findViewById((R.id.longitude));

        logout = findViewById(R.id.logout);
        showMap = findViewById(R.id.show_map);

        if(ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    DashActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE
            );
        }else{
            startLocationService();
        }

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DashActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
        });

        showMap.setOnClickListener(v -> {
            Intent intent = new Intent(DashActivity.this , MapsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.stop).setOnClickListener(v -> stopLocationService());
    }

    public void ShowInCars(View view){
        Intent intent = new Intent(DashActivity.this,ShowActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            for(ActivityManager.RunningServiceInfo service:
                    activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(service.service.getClassName())){
                    if(service.foreground){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationService(){
        if(isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocation(String latitude, String longitude){
        DashActivity.this.runOnUiThread(() -> {
            mLatitudeTextView.setText("Latitude: " + latitude);
            mLongitudeTextView.setText("Longitude: " + longitude);

            AddDataIntoFirebase(latitude, longitude);
        });
    }

    private void AddDataIntoFirebase(String valueOfLatitude, String valueOfLongitude) {
        DatabaseReference locationRoot = FirebaseDatabase.getInstance().getReference().child("Location");
        DatabaseReference polyRoot = FirebaseDatabase.getInstance().getReference().child("Polylines");
        String uid = FirebaseAuth.getInstance().getUid();

        HashMap<String,String> main = new HashMap<>();
        main.put("Latitude",valueOfLatitude);
        main.put("Longitude",valueOfLongitude);

        locationRoot.child(uid).setValue(main).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });

        polyRoot.child(uid).child(Calendar.getInstance().getTime().toString()).setValue(main).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

            }
        });
    }


}