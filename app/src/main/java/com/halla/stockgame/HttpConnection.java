package com.halla.stockgame;

import android.util.Log;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpConnection {
    String ReciveUrl =BuildConfig.RECIVEURL; // 전체 db정보 받아오는 주소
    String SendUrl = BuildConfig.SENDURL; // 바뀐 주가 서버로 올려주는 주소
    String StockPriceUrl = BuildConfig.STOCKPRICEURL; // 종목이름에맞는 주가 받아오는 주소
    String BuyStockPriceUrl = BuildConfig.BUYSTOCKPRICEURL;

    private OkHttpClient client;
    private static HttpConnection instance = new HttpConnection();


    public static HttpConnection getInstance() {
        return instance;
    }

    private HttpConnection() {
        this.client = new OkHttpClient();
    }

    public void ChangeData(String change_stock_price, String change_stock_name, Callback callback2) {
        RequestBody Body = new FormBody.Builder()
                .add("change_stock_price",change_stock_price) // stock_price으로 change_stock_price 변수에 있는 값을 서버로 전달
                .add("change_stock_name",change_stock_name) // stock_name으로 change_stock_name 변수에 있는 값을 서버로 전달
                .build();
        Request request = new Request.Builder()
                .url(SendUrl)
                .post(Body) //post방식으로 Body에있는 변수들을 해당 url로 전달한다
                .build();

        Log.d("HttpConnection", "ChangeData: "+change_stock_name);
        Log.d("HttpConnection", "ChangeData: "+change_stock_price);
        client.newCall(request).enqueue(callback2);
    }

    public void ReciveData(Callback callback) {
        Request request = new Request.Builder()
                .url(ReciveUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void SendData(String recivename, Callback callback) {
        RequestBody Body = new FormBody.Builder()
                .add("recivename",recivename)
                .build();
        Request request = new Request.Builder()
                .url(StockPriceUrl)
                .post(Body) //post방식으로 Body에있는 변수들을 해당 url로 전달한다
                .build();
        client.newCall(request).enqueue(callback);
    }


    public void BuyStockPriceSendData(String recivename, Callback callback) {
        RequestBody Body = new FormBody.Builder()
                .add("recivename",recivename)
                .build();
        Request request = new Request.Builder()
                .url(BuyStockPriceUrl)
                .post(Body) //post방식으로 Body에있는 변수들을 해당 url로 전달한다
                .build();
        client.newCall(request).enqueue(callback);
    }
}
