package com.example.stocks.Calculate;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculateData {

    public double totalDividend(double cashAmount, int frequenzy, double shares){
        return (cashAmount * frequenzy) * shares;
    } 

    public double dividendYield(double dividend, int dividendFrequenzy, double pricePerShare){
        return ((dividend * dividendFrequenzy) / pricePerShare) * 100;
    }

    public double totalValue(double shares, double currentPrice){
        return currentPrice * shares;
    }

    public double totalInvested(double shares, double price){
        return price * shares;
    }

    public double returns(double currentPrice, double shares, double price){
        return  (currentPrice * shares) - (price * shares);
    }

    public double percentage(double returns, double totalInvested){
        return  (returns / totalInvested) * 100;
    }

    public double pricePerShare(double marketCap, Long outstandingShares){
        return marketCap / outstandingShares;
    }

    public double calculateDaysChange(double tall1, double tall2){
        return tall1 - tall2;
    }

    public double roundNumbers(double tall1){
        return BigDecimal
                .valueOf(tall1)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
