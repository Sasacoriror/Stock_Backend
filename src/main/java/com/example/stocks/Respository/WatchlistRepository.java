package com.example.stocks.Respository;

import com.example.stocks.Model.Watchlist;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    @Query("SELECT w.id FROM Watchlist w WHERE w.stockTickerInn = :stockTickerInn")
    Optional<Long> findIdByStockTickerInn(@Param("stockTickerInn") String stockTickerInn);

}
