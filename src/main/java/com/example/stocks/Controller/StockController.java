package com.example.stocks.Controller;

import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Service.PriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
public class StockController {

    private final PriceService priceService;
    private final Endpoints endpoints;

    public StockController(PriceService priceService, Endpoints endpoints) {
        this.priceService = priceService;
        this.endpoints = endpoints;
    }

    @PostMapping("find/{ticker}")
    public void findPrice(@PathVariable("ticker") String ticker) {
        endpoints.setPriceAPI(ticker);
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
