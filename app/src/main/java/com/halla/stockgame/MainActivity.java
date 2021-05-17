package com.halla.stockgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button StartBtn;
    Button HelpBtn;
    Button QuitBtn;
    Button HaveNoSidemoney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper dbHelper = new DBHelper(getApplicationContext(), "SidMoney", null, 1);

        SharedPreferences pref = getSharedPreferences("checkFirst",MainActivity.MODE_PRIVATE);
        boolean checkFirst = pref.getBoolean("checkFirst",true);


        if(checkFirst == true) { // 어플리케이션이 최초로 실행되었을 경우에만 수행하는 if문

            SharedPreferences.Editor editor2 = pref.edit();
            dbHelper.insert("MainUser",10000);
            editor2.putBoolean("checkFirst", false);
            editor2.commit();

        }



        Log.d("실험123", String.valueOf(checkFirst));
        Log.d("실험123123", "onCreate: " + dbHelper.getResult());


        HaveNoSidemoney = (Button) findViewById(R.id.HaveNoSidemoney);
        HaveNoSidemoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.update("MainUser",10000);
                Log.d("시드머니 충전버튼 실행", "시드머니 충전완료");

            }
        });

        StartBtn = (Button) findViewById(R.id.StartBtn);
            StartBtn.setOnClickListener(new View.OnClickListener() { // 게임시작 버튼 . 주가정보 나오는 엑티비티로 이동
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), Stockinfo.class);
                    startActivity(intent);

                }
            });


            HelpBtn = (Button) findViewById(R.id.HelpBtn);
            HelpBtn.setOnClickListener(new View.OnClickListener() { //도움말 버튼
                @Override
                public void onClick(View view) {

                }
            });


            QuitBtn = (Button) findViewById(R.id.QuitBtn);
            QuitBtn.setOnClickListener(new View.OnClickListener() { // 게임종료 버튼
                @Override
                public void onClick(View view) {

                }
            });



    }

}