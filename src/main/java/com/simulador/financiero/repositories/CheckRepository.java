package com.simulador.financiero.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulador.financiero.entities.AccountEntity;

@Repository 
public interface CheckRepository extends JpaRepository<AccountEntity, String>{
    
}