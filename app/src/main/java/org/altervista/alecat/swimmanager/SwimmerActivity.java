package org.altervista.alecat.swimmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    // RecyclerView
    private RecyclerView mSwimmerRecyclerView;
    private FirebaseRecyclerAdapter mSwimmerRecyclerAdapter;

    // ProgressBar
    private ProgressBar mProgressBar; //TODO: Implement the progress bar when the swimmer is waiting for loading new data

    // Firebase variables
    private FirebaseDatabase mFirebaseDatabase; // Do I keep two separated variables?
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

        // Progress Bar
        mProgressBar = (ProgressBar) findViewById(R.id.swimmer_progress_bar);
        mProgressBar.setVisibility(View.GONE);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSwimmerInfoDatabaseReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_SWIMMER_INFO);
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Initialize private variables, initialize swimmer RecyclerView and its adapter
        mSwimmerRecyclerView = (RecyclerView) findViewById(R.id.swimmer_recycle_view);
        mSwimmerRecyclerView.setHasFixedSize(true);
        mSwimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwimmerRecyclerAdapter = new SwimmerRecyclerAdapter(Swimmer.class,
                R.layout.item_swimmer_list,
                SwimmerHolder.class,
                mSwimmerInfoDatabaseReference);
        mSwimmerRecyclerView.setAdapter(mSwimmerRecyclerAdapter);

        // Set action listener on the recyclerView
        mSwimmerRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mSwimmerRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // Open SwimmerEditor Activity
                        DatabaseReference reference = mSwimmerRecyclerAdapter.getRef(position);
                        Intent intent = new Intent(SwimmerActivity.this, SwimmerEditorActivity.class);
                        intent.putExtra("reference", reference.toString());
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        //TODO: Delete swimmers
                        mSwimmerRecyclerAdapter.getRef(position).removeValue();
                    }
                })
        );

        // TODO: Set the view that has to be shown when the listView is empty
        View emptyListView = findViewById(R.id.text_empty_swimmer_list);


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
    }


    private void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
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
        mSwimmerRecyclerAdapter.cleanup();
    }
}
