package com.example.hesus.journalapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Toolbar toolbar;
    FloatingActionButton floatingActionButton;

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    DatabaseReference databaseReference;

    List<Journal> list;

    FirebaseAuth mAuth;

    GoogleSignInClient mGoogleSignInClient;

    FirebaseDatabase mFirebaseInstance;
    private CoordinatorLayout coordinatorLayoutMain;

    String id,documentId,myjournal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_content);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //getSupportActionBar().setTitle("Journo");
        //toolbar.setLogo(R.drawable.ic_timer);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        initUIComponents();

        id = getIntent().getStringExtra("userId");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();


        //Setup Database reference

        databaseReference = mFirebaseInstance.getReference();
        databaseReference.keepSynced(true);

        viewJournals();

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    private void initUIComponents() {

        coordinatorLayoutMain = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        recyclerView = (RecyclerView) findViewById(R.id.rv);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.my_floatbutton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click.
                Intent addIntent = new  Intent(MainActivity.this, AddJournalActivity.class);

              addIntent.putExtra("userId", id);

              startActivity(addIntent);
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

                    String date = journals.getDate();

                    journals.setJournal(journal);
                    journals.setDate(date);
                    list.add(journals);

                }


                myAdapter = new MyAdapter(list,getApplicationContext());
                RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(MainActivity.this);
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
        inflater.inflate(R.menu.journalcontentmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_signout:
                mAuth.signOut();
                finish();

                mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });


                return true;

            case R.id.menu_share:

                return true;

            case R.id.menu_settings:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Uses the ShareCompat Intent builder to create our journal for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     *
     * @return the Intent to use to share our weather forecast
     */



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {


        myjournal = list.get(viewHolder.getAdapterPosition()).getJournal();

        final Journal deletedJournal = list.get(viewHolder.getAdapterPosition());
        final int deletedIndex = viewHolder.getAdapterPosition();

        documentId = (String) viewHolder.itemView.getTag();


        deleteJournal();

        Snackbar snackbar = Snackbar
                .make(coordinatorLayoutMain, myjournal + " has been removed from journals!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo is selected, restore the deleted item
                myAdapter.restoreItem(deletedJournal, deletedIndex);
                databaseReference.child("Journal").child(documentId).setValue(deletedJournal);
            }


        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();



    }



    public  void deleteJournal(){
        databaseReference.child("journal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                databaseReference.child("Journal").child(documentId).removeValue();
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
