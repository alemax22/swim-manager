package org.altervista.alecat.swimmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alessandro Cattapan on 29/08/2017.
 */

public class SwimmerHolder extends RecyclerView.ViewHolder {
    // Fields inside the view
    private final TextView mSwimmerNameSurnameField;
    private final TextView mSwimmerAgeField;

    public SwimmerHolder(View itemView) {
        super(itemView);
        mSwimmerNameSurnameField = (TextView) itemView.findViewById(R.id.text_swimmer_name_surname);
        mSwimmerAgeField = (TextView) itemView.findViewById(R.id.text_swimmer_age);
    }

    public void setNameSurname(String nameSurname){
        mSwimmerNameSurnameField.setText(nameSurname);
    }

    public void setAge(String birthday){
        int age = calculateAge(birthday);
        mSwimmerAgeField.setText(age + "");
    }

    // Return the swimmer'age from his birthday's String
    private int calculateAge(String birthdayString){
        // Get a Date object from a string
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        Date birthdayDate = dateFormatter.parse(birthdayString, new ParsePosition(0));

        // Covert the Date into a Calendar
        Calendar birthday = Calendar.getInstance();
        birthday.setTime(birthdayDate);
        int day = birthday.get(Calendar.DAY_OF_MONTH);
        int month = birthday.get(Calendar.MONTH);
        int year = birthday.get(Calendar.YEAR);

        Calendar today = Calendar.getInstance();
        // Subtract days
        today.add(Calendar.DAY_OF_MONTH, -day);
        // Subtract months
        today.add(Calendar.MONTH, -month);
        // Subtract years
        today.add(Calendar.YEAR, -year);
        // The value inside the year field is the age of the swimmer
        int age = today.get(Calendar.YEAR);
        return age;
    }
}
