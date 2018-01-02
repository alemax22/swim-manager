package org.altervista.alecat.swimmanager.models;

import java.util.ArrayList;

/**
 * Created by Alessandro Cattapan on 15/12/2017.
 */

public class CompetitionResult {
    private String competitionName;
    private ArrayList<Swimmer> swimmerList; // If it contains only one swimmer it is an individual race
    private String place;
    private String date;
    private Race race; // 100 Stile Libero
    private int poolLength;
    private int time; // milliseconds
    private int timingType;

    public CompetitionResult(String competitionName, String place, String date, Race race, int poolLength, Swimmer swimmer, int time, int timingType) {
        this.competitionName = competitionName;
        this.place = place;
        this.date = date;
        this.race = race;
        this.poolLength = poolLength;
        swimmerList.add(swimmer);
        this.time = time;
        this.timingType = timingType;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public int getPoolLength() {
        return poolLength;
    }

    public void setPoolLength(int poolLength) {
        this.poolLength = poolLength;
    }

    public ArrayList<Swimmer> getSwimmer() {
        return swimmerList;
    }

    public Swimmer getFirstSwimmer() {
        return swimmerList.remove(0);
    }

    public void addSwimmer(Swimmer swimmer) {
        swimmerList.add(swimmer);
    }

    public int getNumberOfSwimmer(){
        return swimmerList.size();
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimingType() {
        return timingType;
    }

    public void setTimingType(int timingType){
        this.timingType = timingType;
    }
}
