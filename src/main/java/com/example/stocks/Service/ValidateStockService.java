package com.example.stocks.Service;

import com.example.stocks.Link.Endpoints;
import com.example.stocks.Respository.StockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class ValidateStockService {

    @Autowired
    private Endpoints endpoints;

    @Autowired
    private StockRepository stockRepository;

    private final ObjectMapper objectMapper;

    public ValidateStockService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public boolean isValid(String ticker){
        try {
            endpoints.setPriceAPI(ticker);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(endpoints.getPriceAPI(), String.class);

            if (response.getBody() == null || response.getBody().isEmpty()) {
                return false;
            }

            JsonNode root = objectMapper.readTree(response.getBody());

            if (root.has("resultsCount") && root.get("resultsCount").asInt() == 0) {
                return false;
            }

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public static ResponseEntity<?> ok(String message){
        return ResponseEntity.ok(Map.of("Message", message));
    }

    public static ResponseEntity<?> error(String message, HttpStatus status){
        return ResponseEntity.status(status).body(Map.of("Error",message));
    }

    public  void stockValidation(String ticker){
        String stockName = ticker.toUpperCase();
        if (stockRepository.existsByStockName(stockName)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock already exists");
        } else if (!isValid(stockName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticker: "+stockName+" does not exist");
        }
    }

    public void ifStockExist(String ticker){
        String stockName = ticker.toUpperCase();
        if (!isValid(stockName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticker: "+stockName+" does not exist");
        }
    }
}
