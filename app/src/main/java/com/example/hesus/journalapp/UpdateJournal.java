package com.example.hesus.journalapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kd.dynamic.calendar.generator.ImageGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateJournal extends AppCompatActivity {

    Calendar mCurrentDate;
    ImageGenerator mImageGeneretor;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Date date;

    public EditText journalEditText;
    public Button updateButton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_journal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mFirebaseInstance = FirebaseDatabase.getInstance();


// Create an object of ImageGenerator class in your activity
// and pass the context as the parameter
        ImageGenerator mImageGenerator = new ImageGenerator(this);

// Set the icon size to the generated in dip.
        mImageGenerator.setIconSize(80, 80);

// Set the size of the date and month font in dip.
        mImageGenerator.setDateSize(30);
        mImageGenerator.setMonthSize(10);

// Set the position of the date and month in dip.
        mImageGenerator.setDatePosition(42);
        mImageGenerator.setMonthPosition(14);

// Set the color of the font to be generated
        mImageGenerator.setDateColor(Color.parseColor("#3c6eaf"));
       mImageGenerator.setMonthColor(Color.WHITE);




        initUIComponents();
       // journalEditText.setFocusableInTouchMode(false);

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
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
                    String formattedDate = df.format(date);
                    dataSnapshot.getRef().child("date").setValue(formattedDate);

                    journalEditText.setText("");

                    Toast.makeText(UpdateJournal.this, "Journal updated", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(UpdateJournal.this, MainActivity.class));
                    finish();
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


public  void getSelectedDate(){

    mCurrentDate = Calendar.getInstance();
    int year = mCurrentDate.get(Calendar.YEAR);
    int month = mCurrentDate.get(Calendar.MONTH);
    int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog mDatePicker =  new DatePickerDialog(UpdateJournal.this, new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
            mCurrentDate.set(selectedYear,selectedMonth,selectedDay);

            Toast.makeText(getApplicationContext(),Integer.toString(selectedYear) +" - " + Integer.toString(selectedMonth) + " - "+ Integer.toString(selectedDay) , Toast.LENGTH_SHORT).show();
        }


    },year,month, day);
    mDatePicker.show();



}




    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(journalEditText.getText().toString())
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
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

                Intent shareIntent  = createShareForecastIntent();

                startActivity(shareIntent);
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


