package org.altervista.alecat.swimmanager.models;

import java.util.ArrayList;

/**
 * Created by Alessandro Cattapan on 01/09/2017.
 */

public class Course {

    // Private variables
    private String name;
    private int numLesson;
    private int numLessonDone;
    private int numSwimmer;
    private String trainer;
    private ArrayList<String> swimmer;
    private ArrayList<CourseDay> date;

    public Course(){}

    public Course(String name, String trainer, int numLesson, int numSwimmer, ArrayList<String> swimmer, ArrayList<CourseDay> date) {
        this.name = name;
        this.trainer = trainer;
        this.numLesson = numLesson;
        this.numSwimmer = numSwimmer;
        this.numLessonDone = 0;
        this.swimmer = swimmer;
        this.date = date;
    }

    public Course(String name, String trainer, int numLesson, int numLessonDone, int numSwimmer) {
        this.name = name;
        this.trainer = trainer;
        this.numLesson = numLesson;
        this.numSwimmer = numSwimmer;
        this.numLessonDone = numLessonDone;
    }

    public String getName(){
        return name;
    }

    public String getTrainer(){
        return trainer;
    }

    public int getNumLesson(){
        return numLesson;
    }

    public int getNumSwimmer(){
        return numSwimmer;
    }

    public int getNumLessonDone() {
        return numLessonDone;
    }

    public ArrayList<String> getSwimmer() {return swimmer;}

    public ArrayList<CourseDay> getDate() {return date;}

    public void setName(String name){
        this.name = name;
    }

    public void setTrainer(String trainer){
        this.trainer = trainer;
    }

    public void setNumLesson(int numLesson){
        this.numLesson = numLesson;
    }

    public void setNumSwimmer(int numSwimmers){
        this.numSwimmer = numSwimmers;
    }

    public void setNumLessonDone(int numLessonDone) {
        this.numLessonDone = numLessonDone;
    }

    public void setSwimmer(ArrayList<String> swimmer){
        this.swimmer = swimmer;
    }

    public void setdate(ArrayList<CourseDay> date){
        this.date = date;
    }
}
