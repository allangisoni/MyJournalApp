package com.example.hesus.journalapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Journal> listdata;

    public MyAdapter( List<Journal> listdata) {

        this.listdata = listdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.journalitemlist,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       Journal journal = listdata.get(position);
        holder.nameTxt.setText(journal.getJournal());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }
}
