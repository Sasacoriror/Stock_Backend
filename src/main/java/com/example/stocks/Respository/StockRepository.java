package com.example.stocks.Respository;

import com.example.stocks.Model.Stocks;
import com.example.stocks.Record.PortfolioTotals;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stocks, Long> {

    Page<Stocks> findByPortfolioId(Long portfolioId, Pageable pageable);

    List<Stocks> findByPortfolioId(Long portfolioId);

    boolean existsByStockName(String stockName);

    @Query("SELECT SUM(s.totalInvested), SUM(s.totalDivided), SUM(s.returnValue), SUM(s.totalPrice), SUM(s.openingPrice * s.stockQuantity) FROM Stocks s WHERE s.portfolio.id = :id")
    Object[] getPortfolioTotals(@Param("id") Long id);

    /*
    @Query("SELECT new com.example.Record.PortfolioTotals(" +
            "COALESCE(SUM(s.totalInvested),0.0)," +
            "COALESCE(SUM(s.totalDivided),0.0)," +
            "COALESCE(SUM(s.returnValue),0.0)," +
            "COALESCE(SUM(s.totalPrice),0.0)," +
            "COALESCE(SUM(s.openingPrice * 1.0 * s.stockQuantity),0.0)" +
            ") FROM Stocks s WHERE s.portfolio.id = :id")
    PortfolioTotals getPortfolioTotals(@Param("id") Long id);

     */

    @Transactional
    @Query(value = "INSERT INTO stocks (stock_Ticker, stock_price, stock_quantity, portfolio_id) VALUES (:name, :price, :quantity, :portfolioId) RETURNING id", nativeQuery = true)
    Long insertStockNative(@Param("name") String name, @Param("price") double price, @Param("quantity") double quantity, @Param("portfolioId") Long portfolioId);

}
