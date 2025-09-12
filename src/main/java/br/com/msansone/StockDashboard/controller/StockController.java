package br.com.msansone.StockDashboard.controller;

import br.com.msansone.StockDashboard.model.Stock;
import br.com.msansone.StockDashboard.model.StockDetailResponse;
import br.com.msansone.StockDashboard.service.StockService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = {"https://msansone.com.br/", "http://localhost:4200/"}, allowedHeaders = "*")
public class StockController {

    private final StockService stockService;
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping()
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/{stock}")
    public ResponseEntity<Stock> getStockBySymbol(@PathVariable("stock") String stock) {
        Stock stockData = stockService.getStockBySymbol(stock);
        if (stockData != null) {
            return ResponseEntity.ok(stockData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/detail/{ticker}")
    public ResponseEntity<StockDetailResponse> getStockDetail(@PathVariable("ticker") String ticker) {
        StockDetailResponse stockDetail = stockService.getStockDetail(ticker);
        if (stockDetail != null) {
            return ResponseEntity.ok(stockDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
