package com.example.chatclient.retrofitclient.retrofitapis;

import com.example.chatclient.retrofitclient.retrofitpojos.MessagePojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {
    @GET("messages/{convId}")
    public Call<List<MessagePojo>> getNewMsgs(@Path("convId") int convId);
    @GET("messages/{convId}/{count}")
    public Call<List<MessagePojo>> getMsgsCount(@Path("convId") int convId,@Path("count") int count);
    @GET("messages/{convId}/{from}/{count}")
    public Call<List<MessagePojo>> getMsgsCountFrom(@Path("convId") int convId,@Path("from") long from,@Path("count") int count);

    @POST("messages/")
    public Call<String> sendMsg(@Body MessagePojo msg);
}
