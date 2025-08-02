package br.com.msansone.StockDashboard.repository;

import br.com.msansone.StockDashboard.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
