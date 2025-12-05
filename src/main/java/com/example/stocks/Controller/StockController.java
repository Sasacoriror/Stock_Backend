package com.example.stocks.Controller;

import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Model.Watchlist;
import com.example.stocks.Record.SearchField;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import com.example.stocks.Service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
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
    private final RecordService recordService;

    public StockController(API_Service APIService, DatabaseService databaseService, Endpoints endpoints,
                           StockRepository stockRepository, WatchlistRepository watchlistRepository,
                           ValidateStockService validateStockService, StockService stockService, RecordService recordService) {
        this.APIService = APIService;
        this.databaseService = databaseService;
        this.endpoints = endpoints;
        this.stockRepository = stockRepository;
        this.watchlistRepository = watchlistRepository;
        this.validateStockService = validateStockService;
        this.stockService = stockService;
        this.recordService = recordService;
    }

    ///////////////////////// POSTMAPPING ////////////////////////

    @PostMapping("addWatchlist")
    public ResponseEntity<?> addWatchlist(@Valid @RequestBody Watchlist watchlist) {

        stockService.clearCacheWatchlist();
        String stockName = watchlist.getStockTickerInn().toUpperCase();
        Watchlist watchlistSaved = watchlistRepository.save(watchlist);
        databaseService.addToWatchist(watchlistSaved.getId(), stockName);

        return ResponseEntity.ok(watchlistSaved.getId());
    }


    //////////////////////// GETMAPPING ////////////////////////

    @GetMapping("Watchlist")
    public ResponseEntity<Page<Watchlist>> getWatchlist(
            @RequestParam int page,
            @RequestParam int size) {
        return ResponseEntity.ok(stockService.getEntireWatchlist(page, size));
    }

    @GetMapping("searchFinancialData/{ticker}")
    public ResponseEntity<SearchField> getCompanyInformation(@PathVariable("ticker") String ticker) {
        String stockName = ticker.toUpperCase();
        if (!validateStockService.isValid(stockName)) {
            return (ResponseEntity<SearchField>) validateStockService.
                    error("Ticker: "+stockName+" does not exist", HttpStatus.BAD_REQUEST);
        }
        //endpoints.setFinancialAPI(stockName, 20);
        return ResponseEntity.ok(recordService.getSearchField(stockName));
    }

    //////////////////////// DELETEMAPPING ////////////////////////

    @DeleteMapping("delete/{id}/{IDs}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id, @PathVariable Long IDs) {
        stockService.clearStocksPortfolio(IDs);
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

    @PutMapping("updateData/{id}/{IDs}")
    public ResponseEntity<String> update(
            @PathVariable Long id, @PathVariable Long IDs,
            @Valid @RequestBody Map<String, Integer> data) {

        stockService.clearStocksPortfolio(IDs);
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
