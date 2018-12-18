package com.example.us.childrenenglishgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    TextView tvScore,txtTimer;
    ImageView iv1, iv2, iv3,
            iv11, iv12, iv13;
    Integer[] cardsArray = {1,2,3,11,12,13};
    int img1, img2, img3, img11, img12, img13;
    int firstCard, secondCard;
    int clickFirst, clickSecond;
    int cardNum = 1;
    int score = 0;
    RelativeLayout layout;
    private ProgressBar progressBar;
    CountDownTimer mCountDownTimer;
    int timer=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout= findViewById(R.id.MainLayout);
        layout.setBackgroundResource(R.drawable.background);

        //timer
        txtTimer= findViewById(R.id.txtTimer);
        progressBar = (ProgressBar) findViewById(R.id.pbTimer);
        drawTimer();

        tvScore = findViewById(R.id.tv_Score);
        iv1 = findViewById(R.id.iv_1);
        iv2 = findViewById(R.id.iv_2);
        iv3 = findViewById(R.id.iv_3);

        iv11 = findViewById(R.id.iv_11);
        iv12 = findViewById(R.id.iv_12);
        iv13 = findViewById(R.id.iv_13);

        iv1.setTag("0");
        iv2.setTag("1");
        iv3.setTag("2");
        iv11.setTag("3");
        iv12.setTag("4");
        iv13.setTag("5");

        frontofCardRes();
        Collections.shuffle(Arrays.asList(cardsArray));

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv1, theCard);
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv2, theCard);
            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv3, theCard);
            }
        });

        iv11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv11, theCard);
            }
        });

        iv12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv12, theCard);
            }
        });

        iv13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv13, theCard);
            }
        });

    }
    private void drawTimer(){
        progressBar.setProgress(timer);
        mCountDownTimer=new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer++;
                txtTimer.setText(timer+"/60");
                progressBar.setProgress((int)timer*100/(60000/1000));

            }
            @Override
            public void onFinish() {
                timer++;
                txtTimer.setText(timer+"/60");
                progressBar.setProgress(100);
                popupEndGame();
            }
        };
        mCountDownTimer.start();

    }
    private void checkScore(){
        if(0<timer&& timer<=10){
            score=score+5;
        }
        if(10<timer && timer <=20){
            score =score+3;
        }
        if(20<timer && timer <=40){
            score =score+2;
        }
        if(timer>40){
            score =score+1;
        }
        tvScore.setText("Score: "+score);
        timer=0;
        drawTimer();
    }
    private void doStuff(ImageView iv, int card){
        int arr = cardsArray[card];
        if (arr == 1)
            iv.setImageResource(img1);
        else if (arr == 2)
            iv.setImageResource(img2);
        else if (arr == 3)
            iv.setImageResource(img3);
        else if (arr == 11)
            iv.setImageResource(img11);
        else if (arr == 12)
            iv.setImageResource(img12);
        else if (arr == 13)
            iv.setImageResource(img13);

        if (cardNum == 1) {
            firstCard = cardsArray[card];
            if (firstCard > 10){
                firstCard -= 10;
            }
            cardNum = 2;
            clickFirst = card;
            iv.setEnabled(false);
        }
        else if (cardNum == 2){
            secondCard = cardsArray[card];
            if (secondCard > 10)
                secondCard -= 10;
            cardNum = 1;
            clickSecond = card;
            iv1.setEnabled(false);
            iv2.setEnabled(false);
            iv3.setEnabled(false);
            iv11.setEnabled(false);
            iv12.setEnabled(false);
            iv13.setEnabled(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculate();
                }
            }, 1000);
        }
    }

    private void calculate(){
        if (firstCard == secondCard){
            if (clickFirst == 0){
                iv1.setVisibility(View.INVISIBLE);
            }else  if (clickFirst == 1){
                iv2.setVisibility(View.INVISIBLE);
            }
            else  if (clickFirst == 2){
                iv3.setVisibility(View.INVISIBLE);
            }
            else  if (clickFirst == 3){
                iv11.setVisibility(View.INVISIBLE);
            }
            else  if (clickFirst == 4){
                iv12.setVisibility(View.INVISIBLE);
            }else  if (clickFirst == 5){
                iv13.setVisibility(View.INVISIBLE);
            }

            if (clickSecond == 0){
                iv1.setVisibility(View.INVISIBLE);
            }else  if (clickSecond == 1){
                iv2.setVisibility(View.INVISIBLE);
            }
            else  if (clickSecond == 2){
                iv3.setVisibility(View.INVISIBLE);
            }
            else  if (clickSecond == 3){
                iv11.setVisibility(View.INVISIBLE);
            }
            else  if (clickSecond == 4){
                iv12.setVisibility(View.INVISIBLE);
            }else  if (clickSecond == 5){
                iv13.setVisibility(View.INVISIBLE);
            }
            checkScore();
        }else{
            iv1.setImageResource(R.drawable.icon);
            iv2.setImageResource(R.drawable.icon);
            iv3.setImageResource(R.drawable.icon);
            iv11.setImageResource(R.drawable.icon);
            iv12.setImageResource(R.drawable.icon);
            iv13.setImageResource(R.drawable.icon);
        }
        iv1.setEnabled(true);
        iv2.setEnabled(true);
        iv3.setEnabled(true);
        iv11.setEnabled(true);
        iv12.setEnabled(true);
        iv13.setEnabled(true);

        checkEnd();
    }
    private boolean checkEnd(){
        boolean isEnd=false;
        if(iv1.getVisibility()==View.INVISIBLE && iv2.getVisibility()==View.INVISIBLE
                && iv3.getVisibility()==View.INVISIBLE
                && iv11.getVisibility()==View.INVISIBLE
                && iv12.getVisibility()==View.INVISIBLE
                && iv13.getVisibility()==View.INVISIBLE){
            isEnd=true;
            popupEndGame();
        }
        return isEnd;
    }
    private void popupEndGame(){
        progressBar.setVisibility(View.GONE);
        AlertDialog.Builder alertDiaglogBuilder= new AlertDialog.Builder(MainActivity.this);
        alertDiaglogBuilder.setMessage("GAME OVER! \n Your score is "+score).setCancelable(false)
                .setPositiveButton("NEW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = alertDiaglogBuilder.create();
        alertDialog.show();
    }
    private void frontofCardRes(){
        img1 = R.drawable.card1;
        img2 = R.drawable.card2;
        img3 = R.drawable.card3;
        img11 = R.drawable.card11;
        img12 = R.drawable.card12;
        img13 = R.drawable.card13;
    }
}
