package org.altervista.alecat.swimmanager.models;

import android.util.Log;

import org.altervista.alecat.swimmanager.data.SwimmerContract;
import org.altervista.alecat.swimmanager.error.RankInfoException;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static org.altervista.alecat.swimmanager.data.SwimmerContract.DATE_FORMAT;
import static org.altervista.alecat.swimmanager.data.SwimmerContract.DATE_PDF_INPUT_FORMAT;
import static org.altervista.alecat.swimmanager.data.SwimmerContract.TIMING_AUTOMATIC;
import static org.altervista.alecat.swimmanager.data.SwimmerContract.TIMING_MANUAL;
import static org.altervista.alecat.swimmanager.data.SwimmerContract.TIMING_UNKNOWN;

/**
 * Created by Alessandro Cattapan on 24/12/2017.
 */

public class Rank {

    private static final String TAG = Rank.class.getSimpleName();

    private static final String SERIE_DELIMITER = "Serie Cat.:";
    private static final String ORE_DELIMITER = "ore";
    private static final String VASCA_DELIMITER = "Base v.:";
    private static final String CRONOMETRAGGIO_DELIMITER = "Cron:";
    private static final String GENDER_MALE_STRING = "Maschi";
    private static final String GENDER_FEMALE_STRING = "Femmine";
    private static final String TIMING_MANUAL_STRING = "M";
    private static final String TIMING_AUTOMATIC_STRING = "A";
    private static final String COMMA_DELIMITER = ",";
    private static final String DASH_DELIMITER = "-";

    private String info;
    private String rank;
    private String competitionName = null;
    private String date = null;
    private String place = null;
    private Race race = null;
    private int gender = -1;
    private int poolMeters = -1;
    private int timingType = -1;
    private boolean hasInfo = false;
    private boolean hasRank = false;

    public Rank(String competitionName){
        this.info = null;
        this.rank = null;
        this.competitionName = competitionName;
    }

    public Rank(String info, String rank, String competitionName) {
        this.info = info;
        this.rank = rank;
        this.competitionName = competitionName;
        if (info != null){
            hasInfo = true;
        }
        if (rank != null){
            hasRank = true;
        }
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        if (info != null){
            hasInfo = true;
        }
        this.info = info;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        if (rank != null){
            hasRank = true;
        }
        this.rank = rank;
    }

    public void setCompetitionName(String name){
        competitionName = name;
    }

    public boolean hasRank(){
        return hasRank;
    }

    public boolean hasInfo(){
        return hasInfo;
    }

    /**
     * This method merges two ranks from two different pages into a single rank.
     * It keeps the info String from the first rank, so it is not a good idea to
     * merge different ranks with different info String
     * @param rankToMerge
     * @return rank1 merged
     */
    public void mergeRank(Rank rankToMerge){
        this.setRank(this.getRank() + System.getProperty("line.separator") + rankToMerge.getRank());
        if (this.hasInfo() && rankToMerge.hasInfo()){
            Log.e("Rank", "There might be an error while merging ranks!");
        }
    }

    /**
     * This method returns circuitName
     * @return circuitName
     */
    public String getCompetitionName(){
        return competitionName;
    }

    /**
     * This method searches for the race inside the info String and it retuns an
     * error when you call it in an object with no info String
     * @return race
     * @throws RankInfoException
     */
    public Race getRace(){
        if (!hasInfo){
            throw new RankInfoException();
        }
        if (race == null){
            Scanner scanner = new Scanner(info);
            scanner.useDelimiter(SERIE_DELIMITER);
            race = new Race(scanner.next().trim());
            scanner.close();
        }
        return race;
    }

    /** This method searches for gender inside the info String and it returns an
     * error when you call it in an object with no info String
     * @return gender
     * @throws RankInfoException
     * */
    public int getGender(){
        if (!hasInfo){
            throw new RankInfoException();
        }
        if (gender < 0 ){
            if (info.contains(GENDER_MALE_STRING)){
                gender =  SwimmerContract.GENDER_MALE;
            } else if (info.contains(GENDER_FEMALE_STRING)){
                gender =  SwimmerContract.GENDER_FEMALE;
            } else {
                gender = SwimmerContract.GENDER_UNKNOWN;
            }
        }
        return gender;
    }

    /** This method searches for the place inside the info String and it returns an
     * error when you call it in an object with no info String
     * @return place
     * @throws RankInfoException
     * */
    public String getPlace(){
        if (!hasInfo){
            throw new RankInfoException();
        }
        if (place == null){
            Scanner scanner = new Scanner(info);
            scanner.nextLine();
            scanner.useDelimiter(COMMA_DELIMITER);
            place = scanner.next().trim();
            scanner.close();
        }
        return place;
    }

    /** This method searches for the date inside the info String and it returns an
     * error when you call it in an object with no info String
     * @return date
     * @throws RankInfoException
     * */
    public String getDate(){
        if (!hasInfo){
            throw new RankInfoException();
        }
        if (date == null){
            Scanner scanner = new Scanner(info);
            scanner.useDelimiter(ORE_DELIMITER + "|" + COMMA_DELIMITER);
            scanner.next(); // Delete everything that I don't need!
            String output = scanner.next().trim();
            scanner.close();
            Scanner dateScanner = new Scanner(output);
            dateScanner.next(); // Delete day of week
            output = dateScanner.next();
            // Return the date into the app format
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat(DATE_PDF_INPUT_FORMAT);
            Date circuitDate = dateFormatter1.parse(output, new ParsePosition(0));
            SimpleDateFormat dateFormatter2 = new SimpleDateFormat(DATE_FORMAT);
            String dateToDisplay = dateFormatter2.format(circuitDate);
            date = dateToDisplay;
        }
        return date;
    }

    /** This method searches for pool length inside the info String and it returns an
     * error when you call it in an object with no info String
     * @return poolMeters
     * @throws RankInfoException
     * */
    public int getPoolMeters(){
        if (!hasInfo){
            throw new RankInfoException();
        }
        if (poolMeters < 0){
            Scanner scanner = new Scanner(info);
            scanner.useDelimiter(VASCA_DELIMITER);
            scanner.next(); // Delete everything that I don't need!
            String output = scanner.next().trim();
            scanner.close();
            poolMeters = Integer.parseInt(output);
        }
        return poolMeters;
    }

    /** This method searches for pool length inside the info String and it returns an
     * error when you call it in an object with no info String
     * @return chrono
     * @throws RankInfoException
     * */
    public int getTimingType(){
        if (!hasInfo){
            throw new RankInfoException();
        }
        if (timingType < 0){
            Scanner scanner = new Scanner(info);
            scanner.useDelimiter(CRONOMETRAGGIO_DELIMITER + "|" + DASH_DELIMITER);
            scanner.next(); // Delete everything that I don't need!
            String output = scanner.next().trim();
            scanner.close();
            if (output.equalsIgnoreCase(TIMING_MANUAL_STRING)){
                timingType = TIMING_MANUAL;
            } else if (output.equalsIgnoreCase((TIMING_AUTOMATIC_STRING))){
                timingType = TIMING_AUTOMATIC;
            } else {
                timingType = TIMING_UNKNOWN;
                Log.e(TAG, "Error! Timing Type not identified, the String is: " + output);
            }
        }
        return timingType;
    }
}
