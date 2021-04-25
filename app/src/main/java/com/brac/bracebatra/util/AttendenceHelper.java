package com.brac.bracebatra.util;

import android.util.Log;

import com.brac.bracebatra.model.AttendenceHelperModel;
import com.brac.bracebatra.model.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Amit on 11/8/2017.
 */

public class AttendenceHelper {
    public Map<String,AttendenceHelperModel> attendance;
    public Map<String,Boolean> uniform;



    public AttendenceHelper(List<Student> students) {
        this.attendance = new HashMap<String, AttendenceHelperModel>();

        init(students);

    }
    public void init(List<Student> students){
        for(Student student:students)
        {
            attendance.put(student.studentId,new AttendenceHelperModel(student.isHasUniform(),student.isAttend,student.getStudentFirstName(),student.instituteId));

        }
    }
    public JSONObject getAttendenceInJSON() throws JSONException {
        JSONObject attendenceObj=new JSONObject();



        JSONArray attendenceArray=new JSONArray();

        String key;
        AttendenceHelperModel value=new AttendenceHelperModel();
        for(Map.Entry<String, AttendenceHelperModel> e : attendance.entrySet()) {
            key = e.getKey();
            value = e.getValue();
            JSONObject tempJSON=new JSONObject();
            tempJSON.put("id","");
            //tempJSON.put("unifrom",value.hasUniform);
            tempJSON.put("attend",value.isAttend);
            tempJSON.put("fullName",value.fullName);
            tempJSON.put("instituteId",value.instituteId);
            tempJSON.put("studentId",key);
            tempJSON.put("Date",value.fullName);

            attendenceArray.put(tempJSON);


        }
       attendenceObj.put("Attendencs",attendenceArray);
        Log.d("AttendenceArray",attendenceArray.toString());



        return attendenceObj;
    }
}
