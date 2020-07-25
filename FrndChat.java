package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrndChat extends AppCompatActivity {
    private Toolbar chatTollbar;
private RecyclerView msguser;
private final List<MessagesModel> msglist=new ArrayList<>();
private LinearLayoutManager linearLayoutManager;
private MsgAdapter msgAdapter;

private ImageButton msgsend,imagechat;
private EditText writemsg;
private String receiver_id,receiver_name,sender_id,saveCurDate,saveCurTime;
private TextView custom_name;
private DatabaseReference rootref;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frnd_chat);
        mAuth=FirebaseAuth.getInstance();
        sender_id=mAuth.getCurrentUser().getUid();

        rootref= FirebaseDatabase.getInstance().getReference();
        receiver_id=getIntent().getStringExtra("UserId");
        receiver_name=getIntent().getStringExtra("username");


        initializeFields();
        dispuserInfo();
        msgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMsg();
            }
        });

        FetchMessages();
    }

    private void FetchMessages() {
        rootref.child("Messages").child(sender_id).child(receiver_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.exists()){
                        MessagesModel messagesModel=snapshot.getValue(MessagesModel.class);
                        msglist.add(messagesModel);
                        msgAdapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SendMsg() {
        String msgText=writemsg.getText().toString();
        if(TextUtils.isEmpty(msgText)){
            Toast.makeText(this,"Please type message",Toast.LENGTH_SHORT).show();

        }
        else{
            String msg_send_ref="Messages/" + sender_id + "/"+ receiver_id;
            String msg_rec_ref="Messages/" + receiver_id + "/"+ sender_id;

            DatabaseReference user_msg_key=rootref.child("Messages").child(sender_id).child(receiver_id).push();

            String msg_push_id=user_msg_key.getKey();
            Calendar cal=Calendar.getInstance();
            SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurDate=currentDate.format(cal.getTime());

            Calendar caltime=Calendar.getInstance();
            SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm aa");
            saveCurTime=currentTime.format(caltime.getTime());

            Map messageTextBody=new HashMap();
              messageTextBody.put("message",msgText);
              messageTextBody.put("time",saveCurTime);
              messageTextBody.put("date",saveCurDate);
              messageTextBody.put("type","text");
              messageTextBody.put("from",sender_id);

            Map messageBodyDetails=new HashMap();
            messageBodyDetails.put(msg_send_ref + "/" + msg_push_id , messageTextBody);
            messageBodyDetails.put(msg_rec_ref + "/" + msg_push_id , messageTextBody);
            rootref.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(FrndChat.this,"Message Sent Successfully",Toast.LENGTH_SHORT).show();
                        writemsg.setText(" ");
                    }
                    else{
                        String mmsg=task.getException().getMessage();
                        Toast.makeText(FrndChat.this,"Error"+mmsg,Toast.LENGTH_SHORT).show();
                        writemsg.setText(" ");
                    }
                }
            });

        }
    }

    private void dispuserInfo() {
        custom_name.setText(receiver_name);

    }


    private void initializeFields() {
        chatTollbar=(Toolbar)findViewById(R.id.chat_bar);
        setActionBar(chatTollbar);
       ActionBar actionBar=getSupportActionBar();
       actionBar.setDisplayHomeAsUpEnabled(true);
       actionBar.setDisplayShowCustomEnabled(true);
       LayoutInflater layoutInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View action_bar_view=layoutInflater.inflate(R.layout.chat_custom,null);
       actionBar.setCustomView(action_bar_view);
        msguser = (RecyclerView) findViewById(R.id.msguser);
        msgsend = findViewById(R.id.msgsend);
        imagechat = findViewById(R.id.imagechat);
        writemsg = findViewById(R.id.writemsg);
        custom_name=findViewById(R.id.custom_name);

        msgAdapter=new MsgAdapter(msglist);
        msguser=(RecyclerView)findViewById(R.id.msguser);
        linearLayoutManager=new LinearLayoutManager(this);
        msguser.setHasFixedSize(true);
        msguser.setLayoutManager(linearLayoutManager);
        msguser.setAdapter(msgAdapter);


    }
}