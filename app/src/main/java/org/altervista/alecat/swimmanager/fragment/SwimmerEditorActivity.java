package org.altervista.alecat.swimmanager.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.data.Swimmer;
import org.altervista.alecat.swimmanager.data.SwimmerContract;
import org.joda.time.LocalDate;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.altervista.alecat.swimmanager.data.SwimmerContract.DATE_FORMAT;

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

    // Input Text Error
    private TextInputLayout mNameError;
    private TextInputLayout mSurnameError;
    private TextInputLayout mDateError;

    //Check if there are changes in data
    private boolean mSwimmerHasChanged =  false;
    View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mSwimmerHasChanged = true;
            return false;
        }
    };

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

        // Set onTouchListener
        mGenderSpinner.setOnTouchListener(mTouchListener);
        mLevelSpinner.setOnTouchListener(mTouchListener);
        mBirthdayTextView.setOnTouchListener(mTouchListener);
        mNameEditText.setOnTouchListener(mTouchListener);
        mSurnameEditText.setOnTouchListener(mTouchListener);

        // Initialize TextInputLayout
        mNameError = (TextInputLayout) findViewById(R.id.text_input_name_error);
        mSurnameError = (TextInputLayout) findViewById(R.id.text_input_surname_error);
        mDateError =(TextInputLayout) findViewById(R.id.text_input_birthday_error);

        // Set Listeners
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mNameError.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSurnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSurnameError.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mBirthdayTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mDateError.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
            setTitle(R.string.activity_edit_swimmer_label);
            loadSwimmer();
        } else {
            setTitle(R.string.activity_add_swimmer_label);
        }
        // Create and set spinners in the UI
        setupSpinner();
    }

    private void loadSwimmer(){
        mSwimmerInfoDatabaseReference.child(mUri.getLastPathSegment()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Updates fields
                Swimmer currentSwimmer = dataSnapshot.getValue(Swimmer.class);
                mNameEditText.setText(currentSwimmer.getName());
                mSurnameEditText.setText(currentSwimmer.getSurname());
                mBirthdayTextView.setText(currentSwimmer.getBirthday());

                // Set gender
                switch (currentSwimmer.getGender()){
                    case SwimmerContract.GENDER_MALE:
                        mGenderSpinner.setSelection(1);
                        break;
                    case SwimmerContract.GENDER_FEMALE:
                        mGenderSpinner.setSelection(2);
                        break;
                    default:
                        mGenderSpinner.setSelection(0);
                }

                // Set Level
                switch (currentSwimmer.getLevel()){
                    case SwimmerContract.LEVEL_ONE:
                        mLevelSpinner.setSelection(1);
                        break;
                    case SwimmerContract.LEVEL_TWO:
                        mLevelSpinner.setSelection(2);
                        break;
                    case SwimmerContract.LEVEL_THREE:
                        mLevelSpinner.setSelection(3);
                        break;
                    case SwimmerContract.LEVEL_FOUR:
                        mLevelSpinner.setSelection(4);
                        break;
                    default:
                        mLevelSpinner.setSelection(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // There is an error
                Log.e(TAG, "Error during Swimmer update: " + databaseError);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_swimmer_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // Set invisible "delete action" when the user is adding a new swimmer
        if(mUri == null){
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                saveSwimmer();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (SwimManagerActivity)
                // TODO: Advice the user if he had made changes to the object and let him to choose what to do
                if (!mSwimmerHasChanged){
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(SwimmerEditorActivity.this);
                    }
                };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Shows the dialog when the user wants to go back even though there are some changes unsaved
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discard_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard_dialog, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editig_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Add/modify swimmer's data
    private void saveSwimmer(){

        // Retrieve data from each fields in the Activity
        String name = mNameEditText.getText().toString().trim();
        String surname = mSurnameEditText.getText().toString().trim();
        int gender = mGender;
        int level = mLevel;
        String birthday = mBirthdayTextView.getText().toString().trim();

        // Get today date
        /*SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        String today = dateFormatter.format(new Date());

        // If there isn't any change inside the Activity simply return
        // to the main activity without doing anything
        if (mUri == null && TextUtils.isEmpty(name) && TextUtils.isEmpty(surname) && birthday.equals(today)
                && gender == SwimmerContract.GENDER_UNKNOWN && level == SwimmerContract.LEVEL_UNKNOWN){

            // Return to the previous activity
            finish();
        }*/

        // Data validation
        if (!checkData(name, surname, birthday)){
            // Stay inside the Editor Activity
            return;
        }

        // Convert String
        // TODO: Make this process server side
        String correctName = capitalizeFirstLetter(name);
        String correctSurname = capitalizeFirstLetter(surname);

        Swimmer swimmer = new Swimmer(correctName, correctSurname, birthday, gender,level);
        if (mUri != null){
            Task task = mSwimmerInfoDatabaseReference.child(mUri.getLastPathSegment()).updateChildren(swimmer.toMap()); // TODO: Implement DatabaseReference.CompletionListener listener
            if (task != null){
                // The swimmer is successfully updated
                Toast.makeText(getApplicationContext(), R.string.swimmer_updated, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.swimmer_not_updated, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Put this value inside the database
            Task task = mSwimmerInfoDatabaseReference.push().setValue(swimmer);

            if (task != null){
                // The swimmer is successfully saved
                Toast.makeText(getApplicationContext(), R.string.swimmer_saved, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.swimmer_not_saved, Toast.LENGTH_SHORT).show();
            }
        }

        // Return to the previous activity
        finish();
    }

    // Data validation
    private boolean checkData(String name, String surname, String birthday){

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date birthdayDate = formatter.parse(birthday, new ParsePosition(0));
        Date todayDate = new Date();
        String today = formatter.format(todayDate);

        boolean result = true;

        if (birthdayDate.compareTo(todayDate) >= 0 || today.equals(birthday)){
            mDateError.setError(getString(R.string.date_error_msg));
            result = false;
        }

        if (TextUtils.isEmpty(name)) {
            mNameError.setError(getString(R.string.name_error_msg));
            result = false;
        }

        if (TextUtils.isEmpty(surname)){
            mSurnameError.setError(getString(R.string.surname_error_msg));
            result = false;
        }

        return result;
    }

    // Capitalize first letter of a String
    private String capitalizeFirstLetter(String word){
        String firstLetter = word.substring(0,1).toUpperCase();
        String remainingWord = word.substring(1).toLowerCase();
        String result = firstLetter + remainingWord;
        return result;
    }

    // Delete Swimmer
    private void deleteSwimmer(){
        mSwimmerInfoDatabaseReference.child(mUri.getLastPathSegment()).removeValue();
    }

    // Show delete confirmation Dialog
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_swimmer_dialog_msg);
        builder.setPositiveButton(R.string.delete_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the swimmer.
                deleteSwimmer();
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the swimmer.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!mSwimmerHasChanged){
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        showUnsavedChangesDialog(discardButtonClickListener);
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
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
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
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        String dateToDisplay = dateFormatter.format(selectedDate);

        return dateToDisplay;
    }
}
