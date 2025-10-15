package com.example.stocks.Link;

import com.example.stocks.Service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateAPI {

    @Autowired
    private DatabaseService databaseService;

    @Scheduled(cron = "* * 5 * * *")
    public void UpdateAPI() {
        databaseService.updateStockData();
    }
}
