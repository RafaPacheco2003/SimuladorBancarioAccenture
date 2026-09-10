package com.simulador.financiero.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@AllArgsConstructor 
@Setter 
@NoArgsConstructor 
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String curp;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public UserEntity(@NotBlank(message = "El nombre es obligatorio") String fullName,
        @NotBlank(message = "El CURP es obligatorio") String curp,
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email debe tener un formato válido") String email,
        @NotBlank(message = "La contraseña es obligatoria") @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres") String password,
        String phone) {
        this.fullName = fullName;
        this.curp = curp;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = UserStatus.ACTIVE;
        }

        if (saldo == null) {
            saldo = BigDecimal.ZERO;
        }
    }

    
    
}
