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
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        for (Stocks stock : stocks) {

            endpoints.setPriceAPI(stock.getStockName().toUpperCase());
            endpoints.setDividendAPI(stock.getStockName().toUpperCase());
            endpoints.setFinancialAPI(stock.getStockName().toUpperCase(), 1);

            PriceDTO priceData = getPriceData();
            ResultsDividendDTO dividendData = getDividendData();
            ResultsFinancialDTO financialData = getFinancialData();

            Double newPrice = null;
            Double newDividend = null;


            String name = financialData.getResults().get(0).getCompanyName();
            stock.setCompanyName(name);



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

            double totalInvested = stock.getStockQuantity() * stock.getStockPrice();
            double returns = (stock.getStockQuantity() * newPrice) - (stock.getStockQuantity() * stock.getStockPrice());
            double percentage = (returns / totalInvested) * 100;

            stock.setTotalInvested(totalInvested);
            stock.setReturnValue(Double.parseDouble(df.format(returns)));
            stock.setPercentageReturn(Double.parseDouble(df.format(percentage)));
            stock.setDividend(dividendData.getResults().get(0).getFrequency());


            if (newDividend != null && !Objects.equals(newDividend, stock.getDividend())) {
                //stock.setDividend(newDividend);
                stock.setTotalDivided(stock.getStockQuantity() * newDividend * 4);
                hasChanges = true;
            }

            if (hasChanges) {
                stockRepository.save(stock);
                //UpdateDatabase(stock.getStockName());
            }
        }
    }

    @Transactional
    public void UpdateDatabase(String stockName){
        Optional<Stocks> stocks = stockRepository.findById(stockName);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        ResultsDividendDTO dividendData = getDividendData();


        double dividend = dividendData.getResults().get(0).getCash_amount();
        int shares = stocks.get().getStockQuantity();
        double price = stocks.get().getCurrentPrice();
        int paidPrice = stocks.get().getStockPrice();

        double newTotalDividend = (dividend * 4) * shares;
        double newTotalPrice = (price * shares);
        double newTotalInvested = paidPrice * shares;
        double newReturnValue = (price * shares) - (paidPrice * shares);
        double newPercentageReturn = (newReturnValue / newTotalInvested) * 100;

        stocks.get().setTotalDivided(newTotalDividend);
        stocks.get().setTotalPrice(newTotalPrice);
        stocks.get().setTotalInvested(Double.parseDouble(df.format(newTotalInvested)));
        stocks.get().setReturnValue(Double.parseDouble(df.format(newReturnValue)));
        stocks.get().setPercentageReturn(Double.parseDouble(df.format(newPercentageReturn)));
    }

}
