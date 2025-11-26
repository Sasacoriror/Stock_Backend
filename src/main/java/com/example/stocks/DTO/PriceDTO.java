package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDTO {

    //@JsonProperty("ticker")
    //private String ticker;

    @JsonProperty("results")
    private List<Results> results;
/*
    public String getTicker() {
        return ticker;
    }*/

    public List<Results> getResults() {
        return results;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Results{

        @JsonProperty("c")
        private double closePrice;

        @JsonProperty("o")
        private double openPrice;
/*
        @JsonProperty("h")
        private double highest;

        @JsonProperty("l")
        private double lowest;*/

        public double getClosePrice() {
            return closePrice;
        }

        public double getOpenPrice() {
            return openPrice;
        }
/*
        public double getHighest() {
            return highest;
        }

        public double getLowest() {
            return lowest;
        }*/
    }
}
