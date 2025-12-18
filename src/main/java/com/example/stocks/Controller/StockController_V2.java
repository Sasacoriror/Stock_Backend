package com.example.stocks.Controller;

import com.example.stocks.Model.Portfolio;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Record.Dividends;
import com.example.stocks.Record.PortfolioSummary;
import com.example.stocks.Respository.PortfolioRepository;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    private final ValidateStockService validateStockService;
    private final DividendSummaryService dividendData;
    private final PortfolioSummaryService portfolioSummaryService;

    public StockController_V2(DatabaseService databaseService,
                              StockRepository stockRepository, StockService stockService,
                              PortfolioRepository portfolioRepository, ValidateStockService validateStockService,
                              DividendSummaryService dividendData, PortfolioSummaryService portfolioSummaryService) {
        this.databaseService = databaseService;
        this.stockRepository = stockRepository;
        this.stockService = stockService;
        this.portfolioRepository = portfolioRepository;
        this.validateStockService = validateStockService;
        this.dividendData = dividendData;
        this.portfolioSummaryService = portfolioSummaryService;
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
        String stockName = stock.getStockName().toUpperCase();

        if (stockRepository.existsByStockName(stockName)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Stock already exists");
        } else if (!validateStockService.isValid(stockName)) {
            return validateStockService.
                    error("Ticker: "+stockName+" does not exist", HttpStatus.BAD_REQUEST);
        }

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        stockService.clearStocksPortfolio(id);
        stock.setPortfolio(portfolio);
        databaseService.addToPortfolio(stock);

        return validateStockService.ok("OK");
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
    public ResponseEntity<Page<Stocks>> getStocksFromPortfolio(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam int size){
        return ResponseEntity.ok(stockService.get_All_Shares(id, page, size));
    }

    @GetMapping("{id}/summary2")
    public PortfolioSummary portfolioSummary2(@PathVariable Long id){
        return portfolioSummaryService.getPortfolioSummary2(id);
    }

    @GetMapping("dividend_Summary")
    public Dividends getDividendSummary(){
        return dividendData.dividendData();
    }
}
