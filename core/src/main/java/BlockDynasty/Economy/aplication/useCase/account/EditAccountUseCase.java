package BlockDynasty.Economy.aplication.useCase.account;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.courier.Courier;

import java.util.UUID;

public class EditAccountUseCase {
    private final IRepository dataStore;
    private final SearchAccountUseCase searchAccountUseCase;
    private final Courier courier;

    public EditAccountUseCase(SearchAccountUseCase searchAccountUseCase, IRepository dataStore, Courier courier) {
        this.dataStore = dataStore;
        this.searchAccountUseCase = searchAccountUseCase;
        this.courier = courier;
    }

    public Result<Void> blockReceive(UUID uuid){
        Result<Account> result = searchAccountUseCase.getAccount(uuid);

        if (result.isSuccess()) {
            result.getValue().setCanReceiveCurrency(false);
            dataStore.saveAccount(result.getValue());
            courier.sendUpdateMessage("account", uuid.toString());
        } else {
            return Result.failure("Account not found", result.getErrorCode());
        }

        return Result.success(null);
    }

    public Result<Void> allowReceive(UUID uuid) {
        Result<Account> result = searchAccountUseCase.getAccount(uuid);

        if (result.isSuccess()) {
            result.getValue().setCanReceiveCurrency(true);
            dataStore.saveAccount(result.getValue());
            courier.sendUpdateMessage("account", uuid.toString());
        } else {
            return Result.failure("Account not found", result.getErrorCode());
        }

        return Result.success(null);
    }

    public Result<Void> blockAccount(UUID uuid) {
        Result<Account> result = searchAccountUseCase.getAccount(uuid);

        if (result.isSuccess()) {
            result.getValue().block();
            dataStore.saveAccount(result.getValue());
            courier.sendUpdateMessage("account", uuid.toString());
        } else {
            return Result.failure("Account not found", result.getErrorCode());
        }

        return Result.success(null);
    }

    public Result<Void> unblockAccount(UUID uuid) {
        Result<Account> result = searchAccountUseCase.getAccount(uuid);

        if (result.isSuccess()) {
            result.getValue().unblock();
            dataStore.saveAccount(result.getValue());
            courier.sendUpdateMessage("account", uuid.toString());
        } else {
            return Result.failure("Account not found", result.getErrorCode());
        }

        return Result.success(null);
    }
}
