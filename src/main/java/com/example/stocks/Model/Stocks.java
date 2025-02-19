package com.example.stocks.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Stocks {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String StockName;

    private int stockPrice;

    private int stockQuantity;

    public Stocks(int id, String stockName, int stockPrice, int stockQuantity) {
        this.id = id;
        this.StockName = stockName;
        this.stockPrice = stockPrice;
        this.stockQuantity = stockQuantity;
    }

    public Stocks() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
