package org.altervista.alecat.swimmanager.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.adapter.CourseAdapter;
import org.altervista.alecat.swimmanager.data.SwimmerContract;
import org.altervista.alecat.swimmanager.editoractivity.CourseEditorActivity;
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

    private static final String TAG = CourseFragment.class.getSimpleName();

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
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.course_progress_bar);

        mCourseAdapter = new CourseAdapter(getContext(),
                Course.class,
                R.layout.item_course_list,
                mCoursesActiveReference);
        mCourseListView.setAdapter(mCourseAdapter);

        // Useful for progress bar
        DataSetObserver observer = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                // Hide the progress bar because data loading is finished
                mProgressBar.setVisibility(View.GONE);

                // Set the view to show when the ListView is empty
                mCourseListView.setEmptyView(mEmptyCourseView);
            }
        };

        mCourseAdapter.registerDataSetObserver(observer);

        // Set onItemClickListener
        mCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String reference = mCourseAdapter.getRef(i).toString();
                Log.v(TAG, "Reference: " + reference);
                Intent intent =  new Intent(getActivity(), CourseEditorActivity.class);
                intent.putExtra(SwimmerContract.REFERENCE, reference);
                startActivity(intent);
            }
        });

        // Set the view that has to be shown when the listView is empty
        mEmptyCourseView = rootView.findViewById(R.id.text_empty_swimmer_list);

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
