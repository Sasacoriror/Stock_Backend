package com.example.stocks.Record;

import com.example.stocks.Model.DividendHistory;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DividendSearchSummary(
        @JsonProperty("Frequenzy") String frequenzy,
        @JsonProperty("Latest_Dividend_Date") String latDivDate,
        @JsonProperty("Ex_Dividend_Date") String exDivDate,
        @JsonProperty("Annual_Income") Double annualIncome,
        @JsonProperty("Quarterly_income") Double quarterlyIncome,
        @JsonProperty("Dividend_Growth_1_year") Double dividend1Year,
        @JsonProperty("Dividend_Growth_3_year") Double dividend2Year,
        @JsonProperty("Dividend_Growth_5_year") Double dividend5Year,
        @JsonProperty("Dividend_Growth_10_year") Double dividend10Year
) {}
