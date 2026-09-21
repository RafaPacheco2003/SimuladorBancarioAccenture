package com.simulador.financiero.validators;

import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.entities.ActionEntity;

public class TradingOrderValidator {

    public static void validateHasActions(ActionEntity action) {
        if (action.getQuantity() <= 0) {
            throw new BadRequestException(ExceptionMessageConstants.NO_ACTIONS_FOR_TICKER);
        }
    }

    public static void validateSufficientActions(ActionEntity action, int quantity) {
        if (action.getQuantity() < quantity) {
            throw new BadRequestException(ExceptionMessageConstants.INSUFFICIENT_ACTIONS);
        }
    }

    private TradingOrderValidator() {
        throw new AssertionError("Clase utilitaria, no debe ser instanciada");
    }
}
