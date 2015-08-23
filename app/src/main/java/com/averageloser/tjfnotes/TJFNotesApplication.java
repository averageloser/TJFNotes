package com.averageloser.tjfnotes;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by tj on 8/22/15.
 */
public class TJFNotesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "appId", "key");
    }
}
