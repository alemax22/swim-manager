package org.altervista.alecat.swimmanager.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.Query;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.models.Swimmer;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.altervista.alecat.swimmanager.data.SwimmerContract.DATE_FORMAT;

/**
 * Created by Alessandro Cattapan on 30/08/2017.
 */

public class SwimmerAdapter extends FirebaseListAdapter<Swimmer> {

    private Context mContext;

    public SwimmerAdapter(Context context, Class<Swimmer> modelClass, @LayoutRes int modelLayout, Query query) {
        super(context, modelClass, modelLayout, query);
        mContext = context;
    }

    public SwimmerAdapter(Context context, ObservableSnapshotArray snapshots, @LayoutRes int modelLayout){
        super(context,snapshots,modelLayout);
        mContext = context;
    }

    @Override
    protected void populateView(View view, Swimmer swimmer, int position) {
        // Set the swimmer's Name and Surname string inside the texView
        String nameSurname = swimmer.getName() + " " + swimmer.getSurname();
        ((TextView) view.findViewById(R.id.text_swimmer_name_surname)).setText(nameSurname);

        // Calculate and set the swimmer's age inside the textView
        Integer age = new Integer(calculateAge(swimmer.getBirthday()));
        ((TextView) view.findViewById(R.id.text_swimmer_age)).setText(age.toString());

        // Set the color of the circle
        TextView initialLetter = (TextView) view.findViewById(R.id.initial_letter);
        String initialName = swimmer.getName().substring(0,1);
        String initialSurname = swimmer.getSurname().substring(0,1);
        initialLetter.setText(initialName + initialSurname);

        // Set the proper background color on the circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable swimmerCircle = (GradientDrawable) initialLetter.getBackground();

        // Get the appropriate background color based on the current swimmer initial name letter
        int swimmerCircleColor = getSwimmerCircleColor(initialName);

        // Set the color on the circle
        swimmerCircle.setColor(swimmerCircleColor);

        // If it is the swimmer's birthday show the icon
        if (isBirthday(swimmer.getBirthday())){
            view.findViewById(R.id.birthday_image).setVisibility(View.VISIBLE);
        }
    }

    private int calculateAge(String birthdayString){
        // Get a Date object from a string
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        Date birthdayDate = dateFormatter.parse(birthdayString, new ParsePosition(0));
        long timeInMillis = birthdayDate.getTime();

        // Joda Library: Birthday
        LocalDate birthday = new LocalDate(timeInMillis);

        // Joda Library: Today
        LocalDate today = new LocalDate();

        // Calculate age
        Period period = new Period(birthday, today, PeriodType.yearMonthDay());
        return period.getYears();
    }

    // Check if today is the swimmer's birthday
    private boolean isBirthday (String birthdayString){
        // Get a Date object from a string
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        Date birthdayDate = dateFormatter.parse(birthdayString, new ParsePosition(0));
        Calendar birthday = Calendar.getInstance();
        birthday.setTime(birthdayDate);
        int birthdayDay = birthday.get(Calendar.DAY_OF_MONTH);
        int birthdayMonth = birthday.get(Calendar.MONTH);

        Calendar today = Calendar.getInstance();
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        int todayMonth = today.get(Calendar.MONTH);

        return (birthdayDay == todayDay && birthdayMonth == todayMonth);
    }

    // Choose the correct color to display
    private int getSwimmerCircleColor (String initialLetter){
        int swimmerCircleColor;

        if (initialLetter.compareToIgnoreCase("E") < 1){
            swimmerCircleColor = R.color.colorSwimmerA;
        } else if (initialLetter.compareToIgnoreCase("K") < 1){
            swimmerCircleColor = R.color.colorSwimmerF;
        } else if (initialLetter.compareToIgnoreCase("P") < 1) {
            swimmerCircleColor = R.color.colorSwimmerL;
        } else if (initialLetter.compareToIgnoreCase("U") < 1) {
            swimmerCircleColor = R.color.colorSwimmerQ;
        } else {
            swimmerCircleColor = R.color.colorSwimmerV;
        }

        return ContextCompat.getColor(mContext, swimmerCircleColor);
    }

}
