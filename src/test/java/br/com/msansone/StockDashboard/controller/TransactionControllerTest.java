package br.com.msansone.StockDashboard.controller;

import br.com.msansone.StockDashboard.model.Institution;
import br.com.msansone.StockDashboard.model.Stock;
import br.com.msansone.StockDashboard.model.Transaction;
import br.com.msansone.StockDashboard.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        Stock petr4 = new Stock();
        petr4.setTick("PETR4");
        petr4.setCompany("Petrobras");
        transactions.add(new Transaction(1L, LocalDate.now(), "Compra", petr4, new BigDecimal("28.00"), new BigDecimal("2800.00"), 100L, new Institution(1L, "XP Investimentos")));

        Stock vale3 = new Stock();
        vale3.setTick("VALE3");
        vale3.setCompany("Vale");
        transactions.add(new Transaction(2L, LocalDate.now(), "Venda", vale3, new BigDecimal("80.00"), new BigDecimal("8000.00"), 100L, new Institution(1L, "XP Investimentos")));

        when(transactionService.getAllTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/api/transaction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(transactions.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    public void testGetTransactionById() throws Exception {
        Stock petr4 = new Stock();
        petr4.setTick("PETR4");
        petr4.setCompany("Petrobras");
        Transaction transaction = new Transaction(1L, LocalDate.now(), "Compra", petr4, new BigDecimal("28.00"), new BigDecimal("2800.00"), 100L, new Institution(1L, "XP Investimentos"));

        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        mockMvc.perform(get("/api/transaction/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testCreateTransaction() throws Exception {
        Stock petr4 = new Stock();
        petr4.setTick("PETR4");
        petr4.setCompany("Petrobras");
        Transaction transaction = new Transaction(null, LocalDate.now(), "Compra", petr4, new BigDecimal("28.00"), new BigDecimal("2800.00"), 100L, new Institution(1L, "XP Investimentos"));
        Transaction savedTransaction = new Transaction(1L, LocalDate.now(), "Compra", petr4, new BigDecimal("28.00"), new BigDecimal("2800.00"), 100L, new Institution(1L, "XP Investimentos"));

        when(transactionService.saveTransaction(any(Transaction.class))).thenReturn(savedTransaction);

        mockMvc.perform(post("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        Stock petr4 = new Stock();
        petr4.setTick("PETR4");
        petr4.setCompany("Petrobras");
        Transaction transaction = new Transaction(1L, LocalDate.now(), "Compra", petr4, new BigDecimal("29.00"), new BigDecimal("2900.00"), 100L, new Institution(1L, "XP Investimentos"));

        when(transactionService.saveTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(put("/api/transaction/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.untPrice").value(29.00));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1L);

        mockMvc.perform(delete("/api/transaction/1"))
                .andExpect(status().isNoContent());
    }
}
