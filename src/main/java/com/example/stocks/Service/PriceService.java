package com.example.stocks.Service;

import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.DTO.ResultsDividendDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Respository.StockRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PriceService {

    @Autowired
    private StockRepository stockRepository;

    private final RestTemplate restTemplate;
    private final Endpoints endpoints;
    private final ObjectMapper objectMapper;

    public PriceService(RestTemplate restTemplate, ObjectMapper objectMapper, Endpoints endpoints) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.endpoints = endpoints;
    }

    public String getTickerData() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(endpoints.getPriceAPI(), HttpMethod.GET, entity, String.class);
        //System.out.println("API response: " + response.getBody());
        return response.getBody();
    }

    public PriceDTO getPriceData(){
        ResponseEntity<String> response = restTemplate
                .getForEntity(endpoints.getPriceAPI(), String.class);

        if (response.getBody() == null) {
            throw new RuntimeException("No Response");
        }

        try {
            return objectMapper.readValue(response.getBody(), PriceDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
    }

    public ResultsDividendDTO getDividendData(){
        ResponseEntity<String> response = restTemplate
                .getForEntity(endpoints.getDividendAPI(), String.class);

        if (response.getBody() == null) {
            throw new RuntimeException("No Response");
        }

        try {
            return objectMapper.readValue(response.getBody(), ResultsDividendDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
    }

    public void updateStockData() {
        List<Stocks> stocks = stockRepository.findAll();

        for (Stocks stock : stocks) {
            endpoints.setPriceAPI(stock.getStockName());
            endpoints.setDividendAPI(stock.getStockName());

            PriceDTO priceData = getPriceData();
            ResultsDividendDTO dividendData = getDividendData();

            if (priceData != null && priceData.getResults() != null) {
                stock.setCurrentPrice(priceData.getResults().get(0).getC()); // "C" is closing price
            }

            if (dividendData != null && dividendData.getResults() != null && !dividendData.getResults().isEmpty()) {
                stock.setDividend(dividendData.getResults().get(0).getCash_amount());
            }


            stockRepository.save(stock);
        }
    }

}
