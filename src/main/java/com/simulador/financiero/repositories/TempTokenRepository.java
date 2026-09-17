package com.simulador.financiero.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulador.financiero.entities.TempTockenEntity;
import com.simulador.financiero.entities.UserEntity;

public interface TempTokenRepository extends JpaRepository<TempTockenEntity, Long>{
    Optional<TempTockenEntity> findByToken(String token); //"Busca un TempTockenEntity cuyo campo token sea igual al token que te voy a pasar.
    Optional<TempTockenEntity> findByUser(UserEntity user); //"Busca el token vigente del usuario; a lo mucho hay uno.
}
