package com.example.stocks.Record;

import com.example.stocks.Model.DividendHistory;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DividendSearchSummary(
        @JsonProperty("Frequenzy") int frequenzy,
        @JsonProperty("Latest_Dividend_Date") String latDivDate,
        @JsonProperty("Ex_Dividend_Date") String exDivDate,
        @JsonProperty("Annual_Income") Double annualIncome,
        @JsonProperty("Quarterly_income") Double quarterlyIncome,
        @JsonProperty("Dividend_History")List<DividendHistory> dividendHistory
) {}
