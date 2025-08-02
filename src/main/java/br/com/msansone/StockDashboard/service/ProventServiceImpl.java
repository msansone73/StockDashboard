package br.com.msansone.StockDashboard.service;

import br.com.msansone.StockDashboard.exception.ResourceNotFoundException;
import br.com.msansone.StockDashboard.model.Provents;
import br.com.msansone.StockDashboard.repository.ProventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProventServiceImpl implements ProventService{

    private final ProventRepository proventRepository;

    public ProventServiceImpl(ProventRepository proventRepository) {
        this.proventRepository = proventRepository;
    }

    @Override
    public List<Provents> getAllProvents() {
        return proventRepository.findAll();
    }

    public Provents getProventById(Long id) {
        return proventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provent not found with id: " + id));
    }

    @Override
    public List<Provents> getProventsByStockSymbol(String stockSymbol) {
        return proventRepository.findAllByStock_Tick(stockSymbol);
    }
}
