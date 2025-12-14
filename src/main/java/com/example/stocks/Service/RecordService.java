package com.example.stocks.Service;

import com.example.stocks.Calculate.CalculateData;
import com.example.stocks.DTO.*;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.DividendHistory;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Record.*;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class RecordService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private CalculateData calculateData;

    @Autowired
    private Endpoints endpoints;

    @Autowired
    private API_Service APIService;


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

    public SearchField getSearchField(String ticker) {

        endpoints.setPriceAPI(ticker);
        endpoints.setBasicTickerInfo(ticker);

        CompletableFuture<PriceDTO> priceFuture = CompletableFuture.supplyAsync(APIService::getPriceData);
        CompletableFuture<BasicStockDataDTO> basicsFuture = CompletableFuture.supplyAsync(APIService::getBasicData);

        PriceDTO priceData = priceFuture.join();
        BasicStockDataDTO basicData = basicsFuture.join();

        String companyTicker = ticker;
        String companyName = basicData.getResults().get(0).getName();

        Optional<Long> idOfStock = watchlistRepository.findIdByStockTickerInn(ticker.toUpperCase());

        boolean insideWatchlist = idOfStock.isPresent();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        double currentPrice = priceData.getResults().get(0).getClosePrice();
        double openingPrice = priceData.getResults().get(0).getOpenPrice();
        double daysChangeDollars = Double.parseDouble(df.format(currentPrice - openingPrice));
        double daysChangePercentage = Double.parseDouble(df.format(calculateData.percentage(daysChangeDollars, openingPrice)));

        return new SearchField(
                companyTicker,
                companyName,
                currentPrice,
                daysChangeDollars,
                daysChangePercentage,
                insideWatchlist,
                idOfStock
        );
    }

    public SearchSummary getSummary(String ticker) {

        endpoints.setPriceOverTime(ticker);
        endpoints.setWatchListAPI(ticker);

        CompletableFuture<PriceOverTimeDTO> priceFuture = CompletableFuture.supplyAsync(APIService::getPriceOverTimeData);
        CompletableFuture<TickerOverviewDTO> basicsFuture = CompletableFuture.supplyAsync(APIService::getTickerOverviewlData);

        PriceOverTimeDTO priceData = priceFuture.join();
        TickerOverviewDTO basicData = basicsFuture.join();

        List<Double> prices = priceData.getResults()
                .stream()
                .map(PriceOverTimeDTO.Results::getClosePrice)
                .toList();

        String description = basicData.getResults().getDescription();
        double eps = 0.0;

        return new SearchSummary(
                prices,
                eps,
                description
        );
    }


    public DividendSearchSummary getDividendSummary(String ticker) {
        endpoints.setDividendAPI(ticker, 730);
        DividendDTO dividendData = APIService.getDividendData();

        if (dividendData.getResults() == null || dividendData.getResults().isEmpty()) {
            return new DividendSearchSummary(0, null, null, 0.0, 0.0, List.of());
        }

        var latestData = dividendData.getResults().get(0);

        int frequenzy = latestData.getFrequency();
        double income = latestData.getCash_amount();
        double annualIncome = latestData.getCash_amount() * frequenzy;
        String payDate = latestData.getPayDate();
        String exDate = latestData.getExDate();

        List<DividendHistory> dividendHistory = new ArrayList<>();
        List<DividendDTO.Results> call = dividendData.getResults();

        for (int i = 0; i < call.size(); i++) {
            var currentData = call.get(i);

            Double change = null;
            if (i + 1 < call.size()) {
                double prevDividend = call.get(i + 1).getCash_amount();
                if (prevDividend != 0.0) {
                    change = ((currentData.getCash_amount() - prevDividend) / prevDividend) * 100;
                }
            }

            dividendHistory.add(new DividendHistory(
                    currentData.getDecDate(),
                    currentData.getExDate(),
                    currentData.getRecordDate(),
                    currentData.getPayDate(),
                    currentData.getFrequency(),
                    currentData.getCash_amount(),
                    change
            ));
        }
        return new DividendSearchSummary(
                frequenzy,
                payDate,
                exDate,
                annualIncome,
                income,
                dividendHistory
        );
    }


        public Dividends dividendData () {

            List<Stocks> stockData = stockRepository.findAll();

            double fullDividend = 0.0;
            double fullInvestemnt = 0.0;

            for (Stocks stock : stockData) {
                fullDividend += stock.getTotalDivided();
                fullInvestemnt += stock.getTotalInvested();
            }

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat df = new DecimalFormat("#.##", symbols);

            double monthlyDividend = Double.parseDouble(df.format(fullDividend / 12));
            double dailyDividend = Double.parseDouble(df.format(fullDividend / 365));
            double hourlyDividend = Double.parseDouble(df.format(fullDividend / 8765));
            double yieldOnCost = Double.parseDouble(df.format((fullDividend / fullInvestemnt) * 100));

            return new Dividends(
                    fullDividend,
                    monthlyDividend,
                    dailyDividend,
                    hourlyDividend,
                    yieldOnCost
            );
        }
    }

