package com.example.stocks.Service;

import com.example.stocks.Calculate.CalculateData;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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

    @Autowired
    private CalculateData calculateData;

    public Dividends dividendData() {

        List<Stocks> stockData = stockRepository.findAll();

        double fullDividend = 0.0;
        double fullInvestemnt = 0.0;

        for (Stocks stock : stockData) {
            fullDividend += stock.getTotalDivided();
            fullInvestemnt += stock.getTotalInvested();
        }

        double monthlyDividend = (fullDividend / 12);
        double dailyDividend = (fullDividend / 365);
        double hourlyDividend = (fullDividend / 8765);
        double yieldOnCost = (fullDividend / fullInvestemnt) * 100;

        List<DividendCalender> calenders = new ArrayList<>();

        for (Stocks stock: stockData){

            String stockName = stock.getStockName();
            endpoints.setDividendAPI(stockName, 1);


            DividendDTO dividendData = APIService.getDividendData(stockName, 1, true);
            List<DividendDTO.Results> call = dividendData.getResults();

            if (call != null && !call.isEmpty()) {

                DividendDTO.Results closest = call.get(0);

                String isPaid = isDividendPaid(closest.getPayDate());

                Double yield = ((closest.getCash_amount() * stock.getDividend()) / stock.getCurrentPrice()) * 100;

                calenders.add(new DividendCalender(
                        stockName,
                        stock.getCompanyName(),
                        closest.getPayDate(),
                        closest.getCash_amount() * stock.getStockQuantity(),
                        stock.getDividend(),
                        calculateData.roundNumbers(yield),
                        closest.getExDate(),
                        isPaid
                ));
            } else {
                System.out.println("No dividend data");
            }
        }

        calenders.sort(Comparator.comparing(c -> LocalDate.parse(c.getPaymentDate())));

        return new Dividends(
                fullDividend,
                calculateData.roundNumbers(monthlyDividend),
                calculateData.roundNumbers(dailyDividend),
                calculateData.roundNumbers(hourlyDividend),
                calculateData.roundNumbers(yieldOnCost),
                calenders
        );
    }

    public String isDividendPaid(String date) {
        LocalDate dividendDate = LocalDate.parse(date);
        LocalDate today = LocalDate.now();

        if (dividendDate.isBefore(today)){
            return "Paid";
        } else {
            return "Un-Paid";
        }
    }
}
