package com.simulador.financiero.actionsTable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simulador.financiero.entities.StocksEntity;
import com.simulador.financiero.repositories.StockRespository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadStocks(StockRespository stockRepository) {
        return args -> {

            if (stockRepository.count() == 0) {

                stockRepository.save(
                    new StocksEntity(
                        null,
                        "AAPL",
                        "Apple Inc.",
                        BigDecimal.valueOf(220),
                        LocalDateTime.now()
                    )
                );

                stockRepository.save(
                    new StocksEntity(
                        null,
                        "TSLA",
                        "Tesla Inc.",
                        BigDecimal.valueOf(390),
                        LocalDateTime.now()
                    )
                );

                stockRepository.save(
                    new StocksEntity(
                        null,
                        "MSFT",
                        "Microsoft Corp.",
                        BigDecimal.valueOf(500),
                        LocalDateTime.now()
                    )
                );

                stockRepository.save(
                    new StocksEntity(
                        null,
                        "AMZN",
                        "Amazon",
                        BigDecimal.valueOf(230),
                        LocalDateTime.now()
                    )
                );

                stockRepository.save(
                    new StocksEntity(
                        null,
                        "WMT",
                        "Walmart",
                        BigDecimal.valueOf(100),
                        LocalDateTime.now()
                    )
                );


            }
        };
    }
}