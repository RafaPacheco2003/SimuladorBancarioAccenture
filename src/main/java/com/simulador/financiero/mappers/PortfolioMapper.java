package com.simulador.financiero.mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.simulador.financiero.DTOs.response.ActivePositionResponse;
import com.simulador.financiero.entities.ActionEntity;

@Component
public class PortfolioMapper {

    public ActivePositionResponse toPosition(ActionEntity action) {
        BigDecimal quantity = BigDecimal.valueOf(action.getQuantity());
        BigDecimal averageBuyPrice = action.getAverage();
        BigDecimal currentPrice = action.getStock().getCurrentPrice();

        BigDecimal currentValue = currentPrice.multiply(quantity);
        BigDecimal costBasis = averageBuyPrice.multiply(quantity);
        BigDecimal gainLoss = currentValue.subtract(costBasis);

        return new ActivePositionResponse(
                action.getStock().getTicker(),
                action.getStock().getCompanyName(),
                action.getQuantity(),
                averageBuyPrice,
                currentPrice,
                currentValue,
                gainLoss,
                percentageOf(gainLoss, costBasis));
    }

    private BigDecimal percentageOf(BigDecimal amount, BigDecimal base) {
        if (base.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return amount.divide(base, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
