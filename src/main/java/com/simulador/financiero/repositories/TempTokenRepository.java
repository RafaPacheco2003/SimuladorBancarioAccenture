package com.simulador.financiero.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulador.financiero.entities.TempTokenEntity;
import com.simulador.financiero.entities.UserEntity;

public interface TempTokenRepository extends JpaRepository<TempTokenEntity, Long>{
    Optional<TempTokenEntity> findByToken(String token); 
    Optional<TempTokenEntity> findByUser(UserEntity user);
}
