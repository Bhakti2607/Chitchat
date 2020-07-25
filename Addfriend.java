package com.example.chitchat;





import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;


import android.net.Uri;
import android.os.Bundle;


import android.util.Log;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Nullable;

public class Addfriend extends AppCompatActivity {



    TextView profileName, profileEmail, profilePhone;
    Button addfrnd,cancelButton;
    ImageView profileImageView;
    FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    StorageReference storageReference;
    FirebaseFirestore db;
    String UserId,curUser,CURRENT_STATE,fullName,saveCurrentDate;
    //FirebaseUser curUser;
    DatabaseReference userdata;
   // private String curstate;
   DatabaseReference frndreqref,frndref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        storageReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        curUser=FirebaseAuth.getInstance().getCurrentUser().getUid();//user himself i.e. sender
         fullName = getIntent().getStringExtra("UserId");//receiver


        addfrnd=findViewById(R.id.addfrnd);
        cancelButton=findViewById(R.id.cancel_req);
        CURRENT_STATE = "not_frnds";


        //Toast.makeText (getApplicationContext (),UserId,Toast.LENGTH_LONG).show ();

        profileName = (TextView) findViewById(R.id.profileName);
        profileEmail = (TextView) findViewById(R.id.profileEmail);
        profilePhone = (TextView) findViewById(R.id.profilePhone);
        profileImageView = findViewById(R.id.imageview3);

        StorageReference profileRef = storageReference.child("users/"+fullName+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri> () {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });

        userdata = FirebaseDatabase.getInstance ().getReference ().child ("Users").child (fullName);
        userdata.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child ("uname").getValue ().toString ();
                String phone = snapshot.child ("uphone").getValue ().toString ();
                String email = snapshot.child ("uemail").getValue ().toString ();
                profileName.setText (name);
                profilePhone.setText (phone);
                profileEmail.setText (email);

                MaintainButton();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        frndreqref=FirebaseDatabase.getInstance().getReference().child("FriendRequests");
        frndref=FirebaseDatabase.getInstance().getReference().child("Friends");
        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.setEnabled(false);
        if(curUser.equals(fullName))
        {
            addfrnd.setVisibility(View.INVISIBLE);
            addfrnd.setEnabled(false);
            cancelButton.setVisibility(View.INVISIBLE);

        }
        else{
            addfrnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addfrnd.setEnabled(false);
                    if(CURRENT_STATE.equals("not_frnds")){
                        sendrequest();

                    }
                   if(CURRENT_STATE.equals("request_sent")) {
                       CancelFrndReq();
                   }
                   if(CURRENT_STATE.equals("request_received")){
                       AcceptFrndReq();
                   }
                   if(CURRENT_STATE.equals("friends")){
                       Unfriendthefrnd();
                   }
                }
            });

        }




    }

    private void Unfriendthefrnd() {

        frndref.child(curUser).child(fullName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    frndref.child(fullName).child(curUser).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        addfrnd.setEnabled(true);
                                        CURRENT_STATE="not_frnds";
                                        addfrnd.setText("Send Request");
                                        cancelButton.setVisibility(View.INVISIBLE);
                                        cancelButton.setEnabled(false);

                                    }

                                }
                            });
                }
            }
        });


    }

    private void AcceptFrndReq() {
        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate=currentdate.format(calfordate.getTime());

        frndref.child(curUser).child(fullName).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    frndref.child(fullName).child(curUser).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                frndreqref.child(curUser).child(fullName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            frndreqref.child(fullName).child(curUser).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                addfrnd.setEnabled(true);
                                                                CURRENT_STATE="friends";
                                                                addfrnd.setText("Unfriend");
                                                                cancelButton.setVisibility(View.INVISIBLE);
                                                                cancelButton.setEnabled(false);

                                                            }

                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });


    }

    private void CancelFrndReq() {

        frndreqref.child(curUser).child(fullName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    frndreqref.child(fullName).child(curUser).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        addfrnd.setEnabled(true);
                                        CURRENT_STATE="not_frnds";
                                        addfrnd.setText("Send Request");
                                        cancelButton.setVisibility(View.INVISIBLE);
                                        cancelButton.setEnabled(false);

                                    }

                                }
                            });
                }
            }
        });



    }

    private void MaintainButton() {
        frndreqref.child(curUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(fullName)){
                    String request_type=snapshot.child(fullName).child("request_type").getValue().toString();
                    if(request_type.equals("sent")){
                        CURRENT_STATE="request_sent";
                        addfrnd.setText("Cancel Request");
                        cancelButton.setVisibility(View.INVISIBLE);
                        cancelButton.setEnabled(false);
                    }
                    else if(request_type.equals("received")){
                        CURRENT_STATE="request_received";
                        addfrnd.setText("Accept Request");
                        cancelButton.setVisibility(View.VISIBLE);
                        cancelButton.setEnabled(true);
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CancelFrndReq();
                            }
                        });
                    }
                }
                else{
                    frndref.child(curUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(fullName)){
                                CURRENT_STATE="friends";
                                addfrnd.setText("Unfriend");
                                cancelButton.setVisibility(View.INVISIBLE);
                                cancelButton.setEnabled(false);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void sendrequest() {
        frndreqref.child(curUser).child(fullName).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    frndreqref.child(fullName).child(curUser).child("request_type").setValue("received")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        addfrnd.setEnabled(true);
                                        CURRENT_STATE="request_sent";
                                        addfrnd.setText("Cancel Request");
                                        cancelButton.setVisibility(View.INVISIBLE);
                                        cancelButton.setEnabled(false);

                                    }

                                }
                            });
                }
            }
        });
    }


}

