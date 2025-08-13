package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class financialDTO {

    @JsonProperty("fiscal_period")
    private String fiscalPeriod;

    @JsonProperty("tickers")
    private List<String> tickers;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("financials")
    private Financials financials;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Financials {

        @JsonProperty("income_statement")
        private IncomeStatement incomeStatement;

        public IncomeStatement getIncomeStatement() {
            return incomeStatement;
        }

        public void setIncomeStatement(IncomeStatement incomeStatement) {
            this.incomeStatement = incomeStatement;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IncomeStatement {

        @JsonProperty("revenues")
        private Revenues revenues;

        public Revenues getRevenues() {
            return revenues;
        }

        public void setNRevenues(Revenues revenues) {
            this.revenues = revenues;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Revenues {

        @JsonProperty("value")
        private Double value;

        @JsonProperty("unit")
        private String unit;

        public Double getValue() {
            return value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public void setValue(Double value) {
            this.value = value;
        }
    }
}
