package org.altervista.alecat.swimmanager.utils;

import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;

import org.altervista.alecat.swimmanager.interfaces.FinPdfReader;
import org.altervista.alecat.swimmanager.models.CompetitionResult;
import org.altervista.alecat.swimmanager.models.Rank;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Alessandro Cattapan on 26/12/2017.
 */

public class RankManager {

    private String TAG = RankManager.class.getSimpleName();

    private FinPdfReader finPdfReader;
    private ArrayList<Rank> rankList = null;
    private ArrayList<CompetitionResult> competitionResultsList = null;
    private String competitionName = null;
    private String date = null;
    private String place = null;
    private int poolMeters = -1;
    private int timingType = -1;

    public RankManager(FinPdfReader finPdfReader){
        this.finPdfReader = finPdfReader;
    }

    public ArrayList<CompetitionResult> getResult(){
        if (competitionResultsList == null){
            ArrayList<Rank> individualRankList = getIndividualRaceRank();
            ArrayList<Rank> relayRakList = getRelayRaceRank();

            // Get Individual race result
            while (individualRankList.size() != 0){
                searchIndividualResult(individualRankList.remove(0));
            }

            // Get relay race result
            while (relayRakList.size() != 0){
                searchRelayResult(relayRakList.remove(0));
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
        // TODO: Complete this method
    }

    private void searchRelayResult(Rank rank){
        // TODO: Complete this method
    }
}
