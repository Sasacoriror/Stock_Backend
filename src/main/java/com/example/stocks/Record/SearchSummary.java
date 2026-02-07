package com.example.stocks.Record;

import com.example.stocks.Model.Price;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SearchSummary(
        @JsonProperty("Exchange") String exchange,
        @JsonProperty("Trailing_EPS") Double T_EPS,
        @JsonProperty("Forward_EPS") Double F_EPS,
        @JsonProperty("PE_Ratio") Double peRatio,
        @JsonProperty("Forward_Pe") Double Forward_Pe,
        @JsonProperty("Beta") Double Beta,
        @JsonProperty("Market_Cap") Double Market_Cap,
        @JsonProperty("Description_Of_Company") String description,
        @JsonProperty("current_price") Double currentPrice,
        @JsonProperty("target_mean") Double targetMean,
        @JsonProperty("target_low") Double targetLow,
        @JsonProperty("target_high") Double targetHigh,
        @JsonProperty("analyst_count") int analystCount,
        @JsonProperty("recommendation_mean") Double recommendationMean,
        @JsonProperty("recommendation_key") String recomendationKey,
        @JsonProperty("percentage_Gain") Double percentageGain,
        @JsonProperty("one_Day") Double oneDay,
        @JsonProperty("five_day") Double fiveDay,
        @JsonProperty("one_Month") Double oneMonth,
        @JsonProperty("six_Month") Double sixMonth,
        @JsonProperty("year_To_Date") Double YTD,
        @JsonProperty("one_Year") Double oneYear,
        @JsonProperty("three_Year") Double threeYear,
        @JsonProperty("five_Year") Double fiveYear,
        @JsonProperty("ten_Year") Double tenYears
) {}
