package com.simulador.financiero.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulador.financiero.entities.StocksEntity;

@Repository
public interface StockRespository extends JpaRepository<StocksEntity, Long> {

    Optional<StocksEntity> findByTickerIgnoreCase(String ticker);
}