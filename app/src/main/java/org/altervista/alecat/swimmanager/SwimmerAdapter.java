package org.altervista.alecat.swimmanager;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alessandro Cattapan on 30/08/2017.
 */

public class SwimmerAdapter extends FirebaseListAdapter<Swimmer> {

    public SwimmerAdapter(Context context, Class<Swimmer> modelClass, @LayoutRes int modelLayout, Query query) {
        super(context, modelClass, modelLayout, query);
    }

    @Override
    protected void populateView(View view, Swimmer swimmer, int position) {
        // Set the swimmer's Name and Surname string inside the texView
        String nameSurname = swimmer.getName() + " " + swimmer.getSurname();
        ((TextView) view.findViewById(R.id.text_swimmer_name_surname)).setText(nameSurname);

        // Calculate and set the swimmer's age inside the textView
        Integer age = new Integer(calculateAge(swimmer.getBirthday()));
        ((TextView) view.findViewById(R.id.text_swimmer_age)).setText(age.toString());
    }

    private int calculateAge(String birthdayString){
        // Get a Date object from a string
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
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
}
