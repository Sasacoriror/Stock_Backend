package com.example.stocks.Service;

import com.example.stocks.DTO.FinancialDTO;
import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.DTO.DividendDTO;
import com.example.stocks.DTO.TickerOverviewDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Respository.StockRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class API_Service {

    @Autowired
    private StockRepository stockRepository;

    private final RestTemplate restTemplate;
    private final Endpoints endpoints;
    private final ObjectMapper objectMapper;


    public API_Service(RestTemplate restTemplate, ObjectMapper objectMapper, Endpoints endpoints) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.endpoints = endpoints;
    }
/*
    public String getTickerData() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(endpoints.getPriceAPI(), HttpMethod.GET, entity, String.class);
        //System.out.println("API response: " + response.getBody());
        return response.getBody();
    }
 */

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

        try {
            return objectMapper.readValue(response.getBody(), PriceDTO.class);

        } catch (StockNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
    }


    public DividendDTO getDividendData(){
        ResponseEntity<String> response = restTemplate
                .getForEntity(endpoints.getDividendAPI(), String.class);

        if (response.getBody() == null) {
            throw new RuntimeException("No Response");
        }

        try {
            return objectMapper.readValue(response.getBody(), DividendDTO.class);
        } catch (StockNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
    }

    public FinancialDTO getFinancialData(){
        ResponseEntity<String> response = restTemplate
                .getForEntity(endpoints.getFinancialAPI(), String.class);

        if (response.getBody() == null) {
            throw new RuntimeException("No Response");
        }

        try {
            return objectMapper.readValue(response.getBody(), FinancialDTO.class);
        } catch (StockNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
    }

    public TickerOverviewDTO getTickerOverviewlData(){
        ResponseEntity<String> response = restTemplate
                .getForEntity(endpoints.getWatchListAPI(), String.class);

        if (response.getBody() == null) {
            throw new RuntimeException("No Response");
        }

        try {
            return objectMapper.readValue(response.getBody(), TickerOverviewDTO.class);
        } catch (StockNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
    }
}
