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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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

    @Autowired
    private CacheService cacheService;

    @Transactional
    public void updatePortfolioData() {

        int i = 0;

        List<Stocks> stocks = stockRepository.findAll();

        for (Stocks stock : stocks) {

            if (i == 2) {
                oneMinutte();
                i = 0;
            }

            String stockName = stock.getStockName();

            endpoints.setPriceAPI(stockName);
            endpoints.setDividendAPI(stockName, 1);
            endpoints.setBasicTickerInfo(stockName);

            CompletableFuture<PriceDTO> priceFuture = CompletableFuture.supplyAsync(APIService::getPriceData);
            CompletableFuture<DividendDTO> dividendFuture = CompletableFuture.supplyAsync(APIService::getDividendData);

            try {
                PriceDTO priceData = priceFuture.join();
                DividendDTO dividendData = dividendFuture.join();

                applyPortfolioData(stock, priceData, dividendData);
                stockRepository.save(stock);
            } catch (CompletionException e){
                System.out.println("Failed to update stocks: "+e);
            }
            i++;
        }
    }


    public void applyPortfolioData(Stocks stock, PriceDTO priceData, DividendDTO dividendData) {

            double pricePaid = stock.getStockPrice();
            int shares = stock.getStockQuantity();
            double currentPrice = priceData.getResults().get(0).getClosePrice();
            double opening_Price= priceData.getResults().get(0).getOpenPrice();
            double totalDividend = 0.0;
            int frequenzy = 0;
            double drip = 0.0;

            if (dividendData.getResults() != null && !dividendData.getResults().isEmpty()) {
                double dividend = dividendData.getResults().get(0).getCash_amount();
                frequenzy = dividendData.getResults().get(0).getFrequency();
                totalDividend = calculateData.totalDividend(dividend, frequenzy, shares);
                drip = totalDividend / currentPrice;
            }

            double totalValue = calculateData.totalValue(shares, currentPrice);
            double totalInvested = calculateData.totalInvested(shares, pricePaid);
            double returnValue = calculateData.returns(currentPrice, shares, pricePaid);
            double percentage = calculateData.percentage(returnValue, totalInvested);
            double dayChangeDollars = calculateData.calculateDaysChange(totalValue, (opening_Price * shares));
            double dayChangePercentage = calculateData.percentage(dayChangeDollars, totalValue);

            stock.setCurrentPrice(currentPrice);
            stock.setDividend(frequenzy);
            stock.setTotalDivided(totalDividend);
            stock.setDrip(drip);
            stock.setTotalPrice(totalValue);
            stock.setTotalInvested(calculateData.roundNumbers(totalInvested));
            stock.setReturnValue(calculateData.roundNumbers(returnValue));
            stock.setPercentageReturn(calculateData.roundNumbers(percentage));
            stock.setOpeningPrice(opening_Price);
            stock.setTodaysReturnDollars(dayChangeDollars);
            stock.setTodaysReturnPercentage(dayChangePercentage);


    }

    @Transactional
    public void addToPortfolio(Stocks stocks, Long id) {

        cacheService.clearStocksPortfolio(id);
        cacheService.clearPortfolioCache(id);

        Long stockId = stockRepository.insertStockNative(stocks.getStockName(), stocks.getStockPrice(), stocks.getStockQuantity(),  stocks.getPortfolio().getId());

        String stockName = stocks.getStockName().toUpperCase();
        endpoints.setPriceAPI(stockName);
        endpoints.setDividendAPI(stockName, 1);
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
        double currentPrice = priceData.getResults().get(0).getClosePrice();
        double opening_Price= priceData.getResults().get(0).getOpenPrice();
        double totalDividend = 0.0;
        int frequenzy = 0;
        double drip = 0.0;

        if (dividendData.getResults() != null && !dividendData.getResults().isEmpty()) {
            double dividend = dividendData.getResults().get(0).getCash_amount();
            frequenzy = dividendData.getResults().get(0).getFrequency();
            totalDividend = calculateData.totalDividend(dividend, frequenzy, shares);
            drip = totalDividend / currentPrice;
        }

        double totalValue = calculateData.totalValue(shares, currentPrice);
        double totalInvested = calculateData.totalInvested(shares, pricePaid);
        double returnValue = calculateData.returns(currentPrice, shares, pricePaid);
        double percentage = calculateData.percentage(returnValue, totalInvested);
        double dayChangeDollars = calculateData.calculateDaysChange(totalValue, (opening_Price * shares));
        double dayChangePercentage = calculateData.percentage(dayChangeDollars, totalValue);

        portfolio.setCompanyName(companyName);
        portfolio.setCurrentPrice(currentPrice);
        portfolio.setDividend(frequenzy);
        portfolio.setTotalDivided(totalDividend);
        portfolio.setDrip(drip);
        portfolio.setTotalPrice(totalValue);
        portfolio.setTotalInvested(calculateData.roundNumbers(totalInvested));
        portfolio.setReturnValue(calculateData.roundNumbers(returnValue));
        portfolio.setPercentageReturn(calculateData.roundNumbers(percentage));
        portfolio.setOpeningPrice(opening_Price);
        portfolio.setTodaysReturnDollars(dayChangeDollars);
        portfolio.setTodaysReturnPercentage(dayChangePercentage);

        stockRepository.save(portfolio);
    }

    @Transactional
    public void updateStockData(Long id, Long IDs, Map<String, Integer> data){

        Stocks stock = stockRepository.findById(id).orElseThrow(() -> new RuntimeException("Stock Not found"));

        if (data.containsKey("sharesInn") && data.containsKey("priceInn")){
            stock.setStockQuantity(data.get("sharesInn"));
            stock.setStockPrice(data.get("priceInn"));
        }

        UpdateDatabase(stock);
        cacheService.clearPortfolioCache(IDs);
        cacheService.clearStocksPortfolio(IDs);
    }

    public void UpdateDatabase(Stocks stock){

        DividendDTO dividendData = APIService.getDividendData();

        int shares = stock.getStockQuantity();
        double price = stock.getCurrentPrice();
        double paidPrice = stock.getStockPrice();
        double opening_Price = stock.getOpeningPrice();
        double totalValue = stock.getTotalPrice();

        int dividendFrequenzy = 0;
        double dividend = 0.0;
        double newTotalDividend = 0.0;
        double drip = 0.0;

        if (dividendData.getResults() != null && !dividendData.getResults().isEmpty()) {
            dividendFrequenzy = dividendData.getResults().get(0).getFrequency();
            dividend = dividendData.getResults().get(0).getCash_amount();
            newTotalDividend = calculateData.totalDividend(dividend, dividendFrequenzy, shares);
            drip = newTotalDividend / opening_Price;
        }

        double newTotalPrice = calculateData.totalValue(shares, price);
        double newTotalInvested = calculateData.totalInvested(shares, paidPrice);
        double newReturnValue = calculateData.returns(price, shares, paidPrice);
        double newPercentage = calculateData.percentage(newReturnValue, newTotalInvested);
        double dayChangeDollars = calculateData.calculateDaysChange(newTotalPrice, (opening_Price * shares));
        double dayChangePercentage = calculateData.percentage(dayChangeDollars, totalValue);

        stock.setTotalDivided(newTotalDividend);
        stock.setDrip(drip);
        stock.setTotalPrice(newTotalPrice);
        stock.setTotalInvested(calculateData.roundNumbers(newTotalInvested));
        stock.setReturnValue(calculateData.roundNumbers(newReturnValue));
        stock.setPercentageReturn(calculateData.roundNumbers(newPercentage));
        stock.setTodaysReturnDollars(calculateData.roundNumbers(dayChangeDollars));
        stock.setTodaysReturnPercentage(calculateData.roundNumbers(dayChangePercentage));
    }

    @Transactional
    public void addToWatchist(Long id, String stockName){

        endpoints.setPriceAPI(stockName);
        endpoints.setDividendAPI(stockName, 1);
        endpoints.setFinancialAPI(stockName, 1);
        endpoints.setWatchListAPI(stockName);
        endpoints.setWeekRange(stockName);

        Optional<Watchlist> watchlist = watchlistRepository.findById(id);

        CompletableFuture<PriceDTO> priceFuture = CompletableFuture.supplyAsync(APIService::getPriceData);
        CompletableFuture<TickerOverviewDTO> tickerOverviewFuture = CompletableFuture.supplyAsync(APIService::getTickerOverviewlData);
        CompletableFuture<DividendDTO> dividendFuture = CompletableFuture.supplyAsync(APIService::getDividendData);
        CompletableFuture<FinancialDTO> financialFuture = CompletableFuture.supplyAsync(APIService::getFinancialData);
        CompletableFuture<WeekRangeDTO> rangeFuture = CompletableFuture.supplyAsync(APIService::getWeeksRangeData);

        PriceDTO priceData = priceFuture.join();
        TickerOverviewDTO tickerData = tickerOverviewFuture.join();
        DividendDTO dividendData = dividendFuture.join();
        FinancialDTO financialData = financialFuture.join();
        WeekRangeDTO rangeData = rangeFuture.join();


        String name = financialData.getResults().get(0).getCompanyName();
        double pricePerShare = calculateData.pricePerShare(
                tickerData.getResults().getMarketCap(),
                tickerData.getResults().getWso());
        double dividendYield = calculateData.dividendYield(
                dividendData.getResults().get(0).getCash_amount(),
                dividendData.getResults().get(0).getFrequency(),
                pricePerShare);

        double openingPrice = priceData.getResults().get(0).getOpenPrice();

        double priceChange = calculateData.roundNumbers(pricePerShare - openingPrice);
        double priceChangePercentage = calculateData.roundNumbers(calculateData.percentage(priceChange, openingPrice));

        double EPS = calculateData.roundNumbers(
                financialData.getResults().get(0).getFinancials().getIncomeStatement().getNetIncome().getValue()
                        / tickerData.getResults().getWso());
        double PE_Ratio = calculateData.roundNumbers(priceData.getResults().get(0).getClosePrice() / EPS);

        double highestPrice = rangeData.getResults()
                .stream()
                .mapToDouble(WeekRangeDTO.Results::getHigh)
                .max()
                .orElse(Double.NaN);
        double lowestPrice = rangeData.getResults()
                .stream()
                .mapToDouble(WeekRangeDTO.Results::getLow)
                .min()
                .orElse(Double.NaN);

        watchlist.get().setNameStock(name);
        watchlist.get().setPriceStock(pricePerShare);
        watchlist.get().setChangePrice(priceChange);
        watchlist.get().setChangePercentage(priceChangePercentage);
        watchlist.get().setWeeksLow(lowestPrice);
        watchlist.get().setWeeksHigh(highestPrice);
        watchlist.get().setDividendYield(calculateData.roundNumbers(dividendYield));
        watchlist.get().setPERatio(PE_Ratio); // TODO: shows the wrong PE Ratio
        watchlist.get().setMarketCap(tickerData.getResults().getMarketCap());
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
