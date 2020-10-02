package com.example.chatclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatclient.dataaccessmanagers.AuthUserManager;
import com.example.chatclient.ChatApplication;
import com.example.chatclient.R;
import com.example.chatclient.retrofitclient.ServiceGenerator;
import com.example.chatclient.retrofitclient.retrofitapis.AuthService;
import com.example.chatclient.retrofitclient.retrofitpojos.AuthPojo;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    AuthUserManager authManager;
    AuthService authService;
    EditText emailTxt;
    EditText passwdTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        authManager=((ChatApplication)getApplicationContext()).getAuthManager();
        authService= ServiceGenerator.createService(AuthService.class);

        Button loginBtn=findViewById(R.id.loginBtn);
        Button signupBtn=findViewById(R.id.signupBtn);
        emailTxt=findViewById(R.id.emailTxtField);
        passwdTxt=findViewById(R.id.passwdTxtField);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailTxt.getText().toString();
                String password=passwdTxt.getText().toString();
                if(email.equals("") || password.equals(""))
                    Toast.makeText(getApplicationContext(),"Enter email and password",Toast.LENGTH_LONG).show();
                else {
                    loginUser(email, password);
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignupActivity();
            }
        });
    }

    private void startSignupActivity(){
        Intent startSignupIntent=new Intent(this, Signup.class);
        startActivity(startSignupIntent);
    }

    private void loginUser(String email, String password) {
        AuthPojo auth = new AuthPojo();
        auth.setEmail(email);
        auth.setPasswd(password);

        Call<String> call=authService.login(auth);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String token=response.body();
                if(response.isSuccessful())
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
                    if(authManager.setUser(token)) {
                        ((ChatApplication) getApplicationContext()).fetchData();
                        finish();
                    }
                else{
                    Toast.makeText(getApplicationContext(),"Email or password is incorrect",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }
}