package com.simulador.financiero.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.ActionEntity;
import com.simulador.financiero.entities.StocksEntity;
@Repository
public interface ActionRepository extends JpaRepository<ActionEntity, Long>{
   Optional<ActionEntity> findByAccountAndStock(
            AccountEntity account,
            StocksEntity stock
    );
}