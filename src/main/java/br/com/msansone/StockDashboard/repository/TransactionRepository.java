package br.com.msansone.StockDashboard.repository;

import br.com.msansone.StockDashboard.model.Institution;
import br.com.msansone.StockDashboard.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    List<Transaction> findAllByDateAndMovimentacaoAndStockTickAndUntPriceAndTotalPriceAndQuantityAndInstitutionName(LocalDate date, String movimentacao, String tick, BigDecimal untPrice, BigDecimal totalPrice, Long quantity, String institutionName);

    List<Transaction> findAllByStockTick(String tick);
}
