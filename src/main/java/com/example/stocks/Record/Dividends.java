package com.example.stocks.Record;

import com.example.stocks.Model.DividendCalender;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Dividends(
        @JsonProperty("Annual_Dividend") double annualDividend,
        @JsonProperty("Monthly_Dividend") double monthlyDividend,
        @JsonProperty("Days_Dividend") double dailyDividend,
        @JsonProperty("Hourly_Dividend") double hourlyDividend,
        @JsonProperty("Yield_On_Cost") double yieldOnCost,
        @JsonProperty("Dividend_Payments")List<DividendCalender> dividendCalenders
) {}
