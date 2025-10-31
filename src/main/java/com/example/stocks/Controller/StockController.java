package com.example.stocks.Controller;

import com.example.stocks.DTO.FinancialDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Model.Watchlist;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import com.example.stocks.Service.DatabaseService;
import com.example.stocks.Service.API_Service;
import com.example.stocks.Service.StockService;
import com.example.stocks.Service.ValidateStockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class StockController {

    private final API_Service APIService;
    private final DatabaseService databaseService;
    private final Endpoints endpoints;
    private final StockRepository stockRepository;
    private final WatchlistRepository watchlistRepository;
    private final ValidateStockService validateStockService;
    private final StockService stockService;

    public StockController(API_Service APIService, DatabaseService databaseService, Endpoints endpoints,
                           StockRepository stockRepository, WatchlistRepository watchlistRepository,
                           ValidateStockService validateStockService, StockService stockService) {
        this.APIService = APIService;
        this.databaseService = databaseService;
        this.endpoints = endpoints;
        this.stockRepository = stockRepository;
        this.watchlistRepository = watchlistRepository;
        this.validateStockService = validateStockService;
        this.stockService = stockService;
    }

    ///////////////////////// POSTMAPPING ////////////////////////

    @PostMapping("storeStockData")
    public ResponseEntity<?> storeData(@Valid @RequestBody Stocks stocks) {
        String stockName = stocks.getStockName().toUpperCase();
        if (!validateStockService.isValid(stockName)) {
            return validateStockService.
                    error("Ticker: "+stockName+" does not exist", HttpStatus.BAD_REQUEST);
        }
        stockService.clearCachePortfolio();
        Stocks savedStock = stockRepository.save(stocks);
        databaseService.addToPortfolio(savedStock.getId(), stockName);
        return validateStockService.ok("Stock saved successfully");
    }

    @PostMapping("addWatchlist")
    public ResponseEntity<?> addWatchlist(@Valid @RequestBody Watchlist watchlist) {

        stockService.clearCacheWatchlist();
        String stockName = watchlist.getStockTickerInn().toUpperCase();
        Watchlist watchlistSaved = watchlistRepository.save(watchlist);
        databaseService.addToWatchist(watchlistSaved.getId(), stockName);

        return ResponseEntity.ok("Watchlist saved successfully");
    }


    //////////////////////// GETMAPPING ////////////////////////

    @GetMapping("findAll")
    public ResponseEntity<List<Stocks>> findAll() {
        return ResponseEntity.ok(stockService.getAllShares());
    }

    @GetMapping("Watchlist")
    public ResponseEntity<List<Watchlist>> getWatchlist() {
        return ResponseEntity.ok(stockService.getEntireWatchlist());
    }

    @GetMapping("searchFinancialData/{ticker}")
    public ResponseEntity<FinancialDTO> getCompanyFinancial(@PathVariable("ticker") String ticker) {
        String stockName = ticker.toUpperCase();
        if (!validateStockService.isValid(stockName)) {
            return (ResponseEntity<FinancialDTO>) validateStockService.
                    error("Ticker: "+stockName+" does not exist", HttpStatus.BAD_REQUEST);
        }
        endpoints.setFinancialAPI(stockName, 20);
        return ResponseEntity.ok(APIService.getFinancialData());
    }

    //////////////////////// DELETEMAPPING ////////////////////////

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        stockService.clearCachePortfolio();
        stockRepository.deleteById(id);
        return ResponseEntity.ok("Stock with ticker symbol "+id+" deleted successfully");
    }

    @DeleteMapping("deleteWatchlist/{id}")
    public ResponseEntity<String> deleteWatchlist(@PathVariable("id") Long id) {
        stockService.clearCacheWatchlist();
        watchlistRepository.deleteById(id);
        return ResponseEntity.ok("Watchlist with symbol "+id+" deleted successfully");
    }

    //////////////////////// PUTMAPPING ////////////////////////

    @PutMapping("updateData/{id}")
    public ResponseEntity<String> update(
            @PathVariable Long id,
            @Valid @RequestBody Map<String, Integer> data) {

        stockService.clearCachePortfolio();
        Optional<Stocks> stocks = stockRepository.findById(id);
        Stocks stocks1 = stocks.get();

        if (data.containsKey("sharesInn") && data.containsKey("priceInn")){
            stocks1.setStockQuantity(data.get("sharesInn"));
            stocks1.setStockPrice(data.get("priceInn"));
        }

        stockRepository.save(stocks1);
        databaseService.UpdateDatabase(id);

        return ResponseEntity.ok("Updated successfully");
    }
}
