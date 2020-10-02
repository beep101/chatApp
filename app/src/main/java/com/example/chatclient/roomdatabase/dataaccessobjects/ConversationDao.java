package com.example.chatclient.roomdatabase.dataaccessobjects;

import com.example.chatclient.roomdatabase.entites.Conversation;
import com.example.chatclient.roomdatabase.entites.ConversationWithUsers;
import com.example.chatclient.roomdatabase.entites.UserConvRef;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ConversationDao {
    //insert, update and delete
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertConversations(Conversation... convs);

    @Update
    public void updateConversations(Conversation... convs);

    @Delete
    public void deleteConversations(Conversation... convs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUserConv(UserConvRef... userConv);

    @Delete
    public void deleteUserConv(UserConvRef... userConv);

    //queries
    @Query("SELECT * FROM conversations")
    public List<ConversationWithUsers> getAllConversations();

    @Query("SELECT * FROM conversations WHERE convId=:id LIMIT 1")
    public ConversationWithUsers getConversationById(int id);

    @Query("SELECT * FROM conversations WHERE name LIKE :name")
    public List<ConversationWithUsers> findConversationByName(String name);
}
