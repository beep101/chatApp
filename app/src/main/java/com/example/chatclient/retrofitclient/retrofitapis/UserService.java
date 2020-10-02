package com.example.chatclient.retrofitclient.retrofitapis;

import com.example.chatclient.retrofitclient.retrofitpojos.UserPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {
    @GET("users/all")
    public Call<List<UserPojo>> getAllUsers();
    @GET("users/id/{id}")
    public Call<UserPojo> getUserById(@Part("id") int id);
    @GET("users/find/{term}")
    public Call<List<UserPojo>> findUserByName(@Path("term") String term);
    @PUT("users/{id}")
    public Call<String> modifyData(@Path("id") int userId,@Body UserPojo user);

}
