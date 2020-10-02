package com.example.chatclient.roomdatabase.entites;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

public class ConversationWithUsers {
    @Embedded
    public Conversation conversation;

    @Relation(parentColumn = "convId",entityColumn = "userId",associateBy = @Junction(UserConvRef.class))
    public List<User> users;
}
