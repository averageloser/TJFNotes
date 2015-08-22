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
        Parse.initialize(this, "KY8vqZRS5hHMHNjPQiEImWTfSwuvrAqq0DyYYuEU", "XnPZi9pos9OD7Gy606e4CVXQ5GDBewtF5E1n07FX");
    }
}
