package br.com.msansone.StockDashboard.repository;

import br.com.msansone.StockDashboard.model.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DividendRepository extends JpaRepository<Dividend,Long> {

    void deleteAllByStock(String stock);

    List<Dividend> findAllByStock(String stockSymbol);

    // List all dividend by stock and with date pag more then today
    List<Dividend> findAllByStockAndDatePagAfter(String stockSymbol, LocalDate date);


    List<Dividend> findAllByDatePagAfter(LocalDate date);

}
