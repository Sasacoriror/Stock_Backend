package com.example.stocks.Service;

import com.example.stocks.Calculate.CalculateData;
import com.example.stocks.DTO.BasicStockDataDTO;
import com.example.stocks.DTO.DividendDTO;
import com.example.stocks.DTO.PriceDTO;
import com.example.stocks.Link.Endpoints;
import com.example.stocks.Model.Stocks;
import com.example.stocks.Record.Dividends;
import com.example.stocks.Record.PortfolioSummary;
import com.example.stocks.Record.SearchField;
import com.example.stocks.Respository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
public class RecordService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CalculateData calculateData;

    @Autowired
    private Endpoints endpoints;

    @Autowired
    private API_Service APIService;

    public PortfolioSummary getPortfolioSummary(Long portfolioID){

        List<Stocks> portfolios = stockRepository.findByPortfolioId(portfolioID);

        double totalInvested = 0.0;
        double totalDividend = 0.0;
        double returns = 0.0;
        double value = 0.0;

        double daysChange = 0.0;

        for (Stocks item: portfolios){
            totalInvested += item.getTotalInvested();
            totalDividend += item.getTotalDivided();
            returns += item.getReturnValue();
            value += item.getTotalPrice();

            daysChange = item.getOpeningPrice() * item.getStockQuantity();
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        double totalValue = Double.parseDouble(df.format(totalInvested + returns));
        double dayChangeDollars = Double.parseDouble(df.format(value - daysChange));

        double totalProfit = Double.parseDouble(df.format(value - totalInvested));
        double profitPercentage = Double.parseDouble(df.format(calculateData.percentage(totalProfit, totalInvested)));
        double dayChangePercentage = Double.parseDouble(df.format(calculateData.percentage(dayChangeDollars, value)));

        return new PortfolioSummary(
                totalInvested,
                totalValue,
                totalProfit,
                profitPercentage,
                totalDividend,
                dayChangeDollars,
                dayChangePercentage
        );
    }

    public SearchField getSearchField(String ticker){

        endpoints.setPriceAPI(ticker);
        endpoints.setBasicTickerInfo(ticker);

        CompletableFuture<PriceDTO> priceFuture = CompletableFuture.supplyAsync(APIService::getPriceData);
        CompletableFuture<BasicStockDataDTO> basicsFuture = CompletableFuture.supplyAsync(APIService::getBasicData);

        PriceDTO priceData = priceFuture.join();
        BasicStockDataDTO basicData = basicsFuture.join();

        String companyTicker = ticker;
        String companyName = basicData.getResults().get(0).getName();

        boolean insideWatchlist = true;



        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        double currentPrice = priceData.getResults().get(0).getClosePrice();
        double openingPrice = priceData.getResults().get(0).getOpenPrice();
        double daysChangeDollars = Double.parseDouble(df.format(currentPrice - openingPrice));
        double daysChangePercentage = Double.parseDouble(df.format(calculateData.percentage(daysChangeDollars, openingPrice)));

        return new SearchField(
                companyTicker,
                companyName,
                currentPrice,
                daysChangeDollars,
                daysChangePercentage,
                insideWatchlist
        );
    }

    public Dividends dividendData(){

        List<Stocks> stockData = stockRepository.findAll();

        double fullDividend = 0.0;
        double fullInvestemnt = 0.0;

        for (Stocks stock: stockData){
            fullDividend += stock.getDividend();
            fullInvestemnt += stock.getTotalInvested();
        }

        double monthlyDividend = fullDividend / 12;
        double dailyDividend = fullDividend / 365;
        double hourlyDividend = fullDividend / 8765;
        double yieldOnCost = (fullDividend / fullInvestemnt) * 100;

        return new  Dividends(
                fullDividend,
                monthlyDividend,
                dailyDividend,
                hourlyDividend,
                yieldOnCost
        );
    }
}
