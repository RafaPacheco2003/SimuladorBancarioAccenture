package com.simulador.financiero.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulador.financiero.entities.UserTokenEntity;

public interface UserTokenRepository extends JpaRepository<UserTokenEntity, Long> {

    Optional<UserTokenEntity> findByToken(String token);

}
