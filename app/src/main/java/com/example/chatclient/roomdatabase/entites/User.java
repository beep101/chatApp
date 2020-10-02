package com.example.chatclient.roomdatabase.entites;

import com.example.chatclient.retrofitclient.retrofitpojos.UserPojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    public int userId;

    public String name;


    public User(){
        super();
    }

    public User(int id,String name){
        userId=id;
        this.name=name;
    }

    public User(UserPojo pojo){
        userId=pojo.id;
        name=pojo.name;
    }
}
