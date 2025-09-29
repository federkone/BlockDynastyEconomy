package BlockDynasty.adapters.spongeEconomyApi;

import api.EconomyResponse;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;

import java.math.BigDecimal;
import java.util.Set;

public class TransactionResultAdapter implements TransactionResult {
    private EconomyResponse response;
    private Currency currency;
    private Account account;
    private BigDecimal amount;
    private TransactionType type;

    public TransactionResultAdapter(EconomyResponse response, Currency currency, Account account, BigDecimal amount, TransactionType type) {
        this.response = response;
        this.currency = currency;
        this.account = account;
        this.amount = amount;
        this.type = type;

    }

    @Override
    public Account account() {
        return account;
    }

    @Override
    public Currency currency() {
        return currency;
    }

    @Override
    public BigDecimal amount() {
        return amount;
    }

    @Override
    public Set<Context> contexts() {
        return Set.of();
    }

    @Override
    public ResultType result() {
        if(response.isSuccess()){
            return ResultType.SUCCESS;
        }
        else{
            return ResultType.FAILED;
        }
    }

    @Override
    public TransactionType type() {
        return type;
    }
}
