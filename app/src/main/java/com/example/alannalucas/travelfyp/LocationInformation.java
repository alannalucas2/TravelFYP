package com.example.alannalucas.travelfyp;

public class LocationInformation {

    //public String id;
    public String name;
    public double latitude;
    public double longitude;
    public float rating;
    public String placeID;
    public String time;
    public String address;


    public LocationInformation(){
    }


    public LocationInformation(String name, double latitude,double longitude, float rating, String placeID, String time, String address){
        //this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.placeID = placeID;
        this.time = time;
        this.address = address;

    }

    public LocationInformation(double latitude,double longitude){
        //this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
