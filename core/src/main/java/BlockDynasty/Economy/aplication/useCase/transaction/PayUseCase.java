package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.PayEvent;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.TransferResult;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class PayUseCase extends TransferFundsUseCase {
    private final Log economyLogger;
    private final EventManager eventManager;
    public PayUseCase(SearchCurrencyUseCase searchCurrencyUseCase, SearchAccountUseCase searchAccountUseCase, IAccountService accountService,IRepository dataStore,
                      Courier updateForwarder, Log economyLogger, EventManager eventManager) {
        super( searchCurrencyUseCase, searchAccountUseCase, accountService,dataStore, updateForwarder, economyLogger, eventManager);
        this.economyLogger = economyLogger;
        this.eventManager = eventManager;
    }

    @Override
    protected void logSuccess(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount) {
        this.economyLogger.log("[PAY] Account: " + accountFrom.getNickname() + " paid " + currency.format(amount) + " to " + accountTo.getNickname());
    }
    @Override
    protected void logFailure(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount, Result<TransferResult> result) {
        this.economyLogger.log("[PAY Failed] Account: " + accountFrom.getNickname() + " paid " + currency.format(amount) + " to " + accountTo.getNickname() + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
    }
    @Override
    protected void emitEvent(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount) {
        this.eventManager.emit(new PayEvent(accountFrom.getPlayer(), accountTo.getPlayer(), currency, amount));
    }
}
