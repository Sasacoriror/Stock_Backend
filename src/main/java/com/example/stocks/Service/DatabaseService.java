package com.example.stocks.Service;

import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.DTO.ResultsDividendDTO;
import com.example.stocks.DTO.ResultsFinancialDTO;
import com.example.stocks.DTO.TickerOverviewDTO;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Model.Watchlist;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class DatabaseService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private PriceService priceService;
/*
    @Transactional
    public void updateStockData() {
        List<Stocks> stocks = stockRepository.findAll();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        for (Stocks stock : stocks) {

            PriceDTO priceData = priceService.getPriceData();
            ResultsDividendDTO dividendData = priceService.getDividendData();
            ResultsFinancialDTO financialData = priceService.getFinancialData();

            Double newPrice = null;
            Double newDividend = null;

            String name = financialData.getResults().get(0).getCompanyName();
            stock.setCompanyName(name);

            if (priceData != null && priceData.getResults() != null && !priceData.getResults().isEmpty()) {
                newPrice = priceData.getResults().get(0).getC();
            }

            if (dividendData != null && dividendData.getResults() != null && !dividendData.getResults().isEmpty()) {
                newDividend = dividendData.getResults().get(0).getCash_amount();
            }

            boolean hasChanges = false;

            if (newPrice != null && !Objects.equals(newPrice, stock.getCurrentPrice())) {
                stock.setCurrentPrice(newPrice);
                stock.setTotalPrice(stock.getStockQuantity() * newPrice);
                hasChanges = true;
            }

            double totalInvested = stock.getStockQuantity() * stock.getStockPrice();
            double returns = (stock.getStockQuantity() * newPrice) - (stock.getStockQuantity() * stock.getStockPrice());
            double percentage = (returns / totalInvested) * 100;

            stock.setTotalInvested(totalInvested);
            stock.setReturnValue(Double.parseDouble(df.format(returns)));
            stock.setPercentageReturn(Double.parseDouble(df.format(percentage)));
            stock.setDividend(dividendData.getResults().get(0).getFrequency());

            if (newDividend != null && !Objects.equals(newDividend, stock.getDividend())) {
                //stock.setDividend(newDividend);
                stock.setTotalDivided(stock.getStockQuantity() * newDividend * dividendData.getResults().get(0).getFrequency());
                hasChanges = true;
            }

            if (hasChanges) {
                stockRepository.save(stock);
                UpdateDatabase(stock.getId());
            }
        }
    }
 */

    @Transactional
    public void addToPortfolio(Long id) {
        Optional<Stocks> portfolio = stockRepository.findById(id);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        PriceDTO priceData = priceService.getPriceData();
        ResultsDividendDTO dividendData = priceService.getDividendData();
        ResultsFinancialDTO financialData = priceService.getFinancialData();

        double price = portfolio.get().getStockPrice();
        int shares = portfolio.get().getStockQuantity();

        String companyName = financialData.getResults().get(0).getCompanyName();
        double currentPrice = priceData.getResults().get(0).getC();
        int frequenzy = dividendData.getResults().get(0).getFrequency();
        double totalDividend = (dividendData.getResults().get(0).getCash_amount() * frequenzy) * shares;
        double totalValue = shares * currentPrice;
        double totalInvested = price * shares;
        double returnValue = (currentPrice * shares) - (price * shares);

        double percentage = (returnValue / totalInvested) * 100;

        portfolio.get().setCompanyName(companyName);
        portfolio.get().setCurrentPrice(currentPrice);
        portfolio.get().setDividend(frequenzy);
        portfolio.get().setTotalDivided(totalDividend);
        portfolio.get().setTotalPrice(totalValue);
        portfolio.get().setTotalInvested(Double.parseDouble(df.format(totalInvested)));
        portfolio.get().setReturnValue(Double.parseDouble(df.format(returnValue)));
        portfolio.get().setPercentageReturn(Double.parseDouble(df.format(percentage)));
    }

    @Transactional
    public void UpdateDatabase(Long id){
        Optional<Stocks> stocks = stockRepository.findById(id);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        ResultsDividendDTO dividendData = priceService.getDividendData();

        double dividend = dividendData.getResults().get(0).getCash_amount();
        int shares = stocks.get().getStockQuantity();
        double price = stocks.get().getCurrentPrice();
        int paidPrice = stocks.get().getStockPrice();
        int dividendFrequenzy = dividendData.getResults().get(0).getFrequency();

        double newTotalDividend = (dividend * dividendFrequenzy) * shares;
        double newTotalPrice = (price * shares);
        double newTotalInvested = paidPrice * shares;
        double newReturnValue = (price * shares) - (paidPrice * shares);
        double newPercentage = (newReturnValue / newTotalInvested) * 100;

        stocks.get().setTotalDivided(newTotalDividend);
        stocks.get().setTotalPrice(newTotalPrice);
        stocks.get().setTotalInvested(Double.parseDouble(df.format(newTotalInvested)));
        stocks.get().setReturnValue(Double.parseDouble(df.format(newReturnValue)));
        stocks.get().setPercentageReturn(Double.parseDouble(df.format(newPercentage)));
    }

    @Transactional
    public void addToWatchist(Long id){
        Optional<Watchlist> watchlist = watchlistRepository.findById(id);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        TickerOverviewDTO tickerOverview = priceService.getTickerOverviewlData();
        ResultsDividendDTO dividendData = priceService.getDividendData();
        ResultsFinancialDTO financialData = priceService.getFinancialData();

        double pricePerShare = tickerOverview.getResults().getMarketCap() / tickerOverview.getResults().getWso();
        String name = financialData.getResults().get(0).getCompanyName();
        double totalDividend = dividendData.getResults().get(0).getCash_amount();
        int dividendFrequenzy = dividendData.getResults().get(0).getFrequency();

        double dividendYield = ((totalDividend * dividendFrequenzy) / pricePerShare) * 100;

        //watchlist.get().setStockTickerInn(stockName);
        watchlist.get().setNameStock(name);
        watchlist.get().setPriceStock(pricePerShare);
        watchlist.get().setMarketCap(tickerOverview.getResults().getMarketCap());
        watchlist.get().setDividendYield(Double.parseDouble(df.format(dividendYield)));
    }

    public void oneMinutte() {
        try {
            Thread.sleep(61000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("oneMinute function was interrupted");
        }

    }
}
