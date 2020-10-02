package com.example.chatclient.roomdatabase.entites;

import com.example.chatclient.retrofitclient.retrofitpojos.ConversationPojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "conversations")
public class Conversation {
    @PrimaryKey
    public int convId;

    public String name;

    public Conversation(){
        super();
    }

    public Conversation(ConversationPojo pojo){
        super();
        this.convId=pojo.id;
        this.name=pojo.name;
    }
}

