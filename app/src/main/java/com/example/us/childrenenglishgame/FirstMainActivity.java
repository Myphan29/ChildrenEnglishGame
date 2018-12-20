package com.example.us.childrenenglishgame;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class FirstMainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button btn_Play,btn_SignOut,btn_Exit;
    SignInButton btn_SignIn;
    TextView txt_Name;

    private GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 1;

    public static String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_main);


        txt_Name = (TextView)findViewById(R.id.txtTen);

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

        btn_Play = (Button)findViewById(R.id.btn_Play);
        btn_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainActivy);
            }
        });
        btn_Exit = (Button)findViewById(R.id.btn_Exit);
        btn_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_SignIn = findViewById(R.id.btn_SignIn);
        btn_SignIn.setSize(SignInButton.SIZE_STANDARD);
        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_SignIn:
                        signIn();
                        break;
                }
            }
        });

        btn_SignOut = (Button)findViewById(R.id.btn_SignOut) ;
        btn_SignOut.setVisibility(View.INVISIBLE);
        btn_SignOut.setOnClickListener(new View.OnClickListener() {
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
            txt_Name.setText(account.getDisplayName());
            name = account.getDisplayName();
            btn_SignOut.setVisibility(View.VISIBLE);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        btn_SignIn.setVisibility(View.INVISIBLE);
        txt_Name.setText("");
    }
    public void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                txt_Name.setText("Thoát thành công");
                AlertDialog.Builder alertDiaglogBuilder= new AlertDialog.Builder(FirstMainActivity.this);
                alertDiaglogBuilder.setMessage("Thoát thành công");
                AlertDialog alertDialog = alertDiaglogBuilder.create();
                alertDialog.show();
            }
        });
        btn_SignOut.setVisibility(View.INVISIBLE);
        btn_SignIn.setVisibility(View.VISIBLE);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Failed",connectionResult +"");
    }
    public static String getNamePlayer(){
        return name;
    }
}
