package com.example.stocks.Controller;

import com.example.stocks.Model.DividendHistory;
import com.example.stocks.Model.PortfolioStockView;
import com.example.stocks.Model.Watchlist;
import com.example.stocks.Record.*;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Respository.WatchlistRepository;
import com.example.stocks.Service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    private final CacheService cacheService;
    private final RecordSearchService recordSearchService;
    private final PortfolioSummaryService portfolioSummaryService;

    public StockController(DatabaseService databaseService,
                           StockRepository stockRepository, WatchlistRepository watchlistRepository,
                           ValidateStockService validateStockService, CacheService cacheService, RecordSearchService recordSearchService,
                           PortfolioSummaryService portfolioSummaryService) {
        this.databaseService = databaseService;
        this.stockRepository = stockRepository;
        this.watchlistRepository = watchlistRepository;
        this.validateStockService = validateStockService;
        this.cacheService = cacheService;
        this.recordSearchService = recordSearchService;
        this.portfolioSummaryService = portfolioSummaryService;
    }

    ///////////////////////// POSTMAPPING ////////////////////////

    @PostMapping("addWatchlist")
    public ResponseEntity<?> addWatchlist(@Valid @RequestBody Watchlist watchlist) {

        cacheService.clearCacheWatchlist();
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
        return ResponseEntity.ok(cacheService.getEntireWatchlist(page, size));
    }

    @GetMapping("search/{ticker}")
    public ResponseEntity<SearchField> getCompanyInformation(@PathVariable("ticker") String ticker) {
        validateStockService.ifStockExist(ticker);
        cacheService.clearCacheDividendHistory();
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
        return ResponseEntity.ok(cacheService.getDividendHistory(page, size));
    }


    @GetMapping("getPortfolio/{id}")
    public ResponseEntity<PortfolioView<PortfolioStockView>> getPortfolio(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ){
        return ResponseEntity.ok(portfolioSummaryService.getPortfolio(id, page, size));
    }

    //////////////////////// DELETEMAPPING ////////////////////////

    @DeleteMapping("delete/{id}/{IDs}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id, @PathVariable Long IDs) {
        cacheService.clearStocksPortfolio(IDs);
        cacheService.clearPortfolioCache(IDs);
        stockRepository.deleteById(id);
        return ResponseEntity.ok("Stock with ticker symbol "+id+" deleted successfully");
    }

    @DeleteMapping("deleteWatchlist/{id}")
    public ResponseEntity<String> deleteWatchlist(@PathVariable("id") Long id) {
        cacheService.clearCacheWatchlist();
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
