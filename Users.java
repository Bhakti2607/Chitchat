package com.example.chitchat;

public class Users {

    private String uemail, uname, uphone,upass;




    public Users(){

    }

    public Users(String uemail, String uname, String uphone,String upass) {
        this.uemail = uemail;
        this.uname = uname;
        this.uphone = uphone;
        this.upass=upass;
    }

    public String getUemail() {
        return uemail;
    }

    public String getUname() {
        return uname;
    }

    public String getUpass() {
        return upass;
    }

    public void setUpass(String upass) {
        this.upass = upass;
    }

    public String getUphone() {
        return uphone;

    }
}

