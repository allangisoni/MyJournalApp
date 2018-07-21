package com.example.hesus.journalapp;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView nameTxt, calenderTxt, timerTxt;

    public RelativeLayout viewBackground, viewForeground;


    public MyViewHolder(View itemView) {
        super(itemView);
        nameTxt=(TextView) itemView.findViewById(R.id.nameTxt);
        calenderTxt=(TextView) itemView.findViewById(R.id.tvJournalYear);
        //nameTxt=(TextView) itemView.findViewById(R.id.tvJournalTime);
        viewBackground =(RelativeLayout) itemView.findViewById(R.id.view_background);
        viewForeground = (RelativeLayout) itemView.findViewById(R.id.view_foreground);

    }



}
