package br.com.msansone.StockDashboard.repository;

import br.com.msansone.StockDashboard.model.Provents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ProventRepository extends JpaRepository<Provents,Long> {

    List<Provents> findAllByStock_Tick(String ticker);
    List<Provents> findAllByDateAndTypeAndStockTickAndValueTotalAndQuantity(LocalDate date, String type, String tick, BigDecimal valueTotal, Long quantity);

    @Query("SELECT SUM(p.valueTotal) FROM Provents p WHERE p.stock.tick = :ticker")
    BigDecimal getTotalProventsByStock(@Param("ticker") String ticker);

    @Query("SELECT SUM(p.valueTotal) FROM Provents p WHERE p.stock.tick = :ticker AND p.date >= :date")
    BigDecimal getTotalProventsByStockLast12Months(@Param("ticker") String ticker, @Param("date") LocalDate date);

    @Query("SELECT AVG(p.valueTotal) FROM Provents p WHERE p.stock.tick = :ticker AND p.date >= :date")
    BigDecimal getAverageProventsByStockLast4Months(@Param("ticker") String ticker, @Param("date") LocalDate date);
}