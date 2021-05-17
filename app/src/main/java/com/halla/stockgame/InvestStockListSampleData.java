package com.halla.stockgame;

public class InvestStockListSampleData {
    public String InvestStockName;
    public String InvestStockPrice;

    public InvestStockListSampleData(String InvestStockName,String InvestStockPrice){
        this.InvestStockName = InvestStockName;
        this.InvestStockPrice = InvestStockPrice;
    }

    public String getInvestStockName() {
        return InvestStockName;
    }

    public void setInvestStockName(String investStockName) {
        InvestStockName = investStockName;
    }

    public String getInvestStockPrice() {
        return InvestStockPrice;
    }

    public void setInvestStockPrice(String investStockPrice) {
        InvestStockPrice = investStockPrice;
    }
}
