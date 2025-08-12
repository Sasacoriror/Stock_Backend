package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class financialDTO {

    @JsonProperty("fiscal_period")
    private String fiscalPeriod;

    @JsonProperty("tickers")
    private List<String> tickers;

    @JsonProperty("company_name")
    private String companyName;

    //@JsonProperty("financials")
    //private Financials financials;
}
