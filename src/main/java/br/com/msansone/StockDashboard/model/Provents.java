package br.com.msansone.StockDashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @EqualsAndHashCode.Include
    private LocalDate date;
    @EqualsAndHashCode.Include
    private String type;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_tick")
    @EqualsAndHashCode.Include
    private Stock stock;
    @EqualsAndHashCode.Include
    private BigDecimal valueTotal;
    @EqualsAndHashCode.Include
    private Long quantity;

}
