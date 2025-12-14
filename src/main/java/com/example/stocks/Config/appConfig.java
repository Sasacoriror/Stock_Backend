package com.example.stocks.Config;

import com.example.stocks.Model.Portfolio;
import com.example.stocks.Respository.PortfolioRepository;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class appConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public ApplicationRunner createDeafultPortfolio(PortfolioRepository portfolioRepository){
        return args -> {
            if (portfolioRepository.count() == 0){
                Portfolio portfolio = new Portfolio();
                portfolio.setName("MyPortfolio");
                portfolioRepository.save(portfolio);
            }
        };
    }

    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("getAllShares", "allWatchlist");
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .maximumSize(1000)
        );
        return cacheManager;
    }
}
