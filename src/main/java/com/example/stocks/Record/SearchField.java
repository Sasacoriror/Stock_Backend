package com.example.stocks.Record;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public record SearchField(
        @JsonProperty("ticker") String ticker,
        @JsonProperty("company_Name") String companyName,
        @JsonProperty("current_Stock_Price") double currentPrice,
        @JsonProperty("days_Change_Dollars") double daysChangeDollars,
        @JsonProperty("days_Change_Percent") double days_Change_Percent,
        @JsonProperty("inside_Watchlist") boolean insideWatchlist,
        @JsonProperty("id_Of_Stock") Optional<Long> idOfStock
) {}
