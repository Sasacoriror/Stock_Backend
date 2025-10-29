package com.example.stocks.Controller;

import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Portfolio;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Respository.PortfolioRepository;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import com.example.stocks.Service.API_Service;
import com.example.stocks.Service.DatabaseService;
import com.example.stocks.Service.StockService;
import com.example.stocks.Service.ValidateStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/")
@CrossOrigin(origins = "http://localhost:63342")
public class StockController_V2 {

    private final API_Service APIService;
    private final DatabaseService databaseService;
    private final Endpoints endpoints;
    private final StockRepository stockRepository;
    private final WatchlistRepository watchlistRepository;
    private final ValidateStockService validateStockService;
    private final StockService stockService;
    private final PortfolioRepository portfolioRepository;

    public StockController_V2(API_Service APIService, DatabaseService databaseService, Endpoints endpoints,
                           StockRepository stockRepository, WatchlistRepository watchlistRepository,
                           ValidateStockService validateStockService, StockService stockService, PortfolioRepository portfolioRepository) {
        this.APIService = APIService;
        this.databaseService = databaseService;
        this.endpoints = endpoints;
        this.stockRepository = stockRepository;
        this.watchlistRepository = watchlistRepository;
        this.validateStockService = validateStockService;
        this.stockService = stockService;
        this.portfolioRepository = portfolioRepository;
    }

    ///////////////////////// POSTMAPPING ////////////////////////

    //Creates the new portfolio
    @PostMapping("createPortfolio")
    public Portfolio create(@RequestBody Portfolio portfolio){
        return portfolioRepository.save(portfolio);
    }

    //Adds stock to a specific portfolio
    @PostMapping("{id}/stocks")
    public ResponseEntity<?> addStockToPortfolio(@PathVariable Long id, @RequestBody Stocks stock){
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        stock.setPortfolio(portfolio);

        String stockName = stock.getStockName().toUpperCase();
        Stocks savedStock = stockRepository.save(stock);
        databaseService.addToPortfolio(savedStock.getId(), stockName);

        return ResponseEntity.ok("OK");
    }

    //////////////////////// GETMAPPING ////////////////////////

    //Shows all portfolios and stocks within them
    @GetMapping("allPortfolios")
    public List<Portfolio> getPortfolios(){
        return portfolioRepository.findAll();
    }

    //Shows a specific portfolio and shares within it
    @GetMapping("{id}/stocks")
    public List<Stocks> getStocksFromPortfolio(@PathVariable Long id){
        return stockRepository.findByPortfolioId(id);
    }
}
