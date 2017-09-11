package org.altervista.alecat.swimmanager.notUsed;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.firebase.ui.database.ClassSnapshotParser;
import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.data.Swimmer;
import org.altervista.alecat.swimmanager.data.SwimmerContract;
import org.altervista.alecat.swimmanager.fragment.SwimmerAdapter;

/**
 * Created by Alessandro Cattapan on 06/09/2017.
 *
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  NOT USED  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */

public class DataLoader extends AsyncTaskLoader<FirebaseListAdapter> {

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
    public FirebaseListAdapter loadInBackground() {
        ObservableSnapshotArray data = new FirebaseArray(mSwimmerInfoDatabaseReference,
                new ClassSnapshotParser(Swimmer.class));
        FirebaseListAdapter adapter = new SwimmerAdapter(getContext(),
                data,
                R.layout.item_swimmer_list);
        return adapter;
    }
}
