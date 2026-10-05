package com.simulador.financiero.validators;

import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.Exceptions.ForbiddenException;
import com.simulador.financiero.account.Status;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.entities.AccountEntity;

public class AccountValidator {

    public static void validateIsActive(AccountEntity account) {
        if (account.getStatus() != Status.ACTIVE) {
            throw new BadRequestException(ExceptionMessageConstants.ACCOUNT_NOT_ACTIVE);
        }
    }

    public static void validateOwnership(AccountEntity account, Long userId) {
        if (!account.getUser().getId().equals(userId)) {
            throw new ForbiddenException(ExceptionMessageConstants.ACCOUNT_NOT_OWNED);
        }
    }

    private AccountValidator() {
        throw new AssertionError("Clase utilitaria, no debe ser instanciada");
    }
}
