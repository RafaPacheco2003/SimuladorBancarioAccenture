package com.simulador.financiero.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulador.financiero.entities.TempTockenEntity;

public interface TempTokenRepository extends JpaRepository<TempTockenEntity, Long>{
    Optional<TempTockenEntity> findByEmail(String email); //"Busca un TempTockenEntity cuyo campo email sea igual al email que te voy a pasar.
    Optional<TempTockenEntity> findByToken(String token); //"Busca un TempTockenEntity cuyo campo token sea igual al token que te voy a pasar.
    Optional<TempTockenEntity> findByEmailAndToken(String email, String token);
    
}
