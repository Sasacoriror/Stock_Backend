package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeekRangeDTO {

    @JsonProperty("results")
    private List<WeekRangeDTO.Results> results;

    public List<WeekRangeDTO.Results> getResults() {
        return results;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Results{

        @JsonProperty("h")
        private double high;

        @JsonProperty("l")
        private double low;

        public double getLow() {
            return low;
        }

        public double getHigh() {
            return high;
        }
    }
}
