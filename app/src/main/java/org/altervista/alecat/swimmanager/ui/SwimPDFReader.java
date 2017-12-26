package org.altervista.alecat.swimmanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.models.CompetitionResult;
import org.altervista.alecat.swimmanager.models.Rank;
import org.altervista.alecat.swimmanager.utils.MyPDFReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class SwimPDFReader extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 1;
    private static final String TAG = SwimPDFReader.class.getSimpleName();

    private InputStream mInputStream;
    private TextView mTextView;
    private Uri mUri;
    private MyPDFReader PDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (mUri != null){
            mTextView.setText(mUri.toString());
        }

        mTextView = findViewById(R.id.PDF);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPDF);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        if (mUri != null){
            mTextView.setText(mUri.getPath());
        }
    }*/

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only PDF, using the image MIME data type.
        intent.setType("application/pdf");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                mUri = uri;

                // Open InputStream from PDF
                try {
                    mInputStream = getContentResolver().openInputStream(mUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Create object to read PDF
                PDF = new MyPDFReader(mInputStream);
                try {
                    ArrayList<Rank> ranks = PDF.getAllRank();
                    mTextView.setText(ranks.get(4).getRank());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Inner Thread class
    private class searchResultInsidePdf extends AsyncTask<MyPDFReader, Integer, ArrayList<CompetitionResult>>{

        @Override
        protected ArrayList<CompetitionResult> doInBackground(MyPDFReader... myPDFReaders) {
            return null;
        }
    }

}
