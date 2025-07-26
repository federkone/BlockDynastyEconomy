package BlockDynasty.BukkitImplementation.Integrations.vaultUnloked;

import BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import net.milkbowl.vault2.economy.AccountPermission;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.economy.EconomyResponse;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

public class VaultUnlockedHook implements Economy {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final GetBalanceUseCase getBalanceUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountsUseCase getAccountsUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final DepositUseCase depositUseCase;

    public VaultUnlockedHook(UsesCaseFactory usesCaseFactory) {
        this.getCurrencyUseCase = usesCaseFactory.getCurrencyUseCase();
        this.getBalanceUseCase = usesCaseFactory.getGetBalanceUseCase();
        this.createAccountUseCase = usesCaseFactory.getCreateAccountUseCase();
        this.getAccountsUseCase = usesCaseFactory.getAccountsUseCase();
        this.withdrawUseCase = usesCaseFactory.getWithdrawUseCase();
        this.depositUseCase = usesCaseFactory.getDepositUseCase();
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public @NotNull String getName() {
        return "BlockDynastyEconomy";
    }

    @Override
    public boolean hasSharedAccountSupport() {
        return false;
    }

    @Override
    public boolean hasMultiCurrencySupport() {
        return true;
    }

    @Override
    public @NotNull int fractionalDigits(@NotNull String pluginName) {
        return 0;
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount) {
        Result<Currency> currencyResult = getCurrencyUseCase.getDefaultCurrency();
        if(currencyResult.isSuccess()) return currencyResult.getValue().format(amount);
        return String.valueOf(amount);
    }

    @Override
    public @NotNull String format(@NotNull String pluginName, @NotNull BigDecimal amount) {
        return format(amount);
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount, @NotNull String currency) {
        Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currency);
        if(currencyResult.isSuccess()) return currencyResult.getValue().format(amount);
        return String.valueOf(amount);
    }

    @Override
    public @NotNull String format(@NotNull String pluginName, @NotNull BigDecimal amount, @NotNull String currency) {
        return format(amount, currency);
    }

    @Override
    public boolean hasCurrency(@NotNull String currency) {
        Result<Currency> result = getCurrencyUseCase.getCurrency(currency);
        return result.isSuccess();
    }

    @Override
    public @NotNull String getDefaultCurrency(@NotNull String pluginName) {
        Result<Currency> currencyResult = getCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getSingular();
        }
        return "Undefined";
    }

    @Override
    public @NotNull String defaultCurrencyNamePlural(@NotNull String pluginName) {
        Result<Currency> currencyResult = getCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getPlural();
        }
        return "Undefined";
    }

    @Override
    public @NotNull String defaultCurrencyNameSingular(@NotNull String pluginName) {
        Result<Currency> currencyResult = getCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getSingular();
        }
        return "Undefined";
    }

    @Override
    public @NotNull Collection<String> currencies() {
        List<Currency> currenciesResult = getCurrencyUseCase.getCurrencies();
        return currenciesResult.stream().map(Currency::getSingular).toList();
    }

    @Override
    public boolean createAccount(@NotNull UUID accountID, @NotNull String name) {
        Result<Account> result = createAccountUseCase.executeOffline(accountID, name);
        return result.isSuccess();
    }

    @Override
    public boolean createAccount(@NotNull UUID accountID, @NotNull String name, boolean player) {
        return createAccount(accountID, name);
    }

    @Override
    public boolean createAccount(@NotNull UUID accountID, @NotNull String name, @NotNull String worldName) {
        return createAccount(accountID, name);
    }

    @Override
    public boolean createAccount(@NotNull UUID accountID, @NotNull String name, @NotNull String worldName, boolean player) {
        return createAccount(accountID, name);
    }

    @Override
    public @NotNull Map<UUID, String> getUUIDNameMap() {
        //Result<List<Account>> accountsResult = getAccountsUseCase.getAllAccounts();
        return Map.of();
    }

    @Override
    public Optional<String> getAccountName(@NotNull UUID accountID) {
        Result<Account> accountResult = getAccountsUseCase.getAccount(accountID);
        if (accountResult.isSuccess()) {
            return Optional.of(accountResult.getValue().getNickname());
        }
        return Optional.empty();
    }

    @Override
    public boolean hasAccount(@NotNull UUID accountID) {
        return getAccountsUseCase.getAccount(accountID).isSuccess();
    }

    @Override
    public boolean hasAccount(@NotNull UUID accountID, @NotNull String worldName) {
        return hasAccount(accountID);
    }

    @Override
    public boolean renameAccount(@NotNull UUID accountID, @NotNull String name) {
        return false;
    }

    @Override
    public boolean renameAccount(@NotNull String plugin, @NotNull UUID accountID, @NotNull String name) {
        return false;
    }

    @Override
    public boolean deleteAccount(@NotNull String plugin, @NotNull UUID accountID) {
        return false;
    }

    @Override
    public boolean accountSupportsCurrency(@NotNull String plugin, @NotNull UUID accountID, @NotNull String currency) {
        return true;
    }

    @Override
    public boolean accountSupportsCurrency(@NotNull String plugin, @NotNull UUID accountID, @NotNull String currency, @NotNull String world) {
        return true;
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull String pluginName, @NotNull UUID accountID) {
        Result<Money> result = getBalanceUseCase.getBalance(accountID);
        if(result.isSuccess() ){
            return result.getValue().getAmount();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String world) {
        return getBalance(pluginName, accountID);
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String world, @NotNull String currency) {
        Result<Money> result = getBalanceUseCase.getBalance(accountID,currency);
        if(result.isSuccess() ){
            return result.getValue().getAmount();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public @NotNull BigDecimal balance(@NotNull String pluginName, @NotNull UUID accountID) {
        return getBalance(pluginName, accountID);
    }

    @Override
    public @NotNull BigDecimal balance(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String world) {
        return getBalance(pluginName, accountID,world);
    }

    @Override
    public @NotNull BigDecimal balance(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String world, @NotNull String currency) {
        return getBalance(pluginName, accountID, world, currency);
    }

    @Override
    public boolean has(@NotNull String pluginName, @NotNull UUID accountID, @NotNull BigDecimal amount) {
        Result<Money> balanceResult = getBalanceUseCase.getBalance(accountID);
        if(balanceResult.isSuccess() ){
            return balanceResult.getValue().hasEnough(amount);
        }
        return false;
    }

    @Override
    public boolean has(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull BigDecimal amount) {
        return has(pluginName, accountID, amount);
    }

    @Override
    public boolean has(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull String currency, @NotNull BigDecimal amount) {
        Result<Money> balanceResult = getBalanceUseCase.getBalance(accountID,currency);
        if(balanceResult.isSuccess() ){
            return balanceResult.getValue().hasEnough(amount);
        }
        return false;
    }

    //todo: implementar
    @Override
    public EconomyResponse set(@NotNull String pluginName, @NotNull UUID accountID, @NotNull BigDecimal amount) {
        return Economy.super.set(pluginName, accountID, amount);
    }

    @Override
    public EconomyResponse set(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull BigDecimal amount) {
        return Economy.super.set(pluginName, accountID, worldName, amount);
    }

    @Override
    public EconomyResponse set(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull String currency, @NotNull BigDecimal amount) {
        return Economy.super.set(pluginName, accountID, worldName, currency, amount);
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String pluginName, @NotNull UUID accountID, @NotNull BigDecimal amount) {
        Result<Void> resultWithdraw = withdrawUseCase.execute(accountID, amount);
        if(resultWithdraw.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.SUCCESS, "withdraw success for "+accountID);
        }
        return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultWithdraw.getErrorMessage());
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull BigDecimal amount) {
        return withdraw(pluginName, accountID, amount);
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull String currency, @NotNull BigDecimal amount) {
        Result<Void> resultWithdraw = withdrawUseCase.execute(accountID,currency, amount);
        if(resultWithdraw.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID,"",currency), EconomyResponse.ResponseType.SUCCESS, "withdraw success for "+accountID);
        }
        return new EconomyResponse(amount, getBalance("",accountID,"",currency), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultWithdraw.getErrorMessage());
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String pluginName, @NotNull UUID accountID, @NotNull BigDecimal amount) {
        Result<Void> resultDeposit = depositUseCase.execute(accountID, amount);
        if(resultDeposit.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.SUCCESS, "deposit success for "+accountID);
        }
        return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultDeposit.getErrorMessage());
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull BigDecimal amount) {
        return deposit(pluginName, accountID, amount);
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull String currency, @NotNull BigDecimal amount) {
        Result<Void> resultDeposit = depositUseCase.execute(accountID,currency, amount);
        if(resultDeposit.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID,"",currency), EconomyResponse.ResponseType.SUCCESS, "deposit success for "+accountID);
        }
        return new EconomyResponse(amount, getBalance("",accountID,"",currency), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultDeposit.getErrorMessage());
    }

    @Override
    public boolean createSharedAccount(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String name, @NotNull UUID owner) {
        return false;
    }

    @Override
    public boolean isAccountOwner(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean setOwner(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean isAccountMember(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean addAccountMember(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean addAccountMember(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid, @NotNull AccountPermission... initialPermissions) {
        return false;
    }

    @Override
    public boolean removeAccountMember(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean hasAccountPermission(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid, @NotNull AccountPermission permission) {
        return false;
    }

    @Override
    public boolean updateAccountPermission(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid, @NotNull AccountPermission permission, boolean value) {
        return false;
    }
}
