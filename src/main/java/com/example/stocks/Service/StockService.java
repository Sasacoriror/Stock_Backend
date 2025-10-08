package com.example.stocks.Service;

import com.example.stocks.Model.Stocks;
import com.example.stocks.Respository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Cacheable("allStocks")
    public List<Stocks> getAllShares(){
        System.out.println("");
        System.out.println("Getting all the data");
        System.out.println("");
        return stockRepository.findAll();
    }

    @CacheEvict(value = "allStocks", allEntries = true)
    public void clearCache(){
        System.out.println("Cache cleared.");
    }
}
