package org.altervista.alecat.swimmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.altervista.alecat.swimmanager.data.SwimmerContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alessandro Cattapan on 18/08/2017.
 */

public class SwimmerActivity extends AppCompatActivity {

    // TAG for log messages
    private static final String TAG = SwimmerActivity.class.getSimpleName();

    // Constants
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    private String mUsername;

    // Private variables
    private SwimmerAdapter mSwimmerAdapter;
    private ListView mSwimmerListView;
    private ProgressBar mProgressBar; //TODO: Implement the progress bar when the swimmer is waiting for loading new data

    // Firebase variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSwimmerInfoDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swimmer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsername = ANONYMOUS;

        // initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSwimmerInfoDatabaseReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_SWIMMER_INFO);
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Initialize private variables
        mSwimmerListView = (ListView) findViewById(R.id.swimmer_list_view);

        // Initialize swimmer ListView and its adapter
        List<Swimmer> swimmers = new ArrayList();
        mSwimmerAdapter = new SwimmerAdapter(this, R.layout.item_swimmer_list, swimmers);
        mSwimmerListView.setAdapter(mSwimmerAdapter);

        // Set onItemClickListener
        mSwimmerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =  new Intent(SwimmerActivity.this, SwimmerEditorActivity.class);
                startActivity(intent);
            }
        });

        // Set the view that has to be shown when the listView is empty
        View emptyListView = findViewById(R.id.text_empty_swimmer_list);
        mSwimmerListView.setEmptyView(emptyListView);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(SwimmerActivity.this, SwimmerEditorActivity.class);
                startActivity(intent);
            }
        });

        // Authentication listener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user signed in
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // user signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(!BuildConfig.DEBUG) /* TODO: Delete this line when the app is ready*/
                                    .setAvailableProviders(
                                            Arrays.asList(
                                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }


    private void onSignedInInitialize(String username){
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
        mSwimmerAdapter.clear();
        detachDatabaseReadListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.signed_in_msg, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.signed_in_canceled_msg, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Log.v(TAG, "There was an error!");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_swimmer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.sign_out:
                // sign out
                AuthUI.getInstance().signOut(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Add a child listener to the database so it can update the UI when there
    // are changes inside the database
    private void attachDatabaseReadListener(){
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Swimmer swimmer = dataSnapshot.getValue(Swimmer.class);
                    mSwimmerAdapter.add(swimmer);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mSwimmerInfoDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    // Remove the child listener from the database
    private void detachDatabaseReadListener(){
        if (mChildEventListener != null) {
            mSwimmerInfoDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachDatabaseReadListener();
        mSwimmerAdapter.clear();
    }
}
