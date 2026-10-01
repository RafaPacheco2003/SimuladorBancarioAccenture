package com.simulador.financiero.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulador.financiero.entities.TempTockenEntity;
import com.simulador.financiero.entities.UserEntity;

public interface TempTokenRepository extends JpaRepository<TempTockenEntity, Long>{
    Optional<TempTockenEntity> findByToken(String token); 
    Optional<TempTockenEntity> findByUser(UserEntity user);
}
