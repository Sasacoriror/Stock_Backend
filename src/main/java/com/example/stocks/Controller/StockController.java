package com.example.stocks.Controller;

import com.example.stocks.DTO.FinancialDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Model.Watchlist;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import com.example.stocks.Service.DatabaseService;
import com.example.stocks.Service.PriceService;
import com.example.stocks.Service.ValidateStock;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    private final ValidateStock validateStock;

    public StockController(PriceService priceService, DatabaseService databaseService, Endpoints endpoints,
                           StockRepository stockRepository, WatchlistRepository watchlistRepository, ValidateStock validateStock) {
        this.priceService = priceService;
        this.databaseService = databaseService;
        this.endpoints = endpoints;
        this.stockRepository = stockRepository;
        this.watchlistRepository = watchlistRepository;
        this.validateStock = validateStock;
    }

    ///////////////////////// POSTMAPPING ////////////////////////

    @PostMapping("storeStockData")
    public ResponseEntity<?> storeData(@Valid @RequestBody Stocks stocks) {
        try {
            String stockName = stocks.getStockName().toUpperCase();
            if (!validateStock.isValid(stockName)) {
                return ResponseEntity.badRequest().
                        body(Map.of("Error", "Ticker: " + stockName + " does not exist"));
            }
            Stocks savedStock = stockRepository.save(stocks);
            databaseService.addToPortfolio(savedStock.getId(), stockName);
            return ResponseEntity.ok("Stock saved successfully");

        } catch (Exception e){
            return ResponseEntity.internalServerError().
                    body(Map.of("Error","Failed to add stock: "+e.getMessage()));
        }
    }

    @PostMapping("addWatchlist")
    public ResponseEntity<?> addWatchlist(@Valid @RequestBody Watchlist watchlist) {

        String stockName = watchlist.getStockTickerInn().toUpperCase();
        Watchlist watchlistSaved = watchlistRepository.save(watchlist);
        databaseService.addToWatchist(watchlistSaved.getId(), stockName);

        return ResponseEntity.ok("Watchlist saved successfully");
    }


    //////////////////////// GETMAPPING ////////////////////////

    @GetMapping("findAll")
    public List<Stocks> findAll() {
        return stockRepository.findAll();
    }

    @GetMapping("Watchlist")
    public List<Watchlist> getWatchlist() {
        return watchlistRepository.findAll();
    }

    @GetMapping("searchFinancialData/{ticker}")
    public FinancialDTO getCompanyFinancial(@PathVariable("ticker") String ticker) {
        String stockName = ticker.toUpperCase();
        endpoints.setFinancialAPI(stockName, 20);
        return priceService.getFinancialData();
    }

    //////////////////////// DELETEMAPPING ////////////////////////

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        stockRepository.deleteById(id);
        return ResponseEntity.ok("Stock with ticker symbol "+id+" deleted successfully");
    }

    @DeleteMapping("deleteWatchlist/{id}")
    public ResponseEntity<String> deleteWatchlist(@PathVariable("id") Long id) {
        watchlistRepository.deleteById(id);
        return ResponseEntity.ok("Watchlist with symbol "+id+" deleted successfully");
    }

    //////////////////////// PUTMAPPING ////////////////////////

    @PutMapping("updateData/{tickerSymbol}")
    public ResponseEntity<String> update(
            @PathVariable Long tickerSymbol,
            @Valid @RequestBody Map<String, Integer> data) {

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
}
