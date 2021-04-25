package com.brac.bracebatra.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 *
 */

@Table(name = "Student")
public class Student extends Model{

    @Column(name = "sid")
    public String  sid ;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Column(name = "instituteId")
    public String  instituteId ;

    @Column(name = "studentId")
    public String  studentId ="null";

    @Column(name = "studentFirstName")
    public String  studentFirstName = "null";

    @Column(name = "isAttend")
    public boolean  isAttend =true;

    @Column(name = "hasUniform")
    public boolean  hasUniform = true;

    @Column(name = "gradeId")
    public String  gradeId ;

    @Column(name = "queryInstituteId")
    public String  queryInstituteId ;

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getQueryInstituteId() {
        return queryInstituteId;
    }

    public void setQueryInstituteId(String queryInstituteId) {
        this.queryInstituteId = queryInstituteId;
    }

    public Student() {
    }

    public boolean isAttend() {
        return isAttend;
    }

    public void setAttend(boolean attend) {
        isAttend = attend;
    }

    public boolean isHasUniform() {
        return hasUniform;
    }

    public void setHasUniform(boolean hasUniform) {
        this.hasUniform = hasUniform;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }
}
