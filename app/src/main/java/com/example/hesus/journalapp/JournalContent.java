package com.example.hesus.journalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    DatabaseReference databaseReference;

    List<Journal> list;

    FirebaseAuth mAuth;

    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        initUIComponents();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        //Setup Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();



        viewJournals();


    }

    private void initUIComponents() {

        recyclerView = (RecyclerView) findViewById(R.id.rv);

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
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Journal journals = dataSnapshot1.getValue(Journal.class);

                    String journal = journals.getJournal();

                    journals.setJournal(journal);
                    list.add(journals);

                }

/**
                final RecyclerViewClickListener listener = new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(JournalContent.this, "Position " + position, Toast.LENGTH_SHORT).show();
                       Intent updateIntent = new Intent(JournalContent.this, UpdateJournal.class);
                       Journal journal = (Journal) list.get(position);
                       updateIntent.putExtra("journalnote", journal.getJournal());
                       startActivity(updateIntent);
                    }
                }; **/

                myAdapter = new MyAdapter(list,getApplicationContext());
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_signout:
                mAuth.signOut();

                mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(JournalContent.this, LoginActivity.class));
                        finish();
                    }
                });


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
