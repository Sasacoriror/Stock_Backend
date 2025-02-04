package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDTO {

    @JsonProperty("weighted_shares_outstanding")
    private Double weightedSharesOutstanding;

    @JsonProperty("market_cap")
    private Double markedCap;

    public Double getWeightedSharesOutstanding() {
        return weightedSharesOutstanding;
    }

    public void setWeightedSharesOutstanding(Double weightedSharesOutstanding) {
        this.weightedSharesOutstanding = weightedSharesOutstanding;
    }

    public Double getMarkedCap() {
        return markedCap;
    }

    public void setMarkedCap(Double markedCap) {
        this.markedCap = markedCap;
    }
}
