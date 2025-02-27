package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultsDividendDTO {

    @JsonProperty("results")
    private List<DividendDTO> results;

    public List<DividendDTO> getResults() {
        return results;
    }

    public void setResults(List<DividendDTO> results) {
        this.results = results;
    }
}
