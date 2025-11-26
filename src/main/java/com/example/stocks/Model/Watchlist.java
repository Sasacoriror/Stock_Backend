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

    @JsonProperty("change_Price")
    private double changePrice;

    @JsonProperty("change_Percentage")
    private double changePercentage;

    @JsonProperty("low")
    private double weeksLow;

    @JsonProperty("High")
    private double weeksHigh;

    @JsonProperty("dividendYield")
    private double dividendYield;

    @JsonProperty("PE_Ratio")
    private double PERatio;

    @JsonProperty("market_Cap")
    private Long marketCap;



    public Watchlist(String stockTickerInn, String nameStock, double priceStock, double changePrice, double changePercentage, double weeksLow, double weeksHigh, double dividendYield, double PERatio, Long marketCap) {
        this.stockTickerInn = stockTickerInn;
        this.nameStock = nameStock;
        this.priceStock = priceStock;
        this.changePrice = changePrice;
        this.changePercentage = changePercentage;
        this.weeksLow = weeksLow;
        this.weeksHigh = weeksHigh;
        this.dividendYield = dividendYield;
        this.PERatio = PERatio;
        this.marketCap = marketCap;
    }

    public Watchlist() {}
}
