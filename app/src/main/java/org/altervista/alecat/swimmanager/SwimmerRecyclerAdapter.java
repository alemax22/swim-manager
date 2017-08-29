package org.altervista.alecat.swimmanager;

import android.support.annotation.LayoutRes;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by Alessandro Cattapan on 29/08/2017.
 */

public class SwimmerRecyclerAdapter extends FirebaseRecyclerAdapter<Swimmer, SwimmerHolder> {

    public SwimmerRecyclerAdapter(Class<Swimmer> modelClass, @LayoutRes int modelLayout, Class<SwimmerHolder> viewHolderClass, Query query) {
        super(modelClass, modelLayout, viewHolderClass, query);
    }

    @Override
    protected void populateViewHolder(SwimmerHolder holder, Swimmer swimmer, int position) {
        String nameSurname = swimmer.getName() + " " + swimmer.getSurname();
        holder.setNameSurname(nameSurname);
        holder.setAge(swimmer.getBirthday());
    }
}
