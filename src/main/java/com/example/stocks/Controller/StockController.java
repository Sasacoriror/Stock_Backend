package com.example.stocks.Controller;

import com.example.stocks.Service.PriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
public class StockController {

    private final PriceService priceService;

    public StockController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("TickerPrice")
    public String getStockPrice() {
        return priceService.getTickerData();
    }
}
