package com.example.chitchat;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgViewHolder> {
    private List<MessagesModel> usermsgList;
    private FirebaseAuth mAuth;
    private DatabaseReference userdataref;
    public MsgAdapter (List<MessagesModel> usermsgList){
        this.usermsgList=usermsgList;
    }

    public class MsgViewHolder extends RecyclerView.ViewHolder {
        public TextView sendermsgtext,recmsgtext;
        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            sendermsgtext=(TextView)itemView.findViewById(R.id.sender_textbox);
            recmsgtext=(TextView)itemView.findViewById(R.id.rec_textbox);

        }
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from((parent.getContext()))
               .inflate(R.layout.mesg_layout_user,parent,false);
       mAuth=FirebaseAuth.getInstance();
       return new MsgViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
        String msgsenderid=mAuth.getCurrentUser().getUid();
        MessagesModel messagesModel=usermsgList.get(position);
        String fromuserid=messagesModel.getFrom();
        String frommsgType=messagesModel.getType();

        userdataref= FirebaseDatabase.getInstance().getReference().child("Users").child(fromuserid);
        if(frommsgType.equals("text")){
            holder.recmsgtext.setVisibility(View.INVISIBLE);
            if(fromuserid.equals(msgsenderid)){
                holder.sendermsgtext.setBackgroundResource(R.drawable.sender_msg_text_bg);
                holder.sendermsgtext.setTextColor(Color.WHITE);
                holder.sendermsgtext.setGravity(Gravity.LEFT);
                holder.sendermsgtext.setText(messagesModel.getMessage());
            }
            else{
                holder.sendermsgtext.setVisibility(View.INVISIBLE);
                holder.recmsgtext.setVisibility(View.VISIBLE);

                holder.recmsgtext.setBackgroundResource(R.drawable.rec_msg_text_bg);
                holder.recmsgtext.setTextColor(Color.WHITE);
                holder.recmsgtext.setGravity(Gravity.LEFT);
                holder.recmsgtext.setText(messagesModel.getMessage());

            }
        }

    }

    @Override
    public int getItemCount() {

        return usermsgList.size();
    }
}
