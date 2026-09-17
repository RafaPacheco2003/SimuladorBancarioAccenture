package com.simulador.financiero.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.simulador.financiero.DTOs.external.GlobalQuoteApiResponse;
import com.simulador.financiero.DTOs.external.GlobalQuoteDTO;
import com.simulador.financiero.DTOs.response.StockResponse;
import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.Exceptions.ResourceNotFoundException;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.entities.StocksEntity;
import com.simulador.financiero.mappers.ApiExternaMapper;
import com.simulador.financiero.repositories.StockRespository;
import com.simulador.financiero.services.IConsultarApiExternaService;

@Service
public class ConsultarApiExternaServiceImpl implements IConsultarApiExternaService {

  private static final String FUNCTION_GLOBAL_QUOTE = "GLOBAL_QUOTE";

  private final RestTemplate restTemplate;
  private final StockRespository stockRepository;
  private final ApiExternaMapper apiExternaMapper;
  private final String apiKey;
  private final String baseUrl;

  public ConsultarApiExternaServiceImpl(
      RestTemplate restTemplate,
      StockRespository stockRepository,
      ApiExternaMapper apiExternaMapper,
      @Value("${app.alphavantage.api-key}") String apiKey,
      @Value("${app.alphavantage.base-url:https://www.alphavantage.co/query}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.stockRepository = stockRepository;
    this.apiExternaMapper = apiExternaMapper;
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
  }

  @Override
  public StockResponse consultarYGuardarCotizacion(String symbol) {

    String url = UriComponentsBuilder.fromUriString(baseUrl)
        .queryParam("function", FUNCTION_GLOBAL_QUOTE)
        .queryParam("symbol", symbol)
        .queryParam("apikey", apiKey)
        .toUriString();

    GlobalQuoteApiResponse apiResponse = restTemplate.getForObject(url, GlobalQuoteApiResponse.class);

    if (apiResponse == null
        || apiResponse.note() != null
        || apiResponse.information() != null
        || apiResponse.errorMessage() != null) {
      throw new BadRequestException(ExceptionMessageConstants.EXTERNAL_API_ERROR);
    }

    GlobalQuoteDTO quote = apiResponse.globalQuote();

    if (quote == null || quote.symbol() == null || quote.symbol().isBlank()) {
      throw new ResourceNotFoundException(ExceptionMessageConstants.STOCK_SYMBOL_NOT_FOUND);
    }

    StocksEntity entity = stockRepository.findByTicker(quote.symbol())
        .map(existing -> apiExternaMapper.updateEntity(existing, quote))
        .orElseGet(() -> apiExternaMapper.toEntity(quote));

    StocksEntity saved = stockRepository.save(entity);

    return apiExternaMapper.toResponse(saved);
  }

}
