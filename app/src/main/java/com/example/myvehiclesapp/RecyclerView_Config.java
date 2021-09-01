package com.example.myvehiclesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerView_Config {
    Context mContext;
    StudentAdapter mStudentAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Students>students, List<String>keys) {
        mContext = context;
        mStudentAdapter = new StudentAdapter(students, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mStudentAdapter);
    }

    class StudentItemView extends RecyclerView.ViewHolder{
        TextView mName;
        TextView mCarnum;


        String key;

        public StudentItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.user_list_item,parent,false));

            mName = (TextView) itemView.findViewById(R.id.name);
            mCarnum = (TextView) itemView.findViewById(R.id.carnum);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Details2Activity.class);
                    intent.putExtra("key",key);
                    intent.putExtra("name",mName.getText().toString());
                    intent.putExtra("carnum",mCarnum.getText().toString());

                    mContext.startActivity(intent);
                }
            });
        }
        public void bind(Students students, String key) {
            mName.setText(students.getName());
            mCarnum.setText(students.getCarnum());

            this.key = key;
        }

    }
    class StudentAdapter extends RecyclerView.Adapter<StudentItemView>{
        List<Students> mStudentList;
        List<String> mKeys;

        public StudentAdapter(List<Students> mStudentList, List<String> mKeys) {
            this.mStudentList = mStudentList;
            this.mKeys = mKeys;
        }

        public StudentAdapter() {
            super();
        }

        @NonNull
        @NotNull
        @Override
        public StudentItemView onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new StudentItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView_Config.StudentItemView holder, int position) {
            holder.bind(mStudentList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mStudentList.size();
        }
    }
}
