package com.example.stocks.Calculate;

import org.springframework.stereotype.Service;

@Service
public class CalculateData {

    public double totalDividend(double cashAmount, int frequenzy, int shares){
        return (cashAmount * frequenzy) * shares;
    } 

    public double dividendYield(double dividend, int dividendFrequenzy, double pricePerShare){
        return ((dividend * dividendFrequenzy) / pricePerShare) * 100;
    }

    public double totalValue(int shares, double currentPrice){
        return currentPrice * shares;
    }

    public double totalInvested(int shares, double price){
        return price * shares;
    }

    public double returns(double currentPrice, int shares, double price){
        return  (currentPrice * shares) - (price * shares);
    }

    public double percentage(double returns, double totalInvested){
        return  (returns / totalInvested) * 100;
    }

    public double pricePerShare(double marketCap, Long outstandingShares){
        return marketCap / outstandingShares;
    }
}
