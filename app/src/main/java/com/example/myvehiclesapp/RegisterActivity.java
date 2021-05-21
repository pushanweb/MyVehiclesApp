package com.example.myvehiclesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {

    EditText emailRegister , passwordRegister;
    Button signUpButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialization();

        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpToGoogle();
            }
        });
    }

    private void signUpToGoogle() {
        String email = emailRegister.getText().toString();
        String password = passwordRegister.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String uID = mAuth.getCurrentUser().getUid();
                Toast.makeText(RegisterActivity.this,uID, Toast.LENGTH_SHORT).show();
                profileIntent();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(RegisterActivity.this, "User Already Registered", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void profileIntent() {
        Intent profileActivityIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileActivityIntent);
    }

    private void initialization() {
        emailRegister = findViewById(R.id.user_name);
        passwordRegister = findViewById(R.id.user_mobile_number);
        signUpButton = findViewById(R.id.signup_btn);
    }
}