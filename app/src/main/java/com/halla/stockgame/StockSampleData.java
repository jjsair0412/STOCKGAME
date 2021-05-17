package com.halla.stockgame;

public class StockSampleData {
    public String stockname;
    public String stockprice;

    public StockSampleData(String stockname, String stockprice) {
        this.stockname = stockname;
        this.stockprice = stockprice;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

    public String getStockprice() {
        return stockprice;
    }

    public void setStockprice(String stockprice) {
        this.stockprice = stockprice;
    }
}
