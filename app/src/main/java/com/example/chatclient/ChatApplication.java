package com.example.chatclient;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.chatclient.dataaccessmanagers.AuthUserManager;
import com.example.chatclient.dataaccessmanagers.ConvsManager;
import com.example.chatclient.dataaccessmanagers.MsgsManager;
import com.example.chatclient.dataaccessmanagers.UsersManager;
import com.example.chatclient.roomdatabase.LocalMessagingDatabase;

import androidx.core.content.ContextCompat;
import androidx.room.Room;

public class ChatApplication extends Application {
    private LocalMessagingDatabase locaDb;
    private AuthUserManager authManager;
    private ConvsManager convsManager=null;
    private UsersManager usersManager=null;
    private MsgsManager msgsManager=null;
    private SocketListenerService socketService;
    private ServiceConnection socketServiceConn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SocketListenerService.SocketListenerBinder binder=(SocketListenerService.SocketListenerBinder)iBinder;
            socketService=binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        locaDb= Room.databaseBuilder(this,LocalMessagingDatabase.class,"msgs-database").build();
        authManager=new AuthUserManager(this);
        if(!authManager.getToken().equals("")) {
            generateManagers(authManager.getToken());
            fetchData();
        }
    }

    public void generateManagers(String token){
        usersManager=new UsersManager(this,token);
        convsManager=new ConvsManager(this,token);
        msgsManager=new MsgsManager(this,token);
        fetchData();
    }

    public void fetchData(){

        usersManager.loadUsers();
        convsManager.loadAllConversations();

        Intent socketServiceStart=new Intent(this,SocketListenerService.class);
        startForegroundService(socketServiceStart);
        bindService(socketServiceStart,socketServiceConn, Context.BIND_AUTO_CREATE);
    }


    public void removeManagers(){
        convsManager=null;
        usersManager=null;
        msgsManager=null;
    }

    public LocalMessagingDatabase getLocaDb() {
        return locaDb;
    }

    public AuthUserManager getAuthManager() {
        return authManager;
    }

    public ConvsManager getConvsManager() {
        return convsManager;
    }

    public UsersManager getUsersManager() {
        return usersManager;
    }

    public MsgsManager getMsgsManager() {
        return msgsManager;
    }

    public SocketListenerService getSocketService() {
        return socketService;
    }
}
