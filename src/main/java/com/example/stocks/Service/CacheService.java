package com.example.stocks.Service;

import com.example.stocks.Model.DividendHistory;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Model.Watchlist;
import com.example.stocks.Record.*;
import com.example.stocks.Respository.PortfolioRepository;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private RecordSearchService recordSearchService;

    @Autowired
    private PortfolioSummaryService portfolioSummaryService;

    @Autowired
    private DividendSummaryService dividendData;


    @Cacheable(value = "allWatchlist", key = "#page + '-' + #size")
    public Page<Watchlist> getEntireWatchlist(int page, int size){
        System.out.println("\nGetting watchlist data\n");
        Pageable pageable = PageRequest.of(page, size);
        return watchlistRepository.findAll(pageable);
    }

    @CacheEvict(value = "allWatchlist", allEntries = true)
    public void clearCacheWatchlist(){
        System.out.println("Cache cleared.");
    }

    @Cacheable(value = "getAllShares", key = "#id")
    public Page<Stocks> get_All_Shares(Long id, int page, int size){
        System.out.println("\nGetting all portfolio data\n");
        Pageable pageable = PageRequest.of(page, size);
        return stockRepository.findByPortfolioId(id, pageable);
    }

    @CacheEvict(value = "getAllShares", key = "#id")
    public void clearStocksPortfolio(Long id){
        System.out.println("Cache cleared for portfolio ID: "+id);
    }

    @Cacheable(value = "portfolioSummary", key = "#id")
    public PortfolioSummary getSummary(Long id){
        return portfolioSummaryService.getPortfolioSummary2(id);
    }

    @CacheEvict(value = "portfolioSummary", key = "#id")
    public void clearPortfolioCache(Long id){
        System.out.println("Portfolio summary cache cleared from ID: "+id);
    }

    @Cacheable(value = "BasicStockData", key = "#ticker")
    public SearchField basicStockData(String ticker){
        return recordSearchService.getSearchField(ticker);
    }

    @CacheEvict(value = "BasicStockData", key = "#ticker")
    public void clearBasicStockData(String ticker){
        System.out.println("Basic Stock Data cleared for "+ticker);
    }

    @Cacheable(value = "searchSummary", key = "#ticker")
    public SearchSummary searchSummary(String ticker) {
        return recordSearchService.getSummary(ticker);
    }

    @Cacheable(value = "dividendSummary", key = "#ticker")
    public DividendSearchSummary dividendSummary(String ticker){
        return recordSearchService.getDividendSummary(ticker);
    }

    @Cacheable(value = "dividend_History", key = "#ticker.toUpperCase() + ':' + #page + '-' + #size")
    public PageResponse<DividendHistory> getDividendHistory(String ticker, int page, int size){
        System.out.println("\nGetting dividend history data\n");
        return recordSearchService.getDividendHistory(page, size);
    }

    @CacheEvict(value = "dividend_History", allEntries = true)
    public void clearCacheDividendHistory(){
        System.out.println("Cache cleared.");
    }

    @Cacheable(value = "dividendPayment")
    public Dividends moneyEarned_And_moneyToEarn(){
        return dividendData.dividendData();
    }

    @CacheEvict(value = "dividendPayment", allEntries = true)
    public void clear_MoneyEarned_And_moneyToEarn(){
        System.out.println("MoneyEarned_And_moneyToEarn cached cleared");
    }

}
