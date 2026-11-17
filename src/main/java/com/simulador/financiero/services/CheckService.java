package com.simulador.financiero.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.simulador.financiero.DTOs.response.CheckResponse;
import com.simulador.financiero.Exceptions.ResourceNotFoundException;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.repositories.CheckRepository;
import com.simulador.financiero.validators.AccountValidator;


@Service 
public class CheckService {
    private final CheckRepository checkRepo;

    public CheckService(CheckRepository checkRepo) {
        this.checkRepo = checkRepo;
    }

    public CheckResponse getAccountBalance(Long userId, String number)
    {
        AccountEntity entity=checkRepo.findById(number).orElseThrow(()-> new ResourceNotFoundException("Account not found"));
        AccountValidator.validateOwnership(entity, userId);
        return new CheckResponse(
            entity.getNumber(),
            entity.getCurrency(),
            entity.getBalance(),
            entity.getCreatedAt()
        );
    }
}