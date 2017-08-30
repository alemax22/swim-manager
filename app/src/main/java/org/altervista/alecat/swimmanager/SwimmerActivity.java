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
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.altervista.alecat.swimmanager.data.SwimmerContract;

import java.util.Arrays;


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
    private FirebaseListAdapter mSwimmerAdapter;
    private ListView mSwimmerListView;
    private ProgressBar mProgressBar; //TODO: Implement the progress bar when the swimmer is waiting for loading new data

    // Firebase variables
    private FirebaseDatabase mFirebaseDatabase; // Do I keep two different variables??
    private DatabaseReference mSwimmerInfoDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swimmer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsername = ANONYMOUS;

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSwimmerInfoDatabaseReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_SWIMMER_INFO);
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Initialize private variables
        mSwimmerListView = (ListView) findViewById(R.id.swimmer_list_view);

        // Set onItemClickListener
        mSwimmerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String reference = mSwimmerAdapter.getRef(i).toString();
                Log.v(TAG, "Reference: " + reference);
                Intent intent =  new Intent(SwimmerActivity.this, SwimmerEditorActivity.class);
                intent.setData(Uri.parse(reference));
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
        // Initialize swimmer ListView and its adapter
        mSwimmerAdapter = new SwimmerAdapter(this,
                Swimmer.class,
                R.layout.item_swimmer_list,
                mSwimmerInfoDatabaseReference);
        mSwimmerListView.setAdapter(mSwimmerAdapter);

    }

    private void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
        if (mSwimmerAdapter != null){
            mSwimmerAdapter.cleanup();
        }
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
        mSwimmerAdapter.cleanup();
    }
}
