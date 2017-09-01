package org.altervista.alecat.swimmanager.data;

import java.util.ArrayList;

/**
 * Created by Alessandro Cattapan on 01/09/2017.
 */

public class Course extends CourseInfo{

    // New private Variables
    private ArrayList<CourseDay> dates;
    private ArrayList<String> swimmers;

    public Course(CourseInfo course, ArrayList<String> swimmers, ArrayList<CourseDay> dates){
        this.name = course.getName();
        this.numLesson = course.getNumLesson();
        this.numSwimmers =course.getNumSwimmers();
        this.trainer = course.getTrainer();
        this.swimmers = swimmers;
        this.dates = dates;
    }

    public Course(String name, String trainer, int numLesson, int numSwimmers, ArrayList<String> swimmers, ArrayList<CourseDay> dates) {
        this.name = name;
        this.trainer = trainer;
        this.numLesson = numLesson;
        this.numSwimmers = numSwimmers;
        this.swimmers = swimmers;
        this.dates = dates;
    }

    public ArrayList<String> getSwimmers(){
        return swimmers;
    }

    public ArrayList<CourseDay> getDates(){
        return dates;
    }

    public void setDates(ArrayList<CourseDay> dates){
        this.dates = dates;
    }

    public void setSwimmers(ArrayList<String> swimmers){
        this.swimmers = swimmers;
    }
}
