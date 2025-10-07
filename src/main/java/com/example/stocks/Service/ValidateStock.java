package com.example.stocks.Service;

import com.example.stocks.Link.Endpoints;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidateStock {

    @Autowired
    private Endpoints endpoints;

    private final ObjectMapper objectMapper;

    public ValidateStock(ObjectMapper objectMapper){
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
}
