package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DividendDTO {

    @JsonProperty("results")
    private List<Results> results;

    public List<Results> getResults() {
        return results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Results{

        @JsonProperty("cash_amount")
        private double cash_amount;

        @JsonProperty("frequency")
        private int frequency;

        public double getCash_amount() {
            return cash_amount;
        }

        public int getFrequency() {
            return frequency;
        }

    }
}
