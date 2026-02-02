package com.example.stocks.Record;

import com.example.stocks.Model.DividendCalender;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record Dividends(
        @JsonProperty("Annual_Dividend") BigDecimal annualDividend,
        @JsonProperty("Monthly_Dividend") BigDecimal monthlyDividend,
        @JsonProperty("Days_Dividend") BigDecimal dailyDividend,
        @JsonProperty("Hourly_Dividend") BigDecimal hourlyDividend,
        @JsonProperty("Yield_On_Cost") BigDecimal yieldOnCost,
        @JsonProperty("Dividend_Payments")List<DividendCalender> dividendCalenders
) {}
