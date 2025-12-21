package com.example.stocks.Service;

import com.example.stocks.Model.DividendHistory;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Model.Watchlist;
import com.example.stocks.Record.PageResponse;
import com.example.stocks.Record.PortfolioSummary;
import com.example.stocks.Respository.PortfolioRepository;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class StockService {

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
        System.out.println("Portfolio summary cache cleared fro ID: "+id);
    }

    @Cacheable(value = "dividend_History", key = "#page + '-' + #size")
    public PageResponse<DividendHistory> getDividendHistory(int page, int size){
        System.out.println("\nGetting dividend history data\n");
        return recordSearchService.getDividendHistory(page, size);
    }

    @CacheEvict(value = "dividend_History", allEntries = true)
    public void clearCacheDividendHistory(){
        System.out.println("Cache cleared.");
    }

}
