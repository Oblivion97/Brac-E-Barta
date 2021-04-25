package com.brac.bracebatra.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.brac.bracebatra.R;

public class SchoolActivity extends AppCompatActivity {

    String strSchool;
    String strTotalStudents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_);


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            strSchool = bundle.getString("SchoolName");
            strTotalStudents = bundle.getString("TotalStudents");
        }

        TextView tvSchool = (TextView) findViewById(R.id.tv_school_name_school);
        tvSchool.setText(strSchool);
    }

    public void OnTakeAttendance(View view) {
        Intent intent = new Intent(SchoolActivity.this,AttendanceActivity.class);
        intent.putExtra("SchoolName",strSchool);
        intent.putExtra("TotalStudents",strTotalStudents);
        intent.putExtra("queryInstituteId",strTotalStudents);
        intent.putExtra("option","School");
        startActivity(intent);
    }

    public void onMoney(View view) {

    }
}
