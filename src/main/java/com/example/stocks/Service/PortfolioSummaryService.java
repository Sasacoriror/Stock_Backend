package com.example.stocks.Service;

import com.example.stocks.Calculate.CalculateData;
import com.example.stocks.Model.Portfolio;
import com.example.stocks.Model.PortfolioStockView;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Record.PageResponse;
import com.example.stocks.Record.PortfolioSummary;
import com.example.stocks.Record.PortfolioView;
import com.example.stocks.Respository.PortfolioRepository;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PortfolioSummaryService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

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

    public PortfolioView<PortfolioStockView> getPortfolio(Long id, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        Page<Stocks> stocks = stockRepository.findByPortfolioId(id, pageable);

        Long total = stocks.getTotalElements();

        int fromIndex = page * size;

        if (fromIndex >= total){
            return new PortfolioView<>(List.of(), page, size, total, 0);
        }

        List<PortfolioStockView> portfolio = new ArrayList<>();

        for (Stocks stock: stocks.getContent()) {

            double price_Paid = stock.getStockPrice();
            int shares = stock.getStockQuantity();
            double openingPrice = stock.getOpeningPrice();

            double currentPrice = stock.getCurrentPrice();
            double drip = stock.getTotalDivided() / openingPrice;
            double totalValue = calculateData.totalValue(shares, currentPrice);
            double totalInvested = calculateData.totalInvested(shares, price_Paid);
            double returnValue = calculateData.returns(currentPrice, shares, price_Paid);
            double percent = calculateData.percentage(returnValue, totalInvested);
            double todaysReturn = calculateData.calculateDaysChange(totalValue, (openingPrice * shares));
            double todaysReturnPercentage = calculateData.percentage(todaysReturn, totalValue);

            portfolio.add(new PortfolioStockView(
                    stock.getId(),
                    stock.getStockName(),
                    price_Paid,
                    shares,
                    stock.getCompanyName(),
                    stock.getCurrentPrice(),
                    stock.getDividend(),
                    calculateData.roundNumbers(drip),
                    totalValue,
                    stock.getTotalDivided(),
                    totalInvested,
                    returnValue,
                    calculateData.roundNumbers(percent),
                    calculateData.roundNumbers(todaysReturn),
                    calculateData.roundNumbers(todaysReturnPercentage)
            ));
        }

        int totalPages = stocks.getTotalPages();

        return  new PortfolioView<>(
                portfolio,
                page,
                size,
                total,
                totalPages
        );
    }
}
