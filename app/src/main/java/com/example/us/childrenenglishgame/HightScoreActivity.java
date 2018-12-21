package com.example.us.childrenenglishgame;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class HightScoreActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    ArrayList<String>list_userNew =new ArrayList<String>();
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

        recentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayAdapter<Integer> ada_count;
//
//                ArrayAdapter<Integer> ada_score;
//                ArrayList<Integer>list_count= new ArrayList<Integer>();
//
//                ArrayList<Integer>list_score =new ArrayList<Integer>();

                int count=1;
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    User meo = new User();
                    int Score = user.getValue(User.class).getScore();
                    String Username = user.getValue(User.class).getUsername();

                    meo.setScore(Score);
                    meo.setUsername(Username);
                    count++;
                    listUser.add(meo);
                }
                Collections.sort(listUser);
                for(int i=0;i<5;i++) {
                    try {
                        list_userNew.add(String.valueOf(i++));
                        list_userNew.add(listUser.get(i).getUsername());
                        list_userNew.add(String.valueOf(listUser.get(i).getScore()));
                        Log.d("meomeo", "sds" + listUser.get(i).getUsername());
                    }catch (Exception e) {

                    }

                }
                ada_Usernew = new ArrayAdapter<String>(HightScoreActivity.this, android.R.layout.simple_list_item_1, list_userNew);
                grd_tb.setAdapter(ada_Usernew);
//                ada_count = new ArrayAdapter<Integer>(HightScoreActivity.this, android.R.layout.simple_list_item_1, list_count);
//                ada_score = new ArrayAdapter<Integer>(HightScoreActivity.this, android.R.layout.simple_list_item_2, list_score);

//                ArrayAdapter<User> ada_user = new  ArrayAdapter<User>(HightScoreActivity.this, android.R.layout.simple_list_item_1, listUser);

//                grd_tb.setAdapter(ada_username);
                // grd_tb.setAdapter(ada_user);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("loadPost:onCancelled", "sds");
                // ...
            }
        });

//        for(User user: listUser){
//            Log.d("loadPost:onCancelled", "sds" + user.getScore());
//        }


    }
}
