package com.halla.stockgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyInvestInfoListSub extends LinearLayout {

    TextView investstocknamexml;
    TextView invsetstockpricexml;

    public MyInvestInfoListSub(Context context) {
        super(context);
        inflation(context);

        investstocknamexml = (TextView) findViewById(R.id.investstocknamexml);
        invsetstockpricexml = (TextView) findViewById(R.id.invsetstockpricexml);
    }

    private void inflation(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.myinvestinfosublist_x,this,true);
    }

    public void setName(String name){
        investstocknamexml.setText(name);
    }
    public void setPrice(String price){
        invsetstockpricexml.setText(price);
    }
}
