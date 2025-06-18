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

    @JsonProperty("totalDividend")
    private double totalDivided;

    @JsonProperty("totalPrice")
    private double totalPrice;


    public Stocks(Long id, String stockName, int stockPrice, int stockQuantity,
                  double currentPrice, double dividend, double totalDivided, double totalPrice) {
        this.id = id;
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.stockQuantity = stockQuantity;
        this.currentPrice = currentPrice;
        this.dividend = dividend;
        this.totalDivided = totalDivided;
        this.totalPrice = totalPrice;
    }

    public Stocks() {}

/*
    @Override
    public String toString() {
        return "Stocks{" +
                "id=" + id +
                ", stockName='" + stockName + '\'' +
                ", stockPrice=" + stockPrice +
                ", stockQuantity=" + stockQuantity +
                ", currentPrice=" + currentPrice +
                ", dividend=" + dividend +
                ", totalDivided=" + totalDivided +
                ", totalPrice=" + totalPrice +
                '}';
    }
 */
}
