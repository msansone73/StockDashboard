package br.com.msansone.StockDashboard.service;

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

    @Override
    public Provents getProventById(Long id) {
        return proventRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Provents> getProventsByStockSymbol(String stockSymbol) {
        return proventRepository.findAllByStockTick(stockSymbol);
    }
}
