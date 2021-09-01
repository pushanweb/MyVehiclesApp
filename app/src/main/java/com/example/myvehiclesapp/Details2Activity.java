package com.example.myvehiclesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Details2Activity extends AppCompatActivity {

    EditText edtName,edtCarnum;
    Button update,delete,showmap,deletelast;
    TextView user;

    String name,carnum,key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);

        key = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");
        carnum = getIntent().getStringExtra("carnum");



        edtName = findViewById(R.id.edtName);
        edtCarnum = findViewById(R.id.edtCarnum);
        user = findViewById(R.id.user);


        edtName.setText(name);
        edtCarnum.setText(carnum);
        user.setText(key);


        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        showmap = findViewById(R.id.showmap);
        deletelast = findViewById(R.id.deletelast);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Students students = new Students();
                students.setName(edtName.getText().toString());
                students.setCarnum(edtCarnum.getText().toString());


                new DatabaseHelper().updateStudent(key, students, new DatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Students> students, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(Details2Activity.this, "User record has been updated successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatabaseHelper().deleteStudent(key, null, new DatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Students> students, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(Details2Activity.this, "User record has been deleted", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                });
            }
        });
        showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Details2Activity.this,MapsActivity2.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });
        deletelast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference poly= FirebaseDatabase.getInstance().getReference("Polylines").child(key);
                poly.removeValue();
                Toast.makeText(Details2Activity.this, "Deleted Last Activities", Toast.LENGTH_SHORT).show();
            }
        });

    }

}