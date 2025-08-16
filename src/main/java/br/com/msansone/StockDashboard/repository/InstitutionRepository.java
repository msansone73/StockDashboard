package br.com.msansone.StockDashboard.repository;

import br.com.msansone.StockDashboard.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    Institution findByName(String name);
}
