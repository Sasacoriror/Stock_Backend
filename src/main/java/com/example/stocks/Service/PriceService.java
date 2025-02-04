package com.example.stocks.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PriceService {

    private final RestTemplate restTemplate;
    private final String API_URL = "https://api.polygon.io/v3/reference/tickers/AAPL?apiKey=Ix4tpJivedA1nWgzXSR8nQjJV1si8jbE";

    public PriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getTickerData() {
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);
        return response.getBody();
    }
}
