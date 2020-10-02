package com.example.chatclient.retrofitclient.retrofitpojos;

import com.example.chatclient.roomdatabase.entites.Conversation;
import com.example.chatclient.roomdatabase.entites.ConversationWithUsers;
import com.example.chatclient.roomdatabase.entites.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ConversationPojo {
    @Expose(serialize = false)
    @SerializedName("id")
    public int id;
    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("participants")
    public List<ParticipantPojo> participants;

    public ConversationPojo(){
        super();
    }

    public ConversationPojo(Conversation conv){
        this.id=conv.convId;
        this.name=conv.name;
        participants=new ArrayList<>();
    }

    public ConversationPojo(ConversationWithUsers conv){
        this.id=conv.conversation.convId;
        this.name=conv.conversation.name;
        participants=new ArrayList<>();
        for(User user:conv.users){
            ParticipantPojo participant=new ParticipantPojo();
            participant.user=user.userId;
            participant.conv=id;
            participants.add(participant);
        }
    }
}
