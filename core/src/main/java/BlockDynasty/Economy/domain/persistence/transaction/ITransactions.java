package BlockDynasty.Economy.domain.persistence.transaction;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.result.TransferResult;

import java.math.BigDecimal;

// transactions interface for handling various financial operations
public interface ITransactions {
    Result<TransferResult> transfer(String fromUuid, String toUuid, Currency currency, BigDecimal amount);
    Result<Account> withdraw(String accountUuid, Currency currency, BigDecimal amount);
    Result<Account> deposit(String accountUuid, Currency currency, BigDecimal amount);
    Result<Account> exchange(String fromUuid, Currency fromCurrency, BigDecimal amountFrom,  Currency toCurrency,BigDecimal amountTo);
    Result<TransferResult> trade(String fromUuid, String toUuid, Currency fromCurrency, Currency toCurrency, BigDecimal amountFrom, BigDecimal amountTo);
    Result<Account> setBalance(String accountUuid, Currency currency, BigDecimal amount);
}
