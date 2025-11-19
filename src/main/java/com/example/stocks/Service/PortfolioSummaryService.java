package com.example.stocks.Service;

import com.example.stocks.Calculate.CalculateData;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Record.PortfolioSummary;
import com.example.stocks.Respository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Service
public class PortfolioSummaryService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CalculateData calculateData;

    public PortfolioSummary getPortfolioSummary(Long portfolioID){

        List<Stocks> portfolios = stockRepository.findByPortfolioId(portfolioID);

        double totalInvested = 0.0;
        double totalDividend = 0.0;
        double returns = 0.0;
        double value = 0.0;

        double daysChange = 0.0;

        for (Stocks item: portfolios){
            totalInvested += item.getTotalInvested();
            totalDividend += item.getTotalDivided();
            returns += item.getReturnValue();
            value += item.getTotalPrice();

            daysChange = item.getOpeningPrice() * item.getStockQuantity();
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        double totalValue = Double.parseDouble(df.format(totalInvested + returns));
        double dayChangeDollars = Double.parseDouble(df.format(value - daysChange));

        double totalProfit = Double.parseDouble(df.format(value - totalInvested));
        double profitPercentage = Double.parseDouble(df.format(calculateData.percentage(totalProfit, totalInvested)));
        double dayChangePercentage = Double.parseDouble(df.format(calculateData.percentage(dayChangeDollars, value)));

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
