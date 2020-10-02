package com.example.chatclient.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chatclient.ChatApplication;
import com.example.chatclient.R;

import java.util.concurrent.CompletableFuture;

public class UserOptions extends AppCompatActivity {

    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        logoutBtn=findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChatApplication)getApplicationContext()).getAuthManager().removeUser();
                CompletableFuture.runAsync(()->{
                    ((ChatApplication)getApplicationContext()).getLocaDb().clearAllTables();
                });
                finish();
            }
        });
    }
}