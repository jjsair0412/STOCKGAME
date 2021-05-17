package com.halla.stockgame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {


    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 SidMoney, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        User 문자열 컬럼, Money 정수형 컬럼 생성 */
        db.execSQL("CREATE TABLE IF NOT EXISTS SidMoney (User TEXT, Money INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS BuyStockList (BuyStockNmae TEXT, BuyMomentPrice INTEGER);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String User, int Money) { // sidmoney 테이블 insert문
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO SidMoney VALUES( '" + User + "', " + Money + ");");
        db.close();
    }

    public void BuyStockinsert(String BuyStockNmae, int BuyMomentPrice) { // BuyStockList insert문 매수했을 경우 실행
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO BuyStockList VALUES( '" + BuyStockNmae + "', " + BuyMomentPrice + ");");
        db.close();
    }

    public void update(String User, int Money) { // sidmoney테이블 update문
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE SidMoney SET Money=" + Money + " WHERE User='" + User + "';");
        db.close();
    }


    public void MoneyTalbedelete(String User) { // 시드머니테이블 튜플 삭제
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM SidMoney WHERE User='" + User + "';");
        db.close();
    }

    public void SellStockTable(String BuyStockNmae) { // BuyStockTable 튜블 삭제 . 매도했을 경우 실행
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM BuyStockList WHERE BuyStockNmae='" + BuyStockNmae + "';");
        db.close();
    }

    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM SidMoney", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(1);
        }

        return result;
    }


    public String getStockListResult() { // 산종목 이름을 반환
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BuyStockList", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)+" ";
        }

        return result;
    }

    public String getStockPriceResult(String BuyStockNmae) { // 종목을 산 순간의 가격을 반환 , 수익률계산용
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT BuyMomentPrice FROM BuyStockList WHERE BuyStockNmae='"+BuyStockNmae+"';",null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0);
        }
        return result;
    }

}

