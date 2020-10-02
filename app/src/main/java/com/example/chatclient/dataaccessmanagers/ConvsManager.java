package com.example.chatclient.dataaccessmanagers;

import android.content.Context;
import android.util.Log;

import com.example.chatclient.ChatApplication;
import com.example.chatclient.retrofitclient.ServiceGenerator;
import com.example.chatclient.retrofitclient.retrofitapis.ConversationService;
import com.example.chatclient.retrofitclient.retrofitpojos.ConversationPojo;
import com.example.chatclient.retrofitclient.retrofitpojos.ParticipantPojo;
import com.example.chatclient.roomdatabase.dataaccessobjects.ConversationDao;
import com.example.chatclient.roomdatabase.entites.Conversation;
import com.example.chatclient.roomdatabase.entites.ConversationWithUsers;
import com.example.chatclient.roomdatabase.entites.UserConvRef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Response;

public class ConvsManager {
    ConversationService convService;
    ConversationDao convDao;

    public ConvsManager(Context applicationContext, String token){
        convDao=((ChatApplication)applicationContext).getLocaDb().convDao();
        convService= ServiceGenerator.createService(ConversationService.class,token);
    }

    public void getAllConversations(ICallback cb){
        CompletableFuture.runAsync(()->{
            List<ConversationWithUsers> convs=convDao.getAllConversations();
            cb.cb(convs.toArray());
        });
    }

    public void loadAllConversations(){
        CompletableFuture.runAsync(()->{
            Call<List<ConversationPojo>> call=convService.getConversations();
            try{
                Response<List<ConversationPojo>> response=call.execute();
                if(response.isSuccessful()){
                    List<ConversationPojo> convList=response.body();
                    Conversation[] convArr=new Conversation[convList.size()];
                    List<UserConvRef> userConvs=new ArrayList<>();
                    int n=0;
                    for(ConversationPojo convPojo:convList){
                        Conversation conv=new Conversation(convPojo);
                        convArr[n]=conv;
                        n++;
                        for(ParticipantPojo participantPojo:convPojo.participants)
                            userConvs.add(new UserConvRef(participantPojo));
                    }
                    convDao.insertConversations(convArr);
                    convDao.insertUserConv(userConvs.toArray(new UserConvRef[userConvs.size()]));
                }
            }catch(IOException e){

            }
        });
    }

    public void getConversationById(int id,ICallback cb){
        CompletableFuture.runAsync(()->{
            ConversationWithUsers conv=convDao.getConversationById(id);
            if(conv==null){
                Call<ConversationPojo> call=convService.getConversationById(id);
                try{
                    Response<ConversationPojo> response=call.execute();
                    if(response.isSuccessful()){
                        Conversation convResp=new Conversation(response.body());
                        convDao.insertConversations(convResp);
                        UserConvRef[] userConv=new UserConvRef[response.body().participants.size()];
                        int n=0;
                        for(ParticipantPojo participant:response.body().participants){
                            userConv[n]=new UserConvRef(participant);
                            n++;
                        }
                        convDao.insertUserConv(userConv);
                        conv=convDao.getConversationById(id);
                        cb.cb(conv);
                    }
                }catch (IOException e){
                    cb.cb(null);
                }
            }else{
                cb.cb(conv);
            }
        });
    }

    public void addConversation(ConversationWithUsers convObj,ICallback cb){
        ConversationPojo conv=new ConversationPojo(convObj);
        Log.println(Log.DEBUG,"conv_add_start",conv.name);
        CompletableFuture.runAsync(()->{
            Call<ConversationPojo> call=convService.addConversation(conv);
            try{
                Response<ConversationPojo> response=call.execute();
                if(response.isSuccessful()){
                    Log.println(Log.DEBUG,"conv_add_suc",conv.name);
                    Conversation convIns=new Conversation(response.body());
                    UserConvRef[] userConv=new UserConvRef[response.body().participants.size()];
                    int n=0;
                    for(ParticipantPojo participant:response.body().participants){
                        userConv[n]=new UserConvRef(participant);
                        n++;
                    }
                    convDao.insertConversations(convIns);
                    convDao.insertUserConv(userConv);
                    Log.println(Log.DEBUG,"conv_add_dbins",conv.name);
                    ConversationWithUsers convWithUsers=convDao.getConversationById(convIns.convId);
                    cb.cb(convWithUsers);
                    Log.println(Log.DEBUG,"conv_add_cb_suc",conv.name);
                }else{
                    Log.println(Log.DEBUG,"conv_add_non",String.valueOf(response.code()));
                    cb.cb(null);
                    Log.println(Log.DEBUG,"conv_add_cb_non",conv.name);
                }
            }catch(IOException e){
                Log.println(Log.DEBUG,"conv_add_err",conv.name);
                cb.cb(null);
                Log.println(Log.DEBUG,"conv_add_cb_err",conv.name);
            }
        });
    }

    public void modConversation(ConversationPojo conv){
        CompletableFuture.runAsync(()->{

        });
    }

    public void addParticipant(UserConvRef userConvRef,ICallback cb){
        ParticipantPojo participant=new ParticipantPojo(userConvRef);
        CompletableFuture.runAsync(()->{
            Call<ParticipantPojo> call=convService.addParticipant(participant);
            try{
                Response<ParticipantPojo> response=call.execute();
                if(response.isSuccessful()){
                    UserConvRef userConv=new UserConvRef(response.body());
                    convDao.insertUserConv(userConv);
                    cb.cb(userConv);
                }
            }catch (IOException e){
                cb.cb(null);
            }
        });
    }

    public void removeParticipant(UserConvRef participant,ICallback cb){
        CompletableFuture.runAsync(()->{
            Call<String> call=convService.delParticipant(participant.convId,participant.userId);
            try{
                Response<String> response=call.execute();
                if(response.isSuccessful()){
                    cb.cb(true);
                }
            }catch (IOException e){
                cb.cb(null);
            }
        });
    }
}
