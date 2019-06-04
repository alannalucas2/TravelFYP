package com.example.alannalucas.travelfyp;

public class Users {

    public String username;
    public String name;
    public String address;
    //public String email;
    public String image;


    public Users(){

    }

    public Users(String name, String address, String image, String username) {
        this.name = name;
        this.address = address;
        this.image = image;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
