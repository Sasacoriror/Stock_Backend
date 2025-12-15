package com.example.stocks.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DividendHistory {

    @JsonProperty("Declared_date")
    private String declaredDate;

    @JsonProperty("Ex_Dividend_Day")
    private String exDivDate;

    @JsonProperty("Record_Date")
    private String recordDate;

    @JsonProperty("Payment_Date")
    private String payment_Date;

    @JsonProperty("Frequenzy")
    private int frequenzy;

    @JsonProperty("Amount")
    private Double amount;

    @JsonProperty("Change")
    private Double change;

    public DividendHistory(String decDate, String exDate, String recordDate, String payDate, int frequency, double cashAmount, Double change) {
        this.declaredDate = decDate;
        this.exDivDate = exDate;
        this.recordDate = recordDate;
        this.payment_Date = payDate;
        this.frequenzy = frequency;
        this.amount = cashAmount;
        this.change = change;
    }
}


