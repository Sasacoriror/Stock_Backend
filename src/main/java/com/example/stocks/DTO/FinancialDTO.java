package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialDTO {

    @JsonProperty("results")
    private List<Results> results;

    public List<Results> getResults() {
        return results;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Results {

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


        public String getFiscalPeriod() {
            return fiscalPeriod;
        }

        public String getFiscalYear() {
            return fiscalYear;
        }

        public List<String> getTickers() {
            return tickers;
        }

        public String getCompanyName() {
            return companyName;
        }

        public Financials getFinancials() {
            return financials;
        }

    }

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

        @JsonProperty("net_income_loss_available_to_common_stockholders_basic")
        private NetIncome netIncome;

        public Revenues getRevenues() {
            return revenues;
        }

        public Net_Income_Loss getNet_income_loss() {
            return net_income_loss;
        }

        public Gross_Profit getGross_Profit() {
            return gross_Profit;
        }

        public Operating_Income_Loss getOperating_income_loss() {
            return operating_income_loss;
        }

        public NetIncome getNetIncome() {
            return netIncome;
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
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NetIncome {
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

    }
}
