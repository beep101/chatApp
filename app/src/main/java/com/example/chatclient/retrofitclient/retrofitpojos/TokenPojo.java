package com.example.chatclient.retrofitclient.retrofitpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenPojo {
    @Expose
    @SerializedName("token")
    String token;

    public TokenPojo(){
            super();
    }

    public TokenPojo(String token){
        this.token=token;
    }
}
