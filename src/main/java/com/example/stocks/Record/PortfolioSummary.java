package com.example.stocks.Record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PortfolioSummary(
        @JsonProperty("total_Invested") double totalInvested,
        @JsonProperty("Total_Value") double totalValue,
        @JsonProperty("unrealised_Gain_Loss") double gain_Loss,
        @JsonProperty("percentage_Gain_Loss") double percentageGainLoss,
        @JsonProperty("yearly_Dividend_Income") double dividendIncome,
        @JsonProperty("daily_Change") double dailyChange,
        @JsonProperty("daily_Change_Percentage") double dailyChangePercentage
) {}
