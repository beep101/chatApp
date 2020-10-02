package com.example.chatclient.retrofitclient.retrofitpojos;

import com.example.chatclient.roomdatabase.entites.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPojo {
    @Expose
    @SerializedName("id")
    public int id;
    @Expose
    @SerializedName("email")
    public String email;
    @Expose
    @SerializedName("passwd")
    public String password;
    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("admin")
    public boolean admin;

    public UserPojo(){
        super();
    }

    public UserPojo(User user){
        id=user.userId;
        name=user.name;
    }
}
