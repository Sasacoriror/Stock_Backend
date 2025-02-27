package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDTO {

    @JsonProperty("ticker")
    private String ticker;

    @JsonProperty("results")
    private List<ResultPriceDTO> results;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public List<ResultPriceDTO> getResults() {
        return results;
    }

    public void setResults(List<ResultPriceDTO> results) {
        this.results = results;
    }
}
