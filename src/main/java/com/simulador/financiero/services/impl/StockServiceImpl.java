package com.simulador.financiero.services.impl;

import com.simulador.financiero.services.IStockService;
import com.simulador.financiero.DTOs.response.StockResponse;
import com.simulador.financiero.repositories.StockRespository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements IStockService {

    private final StockRespository stockRepository;

    @Autowired
    public StockServiceImpl(StockRespository stockRepository) {
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
