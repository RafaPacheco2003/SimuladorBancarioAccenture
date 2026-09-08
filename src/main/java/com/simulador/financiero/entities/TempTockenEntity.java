package com.simulador.financiero.entities;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//tabla para 
@Entity 
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class TempTockenEntity {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String email;
    private LocalDateTime expiration;


}
