package com.example.chatclient.retrofitclient.retrofitapis;

import com.example.chatclient.retrofitclient.retrofitpojos.ConversationPojo;
import com.example.chatclient.retrofitclient.retrofitpojos.ParticipantPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ConversationService {
    @GET("conversations")
    public Call<List<ConversationPojo>> getConversations();
    @GET("conversations/id/{id}")
    public Call<ConversationPojo> getConversationById(@Path("id") int id);

    @POST("conversations/")
    public Call<ConversationPojo> addConversation(@Body ConversationPojo newConv);
    @PUT("conversations/{id}")
    public Call<ConversationPojo> modConverastion(@Path("id") int convId,@Body ConversationPojo modConv);

    @POST("conversations/participant")
    public Call<ParticipantPojo> addParticipant(@Body ParticipantPojo newParticipant);
    @DELETE("conversation/participant/{convId}/{userId}")
    public Call<String> delParticipant(@Path("convId") int convId, @Path("userId") int userId);
}
