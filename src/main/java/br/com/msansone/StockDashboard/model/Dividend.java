package br.com.msansone.StockDashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@EqualsAndHashCode
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dividend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String stock;
    private String type;
    private LocalDate dateCom;
    private LocalDate datePag;
    private BigDecimal valor;

    public Dividend(String stock, String type, LocalDate dateCom, LocalDate datePag, BigDecimal valor) {
        this.stock = stock;
        this.type = type;
        this.dateCom = dateCom;
        this.datePag = datePag;
        this.valor = valor;
    }
}
