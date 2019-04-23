package com.example.alannalucas.travelfyp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ListOnlineViewHolder extends RecyclerView.ViewHolder {
    public TextView txtEmail;



    public ListOnlineViewHolder(@NonNull View itemView) {
        super(itemView);
        txtEmail = (TextView)itemView.findViewById(R.id.txt_email);
    }
}
