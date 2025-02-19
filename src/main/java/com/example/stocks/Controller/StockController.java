package com.example.stocks.Controller;

import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Respository.StockRepository;
import com.example.stocks.Service.PriceService;
import org.springframework.web.bind.annotation.*;

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
    public String store(@RequestBody Stocks stocks) {
        stockRepository.save(stocks);
        return "Stock added successfully";
    }

    @PostMapping("find/{ticker}")
    public void findPrice(@PathVariable("ticker") String ticker) {
        endpoints.setPriceAPI(ticker.toUpperCase());
    }

    @GetMapping("TickerInfo")
    public String getStockPrice() {
        return priceService.getTickerData();
    }

    @GetMapping("PriceInfo")
    public PriceDTO getPriceData() {
        return priceService.getPriceData();
    }
}
