package com.example.chatclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chatclient.dataaccessmanagers.AuthUserManager;
import com.example.chatclient.ChatApplication;
import com.example.chatclient.R;
import com.example.chatclient.dataaccessmanagers.ConvsManager;
import com.example.chatclient.dataaccessmanagers.ICallback;
import com.example.chatclient.activities.recyclerviewadapters.ConversationAdapter;
import com.example.chatclient.roomdatabase.entites.ConversationWithUsers;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    AuthUserManager authManager;
    Button addBtn;
    TextView nameTxt;

    private RecyclerView convListView;
    private RecyclerView.Adapter convListAdapter;
    private RecyclerView.LayoutManager convListManager;
    private List<ConversationWithUsers> convs;
    private ConvsManager convManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authManager=((ChatApplication)getApplicationContext()).getAuthManager();
        convManager=((ChatApplication)getApplicationContext()).getConvsManager();
        convs=new ArrayList<>();

        setContentView(R.layout.activity_main);
        convListView=findViewById(R.id.convsRecView);

        convListManager=new LinearLayoutManager(this);
        convListView.setLayoutManager(convListManager);
        convListAdapter=new ConversationAdapter(this,convs);
        convListView.setAdapter(convListAdapter);

        if(authManager.getToken().equals("")){
            startLoginActivity();
        }

        nameTxt=findViewById(R.id.nameTxt);
        nameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserOptionsActivity();
            }
        });

        addBtn=findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateConvActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(authManager.getToken().equals("")) {
            startLoginActivity();
        }else{
            nameTxt.setText(authManager.getName());
            convManager.getAllConversations(new ICallback() {
                @Override
                public void cb(Object... objects) {
                    convs.clear();
                    for(Object o:objects)
                        convs.add((ConversationWithUsers)o);
                    convListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void startLoginActivity(){
        Intent startLoginIntent=new Intent(this, Login.class);
        startActivity(startLoginIntent);
    }

    private void startCreateConvActivity(){
        Intent startCreateConvIntent=new Intent(this, CreateConv.class);
        startActivity(startCreateConvIntent);
    }

    private void startUserOptionsActivity(){
        Intent startUserOptions=new Intent(this,UserOptions.class);
        startActivity(startUserOptions);
    }
}