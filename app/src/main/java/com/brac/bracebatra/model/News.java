package com.brac.bracebatra.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by hhson on 9/25/2017.
 */

@Table(name = "News")
public class News extends Model{

    @Column (name = "nId")
    public String nId;

    @Column (name = "nTitle")
    public String nTitle;

    @Column (name = "nBody")
    public String nBody;

    @Column (name = "nDate")
    public String nDate;

    @Column (name = "nPostBy")
    public String nPostBy;


    public News() {
    }

    public String getnId() {
        return nId;
    }

    public void setnId(String nId) {
        this.nId = nId;
    }

    public String getnTitle() {
        return nTitle;
    }

    public void setnTitle(String nTitle) {
        this.nTitle = nTitle;
    }

    public String getnBody() {
        return nBody;
    }

    public void setnBody(String nBody) {
        this.nBody = nBody;
    }

    public String getnDate() {
        return nDate;
    }

    public void setnDate(String nDate) {
        this.nDate = nDate;
    }

    public String getnPostBy() {
        return nPostBy;
    }

    public void setnPostBy(String nPostBy) {
        this.nPostBy = nPostBy;
    }
}
