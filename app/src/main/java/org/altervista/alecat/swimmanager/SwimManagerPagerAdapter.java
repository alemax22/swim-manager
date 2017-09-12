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
    private Fragment[] mFragments;
    private String[] mFragmentsTitle;
    private Context mContext;

    public SwimManagerPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mFragments = new Fragment[] {SwimmerFragment.newInstance("",""),
                CourseFragment.newInstance("","")};
        mFragmentsTitle = new String[] {mContext.getString(R.string.page_swimmer),
                mContext.getString(R.string.page_courses)};
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentsTitle[position];
    }
}
