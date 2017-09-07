package org.altervista.alecat.swimmanager.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.firebase.ui.database.ClassSnapshotParser;
import com.firebase.ui.database.FirebaseArray;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Alessandro Cattapan on 06/09/2017.
 */

public class DataLoader extends AsyncTaskLoader<List> {

    private static final String LOG_TAG = DataLoader.class.getName();

    private Uri mUri;

    // Firebase variables
    private FirebaseDatabase mFirebaseDatabase; // Do I keep two different variables??
    private DatabaseReference mSwimmerInfoDatabaseReference;

    public DataLoader(Context context/*, Uri uri*/) {
        super(context);
       // mUri = uri;

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSwimmerInfoDatabaseReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_SWIMMER_INFO);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List loadInBackground() {
        List data = new FirebaseArray(mSwimmerInfoDatabaseReference, new ClassSnapshotParser(Swimmer.class));
        return data;
    }
}
