package com.example.chatclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatclient.R;
import com.example.chatclient.retrofitclient.ServiceGenerator;
import com.example.chatclient.retrofitclient.retrofitapis.AuthService;
import com.example.chatclient.retrofitclient.retrofitpojos.SignupPojo;

public class Signup extends AppCompatActivity {

    EditText nameTxt;
    EditText emailTxt;
    EditText passwdTxt;
    EditText passChkTxt;
    Button signupBtn;
    AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authService= ServiceGenerator.createService(AuthService.class);

        nameTxt=findViewById(R.id.nameTxtField);
        emailTxt=findViewById(R.id.emailTxtField);
        passwdTxt=findViewById(R.id.passwdTxtField);
        passChkTxt=findViewById(R.id.passwdChkTxtField);
        signupBtn=findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupPojo newUser=collectNewUserData();
                if(newUser==null){
                    Toast.makeText(getApplicationContext(),"Some data is missing",Toast.LENGTH_LONG).show();
                    return;
                }
                Call<String> call=authService.signup(newUser);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Sign Up successful, use your email and password to log in",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Check your data, email might be already in use",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private SignupPojo collectNewUserData(){
        SignupPojo newUser=new SignupPojo();
        if(emailTxt.getText().toString().equals(""))
            return null;
        else
            newUser.setEmail(emailTxt.getText().toString());
        if(nameTxt.getText().toString().equals(""))
            return null;
        else
            newUser.setName(nameTxt.getText().toString());
        if(passwdTxt.getText().toString().equals(""))
            return null;
        else
            if(!passwdTxt.getText().toString().equals(passChkTxt.getText().toString()))
                return null;
            else
                newUser.setPasswd(passwdTxt.getText().toString());

        return newUser;
    }
}