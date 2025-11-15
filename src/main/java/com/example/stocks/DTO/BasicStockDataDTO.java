package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicStockDataDTO {

    @JsonProperty("results")
    private List<BasicStockDataDTO.Results> results;

    public List<BasicStockDataDTO.Results> getResults() {
        return results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Results{

        @JsonProperty("name")
        private String name;

        public String getName() {
            return name;
        }
    }
}
