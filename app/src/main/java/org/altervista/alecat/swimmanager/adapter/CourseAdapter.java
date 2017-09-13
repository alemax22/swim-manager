package org.altervista.alecat.swimmanager.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.models.Course;

/**
 * Created by Alessandro Cattapan on 10/09/2017.
 */

public class CourseAdapter extends FirebaseListAdapter<Course> {
    private Context mContext;
     
    
    public CourseAdapter (Context context, Class<Course> modelClass, @LayoutRes int modelLayout, Query query) {
        super(context, modelClass, modelLayout, query);
        mContext = context;
    }

    @Override
    protected void populateView(View view, Course course, int position) {

        final String TRAINER = mContext.getString(R.string.course_text_trainer);
        final String LESSON = mContext.getString(R.string.text_course_lesson);
        final String SWIMMER = mContext.getString(R.string.text_course_swimmer);

        // Set Course name
        TextView textViewName = (TextView) view.findViewById(R.id.text_course_name);
        textViewName.setText(course.getName());
        
        // Set Trainer
        TextView textViewTrainer = (TextView) view.findViewById(R.id.text_trainer_name);
        textViewTrainer.setText(TRAINER + ": " + course.getTrainer());

        // Set Swimmers
        TextView textViewSwimmer = (TextView) view.findViewById(R.id.text_number_swimmer);
        textViewTrainer.setText(SWIMMER + ": " + course.getNumSwimmer());

        // Set Lessons
        TextView textViewLesson = (TextView) view.findViewById(R.id.text_number_lesson);
        textViewTrainer.setText(LESSON + ": " + course.getNumLessonDone() + " / " + course.getNumLesson());

    }
}
