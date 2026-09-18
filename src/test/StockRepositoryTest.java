package com.simulador.financiero;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.simulador.financiero.entities.StocksEntity;
import com.simulador.financiero.repositories.StockRespository;

@DataJpaTest
class StockRepositoryTest {

    @Autowired
    private StockRespository stockRepository;

    @Test
    void shouldFindTickerIgnoringCase() {
        StocksEntity stock = new StocksEntity();
        stock.setTicker("AAPL");
        stock.setCompanyName("Apple Inc.");
        stock.setCurrentPrice(BigDecimal.valueOf(220.00));
        stock.setUpdatedAt(LocalDateTime.now());
        stockRepository.save(stock);

        assertThat(stockRepository.findByTickerIgnoreCase("aapl")).isPresent();
        assertThat(stockRepository.findByTickerIgnoreCase("AAPL").orElseThrow().getTicker())
                .isEqualTo("AAPL");
    }
}