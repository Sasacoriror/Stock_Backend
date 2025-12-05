package com.example.stocks.Record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PortfolioSummary(
        @JsonProperty("total_Invested") Double totalInvested,
        @JsonProperty("Total_Value") Double totalValue,
        @JsonProperty("unrealised_Gain_Loss") Double gain_Loss,
        @JsonProperty("percentage_Gain_Loss") Double percentageGainLoss,
        @JsonProperty("yearly_Dividend_Income") Double dividendIncome,
        @JsonProperty("daily_Change") Double dailyChange,
        @JsonProperty("daily_Change_Percentage") Double dailyChangePercentage
) {}
