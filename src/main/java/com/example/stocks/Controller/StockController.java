package com.example.stocks.Controller;

import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.DTO.ResultsDividendDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Service.PriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:63342")
public class StockController {

    private final PriceService priceService;
    private final Endpoints endpoints;
    private final StockRepository stockRepository;

    public StockController(PriceService priceService, Endpoints endpoints,
                           StockRepository stockRepository) {
        this.priceService = priceService;
        this.endpoints = endpoints;
        this.stockRepository = stockRepository;
    }

    @PostMapping("storeStockData")
    public Stocks store(@RequestBody Stocks stocks) {
        Stocks stocks1 = stockRepository.save(stocks);
        endpoints.setPriceAPI(stocks1.getStockName().toUpperCase());
        endpoints.setDividendAPI(stocks1.getStockName().toUpperCase());
        //priceService.saveDividendData();
        priceService.updateStockData();
        //return "Stock added successfully";
        return stocks1;
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
        return ResponseEntity.ok("Stock with ticker symbol "+tickerSymbol+" updated successfully");
    }


    /////////////////////////////////

    @PostMapping("find/{ticker}")
    public void findPrice(@PathVariable("ticker") String ticker) {
        endpoints.setPriceAPI(ticker.toUpperCase());
        endpoints.setDividendAPI(ticker.toUpperCase());
    }
/*
    @GetMapping("TickerInfo")
    public String getStockPrice() {
        return priceService.getTickerData();
    }

    @GetMapping("PriceInfo")
    public PriceDTO getPriceData() {
        return priceService.getPriceData();
    }

    @GetMapping("DividendInfo")
    public ResultsDividendDTO getDividendData() {
        return priceService.getDividendData();
    }
 */
}
