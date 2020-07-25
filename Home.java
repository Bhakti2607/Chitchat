package com.example.chitchat;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser fu=firebaseAuth.getCurrentUser();
        if(fu!=null){
            startActivity(new Intent(Home.this,ContactList.class));
        }
    }
}
