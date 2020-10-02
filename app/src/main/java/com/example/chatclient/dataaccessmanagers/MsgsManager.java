package com.example.chatclient.dataaccessmanagers;

import android.content.Context;
import android.content.Intent;
import android.security.keystore.SecureKeyImportUnavailableException;

import com.example.chatclient.ChatApplication;
import com.example.chatclient.retrofitclient.ServiceGenerator;
import com.example.chatclient.retrofitclient.retrofitapis.MessageService;
import com.example.chatclient.retrofitclient.retrofitpojos.MessagePojo;
import com.example.chatclient.roomdatabase.dataaccessobjects.MessageDao;
import com.example.chatclient.roomdatabase.entites.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Response;

public class MsgsManager {
    MessageService msgService;
    MessageDao msgDao;

    public MsgsManager(Context applicationContext, String token){
        msgDao=((ChatApplication)applicationContext).getLocaDb().msgDao();
        msgService= ServiceGenerator.createService(MessageService.class,token);
    }

    public void getMsgs(int conv,int count,ICallback cb){
        CompletableFuture.runAsync(()->{
            int getCount=count;
            Call<List<MessagePojo>> call=msgService.getNewMsgs(conv);
            try{
                Response<List<MessagePojo>> response=call.execute();
                if(response.isSuccessful()){
                    Message[] msgArr=new Message[response.body().size()];
                    getCount=response.body().size()>count?response.body().size():count;
                    int n=0;
                    for(MessagePojo msg:response.body()){
                        msgArr[n]=new Message(msg);
                        n++;
                    }
                    msgDao.insertMessages(msgArr);
                }
            }catch(IOException e){
                cb.cb();
                return;
            }
            List<Message> msgs=msgDao.getMessges(conv,getCount);
            if(msgs.size()<getCount){
                call=msgService.getMsgsCount(conv,30);
                try{
                    Response<List<MessagePojo>> response=call.execute();
                    if(response.isSuccessful()){
                        Message[] msgArr=new Message[response.body().size()];
                        int n=0;
                        for(MessagePojo msg:response.body()){
                            msgArr[n]=new Message(msg);
                            n++;
                        }
                        msgDao.insertMessages(msgArr);
                    }
                }catch(IOException e){
                    cb.cb();
                    return;
                }
            }
            msgs=msgDao.getMessges(conv,getCount);
            cb.cb(msgs.toArray());
        });
    }

    public void getMsgsFrom(int conv,long msg, int count,ICallback cb){
        CompletableFuture.runAsync(()->{
            List<Message> msgs=msgDao.getMessages(conv,msg,count);
            if(msgs.size()<count){
                Call<List<MessagePojo>> call=msgService.getMsgsCountFrom(conv,msg,count);
                try{
                    Response<List<MessagePojo>> response=call.execute();
                    if(response.isSuccessful()){
                        Message[] msgsArr=new Message[response.body().size()];
                        int n=0;
                        for(MessagePojo msgPojo:response.body()){
                            msgsArr[n]=new Message(msgPojo);
                            n++;
                        }
                        msgDao.insertMessages(msgsArr);
                        cb.cb(msgsArr);
                        return;
                    }

                }catch (IOException e){
                    cb.cb(msgs.toArray());
                    return;
                }
            }
            cb.cb(msgs.toArray());
        });
    }

    public void sendMsg(Message msg,ICallback cb){
        CompletableFuture.runAsync(()->{
            MessagePojo msgPojo=new MessagePojo(msg);
            Call<String> call=msgService.sendMsg(msgPojo);
            try{
                Response<String> response=call.execute();
                cb.cb(response.body());
                return;
            }catch (IOException e){
                cb.cb("catched err");
                return;
            }
        });
    }

    public void addMessage(Message msg){
        CompletableFuture.runAsync(()-> {msgDao.insertMessages(msg); });

    }

}
