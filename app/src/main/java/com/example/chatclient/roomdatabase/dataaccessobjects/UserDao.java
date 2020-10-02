package com.example.chatclient.roomdatabase.dataaccessobjects;

import com.example.chatclient.roomdatabase.entites.User;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    //insert, update and delete
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(User... users);

    @Update
    public void updateUser(User... users);

    @Delete
    public void deleteUsers(User... users);

    //quering
    @Query("SELECT * FROM users")
    public List<User> loadAllUsers();

    @Query("SELECT * FROM users WHERE userId=:id LIMIT 1")
    public User loadUser(int id);

    @Query("SELECT * FROM users WHERE name=:name")
    public List<User> findUser(String name);
}
