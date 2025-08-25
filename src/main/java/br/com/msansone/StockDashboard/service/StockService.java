package br.com.msansone.StockDashboard.service;

import br.com.msansone.StockDashboard.model.Stock;
import br.com.msansone.StockDashboard.model.StockDetailResponse;

import java.util.List;

public interface StockService {
    List<Stock> getAllStocks();
    Stock getStockBySymbol(String stock);
    StockDetailResponse getStockDetail(String ticker);
}