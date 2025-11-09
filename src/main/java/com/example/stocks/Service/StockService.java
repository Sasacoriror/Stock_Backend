package com.example.stocks.Service;

import com.example.stocks.Model.Stocks;
import com.example.stocks.Model.Watchlist;
import com.example.stocks.Respository.PortfolioRepository;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;


    @Cacheable("allWatchlist")
    public List<Watchlist> getEntireWatchlist(){
        System.out.println("\nGetting watchlist data\n");
        return watchlistRepository.findAll();
    }

    @CacheEvict(value = "allWatchlist", allEntries = true)
    public void clearCacheWatchlist(){
        System.out.println("Cache cleared.");
    }

    @Cacheable(value = "getAllShares", key = "#id")
    public List<Stocks> get_All_Shares(Long id){
        System.out.println("\nGetting all portfolio data\n");
        return stockRepository.findByPortfolioId(id);
    }

    @CacheEvict(value = "getAllShares", key = "#id")
    public void clearStocksPortfolio(Long id){
        System.out.println("Cache cleared for portfolio ID: "+id);
    }

}
