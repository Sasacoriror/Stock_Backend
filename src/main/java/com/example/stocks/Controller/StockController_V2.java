package com.example.stocks.Controller;

import com.example.stocks.Model.Portfolio;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Record.Dividends;
import com.example.stocks.Record.PortfolioSummary;
import com.example.stocks.Respository.AllPortfolios;
import com.example.stocks.Respository.PortfolioRepository;
import com.example.stocks.Service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class StockController_V2 {

    private final DatabaseService databaseService;
    private final CacheService cacheService;
    private final PortfolioRepository portfolioRepository;
    private final ValidateStockService validateStockService;
    private final DividendSummaryService dividendData;

    public StockController_V2(DatabaseService databaseService, CacheService cacheService,
                              PortfolioRepository portfolioRepository, ValidateStockService validateStockService,
                              DividendSummaryService dividendData) {
        this.databaseService = databaseService;
        this.cacheService = cacheService;
        this.portfolioRepository = portfolioRepository;
        this.validateStockService = validateStockService;
        this.dividendData = dividendData;
    }

    ///////////////////////// POSTMAPPING ////////////////////////

    //Creates the new portfolio
    @PostMapping("createPortfolio")
    public Portfolio create(@RequestBody Portfolio portfolio){
        return portfolioRepository.save(portfolio);
    }

    //Adds stock to a specific portfolio
    @PostMapping("{id}/stocks")
    public ResponseEntity<?> addStockToPortfolio(@Valid @PathVariable Long id, @RequestBody Stocks stock){
        validateStockService.stockValidation(stock.getStockName());

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        stock.setPortfolio(portfolio);
        databaseService.addToPortfolio(stock, id);

        return validateStockService.ok("OK");
    }

    //////////////////////// GETMAPPING ////////////////////////

    //Shows all portfolios and stocks within them
    @GetMapping("allPortfolios")
    public List<Portfolio> getPortfoliosAndStocks(){
        return portfolioRepository.findAll();
    }

    //Shows all portfolios and Ids to the portfolios
    @GetMapping("portfolios")
    public List<AllPortfolios> getPortfolios(){
        return portfolioRepository.findAllBy();
    }

    //Shows a specific portfolio and shares within it
    @GetMapping("{id}/stocks")
    public ResponseEntity<Page<Stocks>> getStocksFromPortfolio(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam int size){
        return ResponseEntity.ok(cacheService.get_All_Shares(id, page, size));
    }

    @GetMapping("{id}/summary2")
    public PortfolioSummary portfolioSummary2(@PathVariable Long id){
        return cacheService.getSummary(id);
    }

    @GetMapping("dividend_Summary")
    public Dividends getDividendSummary(){
        return cacheService.moneyEarned_And_moneyToEarn();
        //return dividendData.dividendData();
    }
}
