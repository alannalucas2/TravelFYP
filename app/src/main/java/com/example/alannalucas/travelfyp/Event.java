package com.example.alannalucas.travelfyp;

import java.io.Serializable;

public class Event implements Serializable {

    String name, date, venue, image, ticketsURL, eventTime;
    double longitude, latitude;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Event(String name, String date, String venue, double latitude, double longitude) {
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTicketsURL() {
        return ticketsURL;
    }

    public void setTicketsURL(String ticketsURL) {
        this.ticketsURL = ticketsURL;
    }

    public String getEventTime() {
        return eventTime;
    }

    public Event()
    {

    }

    public Event(String name, String venue, String image, String date, String eventTime, String ticketsURL)
    {
        this.name = name;
        this.venue = venue;
        this.image = image;
        this.ticketsURL = ticketsURL;
        this.date = date;
        this.eventTime=eventTime;
        this.ticketsURL = ticketsURL;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Event(String name, String venue, String image, String ticketsURL, String date, String eventTime, double latitude, double longitude)
    {
        this.name = name;
        this.venue = venue;
        this.image = image;
        this.ticketsURL = ticketsURL;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}