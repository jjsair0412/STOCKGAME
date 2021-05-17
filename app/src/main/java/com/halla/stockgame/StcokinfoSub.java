package com.halla.stockgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StcokinfoSub extends LinearLayout {

    TextView stocknamexml;
    TextView stockpricexml;

    public StcokinfoSub(Context context) {
        super(context);
        inflation(context);

        stocknamexml = (TextView) findViewById(R.id.stocknamexml);
        stockpricexml = (TextView) findViewById(R.id.stockpricexml);

    }

    private void inflation(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mainstockinfosample,this,true);

    }

    public void setName(String name){
        stocknamexml.setText(name);
    }
    public void setPrice(String price){
        stockpricexml.setText(price);
    }


}
