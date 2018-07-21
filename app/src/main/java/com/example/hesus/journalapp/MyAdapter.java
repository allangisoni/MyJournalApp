package com.example.hesus.journalapp;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        holder.calenderTxt.setText(journal.getDate());
        holder.itemView.setTag(journal.getJournalId());

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

    public void removeItem(int position) {
        listdata.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Journal journal, int position) {
        listdata.add(position, journal);
        // notify item added by position
        notifyItemInserted(position);
    }
}
