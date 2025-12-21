package com.example.stocks.Service;

import com.example.stocks.Calculate.CalculateData;
import com.example.stocks.Record.PortfolioSummary;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Service
public class PortfolioSummaryService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private CalculateData calculateData;


    public PortfolioSummary getPortfolioSummary2(Long portfolioID) {

        Object[] raw = stockRepository.getPortfolioTotals(portfolioID);

        Object[] data = (Object[]) raw[0];

        double totalInvested = data[0] != null ? ((Number) data[0]).doubleValue() : 0.0;
        double totalDividend = data[1] != null ? ((Number) data[1]).doubleValue() : 0.0;
        double returns = data[2] != null ? ((Number) data[2]).doubleValue() : 0.0;
        double value = data[3] != null ? ((Number) data[3]).doubleValue() : 0.0;
        double daysChange = data[4] != null ? ((Number) data[4]).doubleValue() : 0.0;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        double totalValue = Double.parseDouble(df.format(totalInvested + returns));
        double dayChangeDollars = Double.parseDouble(df.format(value - daysChange));

        double totalProfit = Double.parseDouble(df.format(value - totalInvested));
        double profitPercentage = totalInvested == 0 ? 0 :
                Double.parseDouble(df.format(calculateData.percentage(totalProfit, totalInvested)));

        double dayChangePercentage = value == 0 ? 0 :
                Double.parseDouble(df.format(calculateData.percentage(dayChangeDollars, value)));

        return new PortfolioSummary(
                totalInvested,
                totalValue,
                totalProfit,
                profitPercentage,
                totalDividend,
                dayChangeDollars,
                dayChangePercentage
        );
    }
}
