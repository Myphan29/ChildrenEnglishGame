package com.example.us.childrenenglishgame;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    TextView txt_Score,txt_Timer, txt_user,txt_Level;
    ImageView[] iv;
    Button zPause;
    Integer[] cardsArray;
    Integer[] cardsArrayTemp;
    int[] idCards;
    int firstCard, secondCard;
    int clickFirst, clickSecond;
    int selectedCard = 1;
    int score = 0;
    int x = 0;
    int tempID, countID, remainCards;
    RelativeLayout layout;
    private ProgressBar progressBar;
    CountDownTimer mCountDownTimer;
    int timer=0;
    private DatabaseReference mDatabase;
    User guest;
    final Handler handler = new Handler();
    String tmpTime = "";
    boolean isPause = false;
    long timeRemaining=60000;
    LinearLayout GPA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zPause = (Button)findViewById(R.id.btnPause);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        layout= findViewById(R.id.MainLayout);
        layout.setBackgroundResource(R.drawable.background);
        Gson gson = new Gson();
        guest= gson.fromJson(getIntent().getStringExtra("guest"), User.class);
        txt_user = (TextView)findViewById(R.id.txt_user);
        txt_user.setText(guest.getUsername());
        //declare font awesome
        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        zPause.setTypeface(font);
        //
        GPA = (LinearLayout)findViewById(R.id.layoutHolder);
        //timer
        txt_Timer= findViewById(R.id.txtTimer);
        progressBar = (ProgressBar) findViewById(R.id.pbTimer);
        drawTimer();

        txt_Score = findViewById(R.id.tv_Score);
        txt_Level=findViewById(R.id.txtLevel);


        Shared.context = getApplicationContext();

        tempID = 111;
        playGame(1);

        //
        zPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayAndPause();
            }
        });
    }
    public void PlayAndPause() {
        if (isPause == false) {
            mCountDownTimer.cancel();
            isPause = true;
            zPause.setText(getString(R.string.ic_play));
            for (int i = 0; i < Shared.numbersOfCard; i++){
                iv[i].setClickable(false);
            }
            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setMessage("PAUSE GAME \nYour score is "+score).setCancelable(false)
                    .setPositiveButton("PLAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PlayAndPause();
                        }
                    }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else{
            isPause = false;
            drawTimer();
            zPause.setText(getString(R.string.ic_pause));
            for (int i = 0; i < Shared.numbersOfCard; i++){
                iv[i].setClickable(true);
            }
        }
    }
    private void drawTimer(){
        progressBar.setProgress(timer);
        mCountDownTimer = new CountDownTimer(timeRemaining,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("millisUntilFinished ", "millisUntilFinished: "+millisUntilFinished);
                Log.d("timer ", "timer: "+timer);
                timer++;
                txt_Timer.setText(timer+"/60");
                progressBar.setProgress((int)timer*100/(60000/1000));
                tmpTime = txt_Timer.getText().toString();
                timeRemaining = millisUntilFinished-1000;
            }
            @Override
            public void onFinish() {
                timer++;
                txt_Timer.setText(timer+"/60");
                progressBar.setProgress(100);
                remainCards=0;
                Shared.level=6;
                checkEnd();
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
        remainCards -= 2;
        txt_Score.setText("Score: "+score);
    }

    private void playGame(int level){
        txt_Level.setText("Level "+level);
        Shared.level = level;
        addNumbersOfCard();
        idCards = new int[Shared.numbersOfCard];
        cardsArray = new Integer[Shared.numbersOfCard];
        iv = new ImageView[Shared.numbersOfCard];
        for (int i = 0; i < Shared.numbersOfCard; i++){
            iv[i] = new ImageView(Shared.context);
        }
        countID = Shared.numbersOfCard;
        addScreen(Shared.level);
        setOperation();
    }

    private void setOperation(){
        tempID = 111;
        countID = Shared.numbersOfCard;
        switch (Shared.level) {
            case 1:
            case 2:
                for (int i = 0; i < 2; i++) {
                    switch (Shared.level) {
                        case 1:
                            for (int j = 0; j < 3; j++) {
                                String id = "iv_" + tempID;
                                int resID = getResources().getIdentifier(id, "id", getPackageName());
                                String sDrawableID = "card" + tempID;
                                int resDrawableID = getResources().getIdentifier(sDrawableID, "drawable", getPackageName());
                                if (i == 0) {
                                    iv[i + j] = findViewById(resID);
                                    iv[i + j].setTag(Integer.toString(i + j));
                                    cardsArray[i + j] = tempID;
                                    idCards[i+j] = resDrawableID;
                                } else {
                                    iv[i + j + 2] = findViewById(resID);
                                    iv[i + j + 2].setTag(Integer.toString(i + j + 2));
                                    cardsArray[i+j+2]= tempID;
                                    idCards[i+j+2] = resDrawableID;
                                }
                                tempID++;
                                countID--;
                                if (countID == Shared.numbersOfCard/2){
                                    tempID = 211;
                                }
                            }
                            break;
                        case 2:
                            for (int j = 0; j < 4; j++) {
                                String id = "iv_" + tempID;
                                int resID = getResources().getIdentifier(id, "id", getPackageName());
                                String sDrawableID = "card" + tempID;
                                int resDrawableID = getResources().getIdentifier(sDrawableID, "drawable", getPackageName());
                                if (i == 0) {
                                    iv[i + j] = findViewById(resID);
                                    iv[i + j].setTag(Integer.toString(i + j));
                                    cardsArray[i + j] = tempID;
                                    idCards[i+j] = resDrawableID;
                                } else if (i == 1){
                                    iv[i + j + 3] = findViewById(resID);
                                    iv[i + j + 3].setTag(Integer.toString(i + j + 3));
                                    cardsArray[i + j + 3] = tempID;
                                    idCards[i+j+3] = resDrawableID;
                                }
                                tempID++;
                                countID--;
                                if (countID == Shared.numbersOfCard/2){
                                    tempID = 211;
                                }
                            }
                            break;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        String id = "iv_" + tempID;
                        int resID = getResources().getIdentifier(id, "id", getPackageName());
                        String sDrawableID = "card" + tempID;
                        int resDrawableID = getResources().getIdentifier(sDrawableID, "drawable", getPackageName());
                        if (i == 0) {
                            iv[i + j] = findViewById(resID);
                            iv[i + j].setTag(Integer.toString(i + j));
                            cardsArray[i + j] = tempID;
                            idCards[i+j] = resDrawableID;
                        } else if (i == 1){
                            iv[i + j + 3] = findViewById(resID);
                            iv[i + j + 3].setTag(Integer.toString(i + j + 3));
                            cardsArray[i + j + 3] = tempID;
                            idCards[i+j+3] = resDrawableID;
                        } else {
                            iv[i + j + 6] = findViewById(resID);
                            iv[i + j + 6].setTag(Integer.toString(i + j + 6));
                            cardsArray[i + j +6] = tempID;
                            idCards[i+j+6] = resDrawableID;
                        }
                        tempID++;
                        countID--;
                        if (countID == Shared.numbersOfCard/2){
                            tempID = 211;
                        }
                    }
                }
                break;
            case 4:
            case 5:
            case 6:
                for (int i = 0; i < 4; i++) {
                    switch (Shared.level) {
                        case 4:
                            for (int j = 0; j < 4; j++) {
                                String id = "iv_" + tempID;
                                int resID = getResources().getIdentifier(id, "id", getPackageName());
                                String sDrawableID = "card" + tempID;
                                int resDrawableID = getResources().getIdentifier(sDrawableID, "drawable", getPackageName());
                                if (i == 0) {
                                    iv[i + j] = findViewById(resID);
                                    iv[i + j].setTag(Integer.toString(i + j));
                                    cardsArray[i + j] = tempID;
                                    idCards[i + j] = resDrawableID;
                                } else if (i == 1){
                                    iv[i + j + 3] = findViewById(resID);
                                    iv[i + j + 3].setTag(Integer.toString(i + j + 3));
                                    cardsArray[i + j + 3]= tempID;
                                    idCards[i + j + 3] = resDrawableID;
                                } else if (i == 2){
                                    iv[i + j + 6] = findViewById(resID);
                                    iv[i + j + 6].setTag(Integer.toString(i + j + 6));
                                    cardsArray[i + j + 6]= tempID;
                                    idCards[i + j + 6] = resDrawableID;
                                } else {
                                    iv[i + j + 9] = findViewById(resID);
                                    iv[i + j + 9].setTag(Integer.toString(i + j + 9));
                                    cardsArray[i + j + 9]= tempID;
                                    idCards[i + j + 9] = resDrawableID;
                                }
                                tempID++;
                                countID--;
                                if (countID == Shared.numbersOfCard/2){
                                    tempID = 211;
                                }
                            }
                            break;
                        case 5:
                            for (int j = 0; j < 5; j++) {
                                String id = "iv_" + tempID;
                                int resID = getResources().getIdentifier(id, "id", getPackageName());
                                String sDrawableID = "card" + tempID;
                                int resDrawableID = getResources().getIdentifier(sDrawableID, "drawable", getPackageName());
                                if (i == 0) {
                                    iv[i + j] = findViewById(resID);
                                    iv[i + j].setTag(Integer.toString(i + j));
                                    cardsArray[i + j] = tempID;
                                    idCards[i + j] = resDrawableID;
                                } else if (i == 1){
                                    iv[i + j + 4] = findViewById(resID);
                                    iv[i + j + 4].setTag(Integer.toString(i + j + 4));
                                    cardsArray[i + j + 4]= tempID;
                                    idCards[i + j + 4] = resDrawableID;
                                } else if (i == 2){
                                    iv[i + j + 8] = findViewById(resID);
                                    iv[i + j + 8].setTag(Integer.toString(i + j + 8));
                                    cardsArray[i + j + 8]= tempID;
                                    idCards[i + j + 8] = resDrawableID;
                                } else {
                                    iv[i + j + 12] = findViewById(resID);
                                    iv[i + j + 12].setTag(Integer.toString(i + j + 12));
                                    cardsArray[i + j + 12]= tempID;
                                    idCards[i + j + 12] = resDrawableID;
                                }
                                tempID++;
                                countID--;
                                if (countID == Shared.numbersOfCard/2){
                                    tempID = 211;
                                }
                            }
                            break;
                        case 6:
                            for (int j = 0; j < 6; j++) {
                                String id = "iv_" + tempID;
                                int resID = getResources().getIdentifier(id, "id", getPackageName());
                                String sDrawableID = "card" + tempID;
                                int resDrawableID = getResources().getIdentifier(sDrawableID, "drawable", getPackageName());
                                if (i == 0) {
                                    iv[i + j] = findViewById(resID);
                                    iv[i + j].setTag(Integer.toString(i + j));
                                    cardsArray[i + j] = tempID;
                                    idCards[i + j] = resDrawableID;
                                } else if (i == 1){
                                    iv[i + j + 5] = findViewById(resID);
                                    iv[i + j + 5].setTag(Integer.toString(i + j + 5));
                                    cardsArray[i + j + 5]= tempID;
                                    idCards[i + j + 5] = resDrawableID;
                                } else if (i == 2){
                                    iv[i + j + 10] = findViewById(resID);
                                    iv[i + j + 10].setTag(Integer.toString(i + j + 10));
                                    cardsArray[i + j + 10]= tempID;
                                    idCards[i + j + 10] = resDrawableID;
                                } else {
                                    iv[i + j + 15] = findViewById(resID);
                                    iv[i + j + 15].setTag(Integer.toString(i + j + 15));
                                    cardsArray[i + j + 15]= tempID;
                                    idCards[i + j + 15] = resDrawableID;
                                }
                                tempID++;
                                countID--;
                                if (countID == Shared.numbersOfCard/2){
                                    tempID = 211;
                                }
                            }
                            break;
                    }
                }
                break;
        }
        cardsArrayTemp = new Integer[Shared.numbersOfCard];
        cardsArrayTemp = cardsArray.clone();
        Collections.shuffle(Arrays.asList(cardsArray));
        for (x = 0; x < iv.length; x++){
            iv[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = Integer.parseInt((String) v.getTag());
                    flipCard(iv[tag], tag);
                }
            });
        }
    }

    private void addNumbersOfCard(){
        switch (Shared.level){
            case 1: Shared.numbersOfCard = 3*2;break;
            case 2: Shared.numbersOfCard = 4*2;break;
            case 3: Shared.numbersOfCard = 4*3;break;
            case 4: Shared.numbersOfCard = 4*4;break;
            case 5: Shared.numbersOfCard = 5*4;break;
            case 6: Shared.numbersOfCard = 6*4;break;
        }
        remainCards = Shared.numbersOfCard;
    }

    public void addScreen(int level){
        tempID = 111;
        Shared.layoutHolder = findViewById(R.id.layoutHolder);
        Shared.layoutHolder.removeAllViews();

        LinearLayout outer_ll = new LinearLayout(Shared.context);
        outer_ll.setOrientation(LinearLayout.VERTICAL);
        outer_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        switch (level) {
            case 1:
            case 2:
                outer_ll.setWeightSum(2);
                for (int i = 0; i < 2; i++){
                    LinearLayout inner_ll = new LinearLayout(Shared.context);
                    inner_ll.setOrientation(LinearLayout.HORIZONTAL);
                    inner_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    switch (level){
                        case 1:
                            inner_ll.setWeightSum(3);
                            for (int j = 0; j < 3; j++){
                                generateImgView(inner_ll);
                            }
                            break;
                        case 2:
                            inner_ll.setWeightSum(4);
                            for (int j = 0; j < 4; j++){
                                generateImgView(inner_ll);
                            }
                            break;
                    }
                    outer_ll.addView(inner_ll);
                }
                break;
            case 3:
                outer_ll.setWeightSum(3);
                for (int i = 0; i < 3; i++){
                    LinearLayout inner_ll = new LinearLayout(Shared.context);
                    inner_ll.setOrientation(LinearLayout.HORIZONTAL);
                    inner_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    inner_ll.setWeightSum(4);
                    for (int j = 0; j < 4; j++){
                        generateImgView(inner_ll);
                    }
                    outer_ll.addView(inner_ll);
                }
                break;
            case 4:
            case 5:
            case 6:
                outer_ll.setWeightSum(4);
                for (int i = 0; i < 4; i++){
                    LinearLayout inner_ll = new LinearLayout(Shared.context);
                    inner_ll.setOrientation(LinearLayout.HORIZONTAL);
                    inner_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    switch (level){
                        case 4:
                            inner_ll.setWeightSum(4);
                            for (int j = 0; j < 4; j++){
                                generateImgView(inner_ll);
                            }
                            break;
                        case 5:
                            inner_ll.setWeightSum(5);
                            for (int j = 0; j < 5; j++){
                                generateImgView(inner_ll);
                            }
                            break;
                        case 6:
                            inner_ll.setWeightSum(6);
                            for (int j = 0; j < 6; j++){
                                generateImgView(inner_ll);
                            }
                            break;
                    }
                    outer_ll.addView(inner_ll);
                }
                break;
        }
        Shared.layoutHolder.addView(outer_ll);
    }

    private void generateImgView(LinearLayout parent){
        ImageView imageView = new ImageView(Shared.context);
        String id = "iv_" + tempID;
        int resID = getResources().getIdentifier(id, "id", getPackageName());
        imageView.setId(resID);
        tempID++;
        countID--;
        if (countID == Shared.numbersOfCard/2){
            tempID = 211;
        }
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.icon);
        parent.addView(imageView);
    }

    private void flipCard(ImageView imv, int card){
        int arr = cardsArray[card];
        for(int i=0;i<cardsArrayTemp.length;i++){
            if(arr==cardsArrayTemp[i]){
                imv.setImageResource(idCards[i]);
                break;
            }
        }
        if (selectedCard == 1) {
            firstCard = arr;
            if (firstCard > 200){
                firstCard -= 100;
            }
            selectedCard = 2;
            clickFirst = card;
            imv.setEnabled(false);
        }
        else if (selectedCard == 2){
            secondCard = cardsArray[card];
            if (secondCard > 200)
                secondCard -= 100;
            selectedCard = 1;
            clickSecond = card;
            for (int i = 0; i < iv.length; i++){
                iv[i].setEnabled(false);
            }

            Handler handlerImage = new Handler();
            handlerImage.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculate(clickFirst,clickSecond);
                }
            }, 500);
        }
    }

    private void calculate(int clickFirst, int clickSecond ){
        if (firstCard == secondCard){
            iv[clickFirst].setVisibility(View.INVISIBLE);
            iv[clickSecond].setVisibility(View.INVISIBLE);
            checkScore();
        }else{
            for (int i = 0; i < iv.length; i++){
                iv[i].setImageResource(R.drawable.icon);
            }
        }
        for (int i = 0; i < iv.length; i++){
            iv[i].setEnabled(true);
        }
        checkEnd();
    }
    private void checkEnd(){
        if (remainCards == 0){
            mCountDownTimer.cancel();
            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(MainActivity.this);
            if (Shared.level != 6){
                alertDialogBuilder.setTitle("WIN!\nYour score is " + score);
                alertDialogBuilder.setItems(new CharSequence[]
                                {"NEXT", "RESET", "SAVE SCORE", "EXIT"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        timer=0;
                                        playGame(Shared.level + 1);
                                        drawTimer();
                                        break;
                                    case 1:
                                        score = 0;
                                        txt_Score.setText("Score: " + score);
                                        timer=0;
                                        playGame(1);
                                        drawTimer();
                                        break;
                                    case 2:
                                        SaveScore();
                                        break;
                                    case 3:
                                        finish();
                                        break;
                                }
                            }
                        });
            }
            else {
                alertDialogBuilder.setMessage("GAME OVER! \nYour score is "+score).setCancelable(false)
                        .setPositiveButton("AGAIN", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                score = 0;
                                txt_Score.setText("Score: " + score);
                                timer=0;
                                playGame(1);
                                drawTimer();
                            }
                        }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNeutralButton("SAVE SCORE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SaveScore();
                    }
                });
            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
    private void SaveScore(){
        guest.setScore(score);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        mDatabase.child("users").child(ts).setValue(guest);
    }
}
