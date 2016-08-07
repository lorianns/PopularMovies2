package com.udacity.lorianns.popularmovie2;

import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by lorianns on 7/11/16.
 */
public class OptionalDependencies {

        private Context context;

        public OptionalDependencies(Context context) {
            this.context = context;
        }

        public void initialize() {
            Stetho.initializeWithDefaults(context);
        }

}
