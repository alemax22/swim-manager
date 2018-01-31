package org.altervista.alecat.swimmanager.models;

import android.util.Log;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Scanner;

/**
 * Created by Alessandro Cattapan on 08/01/2018.
 */

public class Timing {

    // Build a formatter
    // minutes:seconds.milliseconds
    private PeriodFormatter formatter = new PeriodFormatterBuilder()
            .printZeroAlways()
            .appendMinutes()
            .appendSeparator(":")
            .appendSeconds()
            .appendSeparator(".")
            .printZeroNever()
            .appendMillis()
            .toFormatter();
    // Build a formatter
    // hour:minutes:seconds.milliseconds
    private PeriodFormatter formatterWithHour = new PeriodFormatterBuilder()
            .printZeroAlways()
            .appendHours()
            .appendSeparator(":")
            .appendMinutes()
            .appendSeparator(":")
            .appendSeconds()
            .appendSeparator(".")
            .printZeroNever()
            .appendMillis()
            .toFormatter();
    private Duration time;

    public Timing(String time){
        time = normalizeTimeString(time);
        if (hasHour(time)){
            this.time = formatterWithHour.parsePeriod(time).toStandardDuration();
        } else {
            this.time = formatter.parsePeriod(time).toStandardDuration();
        }
    }

    public Timing(long millis){
        time = new Duration(millis);
    }

    public Duration getTimeDuration() {
        return time;
    }

    public void setTimeDuration(Duration time) {
        this.time = time;
    }

    public long getTimeMillis(){
        return time.getMillis();
    }

    public void setTimeMillis(long millis){
        time = new Duration(millis);
    }

    private String normalizeTimeString(String timeString){
        int index = timeString.indexOf(".");
        String prefix = timeString.substring(0,index);
        String millis = timeString.substring(index + 1);
        if (millis.length() < 3){
            if (millis.length() == 1){
                millis = millis + "00";
            } else {
                millis = millis + "0";
            }
            return prefix + "." + millis;
        }
        return timeString;
    }

    private boolean hasHour(String time){
        // hh:mm:ss.000
        if (time.length() > 9){
            return true;
        }
        return false;
    }
}
