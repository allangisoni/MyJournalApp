package com.example.hesus.journalapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {

    Calendar mCurrentDate;
    public EditText journalEditText, journalDate;
    public Date date;
    public Button saveButton;

    String myJournal;
    private String userId;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String id;

    DateFormat df;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initUIComponents();

        journalDate.setFocusable(false);

         df = new SimpleDateFormat("EEE, d MMM yyyy");


        mFirebaseInstance = FirebaseDatabase.getInstance();

        //id = getIntent().getStringExtra("userId");

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
        journalDate = (EditText)findViewById(R.id.eTjournalDate);


    }

    private void addJournal() {
        //getting the values to save
        myJournal = journalEditText.getText().toString();


        date = Calendar.getInstance().getTime();


        String formattedDate = df.format(date);


        //checking if the value is provided
        if (!TextUtils.isEmpty(myJournal)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist

            String id = mFirebaseDatabase.push().getKey();


            //creating an Artist Object
            Journal journal = new Journal(id,myJournal,formattedDate);

            //Saving the Artist
            mFirebaseDatabase.child(id).setValue(journal);


            //setting edittext to blank again
            journalEditText.setText("");

            //displaying a success toast
            Toast.makeText(this, "Journal added", Toast.LENGTH_LONG).show();

            startActivity(new Intent(AddJournalActivity.this, MainActivity.class));
            finish();


        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please write your Jornal", Toast.LENGTH_LONG).show();
        }
    }

    public  void getSelectedDate(){

        mCurrentDate = Calendar.getInstance();
        int year = mCurrentDate.get(Calendar.YEAR);
        int month = mCurrentDate.get(Calendar.MONTH);
        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker =  new DatePickerDialog(AddJournalActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                mCurrentDate.set(selectedYear,selectedMonth,selectedDay);
                String selectedDate = Integer.toString(selectedYear) + "-"+ Integer.toString(selectedMonth) + "-" + Integer.toString(selectedDay);

                String formatedDate = df.format(selectedDate);
                journalDate.setText(formatedDate);

                Toast.makeText(getApplicationContext(),formatedDate , Toast.LENGTH_SHORT).show();
            }


        },year,month, day);
        mDatePicker.show();



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


                return true;

            case R.id.menu_share:

                return true;

            case R.id.menu_edit:
                getSelectedDate();
                //  startActivity(new Intent(UpdateJournal.this, mycalendar.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
