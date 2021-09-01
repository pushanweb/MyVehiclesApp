package com.example.myvehiclesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class DetailsActivity extends AppCompatActivity {

    TextView markertext,numbertext;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        markertext=findViewById(R.id.marker);
        numbertext=findViewById(R.id.number);
        title=getIntent().getStringExtra("title");
        DatabaseReference userRoot = FirebaseDatabase.getInstance().getReference().child("Users");
        userRoot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                String uid = snapshot.getKey();
                if(uid.equals(title)){
                    markertext.setText(snapshot.child("name").getValue().toString());
                    numbertext.setText(snapshot.child("carnum").getValue().toString());
                }

            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    public void Show(View view){
        Intent intent=new Intent(DetailsActivity.this,MapsActivity2.class);
        intent.putExtra("key",title);
        startActivity(intent);
    }
    public void Delete(View view){
        DatabaseReference poly=FirebaseDatabase.getInstance().getReference("Polylines").child(title);
        poly.removeValue();
        Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
    }
}