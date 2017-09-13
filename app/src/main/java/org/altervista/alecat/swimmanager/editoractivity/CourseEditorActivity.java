package org.altervista.alecat.swimmanager.editoractivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.data.SwimmerContract;

public class CourseEditorActivity extends AppCompatActivity {

    private String mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        // Decide title of the Activity
        Intent intent = getIntent();
        String mReference = intent.getStringExtra(SwimmerContract.REFERENCE);
        if (mReference == null){
            setTitle(getString(R.string.activity_add_course_label));
        } else {
            setTitle(getString(R.string.activity_edit_course_label));
        }
    }
}
