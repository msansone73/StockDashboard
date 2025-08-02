package br.com.msansone.StockDashboard.repository;

import br.com.msansone.StockDashboard.model.Provents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProventRepository extends JpaRepository<Provents,Long> {

    List<Provents> findAllByStockTick(String ticker);

    // get all provents for a specific stock, month and year from date
//    List<Provents> findByTickerAndDateBetween(String ticker, String startDate, String endDate);

}
