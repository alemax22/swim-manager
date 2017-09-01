package org.altervista.alecat.swimmanager.data;

/**
 * Created by Alessandro Cattapan on 01/09/2017.
 */

public class CourseInfo {

    // Private variables
    protected String name;
    protected int numLesson;
    protected int numSwimmers;
    protected String trainer;

    public CourseInfo(){}

    public CourseInfo(String name, String trainer, int numLesson, int numSwimmers) {
        this.name = name;
        this.trainer = trainer;
        this.numLesson = numLesson;
        this.numSwimmers = numSwimmers;
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
        return numSwimmers;
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
        this.numSwimmers = numSwimmers;
    }
}
