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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

        BigDecimal fullDividend = BigDecimal.ZERO;
        BigDecimal fullInvestemnt = BigDecimal.ZERO;

        for (Stocks stock : stockData) {
            fullDividend = fullDividend.add(BigDecimal.valueOf(stock.getTotalDivided()));
            fullInvestemnt = fullInvestemnt.add(BigDecimal.valueOf(stock.getTotalInvested()));
        }

        BigDecimal monthlyDividend = fullDividend.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal dailyDividend = fullDividend.divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);
        BigDecimal hourlyDividend = dailyDividend.divide(BigDecimal.valueOf(24), 10, RoundingMode.HALF_UP);
        BigDecimal yieldOnCost = fullDividend.divide(fullInvestemnt, 10, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

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
                fullDividend.setScale(3, RoundingMode.HALF_UP),
                monthlyDividend.setScale(3, RoundingMode.HALF_UP),
                dailyDividend.setScale(3, RoundingMode.HALF_UP),
                hourlyDividend.setScale(3, RoundingMode.HALF_UP),
                yieldOnCost.setScale(2,RoundingMode.HALF_UP),
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
