package com.simulador.financiero.repositories;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {}
