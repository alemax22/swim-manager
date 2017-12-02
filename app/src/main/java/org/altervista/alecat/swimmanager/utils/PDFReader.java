package org.altervista.alecat.swimmanager.utils;

import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;

/**
 * Created by Alessandro Cattapan on 24/11/2017.
 */

public class PDFReader {

    private static final String TAG = PDFReader.class.getSimpleName();

    private InputStream mInputStream;

    public PDFReader(InputStream stream){
        mInputStream = stream;
    }

    public String getData() throws MalformedURLException {
        //TODO: Read data from PDF
        String out = "";
        try {
            PdfReader reader = new PdfReader(mInputStream);
            Log.v(TAG,"Number of pages:" + reader.getNumberOfPages());
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            TextExtractionStrategy strategy;
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                out += strategy.getResultantText();
            }
            reader.close();
        }
        catch (IOException e){
            Log.e(TAG, "Error while parsing PDF path!");
        }
        return out;
    }


}
