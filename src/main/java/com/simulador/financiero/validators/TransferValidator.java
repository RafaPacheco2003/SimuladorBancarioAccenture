package com.simulador.financiero.validators;

import java.math.BigDecimal;

import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.Exceptions.ForbiddenException;
import com.simulador.financiero.Exceptions.InsufficientBalanceException;
import com.simulador.financiero.account.Status;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.entities.AccountEntity;

public class TransferValidator {

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

    public static void validateDifferentOwner(AccountEntity origin, AccountEntity destination) {
        if (destination.getUser().getId().equals(origin.getUser().getId())) {
            throw new BadRequestException(ExceptionMessageConstants.SAME_OWNER_TRANSFER);
        }
    }

    public static void validateDifferentAccounts(String originNumber, String destinationNumber) {
        if (originNumber.equals(destinationNumber)) {
            throw new BadRequestException(ExceptionMessageConstants.SAME_ACCOUNT_TRANSFER);
        }
    }

    public static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(ExceptionMessageConstants.INVALID_AMOUNT);
        }
    }

    public static void validateSufficientBalance(AccountEntity account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(ExceptionMessageConstants.INSUFFICIENT_BALANCE);
        }
    }

    private TransferValidator() {
        throw new AssertionError("Clase utilitaria, no debe ser instanciada");
    }
}
