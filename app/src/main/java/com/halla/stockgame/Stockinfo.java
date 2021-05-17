package com.halla.stockgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Stockinfo extends AppCompatActivity {


    private FragmentManager fragmentManager = getSupportFragmentManager();

    private StockListFragment stockListFragment = new StockListFragment(); // 주식종목리스트 프레그먼트
    private fragment_my_invest fragment_my_invest = new fragment_my_invest(); // 투자내역 (내정보) 프레그먼트

    TextView MyMoneyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockinfo);

        DBHelper dbHelper = new DBHelper(getApplicationContext(), "SidMoney", null, 1);


        MyMoneyView = (TextView) findViewById(R.id.MyMoneyView);
        MyMoneyView.setText(dbHelper.getResult());

        // BottomNavigationView를 activity_stockinfo에서 가져옴
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fram_layout,stockListFragment).commitAllowingStateLoss();
        //프레그먼트 처음 켜졌을때 초기화면 지정

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {

                    case R.id.page_1: { // bottomnavilayout에 page_1이 클릭되었을때(왼쪽부터 첫번째)
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class); // main으로 넘어감
                        startActivity(intent);
                        break;
                    }

                    case R.id.page_2: { // bottomnavilayout에 page_2가 클릭되었을때
                        transaction.replace(R.id.fram_layout, stockListFragment).commitAllowingStateLoss(); // 종목리스트 프레그먼트로
                        break;
                    }

                    case R.id.page_3: { // bottomnavilayout에 page_3가 클릭되었을때
                        transaction.replace(R.id.fram_layout, fragment_my_invest).commitAllowingStateLoss(); // 내정보로 넘어감
                        break;
                    }

                }
                return true;
            }
        });

    }


}


