package org.altervista.alecat.swimmanager.editoractivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    private final static int COURSE_COMPLETION = 0;

    // Private variables
    private ListView mSwimmerList;
    private FirebaseListAdapter<Swimmer> mSwimmerAdapter;
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
                mSwimmerReference.orderByChild("name")); //TODO: Set Order
        mSwimmerList.setAdapter(mSwimmerAdapter);
        mSwimmerList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        mSwimmerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Save only keys
                String reference = mSwimmerAdapter.getRef(i).getKey().toString();
                Swimmer currentSwimmer = mSwimmerAdapter.getItem(i);
                View check = view.findViewById(R.id.selected_swimmer_circle);
                if (currentSwimmer.isSelected()){
                    mSelectedSwimmer.remove(reference);
                    currentSwimmer.setSelection(false);
                    check.setVisibility(View.GONE);
                    Log.v(TAG, "Deselected: " + reference);
                } else {
                    mSelectedSwimmer.add(reference);
                   currentSwimmer.setSelection(true);
                    check.setVisibility(View.VISIBLE);
                    Log.v(TAG, "Selected: " + reference);
                }
            }
        });

        // Floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_select_swimmer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedSwimmer.isEmpty()){
                    Toast toast = Toast.makeText(SelectSwimmerCourse.this, R.string.toast_select_at_leat_one_swimmer_msg, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                } else {
                    Intent intent = new Intent(SelectSwimmerCourse.this, CourseEditorActivity.class);
                    intent.putExtra(SwimmerContract.ARRAY_LIST, mSelectedSwimmer);
                    startActivityForResult(intent, COURSE_COMPLETION);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COURSE_COMPLETION){
            if (resultCode == RESULT_OK){
                finish();
            } else {
                Log.e(TAG, "There was an error in COURSE_COMPLETION: resultCode = error");
            }
        } else {
            Log.e(TAG, "Error in OnActivityResult: the requestCode do not match any registered code");
        }
    }
}
