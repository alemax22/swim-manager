package org.altervista.alecat.swimmanager.models;

/**
 * Created by Alessandro Cattapan on 01/09/2017.
 */

public class CourseDay {

    private String day;
    private boolean done;

    public CourseDay(){

    }

    public CourseDay(String day, boolean done){
        this.day = day;
        this.done = done;
    }

    public CourseDay(String day){
        this.day = day;
        this.done = false;
    }

    public String getDay(){
        return day;
    }

    public boolean getDone(){
        return done;
    }

    public void setDay(String day){
        this.day = day;
    }

    public void setDone(boolean done){
        this.done = done;
    }
}
