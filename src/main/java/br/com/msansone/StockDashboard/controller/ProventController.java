package br.com.msansone.StockDashboard.controller;

import br.com.msansone.StockDashboard.model.Provents;
import br.com.msansone.StockDashboard.service.ProventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provent")
@CrossOrigin(origins = {"https://msansone.com.br/", "http://localhost:4200/"}, allowedHeaders = "*")
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
