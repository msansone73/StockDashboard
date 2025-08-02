package br.com.msansone.StockDashboard.controller;

import br.com.msansone.StockDashboard.exception.ResourceNotFoundException;
import br.com.msansone.StockDashboard.model.Stock;
import br.com.msansone.StockDashboard.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllStocks() throws Exception {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("PETR4", "Petrobras"));
        stocks.add(new Stock("VALE3", "Vale"));

        when(stockService.getAllStocks()).thenReturn(stocks);

        mockMvc.perform(get("/api/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(stocks.size()))
                .andExpect(jsonPath("$[0].tick").value("PETR4"))
                .andExpect(jsonPath("$[1].tick").value("VALE3"));
    }

    @Test
    public void testGetStockBySymbol() throws Exception {
        Stock stock = new Stock("PETR4", "Petrobras");

        when(stockService.getStockBySymbol("PETR4")).thenReturn(stock);

        mockMvc.perform(get("/api/stock/PETR4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tick").value("PETR4"))
                .andExpect(jsonPath("$.company").value("Petrobras"));
    }

    @Test
    public void testGetStockBySymbol_NotFound() throws Exception {
        when(stockService.getStockBySymbol("INVALID")).thenThrow(new ResourceNotFoundException("Stock not found"));

        mockMvc.perform(get("/api/stock/INVALID"))
                .andExpect(status().isNotFound());
    }
}
