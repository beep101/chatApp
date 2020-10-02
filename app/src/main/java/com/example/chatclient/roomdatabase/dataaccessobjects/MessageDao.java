package com.example.chatclient.roomdatabase.dataaccessobjects;

import com.example.chatclient.roomdatabase.entites.Message;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMessages(Message... msgs);

    @Update
    public void updateMessages(Message... msgs);

    @Delete
    public void deleteMessages(Message... msgs);

    @Query("SELECT * FROM messages WHERE convId=:conv ORDER BY msgId DESC LIMIT :limit")
    public List<Message> getMessges(int conv, int limit);

    @Query("SELECT * FROM messages WHERE convId=:conv AND msgId<=:msgId ORDER BY msgId DESC LIMIT :limit")
    public List<Message> getMessages(int conv, long msgId, int limit);
}
