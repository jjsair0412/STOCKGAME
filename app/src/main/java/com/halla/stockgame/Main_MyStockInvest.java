package com.halla.stockgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Main_MyStockInvest extends AppCompatActivity {

    private FragmentManager fragmentManager1 = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__my_stock_invest);

        fragment_my_invest fragment_my_invest = new fragment_my_invest();

        FragmentTransaction transaction = fragmentManager1.beginTransaction();
        transaction.replace(R.id.fram_layout,fragment_my_invest).commitAllowingStateLoss();

    }
}