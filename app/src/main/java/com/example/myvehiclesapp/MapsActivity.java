package com.example.myvehiclesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myvehiclesapp.databinding.ActivityMapsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    String latitude , longitude;
    Marker currentLocationMarker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



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



        DatabaseReference locationRoot = FirebaseDatabase.getInstance().getReference().child("Location");

        locationRoot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                String uid = snapshot.getKey();
                latitude = snapshot.child("Latitude").getValue().toString();
                longitude = snapshot.child("Longitude").getValue().toString();

                LatLng mypos = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                currentLocationMarker = mMap.addMarker(new MarkerOptions().position(mypos).title(uid).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_directions_car_24)));
                if(uid.equals(FirebaseAuth.getInstance().getUid())){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mypos,17));
                }

            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                currentLocationMarker.remove();
                String uid = snapshot.getKey();
                latitude = snapshot.child("Latitude").getValue().toString();
                longitude = snapshot.child("Longitude").getValue().toString();

                LatLng mypos = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                currentLocationMarker = mMap.addMarker(new MarkerOptions().position(mypos).title(uid).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_directions_car_24)));

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                currentLocationMarker.remove();
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                String markertitle = marker.getTitle();
                Intent intent=new Intent(MapsActivity.this,DetailsActivity.class);
                intent.putExtra("title",markertitle);
                startActivity(intent);
                return false;
            }
        });

    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context,int vectorResid){
        Drawable vectorDrawable= ContextCompat.getDrawable(context,vectorResid);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicWidth());
        Bitmap bitmap=Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}