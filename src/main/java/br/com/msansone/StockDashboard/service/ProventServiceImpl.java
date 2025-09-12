package br.com.msansone.StockDashboard.service;

import br.com.msansone.StockDashboard.exception.ResourceNotFoundException;
import br.com.msansone.StockDashboard.model.*;
import br.com.msansone.StockDashboard.repository.*;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ProventServiceImpl implements ProventService{

    private Logger logger = Logger.getLogger("ProventServiceImpl");

    private final int QUANTIDADE_DIAS_PARA_SCRAPPING = 10;

    private final ProventRepository proventRepository;
    private final StockResearchI10 stockResearchI10;
    private final DividendRepository dividendRepository;
    private final StockRepository stockRepository;
    private final TransactionRepository transactionRepository;


    public ProventServiceImpl(ProventRepository proventRepository, StockResearchI10 stockResearchI10, DividendRepository dividendRepository, StockRepository stockRepository, TransactionRepository transactionRepository) {
        this.proventRepository = proventRepository;
        this.stockResearchI10 = stockResearchI10;
        this.dividendRepository = dividendRepository;
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;
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

    @Override
    @Transactional
    public Stock getFuturesProventsByStockSymbol(String stockSymbol) {
        logger.info("getFuturesProventsByStockSymbol -> "+stockSymbol);
        Stock stockLido = stockRepository.findByTick(stockSymbol);
        if (stockLido==null){
            logger.info("getFuturesProventsByStockSymbol -> "+stockSymbol+ " - not found");
            return null;
        }
        if (stockLido.getLastUpdate()!=null &&
                LocalDateTime.now().minusDays(QUANTIDADE_DIAS_PARA_SCRAPPING).isAfter(stockLido.getLastUpdate())){

            logger.info("getFuturesProventsByStockSymbol -> "+stockSymbol+" - found - not using cache, go search");
            Stock stock = stockResearchI10.getStockInfo("acoes", stockSymbol);
            if (stock==null){
                return null;
            }
            stockLido.setPl(stock.getPl());
            stockLido.setDividendYield(stock.getDividendYield());
            stockLido.setPvp(stock.getPvp());
            stockLido.setValorAtual(stock.getValorAtual());
            stockLido.setName(stock.getName());
            stockLido.setLastUpdate(LocalDateTime.now());
            stockRepository.save(stockLido);
            if (CollectionUtils.isNotEmpty(stock.getDividends())){
                dividendRepository.deleteAllByStock(stockSymbol);
                dividendRepository.saveAll(stock.getDividends());
            }
            stock.setDividends(stockLido.getDividends().stream().filter(d -> d.getDatePag().isAfter(LocalDate.now())).toList());
            return stock;
        }
        logger.info("getFuturesProventsByStockSymbol -> "+stockSymbol+" - found - using cache");
        List<Dividend> dividendos = dividendRepository.findAllByStock(stockSymbol);
        stockLido.setDividends(dividendos.stream().
                filter(d-> d.getDatePag().isAfter(LocalDate.now())).toList());
        return stockLido;
    }

    @Override
    public List<Receivable> getAllReceivableByStockAndDate(String stockSymbol, LocalDate date) {
        // todo: implementar

        return List.of();
    }

    @Override
    public List<Receivable> getAllReceivableDate(LocalDate date) {
        List<Dividend> dividends = dividendRepository.findAllByDatePagAfter(date);
        List<Transaction> transactions = transactionRepository.findAll();
        List<Receivable> receivables = new ArrayList<>();

        for (Dividend dividend : dividends) {

            Long qtd=0L;
            for (Transaction transaction : transactions){
                if (transaction.getStock()==null){
                    continue;
                }
                if (transaction.getStock().getTick().equals(dividend.getStock()) &&
                                transaction.getDate().isBefore(date)){
                    qtd+=transaction.getQuantity();
                }
            }




            receivables.add(new Receivable(
                    dividend.getDatePag(),
                    new Stock(dividend.getStock()),
                    dividend.getValor(),
                    dividend.getValor().multiply(BigDecimal.valueOf(qtd)),
                    qtd
            ));
        }


        return receivables;
    }
}
