package com.example.stocks.Respository;

import com.example.stocks.Model.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stocks, Long> {
}
