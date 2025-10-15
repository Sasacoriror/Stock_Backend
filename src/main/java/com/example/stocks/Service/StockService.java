package com.example.stocks.Service;

import com.example.stocks.Model.Stocks;
import com.example.stocks.Model.Watchlist;
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
    private WatchlistRepository watchlistRepository;

    @Cacheable("allStocks")
    public List<Stocks> getAllShares(){
        System.out.println("");
        System.out.println("Getting all portfolio data");
        System.out.println("");
        return stockRepository.findAll();
    }

    @Cacheable("allWatchlist")
    public List<Watchlist> getEntireWatchlist(){
        System.out.println("");
        System.out.println("Getting watchlist data");
        System.out.println("");
        return watchlistRepository.findAll();
    }

    @CacheEvict(value = "allStocks", allEntries = true)
    public void clearCachePortfolio(){
        System.out.println("Cache cleared.");
    }

    @CacheEvict(value = "allWatchlist", allEntries = true)
    public void clearCacheWatchlist(){
        System.out.println("Cache cleared.");
    }
}
