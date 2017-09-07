package org.altervista.alecat.swimmanager.fragment;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
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

import com.firebase.ui.database.ClassSnapshotParser;
import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altervista.alecat.swimmanager.R;
import org.altervista.alecat.swimmanager.SwimManagerActivity;
import org.altervista.alecat.swimmanager.data.DataLoader;
import org.altervista.alecat.swimmanager.data.Swimmer;
import org.altervista.alecat.swimmanager.data.SwimmerContract;

import java.util.List;


/**
 * Created by Alessandro Cattapan on 30/08/2017.
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SwimmerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SwimmerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwimmerFragment extends Fragment implements LoaderManager.LoaderCallbacks<List>{

    // TAG for log messages
    private static final String TAG = SwimManagerActivity.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam2;

    // Loader id
    private static final int SWIMMER_LOADER_ID = 1;

    private OnFragmentInteractionListener mListener;

    // Private variables
    private FirebaseListAdapter mSwimmerAdapter;
    private ListView mSwimmerListView;
    private FirebaseArray mSwimmerArray;
    private ProgressBar mProgressBar;
    private View mEmptyListTextView;

    // Firebase variables
    private FirebaseDatabase mFirebaseDatabase; // Do I keep two different variables??
    private DatabaseReference mSwimmerInfoDatabaseReference;

    public SwimmerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwimmerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwimmerFragment newInstance(String param1, String param2) {
        SwimmerFragment fragment = new SwimmerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSwimmerInfoDatabaseReference = mFirebaseDatabase.getReference().child(SwimmerContract.NODE_SWIMMER_INFO);

        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Create the array list
        mSwimmerArray = new FirebaseArray(mSwimmerInfoDatabaseReference, new ClassSnapshotParser(Swimmer.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_swimmer, container, false);

        // Initialize private variables
        mSwimmerListView = (ListView) rootView.findViewById(R.id.swimmer_list_view);

        // Initialize swimmer adapter using an array list, and set the adapter in the listView
        mSwimmerAdapter = new SwimmerAdapter(getContext(),
                mSwimmerArray,
                R.layout.item_swimmer_list);
        mSwimmerListView.setAdapter(mSwimmerAdapter);

        // Useful for progress bar
        DataSetObserver observer = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                // Hide the progress bar because data loading is finished
                mProgressBar.setVisibility(View.GONE);

                // Set the view to show when the ListView is empty
                mSwimmerListView.setEmptyView(mEmptyListTextView);
            }
        };

        mSwimmerAdapter.registerDataSetObserver(observer);

        // Set onItemClickListener
        mSwimmerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String reference = mSwimmerAdapter.getRef(i).toString();
                Log.v(TAG, "Reference: " + reference);
                Intent intent =  new Intent(getActivity(), SwimmerEditorActivity.class);
                intent.setData(Uri.parse(reference));
                startActivity(intent);
            }
        });

        // Set the view that has to be shown when the listView is empty
        mEmptyListTextView = rootView.findViewById(R.id.text_empty_swimmer_list);

        // Progress Bar for data Loading
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

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
        mListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSwimmerAdapter.cleanup();
    }


    // Loader
    @Override
    public Loader<List> onCreateLoader(int i, Bundle bundle) {
        return new DataLoader(getContext() /*Uri*/); //TODO: URI
    }

    @Override
    public void onLoadFinished(Loader<List> loader, List list) {

    }

    @Override
    public void onLoaderReset(Loader<List> loader) {

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
