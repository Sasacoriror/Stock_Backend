package com.example.stocks.Link;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class Endpoints {

    private String priceAPI;
    private String dividendAPI;
    private String financialAPI;
    private String watchListAPI;
    private String basicTickerInfo;
    private String weekRange;

    @Value("${api.key}")
    private String APIkey;

    // Setting the HTTP get calls to the API

    public void setPriceAPI(String ticker) {
        String url = "https://api.polygon.io/v2/aggs/ticker/"+ticker+"/prev?adjusted=true&apiKey="+APIkey;
        this.priceAPI = url;
    }

    public void setDividendAPI(String ticker) {
        String url = "https://api.polygon.io/v3/reference/dividends?ticker="+ticker+"&limit=1&apiKey="+APIkey;
        this.dividendAPI = url;
    }

    public void setFinancialAPI(String ticker, int limit) {
        String url = "https://api.polygon.io/vX/reference/financials?ticker="+ticker+"&order=desc&limit="+limit+"&sort=filing_date&apiKey="+APIkey;
        this.financialAPI = url;
    }

    public void setWatchListAPI(String ticker){
        String url = "https://api.polygon.io/v3/reference/tickers/"+ticker+"?apiKey="+APIkey;
        this.watchListAPI = url;
    }

    public void setBasicTickerInfo(String ticker){
        String url = "https://api.massive.com/v3/reference/tickers?ticker="+ticker+"&market=stocks&active=true&order=asc&limit=1&sort=ticker&apiKey="+APIkey;
        this.basicTickerInfo = url;
    }

    public void setWeekRange(String ticker){
        LocalDate to = LocalDate.now().with(DayOfWeek.FRIDAY);
        LocalDate from = to.minusWeeks(52).with(DayOfWeek.MONDAY);

        String url = "https://api.massive.com/v2/aggs/ticker/"+ticker+"/range/1/week/"+from+"/"+to+"?adjusted=true&sort=asc&limit=500&apiKey="+APIkey;
        this.weekRange = url;
    }


    // Getting the HTTP get calls to the API

    public String getPriceAPI() {
        return priceAPI;
    }

    public String getDividendAPI() {
        return dividendAPI;
    }

    public String getFinancialAPI() {
        return financialAPI;
    }

    public String getWatchListAPI() {
        return watchListAPI;
    }

    public String getBasicTickerInfo() {
        return basicTickerInfo;
    }

    public String getWeekRange() {
        return weekRange;
    }
}
