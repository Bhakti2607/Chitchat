package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Friends extends AppCompatActivity {
private RecyclerView myfrndlist;
private DatabaseReference frndref,userdata;
private FirebaseAuth mAuth;
String online_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        myfrndlist=(RecyclerView)findViewById(R.id.frnd_list);
        mAuth=FirebaseAuth.getInstance();
        online_user_id=mAuth.getCurrentUser().getUid();
        frndref= FirebaseDatabase.getInstance().getReference().child("Friends").child(online_user_id);
        userdata=FirebaseDatabase.getInstance().getReference().child("Users");
        myfrndlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myfrndlist.setLayoutManager(linearLayoutManager);
        DisplayAllFriends();

    }

    private void DisplayAllFriends(){
        FirebaseRecyclerAdapter<FriendsListModel,friendsViewHolder>firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<FriendsListModel, friendsViewHolder>(
                        FriendsListModel.class,
                        R.layout.users_single_layout,
                        friendsViewHolder.class,
                         frndref

        ) {
            @Override
            protected void populateViewHolder(final friendsViewHolder friendsViewHolder, FriendsListModel friendsListModel, int i) {
                final String userids=getRef(i).getKey();
                userdata.child(userids).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            final String username=snapshot.child("uname").getValue().toString();
                            friendsViewHolder.setName(username);
                            friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CharSequence options[]=new CharSequence[]{
                                            username + " 's Profile",
                                            "Send Message"
                                    };
                                    AlertDialog.Builder builder=new AlertDialog.Builder(Friends.this);
                                    builder.setTitle("Select Option");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(i==0){
                                                Intent i1=new Intent(Friends.this,Addfriend.class);
                                                i1.putExtra("UserId",userids);
                                                startActivity(i1);
                                            }
                                            if(i==1){
                                                Intent i2=new Intent(Friends.this,FrndChat.class);
                                                i2.putExtra("UserId",userids);
                                                i2.putExtra("username",username);
                                                startActivity(i2);

                                            }
                                        }
                                    });
                                    builder.show();
                                }
                            });
                           // final String profileimg=snapshot.child("uname").getValue().toString();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
        myfrndlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class friendsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public friendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setName(String name){
            TextView mname = mView.findViewById (R.id.text1);
            mname.setText (name);
        }

    }
}