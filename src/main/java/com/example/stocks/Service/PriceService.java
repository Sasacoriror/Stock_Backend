package com.example.stocks.Service;

import com.example.stocks.DTO.ResultsFinancialDTO;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

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

        if (response.getBody() == null) {
            throw new RuntimeException("No response from price API");
        }

        try {
            JsonNode root = objectMapper.readTree(response.getBody());


            boolean isInvalid = root.has("resultsCount") && root.get("resultsCount").asInt() == 0;

            if (isInvalid) {
                throw new StockNotFoundException("Stock price data not found.");
            }

            return objectMapper.treeToValue(root, PriceDTO.class);

        } catch (StockNotFoundException e) {
            throw e;
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
        } catch (StockNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
    }

    public ResultsFinancialDTO getFinancialData(){
        ResponseEntity<String> response = restTemplate
                .getForEntity(endpoints.getFinancialAPI(), String.class);

        if (response.getBody() == null) {
            throw new RuntimeException("No Response");
        }

        try {
            return objectMapper.readValue(response.getBody(), ResultsFinancialDTO.class);
        } catch (StockNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
    }
}
