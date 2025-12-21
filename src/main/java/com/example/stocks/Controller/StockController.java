package com.example.stocks.Controller;

import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.DividendHistory;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Model.Watchlist;
import com.example.stocks.Record.DividendSearchSummary;
import com.example.stocks.Record.PageResponse;
import com.example.stocks.Record.SearchField;
import com.example.stocks.Record.SearchSummary;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import com.example.stocks.Service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class StockController {

    private final DatabaseService databaseService;
    private final StockRepository stockRepository;
    private final WatchlistRepository watchlistRepository;
    private final ValidateStockService validateStockService;
    private final StockService stockService;
    private final RecordSearchService recordSearchService;

    public StockController(DatabaseService databaseService,
                           StockRepository stockRepository, WatchlistRepository watchlistRepository,
                           ValidateStockService validateStockService, StockService stockService, RecordSearchService recordSearchService) {
        this.databaseService = databaseService;
        this.stockRepository = stockRepository;
        this.watchlistRepository = watchlistRepository;
        this.validateStockService = validateStockService;
        this.stockService = stockService;
        this.recordSearchService = recordSearchService;
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

    @GetMapping("search/{ticker}")
    public ResponseEntity<SearchField> getCompanyInformation(@PathVariable("ticker") String ticker) {
        validateStockService.ifStockExist(ticker);
        stockService.clearCacheDividendHistory();
        return ResponseEntity.ok(recordSearchService.getSearchField(ticker.toUpperCase()));
    }

    @GetMapping("searchSummary/{ticker}")
    public ResponseEntity<SearchSummary> getSummary(@PathVariable String ticker){
        validateStockService.ifStockExist(ticker);
        return ResponseEntity.ok(recordSearchService.getSummary(ticker.toUpperCase()));
    }

    @GetMapping("searchDividendSummary/{ticker}")
    public ResponseEntity<DividendSearchSummary> getDividendSummary(@PathVariable String ticker){
        validateStockService.ifStockExist(ticker);
        return ResponseEntity.ok(recordSearchService.getDividendSummary(ticker.toUpperCase()));
    }

    @GetMapping("searchDividendHistory")
    public ResponseEntity<PageResponse<DividendHistory>> getDividendHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ){
        return ResponseEntity.ok(stockService.getDividendHistory(page, size));
    }


    //////////////////////// DELETEMAPPING ////////////////////////

    @DeleteMapping("delete/{id}/{IDs}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id, @PathVariable Long IDs) {
        stockService.clearStocksPortfolio(IDs);
        stockService.clearPortfolioCache(IDs);
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

        databaseService.updateStockData(id, IDs,data);
        return ResponseEntity.ok("Updated successfully");
    }
}
