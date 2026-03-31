package net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces;

import net.blockdynasty.economy.core.domain.result.Result;

import java.math.BigDecimal;
import java.util.UUID;

public interface IExchangeUseCase {
    Result<BigDecimal> execute(UUID accountUuid, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo);
    Result<BigDecimal> execute(String accountString, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo);
}
