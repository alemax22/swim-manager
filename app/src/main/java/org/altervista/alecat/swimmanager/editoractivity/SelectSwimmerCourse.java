package org.altervista.alecat.swimmanager.editoractivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.SwimManagerActivity;
import org.altervista.alecat.swimmanager.adapter.SwimmerAdapter;
import org.altervista.alecat.swimmanager.data.SwimmerContract;
import org.altervista.alecat.swimmanager.models.Swimmer;

import java.util.ArrayList;

public class SelectSwimmerCourse extends AppCompatActivity {

    private final static String TAG = SelectSwimmerCourse.class.getSimpleName();

    // Private variables
    private ListView mSwimmerList;
    private FirebaseListAdapter mSwimmerAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSwimmerReference;
    private ArrayList<String> mSelectedSwimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_swimmer_course);

        // Set Title and subtitle of the activity
        setTitle(R.string.label_select_swimmer_course);
        // getActionBar().setSubtitle(R.string.action_bar_subtitle_select_swimmer);

        // Initialize Firebase variables
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSwimmerReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_SWIMMER_INFO);

        mSelectedSwimmer = new ArrayList<String>();

        // Set Adapter
        mSwimmerList = (ListView) findViewById(R.id.select_swimmer_list_view);
        mSwimmerAdapter = new SwimmerAdapter(this,
                Swimmer.class,
                R.layout.item_swimmer_list,
                mSwimmerReference);
        mSwimmerList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mSwimmerList.setAdapter(mSwimmerAdapter);

        mSwimmerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String reference = mSwimmerAdapter.getRef(i).toString();
                if (mSwimmerList.isItemChecked(i)){
                    mSelectedSwimmer.remove(reference);
                    mSwimmerList.setItemChecked(i, false);
                    Log.v(TAG, "Deselected: " + reference);
                } else {
                    mSelectedSwimmer.add(reference);
                    mSwimmerList.setItemChecked(i, true);
                    Log.v(TAG, "Selected: " + reference);
                }
            }
        });

        // Floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_select_swimmer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSwimmerCourse.this, CourseEditorActivity.class);
                intent.putExtra(SwimmerContract.ARRAY_LIST, mSelectedSwimmer);
                startActivity(intent);
            }
        });
    }

}
