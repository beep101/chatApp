package com.example.chatclient;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.chatclient.activities.MainActivity;
import com.example.chatclient.dataaccessmanagers.MsgsManager;
import com.example.chatclient.retrofitclient.retrofitpojos.MessagePojo;
import com.example.chatclient.retrofitclient.retrofitpojos.TokenPojo;
import com.example.chatclient.roomdatabase.entites.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URISyntaxException;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketListenerService extends Service {
    private final IBinder binder=new SocketListenerBinder();
    public static final String CHANNEL="SocketListenerService";

    Socket socket;
    private Gson gson;
    private MsgsManager msgsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.println(Log.DEBUG,"service_start","oncreate service");
        try{
            socket= IO.socket("http://ec2-52-57-135-108.eu-central-1.compute.amazonaws.com:8000/msgs");
            socket.connect();
        }catch (URISyntaxException e){

        }

        gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setLenient()
            .create();

        msgsManager=((ChatApplication)getApplicationContext()).getMsgsManager();

        socket.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                TokenPojo token=new TokenPojo(((ChatApplication)getApplicationContext()).getAuthManager().getToken());
                socket.emit("authorize", gson.toJson(token));
            }
        });

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String data=((org.json.JSONObject)args[0]).toString();
                MessagePojo msgPojo=gson.fromJson(data,MessagePojo.class);
                Message msg=new Message(msgPojo);
                msgsManager.addMessage(msg);

                Intent intent=new Intent();
                intent.setAction("msg_reccieved_"+String.valueOf(msg.convId));
                intent.putExtra("convId",msg.convId);
                intent.putExtra("msgId",msg.msgId);
                intent.putExtra("msg",msg.msg);
                intent.putExtra("userCreator",msg.userCreator);

                //notify user
                //notification click opens chat messages activity

                sendBroadcast(intent);
            }
        });

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //click on notification opens main activity
        //createNotificationChannel();
        //Intent mainActivityIntent=new Intent(getApplicationContext(), MainActivity.class);
        //PendingIntent pendingIntent=PendingIntent.getActivity(this,0,mainActivityIntent,0);
        Notification notification=new NotificationCompat.Builder(this,CHANNEL)
                .setContentTitle("Chat App Running")
                .setContentText("Recieving new messages...")
                //.setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return binder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return false;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    public void sendMessage(MessagePojo messagePojo){
        socket.emit("message", gson.toJson(messagePojo));
    }

    private void createNotificationChannel(){
        NotificationChannel serviceChannel=new NotificationChannel(CHANNEL, "Socket Listener Channel", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(serviceChannel);
    }

    private void newMessages(){

    }

    public class SocketListenerBinder extends Binder {

        public SocketListenerService getService() {
            return SocketListenerService.this;

        }
    }
}
