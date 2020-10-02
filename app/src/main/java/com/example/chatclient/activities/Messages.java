package com.example.chatclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatclient.ChatApplication;
import com.example.chatclient.R;
import com.example.chatclient.activities.recyclerviewadapters.MessageAdapter;
import com.example.chatclient.dataaccessmanagers.ICallback;
import com.example.chatclient.retrofitclient.retrofitpojos.MessagePojo;
import com.example.chatclient.roomdatabase.entites.ConversationWithUsers;
import com.example.chatclient.roomdatabase.entites.Message;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity {

    int convId=-1;
    private List<ConversationWithUsers> conv;
    private List<Message> messages;

    private EditText messageTxt;
    private Button sendBtn;

    private RecyclerView messgesRecView;
    private RecyclerView.Adapter messgesViewAdapter;
    private RecyclerView.LayoutManager messagesListManager;
    private BroadcastReceiver reciever;
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messages=new ArrayList<>();
        conv=new ArrayList<>();

        setContentView(R.layout.activity_messages);
        messgesRecView = findViewById(R.id.messagesRecView);

        sendBtn=findViewById(R.id.sendBtn);
        messageTxt=findViewById(R.id.messageTxt);

        messagesListManager = new LinearLayoutManager(this);
        ((LinearLayoutManager)messagesListManager).setReverseLayout(true);
        ((LinearLayoutManager)messagesListManager).setStackFromEnd(true);
        messgesRecView.setLayoutManager(messagesListManager);

        messgesViewAdapter = new MessageAdapter(this, conv, messages);
        messgesRecView.setAdapter(messgesViewAdapter);

        convId=getIntent().getIntExtra("convId",-1);
        if(convId==-1)
            finish();

        reciever=new MessageReciever();
        filter=new IntentFilter("msg_reccieved_"+String.valueOf(convId));

        ((ChatApplication)getApplicationContext()).getConvsManager().getConversationById(convId, new ICallback() {
            @Override
            public void cb(Object... objects) {
                conv.clear();
                conv.add((ConversationWithUsers)objects[0]);
            }
        });
        ((ChatApplication)getApplicationContext()).getMsgsManager().getMsgs(convId, 30, new ICallback() {
            @Override
            public void cb(Object... objects) {
                for(Object o:objects){
                    Message newMsg=(Message)o;
                    messages.add(newMsg);
                    //messgesViewAdapter.notifyItemInserted(messages.indexOf(newMsg));
                }
                messgesViewAdapter.notifyDataSetChanged();
                Log.println(Log.INFO,",msgs_load",String.valueOf(objects.length));

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.println(Log.INFO,"msgs_send","btn pressed");
                Message msg=new Message();
                msg.convId=convId;
                msg.msg=messageTxt.getText().toString();
                ((ChatApplication)getApplicationContext()).getSocketService().sendMessage(new MessagePojo(msg));
                messageTxt.setText("");
            }
        });
        messgesViewAdapter.notifyDataSetChanged();
        messgesRecView.scrollToPosition(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(reciever,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(reciever);
    }

    private class MessageReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg=new Message();
            msg.convId=intent.getIntExtra("convId",-1);
            msg.msgId=intent.getLongExtra("msgId",-1);
            msg.userCreator=intent.getIntExtra("userCreator",-1);
            msg.msg=intent.getStringExtra("msg");
            if(msg.convId!=-1){
                messages.add(0,msg);
                messgesViewAdapter.notifyDataSetChanged();
                messgesRecView.scrollToPosition(0);
            }
        }
    }
}