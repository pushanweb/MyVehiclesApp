package com.example.myvehiclesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import java.util.List;

public class ShowActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        mRecyclerView = findViewById(R.id.recyclerview_students);
        new DatabaseHelper().readStudents(new DatabaseHelper.DataStatus(){
            @Override
            public void DataIsLoaded(List<Students> students, List<String>keys) {
                new RecyclerView_Config().setConfig(mRecyclerView,ShowActivity.this, students, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

    }

}