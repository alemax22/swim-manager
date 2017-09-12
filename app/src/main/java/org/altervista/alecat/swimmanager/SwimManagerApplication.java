package org.altervista.alecat.swimmanager;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Alessandro Cattapan on 12/09/2017.
 */

public class SwimManagerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Save data inside cache when the device is offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
