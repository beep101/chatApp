package com.example.chatclient.roomdatabase;

import com.example.chatclient.roomdatabase.dataaccessobjects.ConversationDao;
import com.example.chatclient.roomdatabase.dataaccessobjects.MessageDao;
import com.example.chatclient.roomdatabase.dataaccessobjects.UserDao;
import com.example.chatclient.roomdatabase.entites.Conversation;
import com.example.chatclient.roomdatabase.entites.Message;
import com.example.chatclient.roomdatabase.entites.User;
import com.example.chatclient.roomdatabase.entites.UserConvRef;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, UserConvRef.class, Conversation.class, Message.class},version = 1)
public abstract class LocalMessagingDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ConversationDao convDao();
    public abstract MessageDao msgDao();
}
