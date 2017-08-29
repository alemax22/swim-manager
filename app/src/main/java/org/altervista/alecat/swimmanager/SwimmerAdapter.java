package org.altervista.alecat.swimmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Alessandro Cattapan on 25/08/2017.
 */

public class SwimmerAdapter extends ArrayAdapter<Swimmer> {

    // Public Constructor that call the superclass' constructor
    public SwimmerAdapter(Context context, int resource, List<Swimmer> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            // Creates a new view: transform the XML code into a View
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_swimmer_list, parent, false);
        }
        Swimmer currentSwimmer = getItem(position);

        // Put Name and Surname inside the textView
        TextView nameSurnameTextView = (TextView) listItemView.findViewById(R.id.text_swimmer_name_surname);
        nameSurnameTextView.setText(currentSwimmer.getName() + " " + currentSwimmer.getSurname());

        // Put Swimmer's Age inside the textView
        TextView ageTextView = (TextView) listItemView.findViewById(R.id.text_swimmer_age);
        ageTextView.setText(calculateAge(currentSwimmer.getBirthday()) + "");

        return listItemView;
    }

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
