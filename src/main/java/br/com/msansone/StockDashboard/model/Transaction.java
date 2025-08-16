package br.com.msansone.StockDashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(exclude = "id")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbtransactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private LocalDate date; // Date in "dd/MM/yyyy" format
    private String movimentacao; // Transaction type (e.g., "Compra", "Venda")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_tick")
    private Stock stock; // Product name (e.g., "PETR3")
    private BigDecimal untPrice; // Unit price of the stock
    private BigDecimal totalPrice; // Total price of the transaction
    private Long quantity; // Quantity of stocks involved in the transaction
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "institution_id")
    private Institution institution; // Institution name (e.g., "XP Investimentos")
}
