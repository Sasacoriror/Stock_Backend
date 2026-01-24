package com.example.stocks.Service;

import com.example.stocks.DTO.DividendDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.DividendCalender;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Record.Dividends;
import com.example.stocks.Respository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DividendSummaryService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private Endpoints endpoints;

    @Autowired
    private API_Service APIService;

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

        List<DividendCalender> calenders = new ArrayList<>();

        for (Stocks stock: stockData){

            String stockName = stock.getStockName();
            endpoints.setDividendAPI(stockName, 1);


            DividendDTO dividendData = APIService.getDividendData(stockName, 1, true);
            List<DividendDTO.Results> call = dividendData.getResults();

            if (dividendData.getResults() != null && !dividendData.getResults().isEmpty()) {



                Double yield = ((call.get(0).getCash_amount() * stock.getDividend()) / stock.getCurrentPrice()) * 100;

                calenders.add(new DividendCalender(
                        stockName,
                        stock.getCompanyName(),
                        call.get(0).getPayDate(),
                        call.get(0).getCash_amount() * stock.getStockQuantity(),
                        stock.getDividend(),
                        yield,
                        call.get(0).getExDate()
                ));
            } else {
                System.out.println("No dividend data");
            }
        }

        return new Dividends(
                fullDividend,
                monthlyDividend,
                dailyDividend,
                hourlyDividend,
                yieldOnCost,
                calenders
        );
    }
}
