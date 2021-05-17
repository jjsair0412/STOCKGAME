    package com.halla.stockgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EachStockInfo extends AppCompatActivity {

    TextView ReciveStockName;
    TextView ReciveStockPrice;
    Button Buy; // 매수버튼
    Button Sell; // 매도버튼

    String reciveitemnum; // stockinfo에서 전달받은 종목
    public String change_stock_price; // 매수 및 매도 등을 진행하면서 바뀐 주가가 저장되어있는 change_stock_price
    public String change_stock_name; // 매수 및 매도 등을 진행하면서 주가가 바뀐 종목이 저장되어있는 change_stock_name

    public String recivename; // 서버로 보낼 종목이름

    public int Price; // 서버에서 클릭한 종목 이름에 맞는 주가를 받아온 ServerPrice를 int로 형변환시켜준 price
    public String Price2;
    public String ServerPrice; //서버에서 클릭한 종목 이름에 맞는 주가를 받아온 ServerPrice

    Timer timer = new Timer();

    private LineChart chart;
    private Thread thread;
    Button BackBtn;
    Handler handler = new Handler();
    TextView sidmoney;

    public int dbprice;
    public int ZeroPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_stock_info);

        DBHelper dbHelper = new DBHelper(getApplicationContext(), "SidMoney", null, 1);
        DBHelper dbHelper2 = new DBHelper(getApplicationContext(), "BuyStockList", null, 1);

        dbprice = Integer.parseInt(dbHelper.getResult());

        sidmoney = (TextView) findViewById(R.id.sidmoney);
        Log.d("실험실험실험", "onCreate: "+dbprice);

        sidmoney.setText(dbHelper.getResult());

        Intent intent = getIntent();

        chart = (LineChart) findViewById(R.id.chart);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setTextColor(Color.WHITE);
        chart.animateXY(2000, 2000);
        chart.invalidate();

        LineData data = new LineData();
        chart.setData(data);

        feedMultiple();

        Buy = (Button) findViewById(R.id.Buy); // 매수
        Buy.setOnClickListener(new View.OnClickListener() { // 사고난뒤 가격이 올라야함
            @Override
            public void onClick(View view) {
                String DBINstockName = dbHelper2.getStockListResult();
                if (DBINstockName.contains(recivename)){
                    Log.d("여러개","같은 종목을 여러개 살 수 없습니다.");
                }else{ // 한 종목을 여러번사지 못하도록 else문에서 작동
                    if(dbprice>=Price){ // 시드가 해당 종목별 가격보다 크거나 같을때만 매수 실행
                        dbprice-=Price; // 매수하면 해당종목가격이 시드머니에서 빠짐
                        Log.d("바뀐시드머니 실험", "onClick: "+dbprice);
                        dbHelper.update("MainUser",dbprice); //db에 바뀐 시드머니 업데이트
                        sidmoney.setText(dbHelper.getResult());

                        dbHelper2.BuyStockinsert(recivename,Price); // db에 매수한 종목 insert, 종목가격이 증가하기 전에 매수했던순간의 값을 db에 저장
                        Log.d("매수종목 들어갓는지 실험", "onClick: "+dbHelper2.getStockListResult());

                        Price+=100; // 시드머니에서 종목가격 빠진 후에 값 증가

                        Price2 =Integer.toString(Price);
                        change_stock_price = Price2;
                        change_stock_name = recivename;

                        ChangeData();
                        Log.d("바뀐값", "onClick: "+Price2);
                    }else {
                        Log.d("시드머니 부족","시드머니가 부족합니다.");
                    }
                }
            }
        });

        Sell = (Button) findViewById(R.id.Sell); // 매도
        Sell.setOnClickListener(new View.OnClickListener() { // 팔고나서 가격이 떨어져야함
            @Override
            public void onClick(View view) {
                String DBINstockName = dbHelper2.getStockListResult();
                if(DBINstockName.contains(recivename) && dbprice>=0){ // 매수한 목록중 현재 켜져있는 종목의 이름이 포함되어 있고, 내 시드머니가 0원보다 같거나 클때만 수행
                        dbprice+=Price; // 판 순간의 가격을 시드머니에 더해준다.
                        dbHelper.update("MainUser",dbprice); //db에 바뀐 시드머니 업데이트
                        dbHelper2.SellStockTable(recivename);
                        sidmoney.setText(dbHelper.getResult());

                        Price-=100; // 주가를 100원 빼준다
                        Price2 =Integer.toString(Price);
                        change_stock_price = Price2;
                        change_stock_name = recivename;
                        if (change_stock_price.equals("0") || Price <= 0){
                            String S_ZeroPrice = Integer.toString(ZeroPrice);
                            change_stock_price = S_ZeroPrice;
                        }
                        ChangeData();
                        Log.d("바뀐값", "onClick: "+Price2);
                }else{
                    Log.d("오류", "다른 종목이거나 매수한 내역이 없습니다");
                }
            }
        });

        BackBtn = (Button) findViewById(R.id.BackBtn);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EachStockInfo.this,Stockinfo.class);
                startActivity(intent);
            }
        });

        ReciveStockPrice = (TextView) findViewById(R.id.ReciveStockPrice);

        ReciveStockName = (TextView) findViewById(R.id.ReciveStockName);


        reciveitemnum = intent.getStringExtra("stockname");


        if (reciveitemnum.equals("0")){
            recivename = "A+TV";
            ReciveStockName.setText(recivename);

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReciveStockPrice.setText(ServerPrice);
                            Log.d("첫번째", "첫번쨰 주가: "+Price);
                        }
                    },1000);
                }
            };
            timer.schedule(timerTask,0,1000);


        }else if(reciveitemnum.equals("1")){
            recivename = "Monani";
            ReciveStockName.setText(recivename);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReciveStockPrice.setText(ServerPrice);
                            Log.d("두번째", "두번쨰 주가: "+Price);
                        }
                    },1000);
                }
            };
            timer.schedule(timerTask,0,1000);


        }else if(reciveitemnum.equals("2")){
            recivename = "Sansoung";
            ReciveStockName.setText(recivename);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReciveStockPrice.setText(ServerPrice);
                            Log.d("세번째", "세번쨰 주가: "+Price);
                        }
                    },1000);
                }
            };
            timer.schedule(timerTask,0,1000);



        }else if(reciveitemnum.equals("3")){
            recivename = "DoJe";
            ReciveStockName.setText(recivename);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReciveStockPrice.setText(ServerPrice);
                            Log.d("네번째", "네번쨰 주가: "+Price);
                        }
                    },1000);
                }
            };
            timer.schedule(timerTask,0,1000);


        }else if(reciveitemnum.equals("4")){
            recivename = "AG";
            ReciveStockName.setText(recivename);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReciveStockPrice.setText(ServerPrice);
                            Log.d("다섯번째", "다섯번쨰 주가: "+Price);
                        }
                    },1000);
                }
            };
            timer.schedule(timerTask,0,1000);


        }else if(reciveitemnum.equals("5")){
            recivename = "Epple";
            ReciveStockName.setText(recivename);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReciveStockPrice.setText(ServerPrice);
                            Log.d("여섯번째", "여섯번쨰 주가: "+Price);
                        }
                    },1000);
                }
            };
            timer.schedule(timerTask,0,1000);


        }else if(reciveitemnum.equals("6")){
            recivename = "NoBalance";
            ReciveStockName.setText(recivename);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReciveStockPrice.setText(ServerPrice);
                            Log.d("일곱번째", "일곱번쨰 주가: "+Price);
                        }
                    },1000);
                }
            };
            timer.schedule(timerTask,0,1000);



        }else if(reciveitemnum.equals("7")){
            recivename = "FaciBook";
            ReciveStockName.setText(recivename);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReciveStockPrice.setText(ServerPrice);
                            Log.d("여덟번째", "여덟번쨰 주가: "+Price);
                        }
                    },1000);
                }
            };
            timer.schedule(timerTask,0,1000);



        }else if(reciveitemnum.equals("8")){
            recivename = "Maike";
            ReciveStockName.setText(recivename);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReciveStockPrice.setText(ServerPrice);
                            Log.d("아홉번째", "아홉번쨰 주가: "+Price);
                        }
                    },1000);
                }
            };
            timer.schedule(timerTask,0,1000);
        }
    }

    private void addEntry() {
        LineData data = chart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), Price) , 0); // y축에는 Price


            data.notifyDataChanged();

            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(50); // x축이 10만큼 늘어나면 칸을 뒤로 넘김
            chart.moveViewToX(data.getEntryCount());

        }
    }

    private LineDataSet createSet(){

        LineDataSet set = new LineDataSet(null, "실시간 주가");
        set.setFillAlpha(110);
        set.setFillColor(Color.parseColor("#d7e7fa"));
        set.setColor(Color.parseColor("#0B80C9"));
        set.setCircleColor(Color.parseColor("#FFA1B4DC"));
        set.setCircleColorHole(Color.BLUE);
        set.setValueTextColor(Color.WHITE);
        set.setDrawValues(false);
        set.setLineWidth(2);
        set.setCircleRadius(6);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(true);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setHighLightColor(Color.rgb(244, 117, 117));

        return set;

    }

    private void feedMultiple() {
        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ZeroPrice = (int) (Math.random()*500+1); // 주가가 0원이됬을경우
                addEntry();
                SendData();
            }
        };

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        runOnUiThread(runnable);
                        try {
                            thread.sleep(500);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                            break;
                        }
                    }
                }
            });
            thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (thread != null)
            thread.interrupt();
            timer.cancel();
    }


    HttpConnection httpConnection = HttpConnection.getInstance();

    private void ChangeData(){
        new Thread(){
            public void run(){
                httpConnection.ChangeData(change_stock_price,change_stock_name,callback2); // 어플에서 변화된 값을 서버로 전달
            }
        }.start();
    }

    private void SendData(){
        new Thread(){
            public void run(){
                Log.d("TAG", "run(): "+recivename);
                httpConnection.SendData(recivename,callback);
            }
        }.start();
    }

    private final okhttp3.Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("applogfragment", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            ServerPrice = response.body().string();
            Log.d("onResponse", "종목명당 가격:"+ServerPrice);

            try {
                Price = Integer.parseInt(ServerPrice);
            }catch (NumberFormatException e){
                Log.d("EachStockinfo", "onResponse: 형식에러");
            }catch (Exception e){
                Log.d("EachStockinfo", "onResponse: 형식에러");

            }
        }
    };

    private final okhttp3.Callback callback2 = new Callback() { // 서버로 변환된 값을 전달하기만하는 callback2
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("applogfragment", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            ServerPrice = response.body().string();
            Log.d("onResponse", "서버전달완료:"+ServerPrice);

        }
    };

}