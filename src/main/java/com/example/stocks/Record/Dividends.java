package com.example.stocks.Record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Dividends(
        @JsonProperty("Annual_Dividend") double annualDividend,
        @JsonProperty("Monthly_Dividend") double monthlyDividend,
        @JsonProperty("days_Dividend") double dailyDividend,
        @JsonProperty("Hourly_Dividend") double hourlyDividend,
        @JsonProperty("Yield_On_Cost") double yieldOnCost
) {
}
