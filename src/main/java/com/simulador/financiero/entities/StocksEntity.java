package com.simulador.financiero.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 

public class StocksEntity {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="destination_ticker", nullable=false)
    private String ticker;

    @Column(name="company_name", nullable=false)
    private String companyName;

    @Column(name="current_price", nullable=false)
    private BigDecimal currentPrice;

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;
    
    @PrePersist 
    public void verification(){
        if(currentPrice.compareTo(BigDecimal.ZERO)< 0){
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

    }

}