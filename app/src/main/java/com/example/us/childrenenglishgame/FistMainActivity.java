package com.example.us.childrenenglishgame;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.account.WorkAccount;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

public class FistMainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button btn_play;
    private GoogleSignInApi mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 1;
    TextView Ten;
    Button singout;
    SignInButton signInButton;
    public static String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fist_main);
        btn_play = (Button)findViewById(R.id.btn_play);
        Ten = (TextView)findViewById(R.id.txtTen);
        singout = (Button)findViewById(R.id.signout) ;
        singout.setVisibility(View.INVISIBLE);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        final Intent mainActivy = new Intent(this, MainActivity.class);
        btn_play.setOnClickListener(new View.OnClickListener() {    
            @Override
            public void onClick(View v) {
                startActivity(mainActivy);
            }
        });
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });

        singout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Ten.setText(account.getDisplayName());
            name = account.getDisplayName();
            singout.setVisibility(View.VISIBLE);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.


        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        signInButton.setVisibility(View.INVISIBLE);
        Ten.setText("");
        Log.d("thanh cong",mGoogleApiClient.isConnected()+" yeah");
    }
    public void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Ten.setText("Thoát thành công");
                AlertDialog.Builder alertDiaglogBuilder= new AlertDialog.Builder(FistMainActivity.this);
                alertDiaglogBuilder.setMessage("Thoát thành công");
                AlertDialog alertDialog = alertDiaglogBuilder.create();
                alertDialog.show();
            }
        });
        singout.setVisibility(View.INVISIBLE);
        signInButton.setVisibility(View.VISIBLE);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Failed",connectionResult +"");
    }
    public static String getNamePlayer(){
        return name;
    }
}
