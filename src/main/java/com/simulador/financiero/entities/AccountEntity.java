package com.simulador.financiero.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.simulador.financiero.account.AccountType;
import com.simulador.financiero.account.Currency;
import com.simulador.financiero.account.Status;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {
    @Id
    @Column(nullable = false, unique = true, length = 16)
    private String number;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @PrePersist
    private void generateAccountNumber() {

        if (number == null) {
            Random random = new Random();
            StringBuilder numberBuilder = new StringBuilder("3141");

            for (int i = 0; i < 12; i++) {
                numberBuilder.append(random.nextInt(10));
            }

            number = numberBuilder.toString();
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = Status.ACTIVE;
        }

        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }
}