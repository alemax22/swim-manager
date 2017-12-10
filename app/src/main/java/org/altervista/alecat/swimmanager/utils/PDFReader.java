package org.altervista.alecat.swimmanager.utils;

import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import org.altervista.alecat.swimmanager.data.SwimmerContract;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Scanner;

/**
 * Created by Alessandro Cattapan on 24/11/2017.
 */

public class PDFReader {

    private static final String TAG = PDFReader.class.getSimpleName();

    private InputStream mInputStream;

    // TODO: Delete hard-coded variables below this line
    private String[] mRaces = {"50 Stile Libero","100 Misti","50 Rana","200 Stile Libero","100 Nuoto Ostacoli"};
    private String mSocietyName1 = "ANTARES NUOTO";
    private String mSocietyName2 = "OBIETTIVO ACQUA";

    // Public constructor
    public PDFReader(InputStream stream){
        mInputStream = stream;
    }

    public String getData() throws MalformedURLException {
        // TODO: Read data from PDF
        String out = "";
        String info = "";
        try {
            PdfReader reader = new PdfReader(mInputStream);
            Log.v(TAG,"Number of pages:" + reader.getNumberOfPages());
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            TextExtractionStrategy strategy;
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {

                strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                out += strategy.getResultantText() + "\n";

                if (i == 1){
                    info = getCircuitInfo(out);
                }
            }
            reader.close();
        }
        catch (IOException e){
            Log.e(TAG, "Error while parsing PDF path!");
        }
        return info + out;
    }

    public String getCircuitInfo(String page){
        Scanner scanner = new Scanner(page);
        String circuitName = scanner.nextLine();
        String race = null;
        String place = null;
        String date = null;
        int meters = 0;
        int gender = 0;

        // Skip three lines
        for (int i = 0; i < 3; i++) {
            scanner.nextLine();
        }

        // Determine race
        String line = scanner.nextLine();
        for (int i = 0; i < mRaces.length; i++) {
            if (line.contains(mRaces[i])){
                race = mRaces[i];
            }
        }

        // Check if race exists
        if (race == null){
            Log.v(TAG, "No RACE found in page number: 1");
        }

        // Determine sex
        if (line.contains("Femmine")){
            gender = SwimmerContract.GENDER_MALE;
        } else if (line.contains("Maschi")) {
            gender = SwimmerContract.GENDER_MALE;
        } else {
            gender = SwimmerContract.GENDER_UNKNOWN;
        }

        // Determine place and date
        line = scanner.nextLine();
        Scanner s = new Scanner(line).useDelimiter(",");
        place = s.next();
        date = s.next();
        s.close();
        Scanner console =  new Scanner(date);
        console.next();
        date = console.next();
        console.close();

        // Meter
        line = scanner.nextLine();
        if (line.contains("25")){
            meters = 25;
        } else if (line.contains("50")){
            meters = 50;
        } else {
            Log.v(TAG, "Error while determine meters!");
        }

        scanner.close();

        StringBuilder builder = new StringBuilder(circuitName);
        builder.append("\n");
        builder.append("Gara: ");
        builder.append(race);
        builder.append("\n");
        builder.append("Sesso: ");
        builder.append(gender);
        builder.append("\n");
        builder.append("Luogo: ");
        builder.append(place);
        builder.append("\n");
        builder.append("Data: ");
        builder.append(date);
        builder.append("\n");
        builder.append("Base Vasca: ");
        builder.append(meters);
        builder.append("\n");

        return builder.toString();
    }

    // This method returns if there is or not an athlete from the society
    private boolean someoneFromSociety(String line){
        return line.contains(mSocietyName1);
    }
}
