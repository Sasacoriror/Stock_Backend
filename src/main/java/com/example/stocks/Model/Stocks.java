package com.example.stocks.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;


@Entity
@Getter
@Setter
public class Stocks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @JsonProperty("stockTickerInn")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Stock name must only contain letters")
    private String stockName;

    @Min(value = 0, message = "The price cannot be lower than 0 (zero)")
    @JsonProperty("priceInn")
    private int stockPrice;

    @Min(value = 1, message = "The number of shares cannot be lower than 1 (one)")
    @JsonProperty("sharesInn")
    private int stockQuantity;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("currentPrice")
    private double currentPrice;

    @JsonProperty("dividend")
    private double dividend;

    @JsonProperty("totalDividend")
    private double totalDivided;

    @JsonProperty("totalPrice")
    private double totalPrice;

    @JsonProperty("totalInvested")
    private double totalInvested;

    @JsonProperty("return")
    private double returnValue;

    @JsonProperty("percentageReturn")
    private double percentageReturn;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    @JsonBackReference
    private Portfolio portfolio;

    //@ManyToOne
    //@JoinColumn(name = "user_id")
    //private User user;


    public Stocks(String stockName, int stockPrice, int stockQuantity, String companyName,  double currentPrice,
                  double dividend, double totalDivided, double totalPrice, double totalInvested, double returnValue, double percentageReturn) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.stockQuantity = stockQuantity;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.dividend = dividend;
        this.totalDivided = totalDivided;
        this.totalPrice = totalPrice;
        this.totalInvested = totalInvested;
        this.returnValue = returnValue;
        this.percentageReturn = percentageReturn;
    }

    protected Stocks() {}

}
