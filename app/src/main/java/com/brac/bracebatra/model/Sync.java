package com.brac.bracebatra.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by hhson on 11/1/2017.
 */

@Table(name = "Model" , id = "Id")
public class Sync  extends Model{

    @Column(name = "isSynced")
    public boolean isSynced ;

    @Column(name = "Title")
    public String Title ;

    @Column(name = "date")
    public String date;

    @Column(name = "attendence")
    public  String attendence;

    @Column(name = "schoolId")
    public  String schoolId;


    public Sync() {
    }
}
