package com.example.stocks.Respository;

import com.example.stocks.Model.Stocks;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stocks, Long> {

    List<Stocks> findByPortfolioId(Long portfolioId);

    boolean existsByStockName(String stockName);

    @Transactional
    @Query(value = "INSERT INTO stocks (stock_Ticker, stock_price, stock_quantity, portfolio_id) VALUES (:name, :price, :quantity, :portfolioId) RETURNING id", nativeQuery = true)
    Long insertStockNative(@Param("name") String name, @Param("price") double price, @Param("quantity") int quantity, @Param("portfolioId") Long portfolioId);

/*
    @Modifying
    @Transactional
    @Query("UPDATE Stocks s SET s.currentPrice = :currentPrice, s.dividend = :dividend WHERE s.stockName = :StockName")
    void updateStockData(@Param("StockName") String stockName,
                         @Param("currentPrice") double currentPrice,
                         @Param("dividend") double dividend);
 */

}
