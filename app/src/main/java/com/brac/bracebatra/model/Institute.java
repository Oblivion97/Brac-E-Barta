package com.brac.bracebatra.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by hhsonet on 8/11/2017.
 */

@Table(name = "Institute")
public class Institute extends Model {

    @Column(name = "insId")
    private   String insId;

    @Column(name = "name")
    private   String name;

    @Column(name = "educationType")
    private  String educationType;

    @Column(name = "poId")
    private  String poId;

    @Column(name = "totalStudent")
    private  String totalStudent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducationType() {
        return educationType;
    }

    public void setEducationType(String educationType) {
        this.educationType = educationType;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public String getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(String totalStudent) {
        this.totalStudent = totalStudent;
    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }
}
