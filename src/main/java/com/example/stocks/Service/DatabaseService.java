package com.example.stocks.Service;

import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.DTO.ResultsDividendDTO;
import com.example.stocks.DTO.ResultsFinancialDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Respository.StockRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class DatabaseService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PriceService priceService;

    @Transactional
    public void updateStockData() {
        List<Stocks> stocks = stockRepository.findAll();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        for (Stocks stock : stocks) {

            PriceDTO priceData = priceService.getPriceData();
            ResultsDividendDTO dividendData = priceService.getDividendData();
            ResultsFinancialDTO financialData = priceService.getFinancialData();

            Double newPrice = null;
            Double newDividend = null;

            String name = financialData.getResults().get(0).getCompanyName();
            stock.setCompanyName(name);

            if (priceData != null && priceData.getResults() != null && !priceData.getResults().isEmpty()) {
                newPrice = priceData.getResults().get(0).getC();
            }

            if (dividendData != null && dividendData.getResults() != null && !dividendData.getResults().isEmpty()) {
                newDividend = dividendData.getResults().get(0).getCash_amount();
            }

            boolean hasChanges = false;

            if (newPrice != null && !Objects.equals(newPrice, stock.getCurrentPrice())) {
                stock.setCurrentPrice(newPrice);
                stock.setTotalPrice(stock.getStockQuantity() * newPrice);
                hasChanges = true;
            }

            double totalInvested = stock.getStockQuantity() * stock.getStockPrice();
            double returns = (stock.getStockQuantity() * newPrice) - (stock.getStockQuantity() * stock.getStockPrice());
            double percentage = (returns / totalInvested) * 100;

            stock.setTotalInvested(totalInvested);
            stock.setReturnValue(Double.parseDouble(df.format(returns)));
            stock.setPercentageReturn(Double.parseDouble(df.format(percentage)));
            stock.setDividend(dividendData.getResults().get(0).getFrequency());

            if (newDividend != null && !Objects.equals(newDividend, stock.getDividend())) {
                //stock.setDividend(newDividend);
                stock.setTotalDivided(stock.getStockQuantity() * newDividend * dividendData.getResults().get(0).getFrequency());
                hasChanges = true;
            }

            if (hasChanges) {
                stockRepository.save(stock);
                //UpdateDatabase(stock.getStockName());
            }
        }
    }

    @Transactional
    public void UpdateDatabase(String stockName){
        Optional<Stocks> stocks = stockRepository.findById(stockName);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        ResultsDividendDTO dividendData = priceService.getDividendData();

        double dividend = dividendData.getResults().get(0).getCash_amount();
        int shares = stocks.get().getStockQuantity();
        double price = stocks.get().getCurrentPrice();
        int paidPrice = stocks.get().getStockPrice();

        double newTotalDividend = (dividend * 4) * shares;
        double newTotalPrice = (price * shares);
        double newTotalInvested = paidPrice * shares;
        double newReturnValue = (price * shares) - (paidPrice * shares);
        double newPercentageReturn = (newReturnValue / newTotalInvested) * 100;

        stocks.get().setTotalDivided(newTotalDividend);
        stocks.get().setTotalPrice(newTotalPrice);
        stocks.get().setTotalInvested(Double.parseDouble(df.format(newTotalInvested)));
        stocks.get().setReturnValue(Double.parseDouble(df.format(newReturnValue)));
        stocks.get().setPercentageReturn(Double.parseDouble(df.format(newPercentageReturn)));
    }

    @Transactional
    public void addToWatchist(String stockName){

    }
}
