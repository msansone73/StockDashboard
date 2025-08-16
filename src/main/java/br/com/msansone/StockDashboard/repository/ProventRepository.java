package br.com.msansone.StockDashboard.repository;

import br.com.msansone.StockDashboard.model.Provents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ProventRepository extends JpaRepository<Provents,Long> {

    List<Provents> findAllByStock_Tick(String ticker);
    List<Provents> findAllByDateAndTypeAndStockTickAndValueTotalAndQuantity(LocalDate date, String type, String tick, BigDecimal valueTotal, Long quantity);
}

