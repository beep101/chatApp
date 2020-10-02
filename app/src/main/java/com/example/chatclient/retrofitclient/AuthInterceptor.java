package com.example.chatclient.retrofitclient;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private String authToken;

    public AuthInterceptor(String authToken){
        this.authToken=authToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request beforeAuth=chain.request();
        Request.Builder builder=beforeAuth.newBuilder().header("Authorization","Bearer "+authToken);
        Request afterAuth=builder.build();
        return chain.proceed(afterAuth);
    }
}
