package org.altervista.alecat.swimmanager.utils;

import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import org.altervista.alecat.swimmanager.error.PdfStructureException;
import org.altervista.alecat.swimmanager.interfaces.FinPdfReader;
import org.altervista.alecat.swimmanager.models.Rank;
import org.altervista.alecat.swimmanager.models.Swimmer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Alessandro Cattapan on 24/11/2017.
 */

public class MyPDFReader implements FinPdfReader{

    private static final String TAG = MyPDFReader.class.getSimpleName();

    private static final String PAGE_START = "RISULTATI RIEPILOGATIVI";
    private static final String RANK_START = "Pos Ser. Cor Atleta Anno Società Tempo Pti";
    private static final String PAGE_END = "Risultati disponibili online nel sito WWW.FINVENETO.ORG";

    private InputStream mInputStream; // Maybe it is useless!
    private PdfReader mReader;
    private int mNumberPages;

    // Public constructor
    public MyPDFReader(InputStream stream){
        mInputStream = stream;

        try{
            mReader = new PdfReader(mInputStream);
            mNumberPages = mReader.getNumberOfPages();
        } catch (IOException e){
            Log.e(TAG, "Error! InputStream do not work!");
        }
    }

    public int getNumberPages(){
        return mNumberPages;
    }

    // If page does not exist return an error
    public String getPageContent(int number) throws IOException{
        if (number > mNumberPages){
            throw new IOException();
        }
        PdfReaderContentParser parser = new PdfReaderContentParser(mReader);
        TextExtractionStrategy strategy;
        strategy = parser.processContent(number, new SimpleTextExtractionStrategy());
        String page  = strategy.getResultantText();
        return page;
    }

    public Rank getRank(String page) throws PdfStructureException{
        // Check PDF form
        boolean hasPageStart = false;
        boolean hasRankStart = false;
        boolean hasPageEnd = false;

        StringBuilder builder1 = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();
        Scanner scanner = new Scanner(page);
        String line;
        String circuitName;

        // Set Circuit Name
        if (scanner.hasNextLine()){
            circuitName = scanner.nextLine();
        }
        else{
            Log.e(TAG, "The PDF is Empty!");
            throw new PdfStructureException();
        }

        // Remove useless line of PDF
        while(scanner.hasNext()
                && (!(hasPageStart = isPageBeginning(line = scanner.nextLine())))){
           // Go to the next line
        }
        while(scanner.hasNext()
                && hasPageStart
                && (!(hasRankStart = isRankBeginnig(line = scanner.nextLine())))
                && (!(hasPageEnd = isPageEnding(line)))){
            builder1.append(line);
            builder1.append(System.getProperty("line.separator")); // Add new line
        }
        while (scanner.hasNext()
                && hasRankStart
                && (!(hasPageEnd = isPageEnding(line = scanner.nextLine())))){
            builder2.append(line);
            builder2.append(System.getProperty("line.separator")); // Add new line
        }

        Rank rank = new Rank(circuitName);

        if (!hasPageStart){
            // The PDF has not the required structure
            throw new PdfStructureException();
        } else {
            if (!hasPageEnd) {
                Log.e(TAG, "The page has no page end! Maybe you are using a wrong PDF");
            }
            if (!hasRankStart) {
                builder1.setLength(builder1.length()-1); // Delete last line
                rank.setRank(builder1.toString());
            } else {
                builder1.setLength(builder1.length()-1); // Delete last line
                builder2.setLength(builder2.length()-1); // Delete last line
                rank.setInfo(builder1.toString());
                rank.setRank(builder2.toString());
            }
        }
        scanner.close();
        return rank;
    }

    public ArrayList<Rank> getAllRank() throws IOException{
        ArrayList<Rank> rankList = new ArrayList<Rank>();
        for (int i = 1; i <= mNumberPages; i++){
            Rank rank = getRank(getPageContent(i));
            if (rank.hasInfo()){
                rankList.add(rank);
            } else if (rankList.size() > 0){
                Rank rankToMerge = rankList.remove(rankList.size()-1);
                rankToMerge.mergeRank(rank);
                rankList.add(rankToMerge);
            }
        }
        return rankList;
    }

    private boolean isPageBeginning(String line){
        return line.contains(PAGE_START);
    }

    private boolean isRankBeginnig(String line){
        return line.contains(RANK_START);
    }

    private boolean isPageEnding(String line){
        return line.contains(PAGE_END);
    }

    // TODO: Remove this DEBUG METHOD
    public String getCircuitInfo(Rank rank){
        StringBuilder builder = new StringBuilder(rank.getCompetitionName());
        builder.append("\n");
        builder.append("Gara: ");
        builder.append(rank.getRace());
        builder.append("\n");
        builder.append("Sesso: ");
        builder.append(rank.getGender());
        builder.append("\n");
        builder.append("Luogo: ");
        builder.append(rank.getPlace());
        builder.append("\n");
        builder.append("Data: ");
        builder.append(rank.getDate());
        builder.append("\n");
        builder.append("Base Vasca: ");
        builder.append(rank.getPoolMeters());
        builder.append("\n");
        builder.append("Cronometraggio: ");
        builder.append(rank.getTimingType());
        builder.append("\n");
        return builder.toString();
    }

    // TODO: Remove this DEBUG METHOD
    private void buildAtleti(){
        ArrayList<Swimmer> list;
        list = new ArrayList<>();
        Swimmer swimmer = new Swimmer("Elia","Dalla Rizza","23 Jul 2000",1,2);
        list.add(swimmer);
        Swimmer swimmer1 = new Swimmer("Ester","Russo","28 Jul 2000",2,2);
        list.add(swimmer1);
        Swimmer swimmer2 = new Swimmer("Francesco","Salomon","16 Nov 2001",1,2);
        list.add(swimmer2);
        Swimmer swimmer3 = new Swimmer("Tania","Baldassa","31 Oct 1999",2,2);
        list.add(swimmer3);
    }
}
