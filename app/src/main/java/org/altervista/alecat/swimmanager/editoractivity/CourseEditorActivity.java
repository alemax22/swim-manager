package org.altervista.alecat.swimmanager.editoractivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.data.SwimmerContract;
import org.altervista.alecat.swimmanager.models.Course;
import org.altervista.alecat.swimmanager.models.Swimmer;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.altervista.alecat.swimmanager.data.SwimmerContract.ARRAY_LIST_REFERENCE;
import static org.altervista.alecat.swimmanager.data.SwimmerContract.ARRAY_LIST_SWIMMER;
import static org.altervista.alecat.swimmanager.data.SwimmerContract.DATE_FORMAT;
import static org.altervista.alecat.swimmanager.data.SwimmerContract.NODE_SWIMMER_COURSE_ACTIVE;

public class CourseEditorActivity extends AppCompatActivity {

    private final static String TAG = CourseEditorActivity.class.getSimpleName();

    private ArrayList<Swimmer> mSwimmerArray;
    private ArrayList<String> mSwimmerArrayReference;
    private EditText mCourseNameEditText;
    private EditText mCourseTrainerEditText;
    private TextView mNumberSwimmerTextView;
    private TextView mStartingDateTextView;
    private TextView mEndDateTextView;
    private boolean[] mWeekDay = new boolean[8];

    // CheckBoxes
    private CheckBox mMondayCheckBox;
    private CheckBox mTuesdayCheckBox;
    private CheckBox mWednesdayCheckBox;
    private CheckBox mThursdayCheckBox;
    private CheckBox mFridayCheckBox;
    private CheckBox mSaturdayCheckBox;
    private CheckBox mSundayCheckBox;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCourseReference;
    private DatabaseReference mSwimmerCourseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCourseReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_COURSE_ACTIVE);
        mSwimmerCourseReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_SWIMMER_COURSE_ACTIVE);

                // Retrieve data from intent
        Intent intent = getIntent();
        mSwimmerArray = (ArrayList<Swimmer>) intent.getSerializableExtra(ARRAY_LIST_SWIMMER);
        mSwimmerArrayReference = intent.getStringArrayListExtra(ARRAY_LIST_REFERENCE);

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

        // Set date
        Calendar today = Calendar.getInstance();
        mStartingDateTextView.setText(displayDate(today));
        mEndDateTextView.setText(displayDate(today));

        // Set number of swimmer
        mNumberSwimmerTextView.setText("" + mSwimmerArray.size());

        // Set Listener
        mStartingDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view, CourseDatePickerFragment.START_DATE_PICKER);
            }
        });

        mEndDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view, CourseDatePickerFragment.END_DATE_PICKER);
            }
        });

        CompoundButton.OnCheckedChangeListener weekListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()){
                    case R.id.checkbox_monday:
                        if(compoundButton.isChecked()){
                            mWeekDay[Calendar.MONDAY] = true;
                        } else {
                            mWeekDay[Calendar.MONDAY] = false;
                        }
                        break;
                    case R.id.checkbox_tuesday:
                        if(compoundButton.isChecked()){
                            mWeekDay[Calendar.TUESDAY] = true;
                        } else {
                            mWeekDay[Calendar.TUESDAY] = false;
                        }
                        break;
                    case R.id.checkbox_wednesday:
                        if(compoundButton.isChecked()){
                            mWeekDay[Calendar.WEDNESDAY] = true;
                        } else {
                            mWeekDay[Calendar.WEDNESDAY] = false;
                        }
                        break;
                    case R.id.checkbox_thursday:
                        if(compoundButton.isChecked()){
                            mWeekDay[Calendar.THURSDAY] = true;
                        } else {
                            mWeekDay[Calendar.THURSDAY] = false;
                        }
                        break;
                    case R.id.checkbox_friday:
                        if(compoundButton.isChecked()){
                            mWeekDay[Calendar.FRIDAY] = true;
                        } else {
                            mWeekDay[Calendar.FRIDAY] = false;
                        }
                        break;
                    case R.id.checkbox_saturday:
                        if(compoundButton.isChecked()){
                            mWeekDay[Calendar.SATURDAY] = true;
                        } else {
                            mWeekDay[Calendar.SATURDAY] = false;
                        }
                        break;
                    case R.id.checkbox_sunday:
                        if(compoundButton.isChecked()){
                            mWeekDay[Calendar.SUNDAY] = true;
                        } else {
                            mWeekDay[Calendar.SUNDAY] = false;
                        }
                        break;
                }
            }
        };

        // Set weekListener
        mMondayCheckBox.setOnCheckedChangeListener(weekListener);
        mTuesdayCheckBox.setOnCheckedChangeListener(weekListener);
        mWednesdayCheckBox.setOnCheckedChangeListener(weekListener);
        mThursdayCheckBox.setOnCheckedChangeListener(weekListener);
        mFridayCheckBox.setOnCheckedChangeListener(weekListener);
        mSaturdayCheckBox.setOnCheckedChangeListener(weekListener);
        mSundayCheckBox.setOnCheckedChangeListener(weekListener);

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
                setResult(RESULT_OK);
                finish();
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
        String startDate = mStartingDateTextView.getText().toString();
        String endDate = mEndDateTextView.getText().toString();
        String name = mCourseNameEditText.getText().toString();
        String trainer = mCourseTrainerEditText.getText().toString();

        ArrayList<String> dates = getDates(startDate, endDate);
        Log.v(TAG,"Dates: " + dates.toString() + "; Total dates: " + dates.size());
        Log.v(TAG,"References: " + mSwimmerArrayReference + "; Total references: " + mSwimmerArrayReference.size());

        // Save number of swimmer
        int count = mSwimmerArray.size();
        int numLesson = dates.size();

        // Get HashMaps
        HashMap<String,Boolean> swimmerMap = getSwimmerMap();
        HashMap<String,Boolean> dateMap = getDayMap(dates);

        // Prepare data for the database
        Course currentCourse = new Course(name, trainer, numLesson, count, swimmerMap, dateMap);
        Log.v(TAG,"ArrayReference = " + mSwimmerArrayReference.size() + "; ArraySwimmers = " + mSwimmerArray.size());
        Log.v(TAG,"ArrayReference = " + dates.size());

        // Save data
        DatabaseReference databaseReference = mCourseReference.push();
        String courseKey = databaseReference.getKey();
        databaseReference.setValue(currentCourse);
        HashMap<String,Object> hashMap = createHashMap(dateMap, courseKey);
        mSwimmerCourseReference.updateChildren(hashMap);
    }

    // Create the Swimmer-Course-node
    private HashMap<String,Object> createHashMap(HashMap<String,Boolean> date, String courseKey){
        HashMap<String,Object> map = new HashMap<String,Object>();
        while(!mSwimmerArrayReference.isEmpty()){
            // Path to the database
            String reference = mSwimmerArrayReference.remove(0) + "/" + courseKey;
            map.put(reference, date);
        }
        return map;
    }

    // Get hashMap from two ArrayList
    private HashMap<String, Boolean> getSwimmerMap(){
        HashMap<String, Boolean> map = new HashMap<>();
        Iterator<Swimmer> swimmerIterator = mSwimmerArray.iterator();
        Iterator<String> stringIterator = mSwimmerArrayReference.iterator();
        while (swimmerIterator.hasNext() && stringIterator.hasNext()){
            Swimmer currentSwimmer = swimmerIterator.next();
            String currentReference = stringIterator.next();
            map.put(currentReference, Boolean.TRUE);
        }
        return map;
    }

    // Get hashMap of days
    private HashMap<String, Boolean> getDayMap(ArrayList<String> date){
        HashMap<String, Boolean> map = new HashMap<>();
        while (!date.isEmpty()){
            String day = date.remove(0);
            map.put(day, Boolean.FALSE);
        }
        return map;
    }

    private ArrayList<String> getDates (String from, String to){
        ArrayList<String> dates = new ArrayList<String>();
        SimpleDateFormat formatter = new SimpleDateFormat(SwimmerContract.DATE_FORMAT);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(formatter.parse(from, new ParsePosition(0)));
        end.setTime(formatter.parse(to, new ParsePosition(0)));

        while (start.compareTo(end) <= 0){
            int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
            if (mWeekDay[dayOfWeek]){
                String date = formatter.format(start.getTime());
                dates.add(date);
            }
            start.add(Calendar.DAY_OF_MONTH,1);
        }
        return dates;
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

    public void showDatePickerDialog(View v, int idPicker) {
        CourseDatePickerFragment newFragment = new CourseEditorActivity.CourseDatePickerFragment();
        newFragment.setNumberPicker(idPicker);
        newFragment.show(getSupportFragmentManager(), "courseDatePicker" + idPicker);
    }

    // Inner class
    public static class CourseDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        public static final int START_DATE_PICKER = 0;
        public static final int END_DATE_PICKER = 1;
        private int mNumPicker;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the selected date as the default date in the picker
            TextView dateView = null;
            switch (mNumPicker) {
                case START_DATE_PICKER:
                    dateView = getActivity().findViewById(R.id.text_course_starting_date);
                    break;
                case END_DATE_PICKER:
                    dateView = getActivity().findViewById(R.id.text_course_end_date);
                    break;
                default:
                    Log.v("CourseDatePicker","Error");
            }
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
            Date date = dateFormatter.parse(dateView.getText().toString(), new ParsePosition(0));
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
            TextView textViewUpdate;
            switch (mNumPicker){
                case START_DATE_PICKER:
                    textViewUpdate = getActivity().findViewById(R.id.text_course_starting_date);
                    break;
                case END_DATE_PICKER:
                    textViewUpdate = getActivity().findViewById(R.id.text_course_end_date);
                    break;
                default:
                    Log.v("CourseDatePicker","Error");
                    return;
            }
            Calendar calendar = new GregorianCalendar(year, month, day);

            // Display the date inside the TextView
            textViewUpdate.setText(displayDate(calendar));
        }

        public void setNumberPicker(int num){
            this.mNumPicker = num;
        }
    }
}
