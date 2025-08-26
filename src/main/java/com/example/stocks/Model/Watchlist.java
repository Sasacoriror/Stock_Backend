package com.example.stocks.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @JsonProperty("stockTickerInn")
    @Pattern(regexp = "^[A-Za-z]+$")
    private String stockTickerInn;

    @JsonProperty("companyName")
    private String nameStock;

    @JsonProperty("latestPrice")
    private double priceStock;

    //private double marketCap;

    @JsonProperty("dividendYield")
    private double dividendYield;

    public Watchlist(String stockTickerInn, String nameStock, double priceStock, double dividendYield) {
        this.stockTickerInn = stockTickerInn;
        this.nameStock = nameStock;
        this.priceStock = priceStock;
        //this.marketCap = marketCap;
        this.dividendYield = dividendYield;
    }

    public Watchlist() {}
}
