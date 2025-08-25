package br.com.msansone.StockDashboard.model;

import java.math.BigDecimal;
import java.util.List;

public class StockDetailResponse {

    private String ticker;
    private StockResume resume;
    private List<Transaction> transactions;
    private List<Provents> provents;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public StockResume getResume() {
        return resume;
    }

    public void setResume(StockResume resume) {
        this.resume = resume;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Provents> getProvents() {
        return provents;
    }

    public void setProvents(List<Provents> provents) {
        this.provents = provents;
    }

    public static class StockResume {
        private Long stockCount;
        private BigDecimal totalInvested;
        private BigDecimal averagePrice;
        private BigDecimal totalProvents;
        private BigDecimal totalProventsLast12Months;
        private BigDecimal averageProventsLast4Months;

        public Long getStockCount() {
            return stockCount;
        }

        public void setStockCount(Long stockCount) {
            this.stockCount = stockCount;
        }

        public BigDecimal getTotalInvested() {
            return totalInvested;
        }

        public void setTotalInvested(BigDecimal totalInvested) {
            this.totalInvested = totalInvested;
        }

        public BigDecimal getAveragePrice() {
            return averagePrice;
        }

        public void setAveragePrice(BigDecimal averagePrice) {
            this.averagePrice = averagePrice;
        }

        public BigDecimal getTotalProvents() {
            return totalProvents;
        }

        public void setTotalProvents(BigDecimal totalProvents) {
            this.totalProvents = totalProvents;
        }

        public BigDecimal getTotalProventsLast12Months() {
            return totalProventsLast12Months;
        }

        public void setTotalProventsLast12Months(BigDecimal totalProventsLast12Months) {
            this.totalProventsLast12Months = totalProventsLast12Months;
        }

        public BigDecimal getAverageProventsLast4Months() {
            return averageProventsLast4Months;
        }

        public void setAverageProventsLast4Months(BigDecimal averageProventsLast4Months) {
            this.averageProventsLast4Months = averageProventsLast4Months;
        }
    }
}
