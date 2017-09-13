package org.altervista.alecat.swimmanager.editoractivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.adapter.SwimmerAdapter;
import org.altervista.alecat.swimmanager.data.SwimmerContract;
import org.altervista.alecat.swimmanager.models.Swimmer;

import java.util.ArrayList;

public class SelectSwimmerCourse extends AppCompatActivity {

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
        getActionBar().setSubtitle(R.string.action_bar_subtitle_select_swimmer);

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
        mSwimmerList.setAdapter(mSwimmerAdapter);
        mSwimmerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String reference = mSwimmerAdapter.getRef(i).toString();
                View check = view.findViewById(R.id.selected_swimmer_check);
                if (mSelectedSwimmer.contains(reference)){
                    mSelectedSwimmer.remove(reference);
                    check.setVisibility(View.GONE);
                } else {
                    mSelectedSwimmer.add(reference);
                    check.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
