package br.com.msansone.StockDashboard.service;

import br.com.msansone.StockDashboard.model.Provents;
import br.com.msansone.StockDashboard.model.Receivable;
import br.com.msansone.StockDashboard.model.Stock;

import java.time.LocalDate;
import java.util.List;

public interface ProventService {

    List<Provents> getAllProvents();
    
    List<Provents> getProventsByStockSymbol(String stockSymbol);

    Stock getFuturesProventsByStockSymbol(String stockSymbol);

    List<Receivable> getAllReceivableByStockAndDate(String stockSymbol, LocalDate date);

    List<Receivable> getAllReceivableDate(LocalDate date);
}
