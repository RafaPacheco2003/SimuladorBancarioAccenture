package com.simulador.financiero.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulador.financiero.entities.TradingOrdersEntity;

public interface TradingOrdersRepository extends JpaRepository<TradingOrdersEntity, Long>{
    
}
