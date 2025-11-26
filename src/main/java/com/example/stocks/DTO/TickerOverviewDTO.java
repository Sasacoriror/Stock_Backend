package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerOverviewDTO {

    @JsonProperty("results")
    private Results results;

    public Results getResults(){
        return results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Results{

        @JsonProperty("description")
        private String description;

        @JsonProperty("market_cap")
        private Long marketCap;

        @JsonProperty("weighted_shares_outstanding")
        private Long wso;

        // TODO: Add name of the company and the stock type

        public Long getMarketCap() {
            return marketCap;
        }

        public Long getWso() {
            return wso;
        }

        public String getDescription() {
            return description;
        }
    }
}
