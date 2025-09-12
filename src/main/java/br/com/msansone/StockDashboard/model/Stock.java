package br.com.msansone.StockDashboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    private String tick;
    @NotNull
    private String company;

    String name;
    String valorAtual;
    String dividendYield;
    String pvp;
    String valorizacao12M;
    String pl;

    LocalDateTime lastUpdate =  LocalDateTime.now();

    @OneToMany(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    List<Dividend> dividends;

    public Stock(Long id, String ticker, String name, String valorAtual, String dividendYield, String pvp, String valorizacao12M, String pl, LocalDateTime lastUpdate, List<Dividend> dividends) {
        this.tick = ticker;
        this.name = name;
        this.valorAtual = valorAtual;
        this.dividendYield = dividendYield;
        this.pvp = pvp;
        this.valorizacao12M = valorizacao12M;
        this.pl = pl;
        this.lastUpdate = lastUpdate;
        this.dividends = dividends;
    }

    public Stock(String importedTick) {
        this.tick = importedTick.split(" - ")[0].trim();
        // find first "-" and return this index
        int index = importedTick.indexOf(" - ");
        if (index != -1) {
            this.company = importedTick.substring(index + 3).trim();
        } else {
            this.company = importedTick.trim();
        }
    }

}
