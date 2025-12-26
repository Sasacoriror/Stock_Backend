package com.example.stocks.Service;

import com.example.stocks.Calculate.CalculateData;
import com.example.stocks.DTO.*;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.DividendHistory;
import com.example.stocks.Model.Price;
import com.example.stocks.Record.*;
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
public class RecordSearchService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private CalculateData calculateData;

    @Autowired
    private Endpoints endpoints;

    @Autowired
    private API_Service APIService;

    @Autowired
    private MarketDataService marketDataService;


    public SearchField getSearchField(String ticker) {

        endpoints.setPriceAPI(ticker);
        endpoints.setBasicTickerInfo(ticker);

        CompletableFuture<PriceDTO> priceFuture = marketDataService.fetchPrice(ticker);
        CompletableFuture<BasicStockDataDTO> basicsFuture = marketDataService.fetchBasics(ticker);
        CompletableFuture.allOf(priceFuture, basicsFuture).join();

        PriceDTO priceData = priceFuture.join();
        BasicStockDataDTO basicData = basicsFuture.join();

        String companyTicker = ticker;
        String companyName = basicData.getResults().get(0).getName();

        Optional<Long> idOfStock = watchlistRepository.findIdByStockTickerInn(ticker.toUpperCase());

        boolean insideWatchlist = idOfStock.isPresent();

        double currentPrice = priceData.getResults().get(0).getClosePrice();
        double openingPrice = priceData.getResults().get(0).getOpenPrice();
        double daysChangeDollars = calculateData.roundNumbers(currentPrice - openingPrice);
        double daysChangePercentage = calculateData.roundNumbers(calculateData.percentage(daysChangeDollars, openingPrice));

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

        CompletableFuture<PriceOverTimeDTO> priceFuture = marketDataService.fetchPriceOverTime(ticker);
        CompletableFuture<TickerOverviewDTO> basicsFuture = marketDataService.fetchTickerData(ticker);
        CompletableFuture.allOf(priceFuture, basicsFuture).join();

        PriceOverTimeDTO priceData = priceFuture.join();
        TickerOverviewDTO basicData = basicsFuture.join();

        List<PriceOverTimeDTO.Results> results = priceData.getResults();
        List<Price> prices = new ArrayList<>();

        for (int i = 0; i < results.size(); i++){
            var currentData = results.get(i);

            prices.add(new Price(
                    currentData.getClosePrice()
            ));
        }

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

        DividendDTO dividendData = APIService.getDividendData(ticker, 730, false);

        if (dividendData.getResults() == null || dividendData.getResults().isEmpty() || dividendData.getResults().size() == 0) {
            return new DividendSearchSummary("", "", "", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        }

        var latestData = dividendData.getResults().get(0);
        var laterData = dividendData.getResults();

        int frequenzy = latestData.getFrequency();
        String frequencyWord = "";

        if (frequenzy == 1){
            frequencyWord = "Annual";
        } else if (frequenzy == 2) {
            frequencyWord = "Semi-Annual";
        } else if (frequenzy == 4) {
            frequencyWord = "Quarterly";
        } else if (frequenzy == 12) {
            frequencyWord = "Monthly";
        }


        double income = latestData.getCash_amount();
        double annualIncome = latestData.getCash_amount() * frequenzy;
        String payDate = latestData.getPayDate();
        String exDate = latestData.getExDate();

        double dividendCagrOneYear = laterData != null && laterData.size() > frequenzy ? Math.pow(latestData.getCash_amount() / laterData.get(frequenzy).getCash_amount(), 1.0 / 1) - 1.0 : 0.0;
        double dividendCagrTwoYear = laterData != null && laterData.size() > frequenzy * 3 ? Math.pow(latestData.getCash_amount() / laterData.get(frequenzy*3).getCash_amount(), 1.0 / 3) - 1.0 : 0.0;
        double dividendCagrFiveYear = laterData != null && laterData.size() > frequenzy * 5 ? Math.pow(latestData.getCash_amount() / laterData.get(frequenzy*5).getCash_amount(), 1.0 / 5) - 1.0 : 0.0;
        double dividendCagrTenYear = laterData != null && laterData.size() > frequenzy * 10 ? Math.pow(latestData.getCash_amount() / laterData.get(frequenzy*10).getCash_amount(), 1.0 / 10) - 1.0 : 0.0;

        return new DividendSearchSummary(
                frequencyWord,
                payDate,
                exDate,
                annualIncome,
                income,
                dividendCagrOneYear * 100,
                dividendCagrTwoYear * 100,
                dividendCagrFiveYear* 100,
                dividendCagrTenYear * 100
        );
    }

    public PageResponse<DividendHistory> getDividendHistory(int page, int size){
        DividendDTO dividendData = APIService.getDividendData("", 730, false);


        List<DividendDTO.Results> call = dividendData.getResults();
        int total = call.size();

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, total);

        if (fromIndex >= total){
            return new PageResponse<>(List.of(), page, size, total, 0);
        }

        List<DividendHistory> dividendHistory = new ArrayList<>();

        for (int i = fromIndex; i < toIndex; i++) {
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

        int totalPages = (int) Math.ceil((double) total / size);

        return new PageResponse<>(
                dividendHistory,
                page,
                size,
                total,
                totalPages
        );
    }
}

