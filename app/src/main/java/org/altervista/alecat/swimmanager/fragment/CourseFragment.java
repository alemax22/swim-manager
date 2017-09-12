package org.altervista.alecat.swimmanager.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.adapter.CourseAdapter;
import org.altervista.alecat.swimmanager.data.SwimmerContract;
import org.altervista.alecat.swimmanager.models.Course;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private FirebaseListAdapter mCourseAdapter;
    private ListView mCourseListView;
    private ProgressBar mProgressBar;
    private View mEmptyCourseView;

    // Firebase variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCoursesActiveReference;

    public CourseFragment() {
        // Required empty public constructor
    }

    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCoursesActiveReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_COURSE_ACTIVE);
        // Keep data from Firebase database inside cache
        mCoursesActiveReference.keepSynced(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        mCourseListView = (ListView) rootView.findViewById(R.id.course_list_view);
        mEmptyCourseView = rootView.findViewById(R.id.text_empty_course_list);

        mCourseAdapter = new CourseAdapter(getContext(),
                Course.class,
                R.layout.item_course_list,
                mCoursesActiveReference);
        mCourseListView.setAdapter(mCourseAdapter);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // TODO: Implement OnFragmentInteractionListener
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
