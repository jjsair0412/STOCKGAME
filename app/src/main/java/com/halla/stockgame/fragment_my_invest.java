package com.halla.stockgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class fragment_my_invest extends Fragment {

    String investstockname; //리스트뷰에 뿌려줄 종목이름
    String investstockprice; //리스트뷰에 뿌려줄 종목가격

    String receive; // 서버에서 받아온 값들 저장하는 receive

    JSONArray jsonArray = new JSONArray();

    TextView interestRate; // 수익률
    ListView MyInvestListView;

    private Thread thread;

    public Handler handler=new Handler(Looper.getMainLooper());

    myAdapter adapter;

    public ArrayList<InvestStockListSampleData> investstocklist = new ArrayList<>();
    InvestStockListSampleData investStockListSampleData;

    public String Stockbuylist;

    public String[] splitbuystock;

    HttpConnection httpConnection = HttpConnection.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_invest, container, false);

        DBHelper dbHelper2 = new DBHelper(view.getContext(), "BuyStockList", null, 1);

        MyInvestListView = (ListView) view.findViewById(R.id.MyInvestListView);


        adapter = new myAdapter(this.getContext(),investstocklist);
        MyInvestListView.setAdapter(adapter);

        Stockbuylist = dbHelper2.getStockListResult();
        splitbuystock = Stockbuylist.split(" "); // 산종목들 db에서 가지고와서 공백을 기준으로 나눔

        MyInvestListView.setOnItemClickListener(itemClickListener);

        ServerReciveStockInfo();

        return view;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(investstockname.equals("A+TV")){
                String ob2 = "0";
                Intent intent = new Intent(getContext(),EachStockInfo.class);
                intent.putExtra("stockname", ob2);
                Log.d("ob실험", "onItemClick: "+ob2);
                startActivity(intent);
            }else if(investstockname.equals("Monani")){
                String ob2 = "1";
                Intent intent = new Intent(getContext(),EachStockInfo.class);
                intent.putExtra("stockname", ob2);
                Log.d("ob실험", "onItemClick: "+ob2);
                startActivity(intent);
            }else if(investstockname.equals("Sansoung")){
                String ob2 = "2";
                Intent intent = new Intent(getContext(),EachStockInfo.class);
                intent.putExtra("stockname", ob2);
                Log.d("ob실험", "onItemClick: "+ob2);
                startActivity(intent);
            }else if(investstockname.equals("Doje")){
                String ob2 = "3";
                Intent intent = new Intent(getContext(),EachStockInfo.class);
                intent.putExtra("stockname", ob2);
                Log.d("ob실험", "onItemClick: "+ob2);
                startActivity(intent);
            }else if(investstockname.equals("AG")){
                String ob2 = "4";
                Intent intent = new Intent(getContext(),EachStockInfo.class);
                intent.putExtra("stockname", ob2);
                Log.d("ob실험", "onItemClick: "+ob2);
                startActivity(intent);
            }else if(investstockname.equals("Epple")){
                String ob2 = "5";
                Intent intent = new Intent(getContext(),EachStockInfo.class);
                intent.putExtra("stockname", ob2);
                Log.d("ob실험", "onItemClick: "+ob2);
                startActivity(intent);
            }else if(investstockname.equals("NoBalance")){
                String ob2 = "6";
                Intent intent = new Intent(getContext(),EachStockInfo.class);
                intent.putExtra("stockname", ob2);
                Log.d("ob실험", "onItemClick: "+ob2);
                startActivity(intent);
            }else if(investstockname.equals("FaciBook")){
                String ob2 = "7";
                Intent intent = new Intent(getContext(),EachStockInfo.class);
                intent.putExtra("stockname", ob2);
                Log.d("ob실험", "onItemClick: "+ob2);
                startActivity(intent);
            }else if(investstockname.equals("Maike")){
                String ob2 = "8";
                Intent intent = new Intent(getContext(),EachStockInfo.class);
                intent.putExtra("stockname", ob2);
                Log.d("ob실험", "onItemClick: "+ob2);
                startActivity(intent);
            }
        }
    };

    class myAdapter extends BaseAdapter {
        Context context;
        public ArrayList<InvestStockListSampleData> investstocklist;
        public myAdapter(Context context, ArrayList<InvestStockListSampleData> investstocklist){
            this.context = context;
            this.investstocklist= investstocklist;
        }

        @Override
        public int getCount() {
            return investstocklist.size();
        }

        @Override
        public Object getItem(int position) {
            return investstocklist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyInvestInfoListSub view = new MyInvestInfoListSub(getContext());
            investStockListSampleData = investstocklist.get(position);
            view.setName(investStockListSampleData.getInvestStockName());
            view.setPrice(investStockListSampleData.getInvestStockPrice());

            return view;
        }
    }

    private void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }


    private void ServerReciveStockInfo() {
        if (thread != null)
            thread.interrupt(); // 스레드에 인터럽트

        final Runnable runnable = new Runnable() {
            @Override
            public void run() { // runnable 내부에 위치한 서버에 값을 받아오는 reciveData()
                reciveData();
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) { // while 무한반복
                    runOnUiThread(runnable); // Main이 아닌곳에서 ui를 수정하기 위해 runonuithread 사용해 runnable 실행
                    try {
                        thread.sleep(1000); // 1초마다 반복
                    } catch (InterruptedException ie) { // InterruptedException이 생기면
                        ie.printStackTrace();
                        break; // while문 빠져나옴
                    }
                }
            }
        });
        thread.start(); // 스레드 시작
    }


    @Override
    public void onPause() { // 해당 엑티비티가 종료되는 시점에 호출되는 메서드인 onPause()
        super.onPause();
        if (thread != null)
            thread.interrupt(); // onPause()가 호출되면, thread는 interrupt
    }



    private void reciveData(){
        new Thread(){
            public void run(){
                for (int i = 0; i<splitbuystock.length; i++){
                    if(i==splitbuystock.length){
                        i=0;
                    }else{
                        httpConnection.BuyStockPriceSendData(splitbuystock[i],callback); // 매수한 종목 하나씩 서버로보냄
                        investstocklist.clear(); // stocklist 초기화
                    }
                }
            }
        }.start();
    }


    //callback으로 서버와 통신
    private final okhttp3.Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("applogfragment", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String body = response.body().string();
                                Log.d("onResponse", "서버에서 응답한 Body:" + body);


                                StringReader realbody = new StringReader(body); // 읽어온 body를 StringReader로 전환
                                BufferedReader reader = new BufferedReader(realbody);
                                String str;
                                StringBuffer buffer = new StringBuffer();
                                while ((str = reader.readLine()) != null) {
                                    buffer.append(str);
                                }
                                receive = buffer.toString();

                                jsonArray = new JSONArray(receive);
                                final int numberOfItemsInResp = jsonArray.length();

                                JSONObject jsonObject = null;


                                for (int i = 0; i < numberOfItemsInResp; i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    investstockname = jsonObject.getString("stock_name");
                                    investstockprice = jsonObject.getString("stock_price");
                                    investstocklist.add(new InvestStockListSampleData(investstockname, investstockprice));
                                }

                                adapter.notifyDataSetChanged(); // 어뎁터에 값이 변화되었다고 알림
                            } catch (JSONException | IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    };
}