package com.example.alannalucas.travelfyp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {


    public List<LocationInformation> feedList;
    private Context context;
    String myLocationId;

    public FeedAdapter() {
    }

    public void updateList(List<LocationInformation> list) {
        feedList = list;
        notifyDataSetChanged();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder{
        TextView usernameStamp, addressStamp, locationStamp, ratingStamp;

        FeedViewHolder(View v) {
            super(v);
            this.locationStamp = (TextView) itemView.findViewById(R.id.locationStamp);
            this.usernameStamp = (TextView) itemView.findViewById(R.id.usernameStamp);
            this.addressStamp = (TextView) itemView.findViewById(R.id.addressStamp);
            this.ratingStamp = (TextView) itemView.findViewById(R.id.ratingStamp);
        }
    }

    public FeedAdapter(List<LocationInformation> feedList, Context context){
        this.feedList = feedList;
        this.context = context;
    }

    @Override
    public FeedAdapter.FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_layout, parent, false);
        return new FeedAdapter.FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        LocationInformation location = feedList.get(position);
        holder.locationStamp.setText(String.valueOf(location.getName()));
        holder.addressStamp.setText(String.valueOf(location.getAddress()));
        //holder.usernameStamp.setText(String.valueOf(location.getName()));
        holder.ratingStamp.setText(String.valueOf(location.getRating()));

        //myLocationId = String.valueOf((location.getPostId()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent i = new Intent(this, )

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.feedList.size();
    }

}



