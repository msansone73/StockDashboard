package br.com.msansone.StockDashboard.controller;

import br.com.msansone.StockDashboard.model.Provents;
import br.com.msansone.StockDashboard.model.Receivable;
import br.com.msansone.StockDashboard.model.Stock;
import br.com.msansone.StockDashboard.service.ProventService;
import br.com.msansone.StockDashboard.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/provent")
@CrossOrigin(origins = {"https://msansone.com.br/", "http://localhost:4200/"}, allowedHeaders = "*")
public class ProventController {

    private final ProventService proventService;
    private final StockService stockService;

    public ProventController(ProventService proventService, StockService stockService) {
        this.proventService = proventService;
        this.stockService = stockService;
    }

    // Return all provents
    @GetMapping
    public ResponseEntity<List<Provents>> getAllProvents() {
        return ResponseEntity.ok(proventService.getAllProvents());
    }

   // Return all provents by stock symbol
    @GetMapping("/stock/{stockSymbol}")
    public ResponseEntity<List<Provents>> getProventsByStockSymbol(@PathVariable("stockSymbol") String stockSymbol) {
        List<Provents> provents = proventService.getProventsByStockSymbol(stockSymbol);
        if (provents != null && !provents.isEmpty()) {
            List<Provents> proventsSorted = provents.stream().sorted((t1, t2) ->  t1.getDate().compareTo(t2.getDate())).toList();
            return ResponseEntity.ok(proventsSorted);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // return all futures provents by stock
    @GetMapping("/future/{stockSymbol}")
    public ResponseEntity<List<Provents>> getFuturesProventsByStockSymbol(@PathVariable("stockSymbol") String stockSymbol) {
        List<Provents> provents = proventService.getFuturesProventsByStockSymbol(stockSymbol.toUpperCase());

        return ResponseEntity.ok(provents);
    }


    // return all futures provents by stock
    @GetMapping("/future")
    public ResponseEntity<List<Receivable>> getAllFuturesProvents() {

        List<Receivable> receivables = proventService.getAllReceivableDate(LocalDate.now());


        return ResponseEntity.ok(receivables);
    }
}
