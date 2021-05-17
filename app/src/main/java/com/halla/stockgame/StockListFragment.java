package com.halla.stockgame;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StockListFragment extends Fragment {

    private Thread thread;

    String stock_name; //서버로가는 종목이름
    String stock_price; //서버로가는 종목가격

    String stockname; //리스트뷰에 뿌려줄 종목이름
    String stockprice; //리스트뷰에 뿌려줄 종목가격

    String receive; // 서버에서 받아온 값들 저장하는 receive

    JSONArray jsonArray = new JSONArray();

    public ArrayList<StockSampleData> stocklist = new ArrayList<>();

    ListView listView;
    myAdapter adapter;

    Handler handler=new Handler(Looper.getMainLooper());
    StockSampleData stockSampleData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_list, container, false);


        listView = (ListView) view.findViewById(R.id.list1);
        adapter = new myAdapter();
        listView.setAdapter(adapter);

        ServerReciveStockInfo();

        listView.setOnItemClickListener(itemClickListener);
        return view;
    }


    private void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }


    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Object ob = (Object)adapterView.getAdapter().getItemId(i);  //리스트뷰의 item의 순서를 가져와 vo에 저장
            Log.d("실험", "onItemClick: "+ob);
            String ob2 = ob.toString();

            Intent intent = new Intent(getContext(),EachStockInfo.class);
            intent.putExtra("stockname", ob2);

            startActivity(intent);


        }
    };



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


    class myAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return stocklist.size();
        }

        @Override
        public Object getItem(int position) {
            return stocklist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StcokinfoSub view = new StcokinfoSub(getContext());
            stockSampleData = stocklist.get(position);

            view.setName(stockSampleData.getStockname());
            view.setPrice(stockSampleData.getStockprice());

            return view;
        }
    }

    HttpConnection httpConnection = HttpConnection.getInstance();


    private void reciveData(){
        new Thread(){
            public void run(){
                httpConnection.ReciveData(callback); // 서버의 값을 받아오기만함
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

                                stocklist.clear(); // stocklist 초기화

                                for (int i = 0; i < numberOfItemsInResp; i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    stockname = jsonObject.getString("stock_name");
                                    stockprice = jsonObject.getString("stock_price");
                                    stocklist.add(new StockSampleData(stockname, stockprice));
                                }
                                adapter.notifyDataSetChanged(); // 어뎁터에 값이 변화되었다고 알림
                            } catch (JSONException | IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                    });
                }
            });

//            String body = response.body().string();
//            Log.d("onResponse", "서버에서 응답한 Body:"+body);
//
//            stocklist.clear(); // stocklist 초기화 무한스크롤 버그 해결
//
//            StringReader realbody = new StringReader(body); // 읽어온 body를 StringReader로 전환
//            BufferedReader reader = new BufferedReader(realbody);
//            String str;
//            StringBuffer buffer = new StringBuffer();
//            while ((str = reader.readLine()) != null) {
//                buffer.append(str);
//            }
//            receive = buffer.toString();
//
//            try {
//                jsonArray = new JSONArray(receive);
//                final int numberOfItemsInResp = jsonArray.length();
//                for(int i = 0; i<numberOfItemsInResp; i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    stockname = jsonObject.getString("stock_name");
//                    stockprice = jsonObject.getString("stock_price");
//
//                    stocklist.add(new StockSampleData(stockname,stockprice));
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    adapter.notifyDataSetChanged(); // 어뎁터에 값이 변화되었다고 알림
//                                }
//                            });
//                        }
//                    },0);
//
//                }
//
//            }catch (JSONException e){
//                e.printStackTrace();
//            }


        }
    };


}