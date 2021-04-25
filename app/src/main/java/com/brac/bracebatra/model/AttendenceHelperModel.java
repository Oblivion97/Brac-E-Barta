package com.brac.bracebatra.model;

/**
 * Created by Amit on 11/8/2017.
 */

public class AttendenceHelperModel {

    public Boolean hasUniform;
    public Boolean isAttend;
    public String fullName;
    public String instituteId;


    public AttendenceHelperModel(Boolean hasUniform, Boolean isAttend, String fullName, String instituteId) {
        this.hasUniform = hasUniform;
        this.isAttend = isAttend;
        this.fullName = fullName;
        this.instituteId = instituteId;
    }

    public AttendenceHelperModel() {

    }



}
