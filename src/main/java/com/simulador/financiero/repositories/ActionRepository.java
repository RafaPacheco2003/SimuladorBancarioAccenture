package com.simulador.financiero.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

   @Query("SELECT a FROM ActionEntity a JOIN FETCH a.stock WHERE a.account = :account")
   List<ActionEntity> findByAccount(@Param("account") AccountEntity account);
}