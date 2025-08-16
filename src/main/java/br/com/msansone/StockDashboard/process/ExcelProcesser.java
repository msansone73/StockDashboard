package br.com.msansone.StockDashboard.process;


import br.com.msansone.StockDashboard.model.*;
import br.com.msansone.StockDashboard.repository.InstitutionRepository;
import br.com.msansone.StockDashboard.repository.ProventRepository;
import br.com.msansone.StockDashboard.repository.StockRepository;
import br.com.msansone.StockDashboard.repository.TransactionRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcelProcesser {

    Logger log = Logger.getLogger("ExcelProcesser");


    private final InstitutionRepository institutionRepository;
    private final StockRepository stockRepository;
    private final ProventRepository proventRepository;
    private final TransactionRepository transactionRepository;
    public ExcelProcesser(InstitutionRepository institutionRepository,
                          StockRepository stockRepository,
                          ProventRepository proventRepository,
                          TransactionRepository transactionRepository) {
        this.institutionRepository = institutionRepository;
        this.stockRepository = stockRepository;
        this.proventRepository = proventRepository;
        this.transactionRepository = transactionRepository;
    }


    /**
     * Processes an Excel file and returns the data as a list of string arrays.
     *
     * @param filePath the path to the Excel file
     * @return a list of string arrays, where each array represents a row in the Excel sheet
     */
    public List<String[]> processFile(String filePath) {

        List<String[]> data = new ArrayList<>();
        List<ExcelMoviment> movimentList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                String[] rowData = new String[row.getPhysicalNumberOfCells()];
                for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                    Cell cell = row.getCell(i);
                    rowData[i] = cell.toString();
                }
                if (!rowData[1].equals("Data")){
                    movimentList.add(rowData.length >= 8 ? new ExcelMoviment(rowData) : null);
                }
                data.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map <String, Object> processData(List<String[]> data) {

        Map <String, Object> map = new HashMap<>();
        List<ExcelMoviment> excelMoviments = generateExcelMoviments(data);
        List<Institution> institutions= generateInstitutions(excelMoviments);
        map.put("institutions", institutions);
        List<Stock> stocks = generateStocks(excelMoviments);
        map.put("stocks", stocks);
        List<Provents> provents = generateProvents(excelMoviments);
        map.put("provents", provents);
        List<Transaction> transactions= generateTansactions(excelMoviments,institutions);
        map.put("transactions", transactions);
        return map;
    }

    private List<Transaction> generateTansactions(List<ExcelMoviment> excelMoviments, List<Institution> institutions) {
        List<Transaction> transactions = new ArrayList<>();
        for (ExcelMoviment row : excelMoviments) {
            if (row.getMovimentacao() != null &&
                    row.getMovimentacao().equalsIgnoreCase("Transferência - Liquidação")) {
                try {
                    Transaction transaction = new Transaction(
                            null,
                            row.getDate(),
                            row.getIo(),
                            new Stock(row.getProduto()),
                            row.getPrecoUnitario(),
                            row.getValorOperacao(),
                            row.getQuantidade(),
                            institutions.stream().filter(i -> i.getName().equals(row.getInstituicao())).findFirst().orElse(null)
                    );
                    transactions.add(transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return transactions;
    }

    private List<ExcelMoviment> generateExcelMoviments(List<String[]> data) {
        List<ExcelMoviment> excelMoviments = new ArrayList<>();
        for (String[] row : data) {
            if (row.length < 8 || row[0].equals("IO") || row[1].equals("Data")) {
                continue; // Skip header or incomplete rows
            }
            try {
                ExcelMoviment moviment = new ExcelMoviment(row);
                excelMoviments.add(moviment);
            } catch (Exception e) {
                System.err.println("Error processing row: " + String.join("|", row));
                e.printStackTrace();
            }
        }
        return excelMoviments;
    }

    private List<Provents> generateProvents(List<ExcelMoviment> data) {
        List<Provents> provents = new ArrayList<>();
        for (ExcelMoviment row : data) {
            if (row.getMovimentacao() != null &&(
                    row.getMovimentacao().equalsIgnoreCase("Dividendo")
                    || row.getMovimentacao().equalsIgnoreCase("Juros Sobre Capital Próprio")
                    || row.getMovimentacao().equalsIgnoreCase("Rendimento")
            )
            ) {
                try {
                    Provents provent = new Provents(
                            null,
                            row.getDate(),
                            row.getIo(),
                            new Stock(row.getProduto()),
                            row.getValorOperacao(),
                            row.getQuantidade()
                    );
                    provents.add(provent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return provents;
    }

    private List<Institution> generateInstitutions(List<ExcelMoviment> data) {
        List<Institution> institutions= new ArrayList<>();
        institutions = data.stream()
                .map(row -> row.getInstituicao())
                .distinct()
                .filter(name -> !name.isEmpty())
                .map(name -> new Institution(null, name))
                .toList();
        return institutions;
    }

    private List<Stock> generateStocks(List<ExcelMoviment> data) {
        List<Stock> stocks = new ArrayList<>();
        stocks = data.stream()
                .map(row -> row.getProduto().replace(".", "").trim())
                .distinct()
                .filter(name -> !name.isEmpty())
                .map(name -> new Stock(name))
                .toList();

        Map<String, Stock> mapStocks = new HashMap<String, Stock>();
        stocks.stream().forEach(
                stock -> {
                    if (!mapStocks.containsKey(stock.getTick())) {
                        mapStocks.put(stock.getTick(), stock);
                    }
                }
        );
        ArrayList<Stock> stocksFiltrado = new ArrayList<>(mapStocks.values());
        return stocksFiltrado;
    }

    public void persisitData(Map<String, Object> map) {
        List<Institution> institutions = (List<Institution>) map.get("institutions");
        List<Stock> stocks = (List<Stock>) map.get("stocks");
        List<Provents> provents = (List<Provents>) map.get("provents");
        List<Transaction> transactions = (List<Transaction>) map.get("transactions");

        institutionRepository.saveAll(institutions);
        stockRepository.saveAll(stocks);
        proventRepository.saveAll(provents);
        transactionRepository.saveAll(transactions);
    }

    public void updateData(Map<String, Object> map) {
        updateInstitutions((List<Institution>) map.get("institutions"));
        updateStocks((List<Stock>) map.get("stocks"));
        updateProvents((List<Provents>) map.get("provents"));
        updateTransactions((List<Transaction>) map.get("transactions"));
    }

    private void updateTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            List<Transaction> achou = transactionRepository.findAllByDateAndMovimentacaoAndStockTickAndUntPriceAndTotalPriceAndQuantityAndInstitutionName(
                    transaction.getDate(),
                    transaction.getMovimentacao(),
                    transaction.getStock().getTick(),
                    transaction.getUntPrice(),
                    transaction.getTotalPrice(),
                    transaction.getQuantity(),
                    transaction.getInstitution().getName());
            if (CollectionUtils.isEmpty(achou)) {
                log.info("transaction = "+transaction.toString());
                Stock stock = stockRepository.findByTick(transaction.getStock().getTick());
                transaction.setStock(stock);
                Institution institute = institutionRepository.findByName(transaction.getInstitution().getName());
                transaction.setInstitution(institute);
                transactionRepository.save(transaction);
            }
        }
    }

    private void updateProvents(List<Provents> provents) {
        for (Provents provent : provents) {
            List<Provents> achados = proventRepository.findAllByDateAndTypeAndStockTickAndValueTotalAndQuantity(
                    provent.getDate(),
                    provent.getType(),
                    provent.getStock().getTick(),
                    provent.getValueTotal(),
                    provent.getQuantity());
            if (CollectionUtils.isEmpty(achados)) {
                log.info("provent = "+provent.toString());
                Stock stock = stockRepository.findByTick(provent.getStock().getTick());
                provent.setStock(stock);
                proventRepository.save(provent);
            }
        }
    }

    private void updateStocks(List<Stock> stocks) {
        List<Stock> currentStocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            if (!currentStocks.contains(stock)) {
                stockRepository.save(stock);
            }
        }
    }

    private void updateInstitutions(List<Institution> institutions) {
        List<Institution> currentInstitutions = institutionRepository.findAll();
        for (Institution institution : institutions) {
            if (!currentInstitutions.contains(institution)) {
                institutionRepository.save(institution);
            }
        }
    }
}
