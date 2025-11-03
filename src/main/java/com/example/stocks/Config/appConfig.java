package com.example.stocks.Config;

import com.example.stocks.Model.Portfolio;
import com.example.stocks.Respository.PortfolioRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class appConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
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
}
