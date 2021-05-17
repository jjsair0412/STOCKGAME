package com.halla.stockgame;

import androidx.appcompat.app.AppCompatActivity;

import com.halla.util.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.halla.stockgame.databinding.ActivityMainBinding;
import com.halla.stocklist_fragment.Stockinfo;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ActivityMainBinding binding;
    private String myMoneyFile;
    private String myMoneyValue;
    private boolean checkFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initVariable();
        checkFirstPlay();
        Log.d("실험123", String.valueOf(checkFirst));
        setHaveNoSideMoneyClickEvent(binding.HaveNoSidemoney);
        setStartButtonClickEvent(binding.StartBtn);

    }
    private void checkFirstPlay()
    {

        if (isFirstPlay(checkFirst)) { // 어플리케이션이 최초로 실행되었을 경우에만 수행하는 if문
            editor.putInt(myMoneyValue, Config.FISTMONEY);
            editor.commit();
        }
    }
    private boolean isFirstPlay(boolean checkFirst)
    {
        return checkFirst;
    }
    private void initVariable()
    {
        myMoneyFile = this.getString(R.string.my_money_file);
        myMoneyValue =this.getString(R.string.my_money_value);
        pref = getSharedPreferences(myMoneyFile, MainActivity.MODE_PRIVATE);
        editor = pref.edit();
        checkFirst= new File("data/data/" + getPackageName() + "/shared_prefs/" + myMoneyFile + ".xml").exists();
    }
    private void setHaveNoSideMoneyClickEvent(Button button)
    {

        button.setOnClickListener((View view) -> {
            editor.putInt(myMoneyValue, Config.FISTMONEY);
            editor.commit();
            Log.d("시드머니 충전버튼 실행", "시드머니 충전완료");
        });

    }
    private void setStartButtonClickEvent(Button button)
    {

        button.setOnClickListener(new View.OnClickListener() { // 게임시작 버튼 . 주가정보 나오는 엑티비티로 이동
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Stockinfo.class);
                startActivity(intent);

            }
        });

    }
}