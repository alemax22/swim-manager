package org.altervista.alecat.swimmanager.models;

import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap<String, Boolean> swimmer;
    private HashMap<String, Boolean> date;

    public Course(){}

    public Course(String name, String trainer, int numLesson, int numSwimmer, HashMap<String, Boolean> swimmer, HashMap<String, Boolean> date) {
        this.name = name;
        this.trainer = trainer;
        this.numLesson = numLesson;
        this.numSwimmer = numSwimmer;
        this.numLessonDone = 0;
        this.swimmer = swimmer;
        this.date = date;
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

    public HashMap<String, Boolean> getSwimmer() {return swimmer;}

    public HashMap<String, Boolean> getDate() {return date;}

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

    public void setSwimmer(HashMap<String, Boolean> swimmer){
        this.swimmer = swimmer;
    }

    public void setDate(HashMap<String, Boolean> date){
        this.date = date;
    }
}
