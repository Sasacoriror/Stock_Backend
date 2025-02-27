package com.example.stocks.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Stocks {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("stockTickerInn")
    @Column(unique = true, nullable = false)
    private String stockName;

    @JsonProperty("priceInn")
    private int stockPrice;

    @JsonProperty("sharesInn")
    private int stockQuantity;

    @JsonProperty("currentPrice")
    private double currentPrice;

    @JsonProperty("dividend")
    private double dividend;

    public Stocks(Long id, String stockName, int stockPrice, int stockQuantity,
                  double currentPrice, double dividend) {
        this.id = id;
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.stockQuantity = stockQuantity;
        this.currentPrice = currentPrice;
        this.dividend = dividend;
    }

    public Stocks() {}
/*
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

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getDividend() {
        return dividend;
    }

    public void setDividend(double dividend) {
        this.dividend = dividend;
    }*/

    @Override
    public String toString() {
        return "Stocks{" +
                "id=" + id +
                ", StockName='" + stockName + '\'' +
                ", stockPrice=" + stockPrice +
                ", stockQuantity=" + stockQuantity +
                ", currentPrice=" + currentPrice +
                ", dividend=" + dividend +
                '}';
    }
}
