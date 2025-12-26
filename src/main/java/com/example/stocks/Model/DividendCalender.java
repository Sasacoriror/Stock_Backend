package com.example.stocks.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DividendCalender {

    @JsonProperty("ticker")
    private String stockTicker;

    @JsonProperty("Company_Name")
    private String companyName;

    //@JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("Payment_Date")
    private String paymentDate;

    @JsonProperty("Dividend_Payment")
    private Double dividendPayment;

    @JsonProperty("Frequency")
    private int frequency;

    @JsonProperty("Dividend_Yield")
    private Double dividendYield;

    @JsonProperty("Ex_Date")
    private String exDate;
}
