package br.com.cucha.archlab;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by eduardo on 10/4/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
