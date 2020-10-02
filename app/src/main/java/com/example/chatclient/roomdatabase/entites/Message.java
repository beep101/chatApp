package com.example.chatclient.roomdatabase.entites;

import com.example.chatclient.retrofitclient.retrofitpojos.MessagePojo;

import androidx.room.Entity;

@Entity(tableName = "messages",primaryKeys = {"convId","msgId"})
public class Message {
    //composite primary key
    public int convId;
    public long msgId;

    public String msg;
    public int userCreator;

    public Message(){
        super();
    }

    public Message(MessagePojo pojo){
        convId=pojo.conv;
        msgId=pojo.msg;
        msg=pojo.message;
        userCreator=pojo.user;
    }
}
