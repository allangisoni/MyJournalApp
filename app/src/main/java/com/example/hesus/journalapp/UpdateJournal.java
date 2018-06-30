package com.example.hesus.journalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpdateJournal extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Date date;

    public EditText journalEditText;
    public Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_journal);

        mFirebaseInstance = FirebaseDatabase.getInstance();



        initUIComponents();

        updateJournal();
    }

    private void initUIComponents() {

        updateButton =(Button) findViewById (R.id.updateButton);
        journalEditText = (EditText)findViewById(R.id.updatejournalEditText);

        journalEditText.setText(getIntent().getStringExtra("myjournalnote"));

        String journalId = getIntent().getStringExtra("myjournalkey").toString();

        mFirebaseDatabase = mFirebaseInstance.getReference().child("Journal").child(journalId);

    }

    private void updateJournal() {
updateButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!TextUtils.isEmpty(journalEditText.getText().toString())) {
                    dataSnapshot.getRef().child("journal").setValue(journalEditText.getText().toString());
                    date = Calendar.getInstance().getTime();
                    dataSnapshot.getRef().child("date").setValue(date);

                    journalEditText.setText("");

                    Toast.makeText(UpdateJournal.this, "Journal updated", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(UpdateJournal.this, JournalContent.class));
                }

                else {
                    //if the value is not given displaying a toast
                    Toast.makeText(UpdateJournal.this, "Please write your Jornal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
});


    }

}


