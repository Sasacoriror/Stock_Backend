package com.example.stocks.Service;

import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.DTO.ResultsDividendDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Respository.StockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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


    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////


    public static class StockNotFoundException extends RuntimeException {
        public StockNotFoundException(String message) {
            super(message);
        }
    }


    public PriceDTO getPriceData(){
        ResponseEntity<String> response = restTemplate
                .getForEntity(endpoints.getPriceAPI(), String.class);

        if (response.getBody() == null) {
            throw new RuntimeException("No response from price API");
        }

        try {
            JsonNode root = objectMapper.readTree(response.getBody());


            boolean isInvalid = root.has("resultsCount") && root.get("resultsCount").asInt() == 0;

            if (isInvalid) {
                throw new StockNotFoundException("Stock data not found.");
            }

            return objectMapper.treeToValue(root, PriceDTO.class);

        } catch (StockNotFoundException e) {
            throw e; // Let it bubble to the controller
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
            JsonNode root = objectMapper.readTree(response.getBody());

            boolean isInvalid = root.has("results") && root.get("results").isArray() && root.get("results").size() == 0;

            if (isInvalid) {
                throw new StockNotFoundException("Stock data not found.");
            }

            return objectMapper.readValue(response.getBody(), ResultsDividendDTO.class);
        } catch (StockNotFoundException e) {
            throw e; // Let it bubble to the controller
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
    }

    public void oneMinutte() {
        try {
            Thread.sleep(61000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("oneMinute function was interrupted");
        }

    }

    @Transactional
    public void updateStockData() {
        List<Stocks> stocks = stockRepository.findAll();
        int counter = 0;

        for (Stocks stock : stocks) {


            if (counter == 2) {
                oneMinutte();
                counter = 0;
                System.out.println("Stopped\ncounter = " + counter);
            }

            endpoints.setPriceAPI(stock.getStockName().toUpperCase());
            endpoints.setDividendAPI(stock.getStockName().toUpperCase());

            PriceDTO priceData = getPriceData();
            ResultsDividendDTO dividendData = getDividendData();

            Double newPrice = null;
            Double newDividend = null;

            if (priceData != null && priceData.getResults() != null && !priceData.getResults().isEmpty()) {
                newPrice = priceData.getResults().get(0).getC();
            }

            if (dividendData != null && dividendData.getResults() != null && !dividendData.getResults().isEmpty()) {
                newDividend = dividendData.getResults().get(0).getCash_amount();
            }

            boolean hasChanges = false;

            if (newPrice != null && !Objects.equals(newPrice, stock.getCurrentPrice())) {
                stock.setCurrentPrice(newPrice);
                stock.setTotalPrice(stock.getStockQuantity() * newPrice);
                hasChanges = true;
            }

            if (newDividend != null && !Objects.equals(newDividend, stock.getDividend())) {
                stock.setDividend(newDividend);
                stock.setTotalDivided(stock.getStockQuantity() * newDividend * 4);
                hasChanges = true;
            }

            if (hasChanges) {
                stockRepository.save(stock);
                UpdateDatabase(stock.getStockName());
            }
            counter++;
        }
    }

    @Transactional
    public void UpdateDatabase(String stockName){
        Optional<Stocks> stocks = stockRepository.findById(stockName);

        double dividend = stocks.get().getDividend();
        int shares = stocks.get().getStockQuantity();

        double newTotalDividend = (dividend * 4) * shares;
        //double newTotalValue = shares*stockPricePayd;

        stocks.get().setTotalDivided(newTotalDividend);
    }

}
