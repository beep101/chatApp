package com.example.chatclient.retrofitclient.retrofitpojos;

import com.example.chatclient.roomdatabase.entites.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessagePojo {
    @Expose
    @SerializedName(value="convId",alternate={"conversation"})
    public int conv;
    @Expose(serialize = false)
    @SerializedName("msgNum")
    public long msg;
    @Expose
    @SerializedName("user")
    public int user;
    @Expose
    @SerializedName(value="message", alternate={"msg"})
    public String message;

    public MessagePojo(){
        super();
    }

    public MessagePojo(Message msg){
        this.conv=msg.convId;
        this.msg=msg.msgId;
        this.user=msg.userCreator;
        this.message=msg.msg;
    }
}
