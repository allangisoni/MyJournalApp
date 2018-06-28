package com.example.hesus.journalapp;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class JournalFirebaseHelper {

    private FirebaseDatabase mFirebaseInstance;
    DatabaseReference mDatabaseReference;
    ArrayList<String> journals=new ArrayList<>();

    public JournalFirebaseHelper(DatabaseReference db) {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Journal");

        this.mDatabaseReference = db;
    }




    //READ
    public ArrayList<String> retrieve()
    {
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return journals;
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        journals.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            String name =ds.getValue(Journal.class).getJournal();
            journals.add(name);
        }
    }

}
