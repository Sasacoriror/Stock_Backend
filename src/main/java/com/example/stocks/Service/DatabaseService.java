package com.example.stocks.Service;

import com.example.stocks.Calculate.CalculateData;
import com.example.stocks.DTO.*;
import com.example.stocks.Link.Endpoints;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class DatabaseService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private API_Service APIService;

    @Autowired
    private Endpoints endpoints;

    @Autowired
    private CalculateData calculateData;

    @Transactional
    public void updateStockData() {
        List<Stocks> stocks = stockRepository.findAll();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        for (Stocks stock : stocks) {

            PriceDTO priceData = APIService.getPriceData();
            DividendDTO dividendData = APIService.getDividendData();
            FinancialDTO financialData = APIService.getFinancialData();

        }
    }

    @Transactional
    public void addToPortfolio(Stocks stocks) {

        Long stockId = stockRepository.insertStockNative(stocks.getStockName(), stocks.getStockPrice(), stocks.getStockQuantity(),  stocks.getPortfolio().getId());

        String stockName = stocks.getStockName().toUpperCase();
        endpoints.setPriceAPI(stockName);
        endpoints.setDividendAPI(stockName);
        endpoints.setBasicTickerInfo(stockName);

        CompletableFuture<PriceDTO> priceFuture = CompletableFuture.supplyAsync(APIService::getPriceData);
        CompletableFuture<DividendDTO> dividendFuture = CompletableFuture.supplyAsync(APIService::getDividendData);
        CompletableFuture<BasicStockDataDTO> basicsFuture = CompletableFuture.supplyAsync(APIService::getBasicData);

        PriceDTO priceData = priceFuture.join();
        DividendDTO dividendData = dividendFuture.join();
        BasicStockDataDTO basicData = basicsFuture.join();

        Stocks portfolio = stockRepository.findById(stockId).orElseThrow();
        String companyName = basicData.getResults().get(0).getName();
        double pricePaid = portfolio.getStockPrice();
        int shares = portfolio.getStockQuantity();
        double currentPrice = priceData.getResults().get(0).getC();
        double totalDividend = 0;
        int frequenzy = 0;

        if (dividendData.getResults() != null && !dividendData.getResults().isEmpty()) {
            double dividend = dividendData.getResults().get(0).getCash_amount();
            frequenzy = dividendData.getResults().get(0).getFrequency();
            totalDividend = calculateData.totalDividend(dividend, frequenzy, shares);
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        double totalValue = calculateData.totalValue(shares, currentPrice);
        double totalInvested = calculateData.totalInvested(shares, pricePaid);
        double returnValue = calculateData.returns(currentPrice, shares, pricePaid);
        double percentage = calculateData.percentage(returnValue, totalInvested);

        portfolio.setCompanyName(companyName);
        portfolio.setCurrentPrice(currentPrice);
        portfolio.setDividend(frequenzy);
        portfolio.setTotalDivided(totalDividend);
        portfolio.setTotalPrice(totalValue);
        portfolio.setTotalInvested(Double.parseDouble(df.format(totalInvested)));
        portfolio.setReturnValue(Double.parseDouble(df.format(returnValue)));
        portfolio.setPercentageReturn(Double.parseDouble(df.format(percentage)));

        stockRepository.save(portfolio);
    }

    @Transactional
    public void UpdateDatabase(Long id){
        Optional<Stocks> stocks = stockRepository.findById(id);

        Stocks stock = stocks.get();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        DividendDTO dividendData = APIService.getDividendData();

        int shares = stock.getStockQuantity();
        double price = stock.getCurrentPrice();
        double paidPrice = stock.getStockPrice();

        int dividendFrequenzy = 0;
        double dividend = 0.0;
        double newTotalDividend = 0.0;

        if (dividendData.getResults() != null && !dividendData.getResults().isEmpty()) {
            dividendFrequenzy = dividendData.getResults().get(0).getFrequency();
            dividend = dividendData.getResults().get(0).getCash_amount();
            newTotalDividend = calculateData.totalDividend(dividend, dividendFrequenzy, shares);
        }

        double newTotalPrice = calculateData.totalValue(shares, price);
        double newTotalInvested = calculateData.totalInvested(shares, paidPrice);
        double newReturnValue = calculateData.returns(price, shares, paidPrice);
        double newPercentage = calculateData.percentage(newReturnValue, newTotalInvested);

        stock.setTotalDivided(newTotalDividend);
        stock.setTotalPrice(newTotalPrice);
        stock.setTotalInvested(Double.parseDouble(df.format(newTotalInvested)));
        stock.setReturnValue(Double.parseDouble(df.format(newReturnValue)));
        stock.setPercentageReturn(Double.parseDouble(df.format(newPercentage)));

        stockRepository.save(stock);
    }

    @Transactional
    public void addToWatchist(Long id, String stockName){

        //endpoints.setPriceAPI(stockName);
        endpoints.setDividendAPI(stockName);
        endpoints.setFinancialAPI(stockName, 1);
        endpoints.setWatchListAPI(stockName);

        Optional<Watchlist> watchlist = watchlistRepository.findById(id);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        TickerOverviewDTO tickerOverview = APIService.getTickerOverviewlData();
        DividendDTO dividendData = APIService.getDividendData();
        FinancialDTO financialData = APIService.getFinancialData();

        String name = financialData.getResults().get(0).getCompanyName();
        double pricePerShare = calculateData.pricePerShare(
                tickerOverview.getResults().getMarketCap(),
                tickerOverview.getResults().getWso());
        double dividendYield = calculateData.dividendYield(
                dividendData.getResults().get(0).getCash_amount(),
                dividendData.getResults().get(0).getFrequency(),
                pricePerShare);

        //watchlist.get().setStockTickerInn(stockName);
        watchlist.get().setNameStock(name);
        watchlist.get().setPriceStock(pricePerShare);
        watchlist.get().setMarketCap(tickerOverview.getResults().getMarketCap());
        watchlist.get().setDividendYield(Double.parseDouble(df.format(dividendYield)));
    }
/*
    public void oneMinutte() {
        try {
            Thread.sleep(61000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("oneMinute function was interrupted");
        }
    }
 */
}
