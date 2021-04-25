package com.brac.bracebatra.ui.activity;

import android.content.Intent;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Attendance;
import com.brac.bracebatra.model.AttendenceHelperModel;
import com.brac.bracebatra.model.Event;
import com.brac.bracebatra.model.Institute;
import com.brac.bracebatra.model.Student;
import com.brac.bracebatra.model.Sync;
import com.brac.bracebatra.ui.adapter.AttendanceAdapter;
import com.brac.bracebatra.ui.adapter.EventsAdapter;
import com.brac.bracebatra.util.AttendanceSync;
import com.brac.bracebatra.util.AttendenceHelper;
import com.brac.bracebatra.util.util;
import com.fastaccess.datetimepicker.DatePickerFragmentDialog;
import com.fastaccess.datetimepicker.callback.DatePickerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity implements AttendanceSync,DatePickerCallback {
    List<Student> attendanceList = new ArrayList<Student>();
    RecyclerView recyclerView;
    AttendanceAdapter adapter;
    private String strSchool;
    private String strDate;

    TextView tvSchoolName;
    TextView tvTotalStudents;
    TextView tvdate;

    public AttendenceHelper attendenceHelper;

    String parent;
    private Long updateID;
    public String querySchoolId="";

    HashMap<String,Student> attendenceMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        tvSchoolName = (TextView) findViewById(R.id.school_name_ta);
        tvTotalStudents = (TextView) findViewById(R.id.tv_att_total_students);
        tvdate= (TextView) findViewById( R.id.date_picker);
        attendenceMap=new HashMap<String, Student>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            parent = bundle.getString("option");

            if (parent.equals("School")) {
                strSchool = bundle.getString("SchoolName");
                tvSchoolName.setText(strSchool);
                tvTotalStudents.setText(bundle.getString("TotalStudents"));
                querySchoolId=bundle.getString("queryInstituteId");
                Log.d("queryInstituteId",querySchoolId);
            } else if (parent.equals("Edit")) {

                strSchool = bundle.getString("SchoolName");
                strDate = bundle.getString("date");
                tvSchoolName.setText(strSchool);
            }
        }
        adapter = new AttendanceAdapter(attendanceList, getApplicationContext(), this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_attendance);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.date_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragmentDialog.newInstance().show(getSupportFragmentManager(), "DatePickerFragmentDialog");
            }
        });
        if (parent.equals("School")) {
            addData();
        } else if (parent.equals("Edit")) {
            tvdate.setEnabled(false);
            // load data for edit
            attendenceMap.clear();
            loadData();
            tvTotalStudents.setText(attendanceList.size()+"");
        }
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    String key;
                    Student value;
                    JSONArray attendences=new JSONArray();
                    for(Map.Entry<String, Student> e : attendenceMap.entrySet()) {
                        key = e.getKey();
                        value = e.getValue();
                        JSONObject tempJSON=new JSONObject();
                        tempJSON.put("id","");
                        tempJSON.put("unifrom",value.hasUniform);
                        tempJSON.put("attend",value.isAttend);
                        tempJSON.put("instituteId",value.queryInstituteId);
                        tempJSON.put("studentId",key);
                        tempJSON.put("gradeId",value.getGradeId());
                        tempJSON.put("attendDate",tvdate.getText());
                        tempJSON.put("fullName",value.getStudentFirstName());
                        tempJSON.put("sid",value.sid);



                        attendences.put(tempJSON);


                    }
                    Log.d("Attendence==",attendences.toString());


                    if (parent.equals("School")) {
                        List<Sync> syncs=new Select().from(Sync.class).execute();
                        boolean isDateExist=false;
                        for(Sync sy:syncs)
                        {
                            if((sy.date.equals(tvdate.getText().toString())&&sy.schoolId.equals(querySchoolId))&&sy.Title.equals(strSchool))
                            {
                                isDateExist=true;
                                break;
                            }
                        }
                        if(isDateExist)
                        {
                            Toast.makeText(AttendanceActivity.this, "Please select another date (Date exist)", Toast.LENGTH_SHORT).show();
                        }
                        else if(tvdate.getText().equals("Pick a Date")||tvdate.getText().toString().contains("Pick"))
                        {
                            Toast.makeText(AttendanceActivity.this, "Please Pick a Date", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Sync sync = new Sync();
                            sync.date = tvdate.getText().toString();
                            sync.Title = strSchool;
                            sync.attendence = attendences.toString();
                            sync.isSynced = false;
                            sync.schoolId=querySchoolId;
                            sync.save();
                            startActivity(new Intent(AttendanceActivity.this, ProgramOrganizerActivity.class));

                        }
                    } else {
                        boolean isDateExist=false;
                        Sync sync = Sync.load(Sync.class, updateID);

                        Log.d("Update Table ", updateID.toString());
                        sync.attendence = attendences.toString();
                        sync.save();

                        /*new Update(Sync.class)
                                .set("attendence = ?",attendences.toString())
                                .where("id = ? ",updateID)
                        .execute();*/
                        startActivity(new Intent(AttendanceActivity.this, ProgramOrganizerActivity.class));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        // notihong :p
    }


    private void loadData() {

        List<Sync> students = new Select().from(Sync.class).where("Title = ? AND date = ?", strSchool, strDate).execute();
        tvdate.setText(strDate);
        attendenceMap.clear();

        updateID = students.get(0).getId();
        if (students.size() > 0) {
            String attendence = students.get(0).attendence;
            try {


                JSONArray jsonArray = new JSONArray(attendence);
                Log.d("LoadData",jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject stdOb = jsonArray.getJSONObject(i);
                    Student student = new Student();
                    student.setStudentId(stdOb.getString("studentId"));
                    student.setStudentFirstName(stdOb.getString("fullName"));
                    student.setAttend(stdOb.getString("attend").equals("true"));
                    student.setHasUniform(stdOb.optString("unifrom").equals("true"));
                    student.setSid(stdOb.getString("sid"));
                    student.queryInstituteId=(stdOb.getString("instituteId"));

                    attendenceMap.put(student.getSid(),student);
                    Log.d(student.getStudentFirstName(),student.isAttend+"");
                    attendanceList.add(student);
                    adapter.notifyDataSetChanged();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //attendenceHelper = new AttendenceHelper(attendanceList);
        }
    }

    private void addData() {

        List<Student> students = new Select().from(Student.class).where("queryInstituteId = ?", querySchoolId).execute();
        for(Student student:students)
        {
            Log.d("Student","{\n"+student.getStudentFirstName()+"\n"+student.getQueryInstituteId()+"\n"+student.getSid()+"\n"+student.getInstituteId()+"\n"+student.getStudentId()+"\n}\n");
            attendenceMap.put(student.getSid(),student);
        }

        if (students.size() > 0) {
            attendanceList.addAll(students);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onAttCheck(String id, Boolean isAttend, Boolean hasUniform,Student student) {
       try{
           Student std=student;
           student.isAttend=isAttend;
           student.hasUniform=hasUniform;
           attendenceMap.put(std.getSid(),student);
       }
       catch (Exception ex)
       {
           ex.printStackTrace();
       }
    }

    @Override
    public void onDateSet(long date) {
        Toast.makeText(this, getDate(date), Toast.LENGTH_SHORT).show();
        tvdate.setText(getDate(date));

    }
    String getDate(long val)
    {
        Date date=new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yy");
        String dateText = df2.format(date);

        return dateText;
    }
}
