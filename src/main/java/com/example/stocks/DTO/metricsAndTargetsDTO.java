package com.example.stocks.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.lang.annotation.Target;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class metricsAndTargetsDTO {

    @JsonProperty("Metrics")
    private Metrics metrics;

    @JsonProperty("Target")
    private Target targets;

    @JsonProperty("priceChangePercentage")
    private PercentageGainMoney percentageGainMoney;

    public Metrics getMetrics() {
        return metrics;
    }

    public Target getTargets() {
        return targets;
    }

    public PercentageGainMoney getPercentageGain() {
        return percentageGainMoney;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metrics {

        @JsonProperty("market_cap")
        private Double marketCap;

        @JsonProperty("trailing_eps")
        private Double T_eps;

        @JsonProperty("forward_eps")
        private Double F_eps;

        @JsonProperty("pe_ratio")
        private Double Pe_ratio;

        @JsonProperty("forward_pe")
        private Double F_pe;

        @JsonProperty("beta")
        private Double beta;

        public Double getMarketCap() {
            return marketCap;
        }

        public Double getT_eps() {
            return T_eps;
        }

        public Double getF_eps() {
            return F_eps;
        }

        public Double getPe_ratio() {
            return Pe_ratio;
        }

        public Double getF_pe() {
            return F_pe;
        }

        public Double getBeta() {
            return beta;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Target {

        @JsonProperty("current_price")
        private Double currentPrice;

        @JsonProperty("target_mean")
        private Double targetMean;

        @JsonProperty("target_low")
        private Double targetLow;

        @JsonProperty("target_high")
        private Double targetHigh;

        @JsonProperty("analyst_count")
        private int analystCount;

        @JsonProperty("recommendation_mean")
        private Double recommendationMean;

        @JsonProperty("recommendation_key")
        private String recomendationKey;

        @JsonProperty("implied_upside_pct")
        private Double percentageGain;

        public Double getCurrentPrice() {
            return currentPrice;
        }

        public Double getTargetMean() {
            return targetMean;
        }

        public Double getTargetLow() {
            return targetLow;
        }

        public Double getTargetHigh() {
            return targetHigh;
        }

        public int getAnalystCount() {
            return analystCount;
        }

        public Double getRecommendationMean() {
            return recommendationMean;
        }

        public String getRecomendationKey() {
            return recomendationKey;
        }

        public Double getPercentageGain() {
            return percentageGain;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PercentageGainMoney {

        @JsonProperty("one_Day")
        private Double oneDay;

        @JsonProperty("five_Day")
        private Double fiveDay;

        @JsonProperty("one_Month")
        private Double oneMonth;

        @JsonProperty("six_Month")
        private Double sixMonth;

        @JsonProperty("year_To_Date")
        private Double YTD;

        @JsonProperty("one_Year")
        private Double oneYear;

        @JsonProperty("three_Year")
        private Double threeYear;

        @JsonProperty("five_Year")
        private Double fiveYear;

        @JsonProperty("ten_Year")
        private Double tenYear;


    }
}
