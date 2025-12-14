package com.example.stocks.Record;

public record PortfolioTotals(
        Double totalInvested,
        Double totalDivided,
        Double returns,
        Double totalValue,
        Double dailyChange
) {}
