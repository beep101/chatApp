package com.example.chatclient.dataaccessmanagers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.chatclient.ChatApplication;
import com.example.chatclient.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

public class AuthUserManager {
    SharedPreferences prefs;
    Context context;

    public AuthUserManager(Context context){
        this.context=context;
        prefs=context.getSharedPreferences(context.getString(R.string.preference_file),Context.MODE_PRIVATE);
        if(!getToken().equals(""))
            ((ChatApplication)context).generateManagers(getToken());
    }

    public boolean setUser(String token){
        SharedPreferences.Editor editor=prefs.edit();

        Map<String,String> userData=decodeUserData(token);

        if(userData==null)
            return false;

        editor.putString("token",token);
        editor.putInt("id", Integer.parseInt(userData.get("id")));
        editor.putString("email",userData.get("email"));
        editor.putString("name",userData.get("name"));
        editor.apply();

        ((ChatApplication)context).generateManagers(getToken());

        return true;
    }

    public void removeUser(){
        SharedPreferences.Editor editor=prefs.edit();

        editor.putString("token","");
        editor.putInt("id", -1);
        editor.putString("email","");
        editor.putString("name","");
        editor.apply();

        ((ChatApplication)context).removeManagers();
    }

    public String getName(){
        return prefs.getString("name","");
    }

    public String getEmail(){
        return prefs.getString("email","");
    }

    public int getId(){
        return prefs.getInt("id", -1);
    }

    public String getToken(){
        return prefs.getString("token","");
    }

    private Map<String,String> decodeUserData(String token){
        String tokenString="";

        byte[] bytes= Base64.decode((token.split("\\."))[1],Base64.URL_SAFE);
        try{
            tokenString=new String(bytes,"UTF-8");
        }catch(UnsupportedEncodingException e){
            return null;
        }

        Type mapStringString=new TypeToken<Map<String,String>>(){}.getType();
        Map<String,String> tokenData=(new Gson()).fromJson(tokenString,mapStringString);
        return tokenData;
    }
}
