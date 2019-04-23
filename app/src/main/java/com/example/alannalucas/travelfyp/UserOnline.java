package com.example.alannalucas.travelfyp;

public class UserOnline {

    private String email,status;

    public UserOnline(){

    }

    public UserOnline(String email, String status){
        this.email = email;
        this.status = status;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
