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
        CompletableFuture<metricsAndTargetsDTO> metricsTargetFuture = marketDataService.fetchMetricsAndTargets(ticker);
        CompletableFuture.allOf(priceFuture, basicsFuture, metricsTargetFuture).join();

        PriceDTO priceData = priceFuture.join();
        BasicStockDataDTO basicData = basicsFuture.join();
        metricsAndTargetsDTO metricsTargetData = metricsTargetFuture.join();

        String companyTicker = ticker;
        String companyName = basicData.getResults().get(0).getName();

        Optional<Long> idOfStock = watchlistRepository.findIdByStockTickerInn(ticker.toUpperCase());

        boolean insideWatchlist = idOfStock.isPresent();

        double currentPrice = metricsTargetData.getTargets().getCurrentPrice();
        double closingPrice = priceData.getResults().get(0).getClosePrice();
        double daysChangeDollars = calculateData.roundNumbers(currentPrice - closingPrice);
        double daysChangePercentage = calculateData.roundNumbers(calculateData.percentage(daysChangeDollars, closingPrice));


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
        CompletableFuture<TickerOverviewDTO> basicsFuture = marketDataService.fetchTickerData(ticker);
        CompletableFuture<metricsAndTargetsDTO> metricsTargetFuture = marketDataService.fetchMetricsAndTargets(ticker);
        CompletableFuture.allOf(metricsTargetFuture, basicsFuture).join();

        TickerOverviewDTO basicData = basicsFuture.join();
        metricsAndTargetsDTO metricsTargetData = metricsTargetFuture.join();

        String exchangeCode = basicData.getResults().getExchange();

        String exchange = switch (exchangeCode) {
            case "XNAS" -> "NASDAQ";
            case "XNYS" -> "NYSE";
            default -> "NASDAQ";
        }+":"+ticker;

        String description = basicData.getResults().getDescription();

        double trailing_eps = metricsTargetData.getMetrics().getT_eps();
        double forward_Eps = metricsTargetData.getMetrics().getF_eps();
        double peRatio = metricsTargetData.getMetrics().getPe_ratio();
        double forward_Pe = metricsTargetData.getMetrics().getF_pe();
        double beta = metricsTargetData.getMetrics().getBeta();
        double marketCap = metricsTargetData.getMetrics().getMarketCap();

        double price = metricsTargetData.getTargets().getCurrentPrice();
        double targetMean = metricsTargetData.getTargets().getTargetMean();
        double targetLow = metricsTargetData.getTargets().getTargetLow();
        double targetHigh = metricsTargetData.getTargets().getTargetHigh();
        int numberOfAnalyst = metricsTargetData.getTargets().getAnalystCount();
        double recommendationMean = metricsTargetData.getTargets().getRecommendationMean();
        String recommendationKey = metricsTargetData.getTargets().getRecomendationKey();

        return new SearchSummary(
                exchange,
                trailing_eps,
                calculateData.roundNumbers(forward_Eps),
                calculateData.roundNumbers(peRatio),
                calculateData.roundNumbers(forward_Pe),
                calculateData.roundNumbers(beta),
                marketCap,
                description,
                price,
                targetMean,
                targetLow,
                targetHigh,
                numberOfAnalyst,
                calculateData.roundNumbers(recommendationMean),
                recommendationKey
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

