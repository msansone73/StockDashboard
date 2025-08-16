package br.com.msansone.StockDashboard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
