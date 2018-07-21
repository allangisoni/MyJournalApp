package com.example.hesus.journalapp;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Date;

public class Journal {

    private String journalId;
    public String journal;
    public String date;




    public Journal() {
    }

    public Journal(String journalId, String journal, String date) {
        this.journalId = journalId;
        this.journal = journal;
        this.date = date;
    }


    public String getJournalId(){
        return journalId;
    }
    public String getJournal(){
        return journal;
    }

    public void setJournal(String journal){
        this.journal = journal;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }



}



