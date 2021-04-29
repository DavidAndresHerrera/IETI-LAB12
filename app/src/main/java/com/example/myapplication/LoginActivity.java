package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TOKEN = "TOKEN";
    private EditText email, password;
    private final ExecutorService executorService = Executors.newFixedThreadPool( 1 );
    private AuthService authService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText)findViewById(R.id.editTextTextEmailAddress);
        password = (EditText)findViewById(R.id.editTextTextPassword);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http:/10.0.2.2:8080") //localhost for emulator
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authService = retrofit.create(AuthService.class);
    }

    public void loginValidator(View view){
        if((email.getText().toString().trim().equalsIgnoreCase("")) || (password.getText().toString().trim().equalsIgnoreCase(""))){
            email.setError("verifica que el campo no este vacio");
            password.setError("verifica que el campo no este vacio");
        }
        else{
            LoginWrapper loginWrapper = new LoginWrapper();
            loginWrapper.setEmail("test@mail.com");
            loginWrapper.setPassword("password");
            Intent intent = new Intent(this, MainActivity.class);
            System.out.println("entre y cree el login wrapper");
            executorService.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println(loginWrapper.getEmail()+" "+loginWrapper.getPassword());
                        Response<Token> response = authService.login(loginWrapper).execute();
                        Token token = response.body();
                        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(TOKEN, token.getAccessToken());
                        editor.commit();
                        startActivity(intent);
                        finish();
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
            } );
        }

    }
}