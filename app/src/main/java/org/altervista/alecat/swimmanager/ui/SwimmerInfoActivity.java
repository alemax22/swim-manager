package org.altervista.alecat.swimmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.data.SwimmerContract;
import org.altervista.alecat.swimmanager.editoractivity.SwimmerEditorActivity;

public class SwimmerInfoActivity extends AppCompatActivity {

    private String mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swimmer_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mReference = intent.getStringExtra(SwimmerContract.REFERENCE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_swimmer_info);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(SwimmerInfoActivity.this, SwimmerEditorActivity.class);
                editIntent.putExtra(SwimmerContract.REFERENCE, mReference);
                startActivity(editIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
