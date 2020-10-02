package com.example.chatclient.roomdatabase.entites;

import androidx.room.Embedded;
import androidx.room.Relation;

public class MessageWithUserAndConv {
    @Embedded Message message;

    @Relation(parentColumn = "convId",entityColumn = "convId")
    public Conversation conv;
    @Relation(parentColumn = "userCreator",entityColumn = "userId")
    public User user;
}
