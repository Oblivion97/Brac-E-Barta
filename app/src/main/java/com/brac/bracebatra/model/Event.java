package com.brac.bracebatra.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by hhson on 9/27/2017.
 */

@Table(name = "Event")
public class Event extends Model{
    public String getKeynoteSpeaker() {
        return keynoteSpeaker;
    }

    public void setKeynoteSpeaker(String keynoteSpeaker) {
        this.keynoteSpeaker = keynoteSpeaker;
    }

    @Column(name = "eventId")
    public String eventId;

    @Column(name = "keynoteSpeaker")
    public String keynoteSpeaker;


    @Column (name = "title")
    public String title;

    @Column (name = "details")
    public String details;

    @Column (name = "dateTime")
    public String dateTime;

    @Column (name = "location")
    public String location;

    @Column (name = "orgBy")
    public String orgBy;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrgBy() {
        return orgBy;
    }

    public void setOrgBy(String orgBy) {
        this.orgBy = orgBy;
    }
}
