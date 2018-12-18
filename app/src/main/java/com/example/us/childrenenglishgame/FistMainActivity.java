package com.example.us.childrenenglishgame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class FistMainActivity extends AppCompatActivity {

    Button btn_play, btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fist_main);
        btn_play = (Button)findViewById(R.id.btn_play);
        btn_login = (Button)findViewById(R.id.btn_login);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        final Intent mainActivy = new Intent(this, MainActivity.class);
        btn_play.setOnClickListener(new View.OnClickListener() {    
            @Override
            public void onClick(View v) {
                startActivity(mainActivy);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
