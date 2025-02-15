package com.example.stocks.Service;

import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.Link.Endpoints;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PriceService {

    private final RestTemplate restTemplate;
    private final Endpoints endpoints;
    private final String API_URL = "https://api.polygon.io/v3/reference/tickers/AAPL?apiKey=7k0sCkhL4aw1lSb0OhKRLibal5qpHV85";
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
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);

        if (response.getBody() == null) {
            throw new RuntimeException("No Response");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode resultsNode = rootNode.path("results");
            return objectMapper.treeToValue(resultsNode, PriceDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
    }
}
