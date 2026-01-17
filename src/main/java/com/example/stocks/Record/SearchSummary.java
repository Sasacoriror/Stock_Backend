package com.example.stocks.Record;

import com.example.stocks.Model.Price;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SearchSummary(
        //@JsonProperty("Prices") List<Price> closingPrice,
        @JsonProperty("Exchange") String exchange,
        @JsonProperty("Earnings_Per_Share") Double EPS,
        @JsonProperty("Description_Of_Company") String description
) {}
