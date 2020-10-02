package com.example.chatclient.retrofitclient.retrofitpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupPojo {
    @Expose
    @SerializedName("email")
    public String email;
    @Expose
    @SerializedName("passwd")
    public String passwd;
    @Expose
    @SerializedName("name")
    public String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
