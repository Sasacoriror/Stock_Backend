package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class financialDTO {

    @JsonProperty("fiscal_period")
    private String fiscalPeriod;

    @JsonProperty("fiscal_year")
    private String fiscalYear;

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

        @JsonProperty("net_income_loss")
        private Net_Income_Loss net_income_loss;

        @JsonProperty("gross_profit")
        private Gross_Profit gross_Profit;

        @JsonProperty("operating_income_loss")
        private Operating_Income_Loss operating_income_loss;

        public Revenues getRevenues() {
            return revenues;
        }

        public void setNRevenues(Revenues revenues) {
            this.revenues = revenues;
        }

        public Net_Income_Loss getNet_income_loss() {
            return net_income_loss;
        }

        public void setNet_income_loss(Net_Income_Loss net_income_loss) {
            this.net_income_loss = net_income_loss;
        }

        public Gross_Profit getGross_Profit() {
            return gross_Profit;
        }

        public void setGross_Profit(Gross_Profit gross_Profit) {
            this.gross_Profit = gross_Profit;
        }

        public Operating_Income_Loss getOperating_income_loss() {
            return operating_income_loss;
        }

        public void setOperating_income_loss(Operating_Income_Loss operating_income_loss) {
            this.operating_income_loss = operating_income_loss;
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Net_Income_Loss {

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gross_Profit {

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Operating_Income_Loss {

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
