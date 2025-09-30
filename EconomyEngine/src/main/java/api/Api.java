/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package api;

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.EditAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.types.GetAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.*;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.log.Log;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//todo : la api deberia proporcionar/exponer, todos los casos de usos existentes
public class Api implements IApi {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final GetBalanceUseCase getBalanceUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final SearchAccountUseCase searchAccountUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final DepositUseCase depositUseCase;
    private final SetBalanceUseCase setBalanceUseCase;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase;
    private final TransferFundsUseCase transferFundsUseCase;
    private final ExchangeUseCase exchangeUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final EditAccountUseCase editAccountUseCase;
    private final CreateCurrencyUseCase createCurrencyUseCase;
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;
    private final SearchAccountUseCase getAccountsUseCase;

    private final IAccountService accountService;
    private final EventManager eventManager;

    public Api(Core core) {
        accountService = core.getServicesManager().getAccountService();
        searchCurrencyUseCase = core.getCurrencyUseCase().getGetCurrencyUseCase();
        getBalanceUseCase = core.getAccountsUseCase().getGetBalanceUseCase();
        createAccountUseCase = core.getAccountsUseCase().getCreateAccountUseCase();
        searchAccountUseCase = core.getAccountsUseCase().getGetAccountsUseCase();
        deleteAccountUseCase = core.getAccountsUseCase().getDeleteAccountUseCase();
        editAccountUseCase = core.getAccountsUseCase().getEditAccountUseCase();
        createCurrencyUseCase = core.getCurrencyUseCase().getCreateCurrencyUseCase();
        deleteCurrencyUseCase = core.getCurrencyUseCase().getDeleteCurrencyUseCase();
        getAccountsUseCase = core.getAccountsUseCase().getGetAccountsUseCase();

        withdrawUseCase = core.getTransactionsUseCase().getWithdrawUseCase();
        depositUseCase = core.getTransactionsUseCase().getDepositUseCase();
        setBalanceUseCase = core.getTransactionsUseCase().getSetBalanceUseCase();
        tradeCurrenciesUseCase = core.getTransactionsUseCase().getTradeCurrenciesUseCase();
        transferFundsUseCase = core.getTransactionsUseCase().getTransferFundsUseCase();
        exchangeUseCase = core.getTransactionsUseCase().getExchangeUseCase();

        this.eventManager= core.getServicesManager().getEventManager();
    }

    public Api(Core core, Log log){
        accountService = core.getServicesManager().getAccountService();
        searchCurrencyUseCase = core.getCurrencyUseCase().getGetCurrencyUseCase();
        getBalanceUseCase = core.getAccountsUseCase().getGetBalanceUseCase();
        createAccountUseCase = core.getAccountsUseCase().getCreateAccountUseCase();
        searchAccountUseCase = core.getAccountsUseCase().getGetAccountsUseCase();
        deleteAccountUseCase = core.getAccountsUseCase().getDeleteAccountUseCase();
        editAccountUseCase = core.getAccountsUseCase().getEditAccountUseCase();
        createCurrencyUseCase = core.getCurrencyUseCase().getCreateCurrencyUseCase();
        deleteCurrencyUseCase = core.getCurrencyUseCase().getDeleteCurrencyUseCase();
        getAccountsUseCase = core.getAccountsUseCase().getGetAccountsUseCase();

        withdrawUseCase = core.getTransactionsUseCase(log).getWithdrawUseCase();
        depositUseCase = core.getTransactionsUseCase(log).getDepositUseCase();
        setBalanceUseCase = core.getTransactionsUseCase(log).getSetBalanceUseCase();
        tradeCurrenciesUseCase = core.getTransactionsUseCase(log).getTradeCurrenciesUseCase();
        transferFundsUseCase = core.getTransactionsUseCase(log).getTransferFundsUseCase();
        exchangeUseCase = core.getTransactionsUseCase(log).getExchangeUseCase();

        this.eventManager= core.getServicesManager().getEventManager();
    }

    public EventManager getEventManager(){
        return eventManager;
    }

    private EconomyResponse handleResult(Result<Void> result){
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    public EconomyResponse deposit(UUID uuid, BigDecimal amount){
        return handleResult(this.depositUseCase.execute(uuid, amount));
    }

    @Override
    public EconomyResponse deposit(String name, BigDecimal amount) {
        return handleResult(this.depositUseCase.execute(name, amount));
    }

    @Override
    public Currency getDefaultCurrency() {
        return this.searchCurrencyUseCase.getDefaultCurrency().getValue();
    }

    public EconomyResponse deposit(UUID uuid, BigDecimal amount, String currency){
        return handleResult(this.depositUseCase.execute(uuid,currency, amount));
    }

    @Override
    public EconomyResponse deposit(String name, BigDecimal amount, String currency) {
        return handleResult(this.depositUseCase.execute(name,currency, amount));
    }

    public EconomyResponse withdraw(UUID uuid, BigDecimal amount){
       return handleResult(this.withdrawUseCase.execute(uuid,  amount));
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount) {
        return handleResult( this.withdrawUseCase.execute(name,  amount));
    }

    @Override
    public EconomyResponse setBalance(UUID uuid, BigDecimal amount) {
        return handleResult(this.setBalanceUseCase.execute(uuid,getDefaultCurrencyNamePlural(), amount));
    }

    @Override
    public EconomyResponse setBalance(String name, BigDecimal amount, String currency) {
        return handleResult(this.setBalanceUseCase.execute(name,currency, amount));
    }

    @Override
    public EconomyResponse setBalance(UUID uuid, BigDecimal amount, String currency) {
        return handleResult( this.setBalanceUseCase.execute(uuid,currency, amount));
    }

    @Override
    public EconomyResponse setBalance(String name, BigDecimal amount) {
        return handleResult(this.setBalanceUseCase.execute(name,getDefaultCurrencyNamePlural(), amount));
    }

    public EconomyResponse withdraw(UUID uuid, BigDecimal amount, String currency){
        return handleResult(this.withdrawUseCase.execute(uuid,currency, amount));
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount, String currency) {
        return handleResult(this.withdrawUseCase.execute(name,currency, amount));
    }

    public BigDecimal getBalance(UUID uuid){
        Result<Money> balanceResult =  this.getBalanceUseCase.getBalance(uuid);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve default balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getAmount();
    }

    @Override
    public BigDecimal getBalance(String name) {
        Result<Money> balanceResult =  this.getBalanceUseCase.getBalance(name);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve default balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getAmount();
    }

    public BigDecimal getBalance(UUID uuid, String currency) {
        Result<Money> balanceResult =  this.getBalanceUseCase.getBalance(uuid, currency);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance for currency '" + currency + "': " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getAmount();
    }

    @Override
    public BigDecimal getBalance(String name, String currency) {
        Result<Money> balanceResult =  this.getBalanceUseCase.getBalance(name, currency);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance for currency '" + currency + "': " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getAmount();
    }

    public EconomyResponse exchange(UUID uuid, String currencyFrom, String currencyTo, BigDecimal amountFrom,BigDecimal amountTo){
        Result<BigDecimal> result =  this.exchangeUseCase.execute(uuid,currencyFrom,currencyTo,amountFrom, amountTo);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    public EconomyResponse transfer(UUID userFrom, UUID userTo, String currency, BigDecimal amount){
        return handleResult(this.transferFundsUseCase.execute(userFrom,userTo,currency,amount));
    }

    @Override
    public EconomyResponse transfer(String userFrom, String userTo, String currency, BigDecimal amount) {
        return handleResult(this.transferFundsUseCase.execute(userFrom,userTo,currency,amount));
    }

    public EconomyResponse trade(UUID userFrom, UUID userTo, String currencyFrom, String currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        return handleResult(this.tradeCurrenciesUseCase.execute(userFrom,userTo,currencyFrom,currencyTo,amountFrom,amountTo));
    }

    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount) {
        Result<Money> balanceResult =  this.getBalanceUseCase.getBalance(uuid);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().hasEnough(amount);
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount) {
        Result<Money> balanceResult =  this.getBalanceUseCase.getBalance(name);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().hasEnough(amount);
    }

    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount, String currency) {
        Result<Money> balanceResult =  this.getBalanceUseCase.getBalance(uuid , currency);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().hasEnough(amount);
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount, String currency) {
        Result<Money> balanceResult =  this.getBalanceUseCase.getBalance(name , currency);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().hasEnough(amount);
    }

    @Override
    public boolean existCurrency(String nameCurrency) {
        Result<Currency> currencyResult =  this.searchCurrencyUseCase.getCurrency(nameCurrency);
        return currencyResult.isSuccess();
    }

    @Override
    public boolean existAccount(UUID uuid) {
        Result<Account> accountResult =  this.searchAccountUseCase.getAccount(uuid);
        return accountResult.isSuccess();
    }

    @Override
    public boolean existAccount(String name) {
        Result<Account> accountResult =  this.searchAccountUseCase.getAccount(name);
        return accountResult.isSuccess();
    }

    @Override
    public String getDefaultCurrencyNamePlural() {
        Result<Currency> currencyResult =  this.searchCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getPlural();
        }
        return "Unknown";
    }

    @Override
    public String getDefaultCurrencyNameSingular() {
        Result<Currency> currencyResult =  this.searchCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getSingular();
        }
        return "Unknown";
    }

    @Override
    public String format(@NotNull BigDecimal amount) {
        Result<Currency> currencyResult =  this.searchCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            Currency currency = currencyResult.getValue();
            return currency.format(amount);
        } else {
            return amount.toString();
        }
    }

    @Override
    public String format(@NotNull BigDecimal amount, @NotNull String currency) {
        Result<Currency> currencyResult =  this.searchCurrencyUseCase.getCurrency(currency);
        if (currencyResult.isSuccess()) {
            Currency curr = currencyResult.getValue();
            return curr.format(amount);
        } else {
            return amount.toString();
        }
    }

    @Override
    public List<String> getCurrenciesNamesList() {
        List<Currency> currenciesResult =  this.searchCurrencyUseCase.getCurrencies();
        return currenciesResult.stream().map(Currency::getPlural).collect(Collectors.toList());
    }

    @Override
    public EconomyResponse createAccount(@NotNull UUID accountID, @NotNull String name) {
        Result<Account> result =  this.createAccountUseCase.execute(accountID, name);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse deleteAccount(@NotNull UUID accountID) {
        Result<Void> result =  this.deleteAccountUseCase.execute(accountID);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse deleteAccount(@NotNull String name) {
        Result<Void> result =  this.deleteAccountUseCase.execute(name);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse blockAccountTransactions(@NotNull UUID accountID) {
        Result<Void> result =  this.editAccountUseCase.blockAccount(accountID);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse unblockAccountTransactions(@NotNull UUID accountID) {
        Result<Void> result =  this.editAccountUseCase.unblockAccount(accountID);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse createCurrency(String plural, String singular) {
        try{
            this.createCurrencyUseCase.createCurrency(plural, singular);
            return EconomyResponse.success();
        }
        catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public EconomyResponse deleteCurrency(String name) {
        try {
            this.deleteCurrencyUseCase.deleteCurrency(name);
            return EconomyResponse.success();
        }catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public Account getAccount(UUID uuid) {
        return this.getAccountsUseCase.getAccount(uuid).getValue();
    }

    @Override
    public Account getAccount(String name) {
        return this.getAccountsUseCase.getAccount(name).getValue();
    }

    @Override
    public List<Account> getTopAccounts(int limit, String currency) {
        return this.searchAccountUseCase.getTopAccounts( currency, limit,0).getValue();
    }

    @Override
    public EconomyResponse setCurrencyStartBalance(String name, BigDecimal startBal) {
        return null;
    }

    @Override
    public EconomyResponse setCurrencyColor(String currencyName, String colorString) {
        return null;
    }

    @Override
    public EconomyResponse setCurrencyRate(String currencyName, BigDecimal rate) {
        return null;
    }

    @Override
    public EconomyResponse setCurrencyDecimalSupport(String currencyName, boolean supportDecimals) {
        return null;
    }

    @Override
    public EconomyResponse setCurrencySymbol(String currencyName, String symbol) {
        return null;
    }

    @Override
    public EconomyResponse setDefaultCurrency(String currencyName) {
        return null;
    }

    @Override
    public EconomyResponse setSingularName(String currentName, String newName) {
        return null;
    }

    @Override
    public EconomyResponse setPluralName(String currentName, String newName) {
        return null;
    }

    @Override
    public EconomyResponse setPayable(String currencyName, boolean isPayable) {
        return null;
    }

    public Currency getCurrency(String name){
        return  this.searchCurrencyUseCase.getCurrency(name).getValue();
    }

    public IAccountService getAccountService(){
        return this.accountService;
    }
}
