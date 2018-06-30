package com.example.hesus.journalapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {


    List<Journal> listdata;
    Context context;


    public MyAdapter( List<Journal> listdata,Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.journalitemlist,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       Journal journal = listdata.get(position);
        holder.nameTxt.setText(journal.getJournal());
        holder.nameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent updateIntent = new Intent(context, UpdateJournal.class);

                Journal myJournal = listdata.get(position);

                updateIntent.putExtra("myjournalnote", myJournal.getJournal());
                updateIntent.putExtra("myjournalkey",myJournal.getJournalId());

                context.startActivity(updateIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


}
