package com.simulador.financiero.mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.simulador.financiero.DTOs.response.PortfolioSummaryResponse;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.ActionEntity;

@Component
public class PortfolioMapper {

    public PortfolioSummaryResponse.Position toPosition(ActionEntity action) {
        BigDecimal quantity = BigDecimal.valueOf(action.getQuantity());
        BigDecimal averageBuyPrice = action.getAverage();
        BigDecimal currentPrice = action.getStock().getCurrentPrice();

        BigDecimal currentValue = currentPrice.multiply(quantity);
        BigDecimal costBasis = averageBuyPrice.multiply(quantity);
        BigDecimal gainLoss = currentValue.subtract(costBasis);


        return new PortfolioSummaryResponse.Position(
                action.getStock().getTicker(),
                action.getStock().getCompanyName(),
                action.getQuantity(),
                averageBuyPrice,
                currentPrice,
                currentValue,
                gainLoss,
                percentageOf(gainLoss, costBasis));
    }

    public PortfolioSummaryResponse toSummary(AccountEntity account, List<ActionEntity> actions) {
        List<PortfolioSummaryResponse.Position> positions = actions.stream()
                .map(this::toPosition)
                .collect(Collectors.toList());

        BigDecimal investedValue = positions.stream()
                .map(PortfolioSummaryResponse.Position::currentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalGainLoss = positions.stream()
                .map(PortfolioSummaryResponse.Position::gainLoss)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCostBasis = investedValue.subtract(totalGainLoss);

        return new PortfolioSummaryResponse(
                positions,
                investedValue,
                account.getBalance(),
                percentageOf(totalGainLoss, totalCostBasis));
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
