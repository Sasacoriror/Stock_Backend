package com.example.stocks.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Watchlist {

    @Id
    @JsonProperty("stockTickerInn")
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[A-Za-z]+$")
    private String stockTickerInn;

    private String nameStock;

    private double priceStock;

    //private double marketCap;

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
