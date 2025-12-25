package com.example.stocks.Service;

import com.example.stocks.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MarketDataService {

    @Autowired
    public API_Service apiService;

    @Async("marketDataExecutor")
    public CompletableFuture<PriceDTO> fetchPrice(String ticker){
        return CompletableFuture.completedFuture(apiService.getPriceData(ticker));
    }

    @Async("marketDataExecutor")
    public CompletableFuture<DividendDTO> fetchDividend(String ticker, int limit){
        return CompletableFuture.completedFuture(apiService.getDividendData(ticker, limit, true));
    }

    @Async("marketDataExecutor")
    public CompletableFuture<BasicStockDataDTO> fetchBasics(String ticker){
        return CompletableFuture.completedFuture(apiService.getBasicData(ticker));
    }

    @Async("marketDataExecutor")
    public CompletableFuture<FinancialDTO> fetchFinancials(String ticker, int limit){
        return CompletableFuture.completedFuture(apiService.getFinancialData(ticker, limit));
    }

    @Async("marketDataExecutor")
    public CompletableFuture<WeekRangeDTO> fetchWeeksRange(String ticker){
        return CompletableFuture.completedFuture(apiService.getWeeksRangeData(ticker));
    }

    @Async("marketDataExecutor")
    public CompletableFuture<TickerOverviewDTO> fetchTickerData(String ticker){
        return CompletableFuture.completedFuture(apiService.getTickerOverviewlData(ticker));
    }

    @Async("marketDataExecutor")
    public CompletableFuture<PriceOverTimeDTO> fetchPriceOverTime(String ticker){
        return CompletableFuture.completedFuture(apiService.getPriceOverTimeData(ticker));
    }

}
