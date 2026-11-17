package com.simulador.financiero.services.impl;

import com.simulador.financiero.services.IStockService;
import com.simulador.financiero.dtos.response.StockResponse;
import com.simulador.financiero.repositories.StockRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements IStockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<StockResponse> consultaAcciones() {
        return stockRepository.findAll()
                    .stream()
                    .map(stock -> new StockResponse(
                        stock.getId(),
                        stock.getTicker(),
                        stock.getCompanyName(),
                        stock.getCurrentPrice(),
                        stock.getUpdatedAt()))
                    .toList();
    }
}
