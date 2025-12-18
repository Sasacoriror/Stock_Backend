package com.example.stocks.Service;

import com.example.stocks.Model.Stocks;
import com.example.stocks.Record.Dividends;
import com.example.stocks.Respository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Service
public class DividendSummaryService {

    @Autowired
    private StockRepository stockRepository;

    public Dividends dividendData() {

        List<Stocks> stockData = stockRepository.findAll();

        double fullDividend = 0.0;
        double fullInvestemnt = 0.0;

        for (Stocks stock : stockData) {
            fullDividend += stock.getTotalDivided();
            fullInvestemnt += stock.getTotalInvested();
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        double monthlyDividend = Double.parseDouble(df.format(fullDividend / 12));
        double dailyDividend = Double.parseDouble(df.format(fullDividend / 365));
        double hourlyDividend = Double.parseDouble(df.format(fullDividend / 8765));
        double yieldOnCost = Double.parseDouble(df.format((fullDividend / fullInvestemnt) * 100));

        return new Dividends(
                fullDividend,
                monthlyDividend,
                dailyDividend,
                hourlyDividend,
                yieldOnCost
        );
    }
}
