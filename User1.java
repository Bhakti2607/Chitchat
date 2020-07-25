package com.example.chitchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class User1 {
private static String uname;
private String umail;
private String uphone;
private String upass;
private String uimg;

private SharedPreferences prefs;

    public User1(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }
    public User1() {
    }

    public User1(String uname, String umail, String uphone, String upass) {
        this.uname = uname;
        this.umail = umail;
        this.uphone = uphone;
        this.upass = upass;
        this.uimg=uimg;
    }

    public String getUname() {

        return uname;
    }

    public void setUname(String uname) {

        this.uname = uname;
    }

    public String getUmail() {
      //  String uemail = prefs.getString("umail","");
        return umail;
    }

    public void setUmail(String umail) {
       // prefs.edit().putString("uemail", umail).commit();
        this.umail = umail;
    }

    public String getUphone() {

        return uphone;
    }

    public void setUphone(String uphone) {

        this.uphone = uphone;
    }

    public String getUpass() {
      //  String upass = prefs.getString("upass","");
        return upass;
    }

    public void setUpass(String upass) {
    //    prefs.edit().putString("upass", upass).commit();
        this.upass = upass;
    }
    public String getImg(){

        return uimg;
    }
    public void setUimg(String uimg)
    {
        this.uimg=uimg;
    }
}

