package com.example.stocks.Controller;

import com.example.stocks.Model.Portfolio;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Respository.PortfolioRepository;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Service.DatabaseService;
import com.example.stocks.Service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class StockController_V2 {

    private final DatabaseService databaseService;
    private final StockRepository stockRepository;
    private final StockService stockService;
    private final PortfolioRepository portfolioRepository;

    public StockController_V2(DatabaseService databaseService,
                              StockRepository stockRepository, StockService stockService,
                               PortfolioRepository portfolioRepository) {
        this.databaseService = databaseService;
        this.stockRepository = stockRepository;
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

        stockService.clearCachePortfolio();
        String stockName = stock.getStockName().toUpperCase();
        Stocks savedStock = stockRepository.save(stock);
        databaseService.addToPortfolio(savedStock.getId(), stockName);

        return ResponseEntity.ok("OK");
    }

    //////////////////////// GETMAPPING ////////////////////////

    //Shows all portfolios and stocks within them
    @GetMapping("allPortfolios")
    public List<Portfolio> getPortfoliosAndStocks(){
        return portfolioRepository.findAll();
    }

    //Shows all portfolios and stocks within them
    @GetMapping("portfolios")
    public List<Portfolio> getPortfolios(){
        return portfolioRepository.findAll()
                .stream()
                .map(p -> new Portfolio(p.getId(), p.getName()))
                .toList();
    }

    //Shows a specific portfolio and shares within it
    @GetMapping("{id}/stocks")
    public List<Stocks> getStocksFromPortfolio(@PathVariable Long id){
        return stockRepository.findByPortfolioId(id);
    }
}
