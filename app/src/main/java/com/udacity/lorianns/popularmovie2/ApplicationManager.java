package com.udacity.lorianns.popularmovie2;

import android.app.Application;

/**
 * Created by lorianns on 7/11/16.
 */
public class ApplicationManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new OptionalDependencies(this).initialize();
    }
}
