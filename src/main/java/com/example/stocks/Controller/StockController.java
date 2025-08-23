package com.example.stocks.Controller;

import com.example.stocks.DTO.ResultsFinancialDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Model.Watchlist;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import com.example.stocks.Service.DatabaseService;
import com.example.stocks.Service.PriceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:63342")
public class StockController {

    private final PriceService priceService;
    private final DatabaseService databaseService;
    private final Endpoints endpoints;
    private final StockRepository stockRepository;
    private final WatchlistRepository watchlistRepository;

    public StockController(PriceService priceService, DatabaseService databaseService, Endpoints endpoints,
                           StockRepository stockRepository, WatchlistRepository watchlistRepository) {
        this.priceService = priceService;
        this.databaseService = databaseService;
        this.endpoints = endpoints;
        this.stockRepository = stockRepository;
        this.watchlistRepository = watchlistRepository;
    }

    @PostMapping("storeStockData")
    public ResponseEntity<?> storeData(@Valid @RequestBody Stocks stocks, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        String stockName = stocks.getStockName().toUpperCase();
        endpoints.setPriceAPI(stockName);
        endpoints.setDividendAPI(stockName);
        endpoints.setFinancialAPI(stockName, 1);

        priceService.getPriceData();
        priceService.getDividendData();
        priceService.getFinancialData();

        Stocks savedStock = stockRepository.save(stocks);

        databaseService.updateStockData();

        return ResponseEntity.ok("Stock saved successfully");
    }

    @PostMapping("addWatchlist")
    public ResponseEntity<?> addWatchlist(@Valid @RequestBody Watchlist watchlist, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        String stockName = watchlist.getStockTickerInn().toUpperCase();
        endpoints.setPriceAPI(stockName);
        endpoints.setDividendAPI(stockName);
        endpoints.setFinancialAPI(stockName, 1);

        priceService.getPriceData();
        priceService.getDividendData();
        priceService.getFinancialData();

        Watchlist watchlistSaved = watchlistRepository.save(watchlist);

        return null;
    }

    @GetMapping("Watchlist")
    public List<Watchlist> getWatchlist() {
        return watchlistRepository.findAll();
    }


    @GetMapping("findAll")
    public List<Stocks> findAll() {
        return stockRepository.findAll();
    }

    @DeleteMapping("delete/{tickerSymbol}")
    public ResponseEntity<String> delete(@PathVariable String tickerSymbol) {
        stockRepository.deleteById(tickerSymbol);
        return ResponseEntity.ok("Stock with ticker symbol "+tickerSymbol+" deleted successfully");
    }


    @PutMapping("updateData/{tickerSymbol}")
    public ResponseEntity<String> update(
            @PathVariable String tickerSymbol,
            @RequestBody Map<String, Integer> data) {

        Optional<Stocks> stocks = stockRepository.findById(tickerSymbol);

        Stocks stocks1 = stocks.get();

        if (data.containsKey("sharesInn")){
            stocks1.setStockQuantity(data.get("sharesInn"));
        }

        if (data.containsKey("priceInn")){
            stocks1.setStockPrice(data.get("priceInn"));

        }

        stockRepository.save(stocks1);

        databaseService.UpdateDatabase(tickerSymbol);

        return ResponseEntity.ok("Stock with ticker symbol "+tickerSymbol+" updated successfully");
    }

    @GetMapping("searchFinancialData/{ticker}")
    public ResultsFinancialDTO getCompanyFinancial(@PathVariable("ticker") String ticker) {
        String stockName = ticker.toUpperCase();
        endpoints.setFinancialAPI(stockName, 20);
        return priceService.getFinancialData();
    }
}
