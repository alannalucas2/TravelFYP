package com.example.alannalucas.travelfyp;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListedLocations extends ArrayAdapter<LocationInformation> {

    private Activity context;
    private List<LocationInformation> locationList;

    public ListedLocations(Activity context, List<LocationInformation> locationList){
        super(context, R.layout.list_layout, locationList);
        this.context = context;
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        //TextView textViewOther = (TextView) listViewItem.findViewById(R.id.textViewOther);

        LocationInformation location = locationList.get(position);

        textViewName.setText(location.getName());

        return listViewItem;
    }




}
