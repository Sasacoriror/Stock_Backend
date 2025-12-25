package com.example.stocks.Config;

import com.example.stocks.Service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class UpdateAPI {

    @Autowired
    private DatabaseService databaseService;

    @Scheduled(cron = "0 0 14 * * MON-FRI", zone = "Europe/Oslo")
    public void UpdateAPI() {
        System.out.println("\n");
        System.out.println("Updating data");
        System.out.println("\n");
        databaseService.updatePortfolioData();
    }
}
