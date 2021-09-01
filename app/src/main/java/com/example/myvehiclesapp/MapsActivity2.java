package com.example.myvehiclesapp;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myvehiclesapp.databinding.ActivityMaps2Binding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    ArrayList<LatLng> array = new ArrayList<LatLng>();
    ArrayList<String> timeArray=new ArrayList<String>();
    DatabaseReference myref;
    String latitude,longitude,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        uid = getIntent().getStringExtra("key");

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

        PolylineOptions rect = new PolylineOptions();

        myref = FirebaseDatabase.getInstance().getReference("Polylines").child(uid);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String time=ds.getKey();
                    timeArray.add(time);
                    latitude = ds.child("Latitude").getValue().toString();
                    longitude = ds.child("Longitude").getValue().toString();

                    LatLng mypos = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    array.add(mypos);
                    rect.add(mypos);

                }
                if(array.size()>0){
                    mMap.addMarker(new MarkerOptions().position(array.get(0)).title("Start at " + timeArray.get(0))
                            .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_arrow_forward_ios_30)));
                    for(int i=1;i < array.size()-1;i++){
                        mMap.addMarker(new MarkerOptions().position(array.get(i)).title(timeArray.get(i))
                                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_lens_24)));
                    }
                    mMap.addMarker(new MarkerOptions().position(array.get(array.size()-1)).title("End at "+timeArray.get(timeArray.size()-1))
                            .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_block_24)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(array.get(array.size()-1),17));
                    mMap.addPolyline(rect);
                }
                else{
                    DatabaseReference location = FirebaseDatabase.getInstance().getReference("Location").child(uid);
                    location.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            latitude = snapshot.child("Latitude").getValue().toString();
                            longitude = snapshot.child("Longitude").getValue().toString();

                            LatLng mypos = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                            mMap.addMarker(new MarkerOptions().position(mypos).title("Last Location"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mypos,17));

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResid){
        Drawable vectorDrawable= ContextCompat.getDrawable(context,vectorResid);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicWidth());
        Bitmap bitmap=Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}