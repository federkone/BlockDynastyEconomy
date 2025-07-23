package BlockDynasty.Economy.aplication.useCase.account;

import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Balance;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

//TODO: DE MOMENTO ESTE CASO SE USA DE MOMENTO CUANDO UN USUARIO ENTRA AL SERVER, SI NO EXISTE SE CREARA UNA CUENTA.
public class CreateAccountUseCase {
    private final IAccountService accountService;
    private final ICurrencyService currencyService;
    private final IRepository dataStore;
    private final GetAccountsUseCase getAccountsUseCase;

    public CreateAccountUseCase(IAccountService accountService, ICurrencyService currencyService, GetAccountsUseCase getAccountsUseCase, IRepository dataStore) {
        this.accountService = accountService;
        this.currencyService = currencyService;
        this.dataStore = dataStore;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public Result<Account> executeOffline(UUID userUuid , String userName) {
        Result<Account> accountResult =  this.getAccountsUseCase.getAccount(userName);
        if (accountResult.isSuccess()) {
            return Result.failure("Account already exists for: " + accountResult.getValue().getNickname(), ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
        Account account = new Account(userUuid, userName);
        initializeAccountWithDefaultCurrencies(account);
        try {
            this.dataStore.createAccount(account);
            this.accountService.addAccountToCache(account);
        } catch (TransactionException t) {
            return Result.failure("Error creating account for: " + account.getNickname(), ErrorCode.DATA_BASE_ERROR);
        }
        return Result.success(account);
    }


    public Result<Account> execute(UUID userUuid , String userName) {
        Result<Account> accountResult =  this.getAccountsUseCase.getAccount(userUuid);
        if (accountResult.isSuccess()) {
            return Result.failure("Account already exists for: " + accountResult.getValue().getNickname(), ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
        Account account = new Account(userUuid, userName);
        initializeAccountWithDefaultCurrencies(account);
        try {
            this.dataStore.createAccount(account);
            this.accountService.addAccountToCache(account);
        } catch (TransactionException t) {
            return Result.failure("Error creating account for: " + account.getNickname(), ErrorCode.DATA_BASE_ERROR);
        }
        return Result.success(account);
    }

    private void initializeAccountWithDefaultCurrencies(Account account) {
        List<Balance> balances =  this.currencyService.getCurrencies().stream()
                .map(Balance::new)
                .collect(Collectors.toList());
        account.setBalances(balances);
    }

}
