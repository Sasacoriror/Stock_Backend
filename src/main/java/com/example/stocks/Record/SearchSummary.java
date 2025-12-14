package com.example.stocks.Record;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SearchSummary(
        @JsonProperty("Prices") List<Double> closingPrice,
        @JsonProperty("Earnings_Per_Share") Double EPS,
        @JsonProperty("Description_Of_Company") String description
) {}
