package org.altervista.alecat.swimmanager.data;

import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Alessandro Cattapan on 01/09/2017.
 */

public class Course extends CourseInfo{

    // New private Variables
    private ObservableSnapshotArray<CourseDay> dates;
    private ObservableSnapshotArray<String> swimmers;

    public Course(CourseInfo course, ObservableSnapshotArray<String> swimmers, ObservableSnapshotArray<CourseDay> dates){
        this.name = course.getName();
        this.numLesson = course.getNumLesson();
        this.numSwimmers = course.getNumSwimmers();
        this.trainer = course.getTrainer();
        this.swimmers = swimmers;
        this.dates = dates;
    }

    public Course(CourseInfo course, DatabaseReference courseRef){
        this.name = course.getName();
        this.numLesson = course.getNumLesson();
        this.numSwimmers = course.getNumSwimmers();
        this.trainer = course.getTrainer();

        // Get Snapshot array of Dates
        DatabaseReference dateNode = courseRef.child(SwimmerContract.NODE_DATE);
        this.dates = new FirebaseArray<CourseDay>(dateNode, CourseDay.class);

        // Get Snapshot array of Swimmers
        DatabaseReference swimmerNode = courseRef.child(SwimmerContract.NODE_SWIMMER);
        this.swimmers = new FirebaseArray<String>(swimmerNode, String.class);
    }

    public Course(String name, String trainer, int numLesson, int numSwimmers, ObservableSnapshotArray<String> swimmers, ObservableSnapshotArray<CourseDay> dates) {
        this.name = name;
        this.trainer = trainer;
        this.numLesson = numLesson;
        this.numSwimmers = numSwimmers;
        this.swimmers = swimmers;
        this.dates = dates;
    }

    public ObservableSnapshotArray<String> getSwimmers(){
        return swimmers;
    }

    public ObservableSnapshotArray<CourseDay> getDates(){
        return dates;
    }

    public void setDates(ObservableSnapshotArray<CourseDay> dates){
        this.dates = dates;
    }

    public void setSwimmers(ObservableSnapshotArray<String> swimmers){
        this.swimmers = swimmers;
    }
}
