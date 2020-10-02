package com.example.chatclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatclient.ChatApplication;
import com.example.chatclient.R;
import com.example.chatclient.dataaccessmanagers.ICallback;
import com.example.chatclient.dataaccessmanagers.UsersManager;
import com.example.chatclient.activities.recyclerviewadapters.UserAdapter;
import com.example.chatclient.roomdatabase.entites.Conversation;
import com.example.chatclient.roomdatabase.entites.ConversationWithUsers;
import com.example.chatclient.roomdatabase.entites.User;

import java.util.ArrayList;
import java.util.List;

public class CreateConv extends AppCompatActivity {

    private EditText convNameTxt;
    private Button addBtn;

    private RecyclerView usersListView;
    private RecyclerView.Adapter userListAdapter;
    private RecyclerView.LayoutManager userListManager;
    private List<User> users;
    private UsersManager userManager;

    private int userId;
    private User crrUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        users=new ArrayList<>();
        userManager=((ChatApplication)getApplicationContext()).getUsersManager();

        userId=((ChatApplication)getApplicationContext()).getAuthManager().getId();

        userManager.getAllUsers(new ICallback() {
            @Override
            public void cb(Object... objects) {
                users.clear();
                for(Object u:objects)
                    if(((User)u).userId==userId)
                        crrUser=(User)u;
                    else
                        users.add((User)u);
                userListAdapter.notifyDataSetChanged();
            }
        });

        setContentView(R.layout.activity_create_conv);
        usersListView=findViewById(R.id.userList);

        addBtn=findViewById(R.id.addBtn);
        convNameTxt=findViewById(R.id.nameTxtField);

        userListManager=new LinearLayoutManager(this);
        usersListView.setLayoutManager(userListManager);
        userListAdapter=new UserAdapter(users);
        usersListView.setAdapter(userListAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConversationWithUsers conv=new ConversationWithUsers();
                conv.conversation=new Conversation();
                conv.users=new ArrayList<>();
                conv.conversation.name=convNameTxt.getText().toString();
                conv.users.add(crrUser);
                conv.users.addAll(((UserAdapter)userListAdapter).getSelected());
                ((ChatApplication)getApplicationContext()).getConvsManager().addConversation(conv, new ICallback() {
                    @Override
                    public void cb(Object... objects) {
                        if(objects==null)
                            Toast.makeText(getApplicationContext(),"Error while creating new chat",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
    }
}