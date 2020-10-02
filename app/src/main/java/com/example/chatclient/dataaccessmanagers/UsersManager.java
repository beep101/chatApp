package com.example.chatclient.dataaccessmanagers;

import android.content.Context;
import android.util.Log;

import com.example.chatclient.ChatApplication;
import com.example.chatclient.retrofitclient.ServiceGenerator;
import com.example.chatclient.retrofitclient.retrofitapis.UserService;
import com.example.chatclient.retrofitclient.retrofitpojos.UserPojo;
import com.example.chatclient.roomdatabase.dataaccessobjects.UserDao;
import com.example.chatclient.roomdatabase.entites.User;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersManager {
    UserService userService;
    UserDao userDao;

    public UsersManager(Context applicationContext,String token){
        userDao=((ChatApplication)applicationContext).getLocaDb().userDao();
        userService= ServiceGenerator.createService(UserService.class,token);
    }

    public void getAllUsers(ICallback cb){
        CompletableFuture.runAsync(()-> {
                List<User> users=userDao.loadAllUsers();
                Log.println(Log.INFO,"users_manager",String.valueOf(users.size()));
                cb.cb(users.toArray(new User[users.size()]));
        });
    }

    public void getUserById(int id,ICallback cb){
        CompletableFuture.runAsync(()-> {
            User user = userDao.loadUser(id);
            if (user == null) {
                Call<UserPojo> getUser = userService.getUserById(id);
                try {
                    Response<UserPojo> response = getUser.execute();
                    if (response.isSuccessful()) {
                        UserPojo userResp = response.body();
                        User userIns = new User();
                        userIns.name = userResp.name;
                        userIns.userId = userResp.id;
                        userDao.updateUser(userIns);
                        cb.cb(userIns);
                    } else {
                        cb.cb();
                    }
                } catch (IOException e) {

                }
            } else {
                cb.cb(user);
            }
        });
    }

    public void findUsers(String term,ICallback cb){
        CompletableFuture.runAsync(()->{
            List<User> users = userDao.findUser(term);
            if (users.size() < 1) {
                Call<List<UserPojo>> getUsers = userService.findUserByName(term);
                try {
                    Response<List<UserPojo>> response = getUsers.execute();
                    if (response.isSuccessful()) {
                        List<UserPojo> usersResp = response.body();
                        User[] userArr = new User[usersResp.size()];
                        int n = 0;
                        for (UserPojo ur : usersResp) {
                            User ui = new User();
                            ui.name = ur.name;
                            ui.userId = ur.id;
                            userArr[n] = ui;
                            n++;
                        }

                        Log.println(Log.INFO,"users_insert",String.valueOf(userArr.length));
                        userDao.insertUsers(userArr);

                        cb.cb(userArr);
                    } else {
                        cb.cb();
                    }
                } catch (IOException e) {

                }
            } else {
                cb.cb(users);
            }
        });

    }

    public void loadUsers(){
       CompletableFuture.runAsync(()-> {
           Call<List<UserPojo>> getUsers = userService.getAllUsers();
           try {
               Response<List<UserPojo>> response = getUsers.execute();
               if (response.isSuccessful()) {
                   List<UserPojo> usersResp = response.body();
                   Log.println(Log.INFO,"load_users",String.valueOf(usersResp.size()));
                   User[] userArr = new User[usersResp.size()];
                   int n = 0;
                   for (UserPojo ur : usersResp) {
                       User ui = new User();
                       ui.name = ur.name;
                       ui.userId = ur.id;
                       userArr[n] = ui;
                       n++;
                   }
                   Log.println(Log.INFO,"users_insert",String.valueOf(userArr.length));
                   userDao.insertUsers(userArr);
               }
           } catch (IOException e) {

           }
       });
    }

}
