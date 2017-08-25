package org.altervista.alecat.swimmanager.data;

/**
 * Created by Alessandro Cattapan on 23/08/2017.
 */

public final class SwimmerContract {

    // Private constructor
    private SwimmerContract() {
    }

    // Useful constants inside this app
    // Swimmer's genders
    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    // Swimmer's levels
    public static final int LEVEL_UNKNOWN = 0;
    public static final int LEVEL_ONE = 1;
    public static final int LEVEL_TWO = 2;
    public static final int LEVEL_THREE = 3;
    public static final int LEVEL_FOUR = 4;

    // Firebase main nodes
    public static final String NODE_SWIMMER_INFO = "swimmers-info";
    public static final String NODE_SWIMMER_COURSE_ACTIVE = "swimmers-course-active";
    public static final String NODE_SWIMMER_COURSE_ARCHIVED = "swimmers-course-archived";
    public static final String NODE_COURSE_ACTIVE = "courses-active";
    public static final String NODE_COURSE_ARCHIVED = "courses-archived";
    public static final String NODE_TRAINER = "trainers";
    public static final String NODE_TRAINER_COURSE_ACTIVE = "trainers-courses-active";
    public static final String NODE_TRAINER_COURSE_ARCGIVED = "trainers-courses-archived";

    // Firebase other useful nodes' name
    public static final String NODE_DATE = "dates";
    public static final String NODE_INFO = "info";
    public static final String NODE_SWIMMER = "swimmers";
    public static final String NODE_SEASON = "season-";

}
