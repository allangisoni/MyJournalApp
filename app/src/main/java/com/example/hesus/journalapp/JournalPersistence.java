package com.example.hesus.journalapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class JournalPersistence extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // Persisting our data for offline usage
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
