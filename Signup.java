package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
EditText et,et2;
    private FirebaseAuth auth;
Button bb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FirebaseApp.initializeApp (Signup.this);
        auth = FirebaseAuth.getInstance();

        et=findViewById(R.id.editTextTextPersonName4);
        et2=findViewById(R.id.editTextTextPersonName5);
        bb=findViewById(R.id.button4);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailid, password;
                emailid = et.getText().toString();
                password = et2.getText().toString();


                auth.signInWithEmailAndPassword (emailid, password).addOnCompleteListener (Signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful ()){
                            Toast.makeText (Signup.this, "Failed to login!!! ", Toast.LENGTH_SHORT).show ();
                        }else{
                            Intent it = new Intent (Signup.this, ContactList.class);
                            startActivity (it);
                            finish ();
                        }
                    }
                });
            }
        });
    }
}