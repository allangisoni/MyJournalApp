package com.example.hesus.journalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {

    public EditText journalEditText;
    public Date date;
    public Button saveButton;

    String myJournal;
    private String userId;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        initUIComponents();


        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'Journal' node
        mFirebaseDatabase = mFirebaseInstance.getReference("Journal");


        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("My Journal");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              addJournal();

            }
        });
    }

    // Initializing our views
    private void initUIComponents() {

        saveButton =(Button) findViewById (R.id.saveButton);
        journalEditText = (EditText)findViewById(R.id.journalEditText);


    }

    private void addJournal() {
        //getting the values to save
        myJournal = journalEditText.getText().toString();

        date = Calendar.getInstance().getTime();

        //checking if the value is provided
        if (!TextUtils.isEmpty(myJournal)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist

            String id = mFirebaseDatabase.push().getKey();


            //creating an Artist Object
            Journal journal = new Journal(id,myJournal,date);

            //Saving the Artist
            mFirebaseDatabase.child(id).setValue(journal);


            //setting edittext to blank again
            journalEditText.setText("");

            //displaying a success toast
            Toast.makeText(this, "Journal added", Toast.LENGTH_LONG).show();

            startActivity(new Intent(AddJournalActivity.this, JournalContent.class));
            finish();


        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please write your Jornal", Toast.LENGTH_LONG).show();
        }
    }

}
