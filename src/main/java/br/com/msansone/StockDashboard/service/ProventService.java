package br.com.msansone.StockDashboard.service;

import br.com.msansone.StockDashboard.model.Provents;

import java.util.List;

public interface ProventService {

    List<Provents> getAllProvents();
    
    List<Provents> getProventsByStockSymbol(String stockSymbol);

}
