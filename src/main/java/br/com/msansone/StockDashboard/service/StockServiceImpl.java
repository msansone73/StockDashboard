package br.com.msansone.StockDashboard.service;

import br.com.msansone.StockDashboard.exception.ResourceNotFoundException;
import br.com.msansone.StockDashboard.model.Stock;
import br.com.msansone.StockDashboard.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService{

    private final StockRepository stockRepository;
    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

     public List<Stock> getAllStocks() {
         return stockRepository.findAll();
     }

    @Override
    public Stock getStockBySymbol(String stock) {
        return stockRepository.findById(stock)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with symbol: " + stock));
    }
}
