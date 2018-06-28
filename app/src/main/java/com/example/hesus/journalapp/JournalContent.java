package com.example.hesus.journalapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JournalContent extends AppCompatActivity {

    FloatingActionButton floatingActionButton;

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    JournalFirebaseHelper journalFirebaseHelper;
    DatabaseReference databaseReference;

    List<Journal> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        initUIComponents();
        recyclerView = (RecyclerView) findViewById(R.id.rv);


        //Setup Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();


        viewJournals();


    }

    private void initUIComponents() {

         floatingActionButton = (FloatingActionButton) findViewById(R.id.my_floatbutton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click.
                startActivity(new Intent(JournalContent.this, AddJournalActivity.class));
            }
        });
    }



    public void viewJournals() {


        databaseReference.child("Journal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                // StringBuffer stringbuffer = new StringBuffer();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Journal journals = dataSnapshot1.getValue(Journal.class);

                    String journal = journals.getJournal();

                    journals.setJournal(journal);
                    list.add(journals);
                    // Toast.makeText(MainActivity.this,""+name,Toast.LENGTH_LONG).show();

                }

                myAdapter = new MyAdapter(list);
                RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(JournalContent.this);
                recyclerView.setLayoutManager(layoutmanager);

                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(myAdapter);

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
