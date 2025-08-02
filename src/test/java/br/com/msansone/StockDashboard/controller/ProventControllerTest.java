package br.com.msansone.StockDashboard.controller;

import br.com.msansone.StockDashboard.model.Provents;
import br.com.msansone.StockDashboard.model.Stock;
import br.com.msansone.StockDashboard.service.ProventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProventController.class)
public class ProventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProventService proventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllProvents() throws Exception {
        List<Provents> provents = new ArrayList<>();
        provents.add(new Provents(1L, LocalDate.now(), "JCP", new Stock("PETR4", "Petrobras"), new BigDecimal("100.00"), 100L));
        provents.add(new Provents(2L, LocalDate.now(), "DIVIDENDOS", new Stock("VALE3", "Vale"), new BigDecimal("200.00"), 200L));

        when(proventService.getAllProvents()).thenReturn(provents);

        mockMvc.perform(get("/api/provent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(provents.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    public void testGetProventsByStockSymbol() throws Exception {
        List<Provents> provents = new ArrayList<>();
        provents.add(new Provents(1L, LocalDate.now(), "JCP", new Stock("PETR4", "Petrobras"), new BigDecimal("100.00"), 100L));

        when(proventService.getProventsByStockSymbol("PETR4")).thenReturn(provents);

        mockMvc.perform(get("/api/provent/stock/PETR4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    public void testGetProventsByStockSymbol_NotFound() throws Exception {
        when(proventService.getProventsByStockSymbol("INVALID")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/provent/stock/INVALID"))
                .andExpect(status().isNotFound());
    }
}
