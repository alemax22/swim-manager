package org.altervista.alecat.swimmanager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import org.altervista.alecat.swimmanager.fragment.CourseFragment;
import org.altervista.alecat.swimmanager.fragment.SwimmerFragment;

/**
 * Created by Alessandro Cattapan on 01/09/2017.
 */

public class SwimManagerPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = SwimManagerPagerAdapter.class.getSimpleName();
    private static final int NUM_PAGES = 2;
    private Context mContext;

    public SwimManagerPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SwimmerFragment.newInstance("","");
            case 1:
                return CourseFragment.newInstance("","");
            default:
                Log.v(TAG,"Invalid page position: " + position);
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.page_swimmer);
            case 1:
                return mContext.getString(R.string.page_courses);
            default:
                return "NO NAME"; // Error
        }
    }
}
