package com.example.chatclient.retrofitclient;

import android.text.TextUtils;

import com.example.chatclient.retrofitclient.AuthInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final String BASE_URL="http://ec2-52-57-135-108.eu-central-1.compute.amazonaws.com:8000/";

    private static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setLenient()
            .create();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit=builder.build();

    private static OkHttpClient.Builder httpClient=new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass){
        return createService(serviceClass,null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken){
        if(!TextUtils.isEmpty(authToken)){
            AuthInterceptor interceptor=new AuthInterceptor(authToken);
            if(!httpClient.interceptors().contains(interceptor)){
                httpClient.addInterceptor(interceptor);
                builder.client(httpClient.build());
                retrofit=builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }
}
