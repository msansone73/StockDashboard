package br.com.msansone.StockDashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExcelMoviment {

    private String io;
    private LocalDate date;
    private String movimentacao;
    private String produto;
    private String instituicao;
    private Long quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal valorOperacao;


    public ExcelMoviment(String[] row) {
        if (row.length < 8) {
            throw new IllegalArgumentException("Row must contain at least 8 elements");
        }
        this.io = row[0];
        this.date = LocalDate.parse(row[1], java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.movimentacao = row[2];
        this.produto = row[3];
        this.instituicao = row[4];
        this.quantidade = toLong(row[5]);
        this.precoUnitario = new BigDecimal(row[6].equals("-") ? "0" : row[6].replace(",", "."));
        this.valorOperacao = new BigDecimal(row[7].equals("-") ? "0" : row[7].replace(",", "."));

    }


    private long toLong(String value) {
        if (value == null || value.isEmpty() || value.equals("-")) {
            return 0L;
        }
        Double doubleValue = Double.parseDouble(value.replace(",", "."));
        return doubleValue.intValue();
    }
}
