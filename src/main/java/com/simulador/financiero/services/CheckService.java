package com.simulador.financiero.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.simulador.financiero.DTOs.response.CheckResponse;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.repositories.CheckRepository;


@Service 
public class CheckService {
    private final CheckRepository checkRepo;

    public CheckService(CheckRepository checkRepo) {
        this.checkRepo = checkRepo;
    }

    public CheckResponse getAccountBalance(String number)
    {
        AccountEntity entity=checkRepo.findById(number).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new CheckResponse(
            entity.getNumber(),
            entity.getCurrency(),
            entity.getBalance(),
            entity.getCreatedAt()
        );
    }
}