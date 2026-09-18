package com.simulador.financiero.repositories;

import com.simulador.financiero.entities.AccountEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {

    Optional<AccountEntity> findByUser_Email(String email);
}