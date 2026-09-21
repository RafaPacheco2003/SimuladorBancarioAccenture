package com.simulador.financiero.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simulador.financiero.DTOs.response.PortfolioSummaryResponse;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.ActionEntity;
import com.simulador.financiero.mappers.PortfolioMapper;
import com.simulador.financiero.repositories.ActionRepository;
import com.simulador.financiero.services.IAccountService;
import com.simulador.financiero.services.IPortfolioService;
import com.simulador.financiero.validators.AccountValidator;

@Service
public class PortfolioServiceImpl implements IPortfolioService {

    private final IAccountService accountService;
    private final ActionRepository actionRepository;
    private final PortfolioMapper portfolioMapper;

    public PortfolioServiceImpl(
            IAccountService accountService,
            ActionRepository actionRepository,
            PortfolioMapper portfolioMapper) {
        this.accountService = accountService;
        this.actionRepository = actionRepository;
        this.portfolioMapper = portfolioMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PortfolioSummaryResponse getPortfolioSummary(Long userId, String accountNumber) {
        AccountEntity account = accountService.findByNumber(accountNumber);
        AccountValidator.validateOwnership(account, userId);

        List<ActionEntity> actions = actionRepository.findByAccount(account);
        return portfolioMapper.toSummary(account, actions);
    }
}
