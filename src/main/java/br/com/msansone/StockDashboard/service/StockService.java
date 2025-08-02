package br.com.msansone.StockDashboard.service;

import br.com.msansone.StockDashboard.model.Stock;

import java.util.List;

public interface StockService {
    List<Stock> getAllStocks();
    Stock getStockBySymbol(String stock);
}
