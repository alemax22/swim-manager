package org.altervista.alecat.swimmanager.models;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Scanner;

/**
 * Created by Alessandro Cattapan on 26/12/2017.
 */

public class Race implements Comparable<Race>{

    private static final String RELAY_RACE_1 = "4x";
    private static final String RELAY_RACE_2 = "4X";
    private static final String TAG = Race.class.getSimpleName();

    private int length;
    private String style;
    private boolean isRelayRace;

    public Race(int length, String style, boolean isRelayRace) {
        this.length = length;
        this.style = style;
        this.isRelayRace = isRelayRace;
    }

    // TODO: Fix this method
    public Race(String race){
        this.isRelayRace = race.contains(RELAY_RACE_1) || race.contains(RELAY_RACE_2);
        Scanner scanner = new Scanner(race);
        if (isRelayRace){
            style = race;
        } else {
            length = scanner.nextInt();
            style = race;
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isRelayRace() {
        return isRelayRace;
    }

    public void setRelayRace(boolean relayRace) {
        isRelayRace = relayRace;
    }

    // TODO: Fix this method
    @Override
    public int compareTo(@NonNull Race race) {
        return this.toString().compareTo(race.toString());
    }

    // TODO: Fix this method
    @Override
    public String toString() {
        if (isRelayRace){
            return style;
        }
        return style;
    }
}
