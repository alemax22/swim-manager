package org.altervista.alecat.swimmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import org.altervista.alecat.swimmanager.data.SwimmerContract;

import java.util.Calendar;

/**
 * Created by Alessandro Cattapan on 18/08/2017.
 */

public class UserEditorActivity extends AppCompatActivity {

    // Gender Spinner
    Spinner mGenderSpinner;
    int mGender;

    // Level Spinner
    Spinner mLevelSpinner;
    int mLevel;

    // Birthday EditText
    TextView mBirthdayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_editor);

        // Initialize private variables
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mLevelSpinner = (Spinner) findViewById(R.id.spinner_level);
        mBirthdayTextView = (TextView) findViewById(R.id.edit_user_birthday);

        // Set an OnClickListener for the birthday's EditText
        mBirthdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        // Set the text inside the Birthday textView
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mBirthdayTextView.setText(day + "/" + month + "/" + year);

        // Create and set spinners in the UI
        setupSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_user_editor, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //TODO: Save the user through firebase real time database
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                //TODO: Delete the user and warn the user about what he is doing
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (UserActivity)
                // TODO: Advice the user if he had made changes to the object and let him to choose what to do
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the user.
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
                mGender = 0; // Unknown
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
                mLevel = 0; // Unknown
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
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            // Update the View after the user selects the swimmer's birthday
            EditText birthdayEdit = getActivity().findViewById(R.id.edit_user_birthday);
            birthdayEdit.setText(day + "/" + month + "/" + year);
        }
    }

}
