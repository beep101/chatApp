package com.example.chatclient.retrofitclient.retrofitapis;

import com.example.chatclient.retrofitclient.retrofitpojos.AuthPojo;
import com.example.chatclient.retrofitclient.retrofitpojos.SignupPojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("login")
    Call<String> login(@Body AuthPojo userData);
    @POST("signup")
    Call<String> signup(@Body SignupPojo userData);

}
