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

        @JsonProperty("record_date")
        private String recordDate;

        @JsonProperty("pay_date")
        private String payDate;

        @JsonProperty("declaration_date")
        private String decDate;

        @JsonProperty("ex_dividend_date")
        private String exDate;

        @JsonProperty("frequency")
        private int frequency;

        @JsonProperty("cash_amount")
        private double cash_amount;

        public String getRecordDate() {
            return recordDate;
        }

        public String getPayDate() {
            return payDate;
        }

        public String getDecDate() {
            return decDate;
        }

        public String getExDate() {
            return exDate;
        }

        public int getFrequency() {
            return frequency;
        }

        public double getCash_amount() {
            return cash_amount;
        }
    }
}
