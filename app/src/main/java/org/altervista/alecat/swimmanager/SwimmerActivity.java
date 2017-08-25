package org.altervista.alecat.swimmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altervista.alecat.swimmanager.data.SwimmerContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alessandro Cattapan on 18/08/2017.
 */

public class SwimmerActivity extends AppCompatActivity {

    // TAG for log messages
    private static final String TAG = SwimmerActivity.class.getSimpleName();

    // Private variables
    private SwimmerAdapter mSwimmerAdapter;
    private ListView mSwimmerListView;
    private ProgressBar mProgressBar; //TODO: Implement the progress bar when the swimmer is waiting for loading new data

    // Firebase variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swimmer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabaseReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_SWIMMER_INFO);

        // Initialize private variables
        mSwimmerListView = (ListView) findViewById(R.id.swimmer_list_view);

        // Initialize swimmer ListView and its adapter
        List<Swimmer> swimmers = new ArrayList();
        mSwimmerAdapter = new SwimmerAdapter(this, R.layout.item_swimmer_list, swimmers);
        mSwimmerListView.setAdapter(mSwimmerAdapter);

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
        }
        return super.onOptionsItemSelected(item);
    }
}
