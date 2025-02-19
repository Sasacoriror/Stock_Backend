package com.example.stocks.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Stocks {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("stockTickerInn")
    private String StockName;

    @JsonProperty("priceInn")
    private int stockPrice;

    @JsonProperty("sharesInn")
    private int stockQuantity;

    public Stocks(Long id, String stockName, int stockPrice, int stockQuantity) {
        this.id = id;
        this.StockName = stockName;
        this.stockPrice = stockPrice;
        this.stockQuantity = stockQuantity;
    }

    public Stocks() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockName() {
        return StockName;
    }

    public void setStockName(String stockName) {
        StockName = stockName;
    }

    public int getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(int stockPrice) {
        this.stockPrice = stockPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "Stocks{" +
                "id=" + id +
                ", StockName='" + StockName + '\'' +
                ", stockPrice=" + stockPrice +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
