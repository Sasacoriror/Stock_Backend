package com.example.stocks.Controller;

import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.DTO.ResultsDividendDTO;
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
    public Stocks store(@RequestBody Stocks stocks) {
        Stocks stocks1 = stockRepository.save(stocks);
        endpoints.setPriceAPI(stocks1.getStockName());
        endpoints.setDividendAPI(stocks1.getStockName());
        priceService.updateStockData();
        //return "Stock added successfully";
        return stocks1;
    }
/*
    @PostMapping("find/{ticker}")
    public void findPrice(@PathVariable("ticker") String ticker) {
        endpoints.setPriceAPI(ticker.toUpperCase());
        endpoints.setDividendAPI(ticker.toUpperCase());
    }*/

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
}
