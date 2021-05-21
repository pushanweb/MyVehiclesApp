package com.example.myvehiclesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    EditText name , phoneNUmber;
    Button submit;

    FirebaseAuth mAuth;
    DatabaseReference UserDB = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialization();
        mAuth = FirebaseAuth.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String phoneNUmberText = phoneNUmber.getText().toString();

                HashMap<String,String> main = new HashMap<>();
                main.put("Name",userName);
                main.put("PhoneNumber",phoneNUmberText);

                String uid = mAuth.getCurrentUser().getUid();

                UserDB.child("Users").child(uid).setValue(main).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(ProfileActivity.this, "successful", Toast.LENGTH_SHORT).show();

                        MapsActivityIntent();
                    }
                });
            }
        });
    }

    private void MapsActivityIntent() {
        Intent mapsIntent = new Intent(this, StartActivity.class);
        startActivity(mapsIntent);
    }

    private void initialization() {
        name = findViewById(R.id.user_name);
        phoneNUmber = findViewById(R.id.user_mobile_number);

        submit = findViewById(R.id.signup_btn);
    }
}