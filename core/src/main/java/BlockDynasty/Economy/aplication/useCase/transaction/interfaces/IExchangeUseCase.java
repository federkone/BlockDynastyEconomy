package BlockDynasty.Economy.aplication.useCase.transaction.interfaces;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;

import java.math.BigDecimal;
import java.util.UUID;

public interface IExchangeUseCase {
    Result<BigDecimal> execute(UUID accountUuid, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo);
    Result<BigDecimal> execute(String accountString, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo);
}
