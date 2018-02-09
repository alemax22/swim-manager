package org.altervista.alecat.swimmanager.utils;

import android.nfc.Tag;
import android.util.Log;

import org.altervista.alecat.swimmanager.interfaces.FinPdfReader;
import org.altervista.alecat.swimmanager.models.CompetitionResult;
import org.altervista.alecat.swimmanager.models.Rank;
import org.altervista.alecat.swimmanager.models.Swimmer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Alessandro Cattapan on 26/12/2017.
 */

public class RankManager {

    private String TAG = RankManager.class.getSimpleName();

    private FinPdfReader finPdfReader;
    private ArrayList<Rank> rankList = null;
    private String teamName =  null;
    private ArrayList<CompetitionResult> competitionResultsList = null;
    private String competitionName = null;
    private String date = null;
    private String place = null;
    private int poolMeters = -1;
    private int timingType = -1;

    public RankManager(FinPdfReader finPdfReader, String teamName){
        this.finPdfReader = finPdfReader;
        this.teamName = teamName.toUpperCase();
    }

    public ArrayList<CompetitionResult> getResult(){
        if (competitionResultsList == null){
            // Create the list
            competitionResultsList = new ArrayList<CompetitionResult>();

            ArrayList<Rank> individualRankList = getIndividualRaceRank();
            ArrayList<Rank> relayRankList = getRelayRaceRank();


            Log.v(TAG,"Empty List created!");

            // Get Individual race result
            for (int i = 0; i < individualRankList.size(); i++){
                Log.v(TAG, "Search inside rank number: " + i + " of total " + individualRankList.size());
                searchIndividualResult(individualRankList.get(i));
            }

            // Get relay race result
            for (int i = 0; i < relayRankList.size(); i++){
               Log.v(TAG, "Search inside relay rank number: " + i + " of total " + relayRankList.size());
               searchRelayResult(relayRankList.get(i));
            }

        }
        return competitionResultsList;
    }

    public ArrayList<Rank> getRelayRaceRank(){
        ArrayList<Rank> relayRankList = new ArrayList<Rank>();
        if (rankList == null){
            getAllRank();
        }
        if (rankList.size() == 0){
            Log.e(TAG, "Error while searching ranks inside pdf!");
        }
        for (int i = 0; i < rankList.size(); i++){
            Rank rank = rankList.get(i);
            if (rank.getRace().isRelayRace()){
                relayRankList.add(rank);
            }
        }
       return relayRankList;
    }

    public ArrayList<Rank> getIndividualRaceRank(){
        ArrayList<Rank> individualRankList = new ArrayList<Rank>();
        if (rankList == null){
            getAllRank();
        }
        if (rankList.size() == 0){
            Log.e(TAG, "Error while searching ranks inside pdf!");
        }
        for (int i = 0; i < rankList.size(); i++){
            Rank rank = rankList.get(i);
            if (!rank.getRace().isRelayRace()){
                individualRankList.add(rank);
            }
        }
        return individualRankList;
    }

    public void setTeamName(String name){
        teamName = name.toUpperCase();
    }

    public String getTeamName(){
        return teamName;
    }

    public ArrayList<Rank> getAllRank(){
        if (rankList == null){
            try {
                rankList = finPdfReader.getAllRank();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error while searching ranks inside pdf!");
            }
        }
        return rankList;
    }

    public String getCompetitionName() {
        if (rankList == null){
            getAllRank();
        }
        if (competitionName == null){
            if (rankList.size() == 0){
                Log.e(TAG,"There are no ranks inside PDF!");
            } else {
                competitionName = rankList.get(0).getCompetitionName();
            }
        }
        return competitionName;
    }

    public String getDate() {
        if (rankList == null){
            getAllRank();
        }
        if (date == null){
            if (rankList.size() == 0){
                Log.e(TAG,"There are no ranks inside PDF!");
            } else {
                date = rankList.get(0).getDate();
            }
        }
        return date;
    }

    public String getPlace() {
        if (rankList == null){
            getAllRank();
        }
        if (place == null){
            if (rankList.size() == 0){
                Log.e(TAG,"There are no ranks inside PDF!");
            } else {
               place = rankList.get(0).getPlace();
            }
        }
        return place;
    }

    public int getPoolMeters() {
        if (rankList == null){
            getAllRank();
        }
        if (poolMeters < 0 ){
            if (rankList.size() == 0){
                Log.e(TAG,"There are no ranks inside PDF!");
            } else {
                poolMeters = rankList.get(0).getPoolMeters();
            }
        }
        return poolMeters;
    }

    public int getTimingType() {
        if (rankList == null){
            getAllRank();
        }
        if (timingType < 0){
            if (rankList.size() == 0){
                Log.e(TAG,"There are no ranks inside PDF!");
            } else {
                timingType = rankList.get(0).getTimingType();
            }
        }
        return timingType;
    }

    private void searchIndividualResult(Rank rank){
        Scanner scanner = new Scanner(rank.getRank());
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if (line.contains(teamName)) {
                if (!line.substring(0,1).equals("-")) {
                    Scanner console = new Scanner(line);
                    Log.v(TAG, "Line: " + line);
                    console.useDelimiter(teamName);
                    String string = console.next(); // Ex: "1 1 1 SURNAME NAME 2000 "
                    String timeString = console.next(); // Ex: " 00:34.1 POINTS"
                    Scanner timeScanner = new Scanner(timeString);
                    Timing time = new Timing(timeScanner.next());
                    // String points = timeScanner.next(); // It can contain "FG", it can also be empty!!
                    timeScanner.close();
                    // TODO: Extract Swimmer
                    Swimmer swimmer = getSwimmer(string);
                    competitionResultsList.add(
                            new CompetitionResult(getCompetitionName(),
                                    getPlace(),
                                    getDate(),
                                    rank.getRace(),
                                    getPoolMeters(),
                                    time.getTimeMillis(),
                                    getTimingType(),
                                    swimmer));
                    Log.v(TAG, "Added new Result");
                    console.close();
                }
            }
        }
        scanner.close();
    }

    private Swimmer getSwimmer(String line){
        // TODO: Extract Swimmer
        Scanner scanner = new Scanner(line);
        scanner.close();
        return new Swimmer();
    }

    private void searchRelayResult(Rank rank){
        Scanner scanner = new Scanner(rank.getRank());
        while (scanner.hasNextLine()){
            LinkedList<String> lines = new LinkedList<String>();
            // Create a group of five lines
            for (int i = 0; i <= 4 && scanner.hasNextLine(); i++){
                lines.addLast(scanner.nextLine());
            }
            if (lines.getLast().contains(teamName)){
                if (!lines.getLast().substring(0,1).equals("-")) {
                    // TODO: Extract Swimmer
                    // Swimmers
                    Swimmer swimmer1 = new Swimmer();
                    Swimmer swimmer2 = new Swimmer();
                    Swimmer swimmer3 = new Swimmer();
                    Swimmer swimmer4 = new Swimmer();

                    Scanner timeScanner = new Scanner(lines.getLast());
                    timeScanner.useDelimiter(teamName);
                    timeScanner.next(); // Ex: "1 1 1 "
                    String line = timeScanner.next(); // Ex: " 00:00.000 FG"
                    timeScanner.close();
                    Scanner s = new Scanner(line);
                    String timeString = s.next();
                    Timing time = new Timing(timeString);
                    s.close();

                    competitionResultsList.add(
                            new CompetitionResult(getCompetitionName(),
                                    getPlace(),
                                    getDate(),
                                    rank.getRace(),
                                    getPoolMeters(),
                                    time.getTimeMillis(),
                                    getTimingType(),
                                    swimmer1,
                                    swimmer2,
                                    swimmer3,
                                    swimmer4));
                }
            }
        }
        scanner.close();
    }
}
