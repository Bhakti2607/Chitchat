package com.example.chitchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Chat extends AppCompatActivity {
   // private Toolbar tb;
    private RecyclerView userl;
    private DatabaseReference dr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //tb = (Toolbar)findViewById(R.id.users_appBar);
      //  getSupportActionBar ().setTitle ("All Users");
       // getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        dr = FirebaseDatabase.getInstance ().getReference ().child ("Users");
        userl = (RecyclerView)findViewById (R.id.userlist);
        userl.setHasFixedSize (true);
        userl.setLayoutManager (new LinearLayoutManager (this));
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<User1, UserViewHolder> fa = new FirebaseRecyclerAdapter<User1, UserViewHolder> (User1.class, R.layout.users_single_layout, UserViewHolder.class, dr) {
            @Override
            protected void populateViewHolder(UserViewHolder uh, User1 u1, int i ){
                uh.setName(u1.getUname ());
                final String user_id = getRef (i).getKey ();
                uh.mview.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        Intent it = new Intent (Chat.this, Addfriend.class);
                        it.putExtra ("UserId",user_id);
                        startActivity (it);
                    }
                });
            }
        };
        userl.setAdapter (fa);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mview;
        public UserViewHolder(@NonNull View itemView) {
            super (itemView);
            mview = itemView;
        }
        public void setName(String name){
            TextView mname = mview.findViewById (R.id.text1);
            mname.setText (name);
        }
    }
}