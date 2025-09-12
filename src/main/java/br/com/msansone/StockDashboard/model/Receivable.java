package br.com.msansone.StockDashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receivable {

    private LocalDate data;
    private Stock stock;
    private BigDecimal unitValue;
    private BigDecimal totalValue;
    private Long quantity;

}
