package com.example.chatclient.retrofitclient.retrofitpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthPojo {
    @Expose
    @SerializedName("email")
    public String email;
    @Expose
    @SerializedName("passwd")
    public String passwd;

    public AuthPojo(){
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
