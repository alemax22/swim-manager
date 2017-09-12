package org.altervista.alecat.swimmanager.models;

/**
 * Created by Alessandro Cattapan on 01/09/2017.
 */

public class Course {

    // Private variables
    protected String name;
    protected int numLesson;
    protected int numLessonDone;
    protected int numSwimmer;
    protected String trainer;

    public Course(){}

    public Course(String name, String trainer, int numLesson, int numSwimmers) {
        this.name = name;
        this.trainer = trainer;
        this.numLesson = numLesson;
        this.numSwimmer = numSwimmers;
        this.numLessonDone = 0;
    }

    public Course(String name, String trainer, int numLesson, int numLessonDone, int numSwimmers) {
        this.name = name;
        this.trainer = trainer;
        this.numLesson = numLesson;
        this.numSwimmer = numSwimmers;
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

    public int getNumSwimmers(){
        return numSwimmer;
    }

    public int getNumLessonDone() {
        return numLessonDone;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTrainer(String trainer){
        this.trainer = trainer;
    }

    public void setNumLesson(int numLesson){
        this.numLesson = numLesson;
    }

    public void setNumSwimmers(int numSwimmers){
        this.numSwimmer = numSwimmers;
    }

    public void setNumLessonDone(int numLessonDone) {
        this.numLessonDone = numLessonDone;
    }
}