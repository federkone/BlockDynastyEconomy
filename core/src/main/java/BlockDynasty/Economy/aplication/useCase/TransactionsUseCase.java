package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.transaction.*;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;

public class TransactionsUseCase {
    private final WithdrawUseCase withdrawUseCase;
    private final DepositUseCase depositUseCase;
    private final SetBalanceUseCase setBalanceUseCase;
    private final PayUseCase payUseCase;
    private final ExchangeUseCase exchangeUseCase;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase;
    private final TransferFundsUseCase transferFundsUseCase;

    public TransactionsUseCase(CurrencyUseCase currencyUseCase, AccountsUseCase accountsUseCase, IAccountService accountService,
                               IRepository repository, Courier courier, Log economyLogger, EventManager eventManager) {
        this.withdrawUseCase = new WithdrawUseCase( currencyUseCase.getGetCurrencyUseCase() ,
                accountsUseCase.getGetAccountsUseCase(),
                accountService,
                repository,
                courier,
                economyLogger,
                eventManager);
        this.depositUseCase = new DepositUseCase( currencyUseCase.getGetCurrencyUseCase(),
                accountsUseCase.getGetAccountsUseCase(),
                accountService,
                repository,
                courier,
                economyLogger,
                eventManager);
        this.setBalanceUseCase = new SetBalanceUseCase( currencyUseCase.getGetCurrencyUseCase(),
                accountsUseCase.getGetAccountsUseCase(),
                accountService,
                repository,
                courier,
                economyLogger,
                eventManager);
        this.payUseCase = new PayUseCase(currencyUseCase.getGetCurrencyUseCase(),
                accountsUseCase.getGetAccountsUseCase(),
                accountService,
                repository,
                courier,
                economyLogger,
                eventManager);
        this.exchangeUseCase = new ExchangeUseCase( currencyUseCase.getGetCurrencyUseCase(),
                accountsUseCase.getGetAccountsUseCase(),
                accountService,
                repository,
                courier,
                economyLogger,
                eventManager);
        this.tradeCurrenciesUseCase = new TradeCurrenciesUseCase( currencyUseCase.getGetCurrencyUseCase(),
                accountsUseCase.getGetAccountsUseCase(),
                accountService,
                repository,
                courier,
                economyLogger,
                eventManager);
        this.transferFundsUseCase = new TransferFundsUseCase(currencyUseCase.getGetCurrencyUseCase(),
                accountsUseCase.getGetAccountsUseCase(),
                accountService,
                repository,
                courier,
                economyLogger,
                eventManager);
    }

    public WithdrawUseCase getWithdrawUseCase() {
        return withdrawUseCase;
    }

    public DepositUseCase getDepositUseCase() {
        return depositUseCase;
    }

    public SetBalanceUseCase getSetBalanceUseCase() {
        return setBalanceUseCase;
    }

    public PayUseCase getPayUseCase() {
        return payUseCase;
    }

    public ExchangeUseCase getExchangeUseCase() {
        return exchangeUseCase;
    }

    public TradeCurrenciesUseCase getTradeCurrenciesUseCase() {
        return tradeCurrenciesUseCase;
    }

    public TransferFundsUseCase getTransferFundsUseCase() {
        return transferFundsUseCase;
    }
}
