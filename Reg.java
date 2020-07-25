package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.firestore.FirebaseFirestore.*;

public class Reg extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText e1,e2,e3,e4;
Button b1;
String mail, pass;
String phone,name;
private FirebaseAuth mAuth;
FirebaseFirestore db;
String UserId;

ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        e1=findViewById(R.id.editTextTextPersonName);
        e2=findViewById(R.id.editTextTextPersonName2);
        e3=findViewById(R.id.editTextTextPersonName3);
        e4=findViewById(R.id.editTextTextPersonName6);
        b1=findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=e4.getText().toString();
                mail=e1.getText().toString().trim();

                phone=e2.getText().toString();
                pass=e3.getText().toString().trim();
                if(name.isEmpty()){
                    e4.setError("Fill this field");
                    e4.requestFocus();
                }
                else if (mail.isEmpty()){
                    e1.setError("Fill this Field");
                    e1.requestFocus();
                }
                else if(pass.isEmpty()){
                    e3.setError("Fi;; this field");
                    e3.requestFocus();
                }
                else if(phone.isEmpty()){
                    e2.setError("Fill this field");
                    e2.requestFocus();
                }

                ((FirebaseAuth) mAuth).createUserWithEmailAndPassword (mail, pass).addOnCompleteListener (Reg.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful ()){
                            Toast.makeText (Reg.this,"Done!!!", Toast.LENGTH_LONG).show ();
                            FirebaseUser fu = mAuth.getInstance ().getCurrentUser ();
                            UserId = fu.getUid ();
                            DatabaseReference dr = FirebaseDatabase.getInstance ().getReference ().child("Users").child(UserId);


                            //UserId = mAuth.getCurrentUser().getUid();
                            /*DocumentReference dr = db.collection("users").document(UserId);*/
                            Map<String, Object> user = new HashMap<>();
                            user.put("uname",name);
                            user.put("uemail",mail);
                            user.put("uphone",phone);
                            dr.setValue (user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"User registration is done for:"+UserId);
                                }
                            });
                            Intent it = new Intent (Reg.this, MainActivity.class);
                            startActivity (it);
                        }else{
                            Toast.makeText (Reg.this,"Failed to create account", Toast.LENGTH_LONG).show ();
                        }
                    }
                });




            }

        });

    }
}