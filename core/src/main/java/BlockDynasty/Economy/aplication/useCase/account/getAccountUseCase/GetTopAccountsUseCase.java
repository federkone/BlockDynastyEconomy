package BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;

import java.util.List;

public class GetTopAccountsUseCase {
    private final IAccountService accountService;
    private final IRepository dataStore;

    public GetTopAccountsUseCase (IAccountService accountService, IRepository repository) {
        this.accountService = accountService;
        this.dataStore = repository;
    }

    public Result<List<Account>> execute(String currency, int limit, int offset){
        if (limit <= 0) {
            return Result.failure("Limit must be greater than 0", ErrorCode.INVALID_ARGUMENT);
        }
        List<Account> cache =  this.accountService.getAccountsTopList(currency);
        if (!cache.isEmpty() && cache.size() >= limit + offset) {
            //System.out.println("Cache hit");
            return Result.success(cache.subList(offset, Math.min(offset + limit, cache.size())));
        }
        if (!dataStore.isTopSupported()) {
            return Result.failure("Repository not support top", ErrorCode.REPOSITORY_NOT_SUPPORT_TOP);
        }
        List<Account> accounts;
        try {
            //System.out.println("DATABASE HIT");
            accounts =  this.dataStore.getAccountsTopByCurrency(currency, limit, offset);
            for (Account account : accounts) {
                this.accountService.addAccountToTopList(account, currency);
            }
        } catch (TransactionException e) {
            return Result.failure("Error in transaction", ErrorCode.DATA_BASE_ERROR);
        }
        if (accounts.isEmpty()) {
            return Result.failure("No accounts found for currency: "+ currency, ErrorCode.ACCOUNT_NOT_FOUND);
        }
        return Result.success(accounts);
    }
}
