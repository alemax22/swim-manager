package org.altervista.alecat.swimmanager.editoractivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.data.SwimmerContract;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.altervista.alecat.swimmanager.data.SwimmerContract.ARRAY_LIST;
import static org.altervista.alecat.swimmanager.data.SwimmerContract.DATE_FORMAT;

public class CourseEditorActivity extends AppCompatActivity {

    private String mReference;
    private ArrayList<String> mReferenceArray;
    private EditText mCourseNameEditText;
    private EditText mCourseTrainerEditText;
    private TextView mNumberSwimmerTextView;
    private TextView mStartingDateTextView;
    private TextView mEndDateTextView;

    // CheckBoxes
    private CheckBox mMondayCheckBox;
    private CheckBox mTuesdayCheckBox;
    private CheckBox mWednesdayCheckBox;
    private CheckBox mThursdayCheckBox;
    private CheckBox mFridayCheckBox;
    private CheckBox mSaturdayCheckBox;
    private CheckBox mSundayCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        // Decide title of the Activity
        Intent intent = getIntent();
        mReference = intent.getStringExtra(SwimmerContract.REFERENCE);
        mReferenceArray = intent.getStringArrayListExtra(ARRAY_LIST);
        if (mReference == null){
            setTitle(getString(R.string.activity_add_course_label));
        } else {
            setTitle(getString(R.string.activity_edit_course_label));
        }

        // Initialize Variables
        mCourseNameEditText = (EditText) findViewById(R.id.edit_course_name);
        mCourseTrainerEditText = (EditText) findViewById(R.id.edit_trainer);
        mNumberSwimmerTextView = (TextView) findViewById(R.id.number_of_swimmers);
        mStartingDateTextView = (TextView) findViewById(R.id.text_course_starting_date);
        mEndDateTextView = (TextView) findViewById(R.id.text_course_end_date);

        // Initialize CheckBoxes
        mMondayCheckBox = (CheckBox) findViewById(R.id.checkbox_monday);
        mTuesdayCheckBox = (CheckBox) findViewById(R.id.checkbox_tuesday);
        mWednesdayCheckBox = (CheckBox) findViewById(R.id.checkbox_wednesday);
        mThursdayCheckBox = (CheckBox) findViewById(R.id.checkbox_thursday);
        mFridayCheckBox = (CheckBox) findViewById(R.id.checkbox_friday);
        mSaturdayCheckBox = (CheckBox) findViewById(R.id.checkbox_saturday);
        mSundayCheckBox = (CheckBox) findViewById(R.id.checkbox_sunday);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_course_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                saveCourse();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (SwimManagerActivity)
                // TODO: Advice the user if he had made changes to the object and let him to choose what to do
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCourse(){
        // TODO: Save Course
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
