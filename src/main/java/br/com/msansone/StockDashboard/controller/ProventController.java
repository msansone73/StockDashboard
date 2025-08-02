package br.com.msansone.StockDashboard.controller;

import br.com.msansone.StockDashboard.model.Provents;
import br.com.msansone.StockDashboard.service.ProventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/provent")
public class ProventController {

    private final ProventService proventService;

    public ProventController(ProventService proventService) {
        this.proventService = proventService;
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
            return ResponseEntity.ok(provents);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
