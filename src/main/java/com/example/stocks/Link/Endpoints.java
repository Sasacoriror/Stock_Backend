package com.example.stocks.Link;

import org.springframework.stereotype.Component;

@Component
public class Endpoints {

    private String priceAPI;

    public void setPriceAPI(String ticker) {
        String url = "https://api.polygon.io/v3/reference/tickers/"+ticker+"?apiKey=7k0sCkhL4aw1lSb0OhKRLibal5qpHV85";
        this.priceAPI = url;
    }

    public String getPriceAPI() {
        return priceAPI;
    }


}
