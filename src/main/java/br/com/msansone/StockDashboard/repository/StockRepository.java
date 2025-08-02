package br.com.msansone.StockDashboard.repository;

import br.com.msansone.StockDashboard.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock,String> {
}
