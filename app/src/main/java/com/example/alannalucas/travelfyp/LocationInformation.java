package com.example.alannalucas.travelfyp;

public class LocationInformation {

    //public String id;
    public String name;
    public double latitude;
    public double longitude;


    public LocationInformation(){
    }

    public LocationInformation(String name, double latitude,double longitude){
        //this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

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
}
