package com.example.chatclient.roomdatabase.entites;

import com.example.chatclient.retrofitclient.retrofitpojos.ParticipantPojo;

import androidx.room.Entity;

@Entity(primaryKeys = {"convId","userId"})
public class UserConvRef{
    public int convId;
    public int userId;

    public UserConvRef(){
        super();
    }

    public UserConvRef(ParticipantPojo pojo){
        this.convId=pojo.conv;
        this.userId=pojo.user;
    }
}
