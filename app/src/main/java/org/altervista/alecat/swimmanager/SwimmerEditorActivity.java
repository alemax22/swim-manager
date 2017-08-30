package org.altervista.alecat.swimmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altervista.alecat.swimmanager.data.SwimmerContract;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Alessandro Cattapan on 18/08/2017.
 */

public class SwimmerEditorActivity extends AppCompatActivity {

    // TAG for log messages
    private static final String TAG = SwimmerEditorActivity.class.getSimpleName();
    private Uri mUri;

    // Gender Spinner
    private Spinner mGenderSpinner;
    private int mGender;

    // Level Spinner
    private Spinner mLevelSpinner;
    private int mLevel;

    // Data fields
    private EditText mNameEditText;
    private EditText mSurnameEditText;

    // Birthday EditText
    private TextView mBirthdayTextView;

    // Firebase variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSwimmerInfoDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swimmer_editor);

        // Initialize private variables
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mLevelSpinner = (Spinner) findViewById(R.id.spinner_level);
        mBirthdayTextView = (TextView) findViewById(R.id.text_swimmer_birthday);
        mNameEditText = (EditText) findViewById(R.id.edit_swimmer_name);
        mSurnameEditText = (EditText) findViewById(R.id.edit_swimmer_surname);

        // Initialize Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSwimmerInfoDatabaseReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_SWIMMER_INFO);


        // Set an OnClickListener for the birthday's EditText
        mBirthdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        // Set the text inside the Birthday textView
        final Calendar calendar = Calendar.getInstance();
        mBirthdayTextView.setText(displayDate(calendar));

        // Change the label of the activity
        Intent intent = getIntent();
        mUri = intent.getData();

        // Change Activity label
        if (mUri != null) {

        } else {

        }
        // Create and set spinners in the UI
        setupSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_swimmer_editor, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                saveSwimmer();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                //TODO: Delete the swimmer and warn the swimmer about what he is doing
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (SwimmerActivity)
                // TODO: Advice the user if he had made changes to the object and let him to choose what to do
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Add/modify swimmer's data
    private void saveSwimmer(){
        // Retrieve data from each fields in the Activity
        String name = mNameEditText.getText().toString().trim();
        String surname = mSurnameEditText.getText().toString().trim();
        int gender = mGender;
        int level = mLevel;
        String birthday = mBirthdayTextView.getText().toString().trim();

        Swimmer swimmer = new Swimmer(name, surname, birthday, gender,level);

        // Put this value inside the database
        Task task = mSwimmerInfoDatabaseReference.push().setValue(swimmer);

        if (task != null){
            // The swimmer is successfully saved
            Toast.makeText(getApplicationContext(), R.string.swimmer_saved, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.swimmer_not_saved, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the swimmer.
     */
    private void setupSpinner() {

        // GENDER SPINNER
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = SwimmerContract.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = SwimmerContract.GENDER_FEMALE; // Female
                    } else {
                        mGender = SwimmerContract.GENDER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = SwimmerContract.GENDER_UNKNOWN; // Unknown
            }
        });

        // LEVEL SPINNER
        // Do the same thing for the levels Spinner
        // the spinner will use the default layout
        ArrayAdapter levelSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_level_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        levelSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mLevelSpinner.setAdapter(levelSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.level_one))) {
                        mLevel = SwimmerContract.LEVEL_ONE; // 1st level
                    } else if (selection.equals(getString(R.string.level_two))) {
                        mLevel = SwimmerContract.LEVEL_TWO; // 2nd level
                    } else if (selection.equals(getString(R.string.level_three))) {
                        mLevel = SwimmerContract.LEVEL_THREE; // 3rd level
                    } else if (selection.equals(getString(R.string.level_four))) {
                        mLevel = SwimmerContract.LEVEL_FOUR; // 4th level
                    } else {
                        mLevel = SwimmerContract.LEVEL_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mLevel = SwimmerContract.LEVEL_UNKNOWN; // Unknown
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    // Inner class that create a picker or choosing the swimmer's birthday
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the selected date as the default date in the picker
            TextView birthdayText = getActivity().findViewById(R.id.text_swimmer_birthday);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
            Date date = dateFormatter.parse(birthdayText.getText().toString(), new ParsePosition(0));
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            // Update the View after the user selects the swimmer's birthday
            TextView birthdayText = getActivity().findViewById(R.id.text_swimmer_birthday);
            Calendar calendar = new GregorianCalendar(year, month, day);

            // Display the date inside the TextView
            birthdayText.setText(displayDate(calendar));
        }
    }

    // This method returns the String date to display in the TextView field
    private static String displayDate(Calendar calendar){
        // Get a Date Object
        Date selectedDate = calendar.getTime();

        // Set a patter for the date
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        String dateToDisplay = dateFormatter.format(selectedDate);

        return dateToDisplay;
    }
}
