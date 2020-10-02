package com.example.chatclient.retrofitclient.retrofitpojos;

import android.provider.Telephony;

import com.example.chatclient.roomdatabase.entites.UserConvRef;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParticipantPojo {
    @Expose
    @SerializedName("user")
    public int user;
    @Expose
    @SerializedName("conv")
    public int conv;
    @Expose
    @SerializedName("recieved")
    public long recieved;
    @Expose
    @SerializedName("seen")
    public long seen;
    @Expose(serialize = false)
    @SerializedName("active")
    public boolean active;
    @Expose(serialize = false)
    @SerializedName("userObj")
    public UserPojo userObj;

    public ParticipantPojo(){
        super();
    }

    public ParticipantPojo(UserConvRef userConv){
        this.conv=userConv.convId;
        this.user=userConv.userId;
    }
}
