package com.simulador.financiero.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.simulador.financiero.entities.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

	@Query("""
			SELECT t FROM TransactionEntity t
			WHERE t.originAccount.number = :accountNumber
			   OR t.destinationAccount.number = :accountNumber
			ORDER BY t.date DESC, t.id DESC
			""")
	List<TransactionEntity> findHistoryByAccountNumber(@Param("accountNumber") String accountNumber);

}
