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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {

    public enum Status {
        ACTIVE,
        BLOCKED
    }

    public enum TipoCuenta {
        AHORRO,
        CORRIENTE
    }

    public enum Moneda {
        MXN,
        USD
    }

    @Id
    @Column(nullable = false, unique = true, length = 16)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuenta tipoCuenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Moneda moneda;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void generarNumeroTarjeta() {
        if (number == null) {
            Random random = new Random();
            StringBuilder numero = new StringBuilder("3141");

            for (int i = 0; i < 12; i++) {
                numero.append(random.nextInt(10));
            }
            number = numero.toString();
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
