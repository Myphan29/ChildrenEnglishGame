package com.example.us.childrenenglishgame;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HighScoreActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Button btn_back;
    GridView grd_tb;
    ArrayAdapter<String> ada_Usernew;
    ArrayList<User> listUser = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hight_score);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query recentUser = mDatabase.child("users");
        grd_tb = (GridView) findViewById(R.id.grd_tb);
        btn_back = (Button) findViewById(R.id.btn_backTutorial);
        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        btn_back.setTypeface(font);
        recentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list_userNew = new ArrayList<String>();
                int count = 1;
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    User meo = new User();
                    int Score = user.getValue(User.class).getScore();
                    String Username = user.getValue(User.class).getUsername();

                    meo.setScore(Score);
                    meo.setUsername(Username);
                    count++;
                    listUser.add(meo);
                }
                ArrayList<User> listSortNew = new ArrayList<User>();
                listUser = sortSontheB(listUser);
                if (listUser.size() > 5) {
                    int CountS = 1;
                    for (int i = 0; i < 5; i++) {
                        list_userNew.add(String.valueOf(CountS));
                        list_userNew.add(listUser.get(i).getUsername());
                        list_userNew.add(String.valueOf(listUser.get(i).getScore()));
                        CountS++;
                    }
                } else {
                    int Count = 1;
                    for (User user : listUser) {
                        list_userNew.add(String.valueOf(Count));
                        list_userNew.add(user.getUsername());
                        list_userNew.add(String.valueOf(user.getScore()));
                        Count++;
                    }
                }
                ada_Usernew = new ArrayAdapter<String>(HighScoreActivity.this, android.R.layout.simple_list_item_1, list_userNew);
                grd_tb.setAdapter(ada_Usernew);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("loadPost:onCancelled", "sds");
                // ...
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public ArrayList<User> sortSontheB(ArrayList<User> arrUser) {
        for (int i = 0; i < arrUser.size(); i++) {
            for (int j = 0; j < arrUser.size() - 1; j++) {
                if (arrUser.get(i).getScore() > arrUser.get(j).getScore()) {
                    int c = arrUser.get(i).getScore();
                    User meo = new User();
                    meo.setUsername(arrUser.get(i).getUsername());
                    meo.setScore(arrUser.get(i).getScore());
                    arrUser.remove(i);
                    arrUser.add(i, arrUser.get(j));

                    arrUser.remove(j);
                    arrUser.add(j, meo);
                }
            }
        }
        return arrUser;
    }
}
