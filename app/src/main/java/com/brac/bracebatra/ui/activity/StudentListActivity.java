package com.brac.bracebatra.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Institute;
import com.brac.bracebatra.model.Student;
import com.brac.bracebatra.ui.adapter.SchoolAdapter;
import com.brac.bracebatra.ui.adapter.StudentAdapter;
import com.brac.bracebatra.util.util;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    List<Student> instituteList = new ArrayList<Student>();
    RecyclerView recyclerView;
    StudentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);


        adapter = new StudentAdapter(instituteList, getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.student_list);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setAdapter(adapter);

        loadDate();
    }
    private void loadDate() {
        List<Student> institutes = new Select().from(Student.class).execute();
        if (institutes.size() > 0) {
            instituteList.addAll(institutes);
        }
        Toast.makeText(StudentListActivity.this, "Total Student =" + institutes.size() + " " + util.CURRENT_PO, Toast.LENGTH_SHORT).show();
    }

}
