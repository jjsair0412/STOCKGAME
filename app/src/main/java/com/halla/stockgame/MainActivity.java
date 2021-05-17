package com.halla.stockgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.halla.stockgame.databinding.ActivityMainBinding;
import com.halla.stocklist_fragment.Stockinfo;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DBHelper dbHelper = new DBHelper(getApplicationContext(), "SidMoney", null, 1);

        SharedPreferences pref = getSharedPreferences("checkFirst",MainActivity.MODE_PRIVATE);
        boolean checkFirst = pref.getBoolean("checkFirst",true);


        if(checkFirst) { // 어플리케이션이 최초로 실행되었을 경우에만 수행하는 if문

            SharedPreferences.Editor editor2 = pref.edit();
            dbHelper.insert("MainUser",10000);
            editor2.putBoolean("checkFirst", false);
            editor2.commit();

        }



        Log.d("실험123", String.valueOf(checkFirst));
        Log.d("실험123123", "onCreate: " + dbHelper.getResult());


        binding.HaveNoSidemoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.update("MainUser",10000);
                Log.d("시드머니 충전버튼 실행", "시드머니 충전완료");

            }
        });

            binding.StartBtn.setOnClickListener(new View.OnClickListener() { // 게임시작 버튼 . 주가정보 나오는 엑티비티로 이동
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), Stockinfo.class);
                    startActivity(intent);

                }
            });


            binding.HelpBtn.setOnClickListener(new View.OnClickListener() { //도움말 버튼
                @Override
                public void onClick(View view) {

                }
            });


            binding.QuitBtn.setOnClickListener(new View.OnClickListener() { // 게임종료 버튼
                @Override
                public void onClick(View view) {

                }
            });



    }

}