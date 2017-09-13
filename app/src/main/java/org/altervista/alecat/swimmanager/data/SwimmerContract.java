package org.altervista.alecat.swimmanager.data;

/**
 * Created by Alessandro Cattapan on 23/08/2017.
 */

public final class SwimmerContract {

    // Private constructor
    private SwimmerContract() {
    }

    // Date Format
    public static final String DATE_FORMAT = "dd MMM yyyy";

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

    // Reference
    public static final String REFERENCE = "firebase-reference";

    // Firebase main nodes
    public static final String NODE_SWIMMER_INFO = "swimmer";
    public static final String NODE_SWIMMER_COURSE_ACTIVE = "swimmer-course";
    public static final String NODE_SWIMMER_COURSE_ARCHIVED = "swimmer-course-archived";
    public static final String NODE_COURSE_ACTIVE = "course";
    public static final String NODE_COURSE_ARCHIVED = "course-archived";
    public static final String NODE_TRAINER = "trainer";
    public static final String NODE_TRAINER_COURSE_ACTIVE = "trainer-course";
    public static final String NODE_TRAINER_COURSE_ARCGIVED = "trainer-course-archived";

    // Firebase other useful nodes' name
    public static final String NODE_DATE = "date";
    public static final String NODE_SWIMMER = "swimmer";
    public static final String NODE_SEASON = "season-";

}
