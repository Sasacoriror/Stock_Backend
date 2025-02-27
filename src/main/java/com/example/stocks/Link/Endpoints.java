package com.example.stocks.Link;

import org.springframework.stereotype.Component;

@Component
public class Endpoints {

    private String priceAPI;
    private String dividendAPI;

    public void setPriceAPI(String ticker) {
        String url = "https://api.polygon.io/v2/aggs/ticker/"+ticker+"/prev?adjusted=true&apiKey=7k0sCkhL4aw1lSb0OhKRLibal5qpHV85";
        this.priceAPI = url;
    }

    public String getPriceAPI() {
        return priceAPI;
    }

    public void setDividendAPI(String ticker) {
        String url = "https://api.polygon.io/v3/reference/dividends?ticker="+ticker+"&limit=1&apiKey=7k0sCkhL4aw1lSb0OhKRLibal5qpHV85";
        this.dividendAPI = url;
    }

    public String getDividendAPI() {
        return dividendAPI;
    }


}
