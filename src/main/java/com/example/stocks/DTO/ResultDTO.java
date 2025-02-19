package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultDTO {

    @JsonProperty("c")
    private double c;

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }
}
