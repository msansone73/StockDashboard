package br.com.msansone.StockDashboard.service;

import br.com.msansone.StockDashboard.exception.ResourceNotFoundException;
import br.com.msansone.StockDashboard.model.Stock;
import br.com.msansone.StockDashboard.model.StockDetailResponse;
import br.com.msansone.StockDashboard.model.Transaction;
import br.com.msansone.StockDashboard.repository.ProventRepository;
import br.com.msansone.StockDashboard.repository.StockRepository;
import br.com.msansone.StockDashboard.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class StockServiceImpl implements StockService{

    private final StockRepository stockRepository;
    private final TransactionRepository transactionRepository;
    private final ProventRepository proventRepository;

    public StockServiceImpl(StockRepository stockRepository, TransactionRepository transactionRepository, ProventRepository proventRepository) {
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;
        this.proventRepository = proventRepository;
    }

     public List<Stock> getAllStocks() {
         return stockRepository.findAll();
     }

    @Override
    public Stock getStockBySymbol(String stock) {
        return stockRepository.findById(stock)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with symbol: " + stock));
    }

    @Override
    public StockDetailResponse getStockDetail(String ticker) {
        Stock stock = getStockBySymbol(ticker);
        List<Transaction> transactions = transactionRepository.findAllByStockTick(ticker).stream().sorted((t1, t2) ->  t2.getDate().compareTo(t1.getDate())).toList();
        List<br.com.msansone.StockDashboard.model.Provents> provents = proventRepository.findAllByStock_Tick(ticker).stream().sorted((t1, t2) ->  t2.getDate().compareTo(t1.getDate())).toList();

        StockDetailResponse response = new StockDetailResponse();
        response.setTicker(stock.getTick());
        response.setTransactions(transactions);
        response.setProvents(provents);

        StockDetailResponse.StockResume resume = new StockDetailResponse.StockResume();
        long quantity = transactions.stream().mapToLong(Transaction::getQuantity).sum();
        BigDecimal totalInvested = transactions.stream().map(Transaction::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        resume.setStockCount(quantity);
        resume.setTotalInvested(totalInvested);
        if (quantity > 0) {
            resume.setAveragePrice(totalInvested.divide(BigDecimal.valueOf(quantity), RoundingMode.HALF_UP));
        }

        resume.setTotalProvents(proventRepository.getTotalProventsByStock(ticker));
        resume.setTotalProventsLast12Months(proventRepository.getTotalProventsByStockLast12Months(ticker, LocalDate.now().minusYears(1)));
        resume.setAverageProventsLast4Months(proventRepository.getAverageProventsByStockLast4Months(ticker, LocalDate.now().minusMonths(4)));

        response.setResume(resume);

        return response;
    }
}