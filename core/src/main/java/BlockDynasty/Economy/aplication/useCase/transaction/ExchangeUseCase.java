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

package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.events.transactionsEvents.ExchangeEvent;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class ExchangeUseCase {
    private  final SearchCurrencyUseCase searchCurrencyUseCase;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final Log economyLogger;
    private final SearchAccountUseCase searchAccountUseCase;
    private final IAccountService accountService;
    private final EventManager eventManager;

    public ExchangeUseCase(ICurrencyService currencyService, IAccountService accountService, IRepository dataStore, Courier updateForwarder,
                           Log economyLogger, EventManager eventManager) {
        this.searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, dataStore);
        this.searchAccountUseCase = new SearchAccountUseCase(accountService, dataStore);
        this.accountService = accountService;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.eventManager = eventManager;
    }

    public Result<BigDecimal> execute(UUID accountUuid, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(accountUuid);
        return execute(accountResult,currencyFromName, currencyToname, amountFrom, amountTo);
    }

    public Result<BigDecimal> execute(String accountString, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(accountString);
        return execute(accountResult,currencyFromName, currencyToname, amountFrom, amountTo);
    }

    private Result<BigDecimal> execute(Result<Account> accountResult, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyFromResult = this.searchCurrencyUseCase.getCurrency(currencyFromName);
        if (!currencyFromResult.isSuccess()) {
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<Currency> currencyToResult = this.searchCurrencyUseCase.getCurrency(currencyToname);
        if (!currencyToResult.isSuccess()) {
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return performExchange(accountResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);
    }

    private Result<BigDecimal> performExchange(Account account, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        if (currencyFrom.equals(currencyTo)) {
            return Result.failure("Cannot exchange the same currency", ErrorCode.CURRENCY_MUST_BE_DIFFERENT);
        }

        if (account.isBlocked()){
            return Result.failure("Account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }
        if (!account.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if(!currencyTo.isValidAmount(amountTo)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        if (amountFrom == null) { //calculo atumatico segun el ratio
            amountFrom = amountTo.multiply(BigDecimal.valueOf(currencyFrom.getExchangeRate()))
                    .divide(BigDecimal.valueOf(currencyTo.getExchangeRate()), 4, RoundingMode.HALF_UP);
        }

        if (!currencyFrom.isValidAmount(amountFrom)) {
            return Result.failure("Decimal not supported for currency ", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account>  result =this.dataStore.exchange(account.getUuid().toString(),currencyFrom,amountFrom,currencyTo,amountTo);

        if(!result.isSuccess()){
            this.economyLogger.log("[EXCHANGE failed] Account: " + account.getNickname() + " exchanged " + currencyFrom.format(amountFrom) + " to " + currencyTo.format(amountTo) + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.accountService.syncOnlineAccount(result.getValue());
        this.updateForwarder.sendUpdateMessage("account", account.getUuid().toString());// esto es para bungee
        this.economyLogger.log("[EXCHANGE] Account: " + account.getNickname() + " exchanged " + currencyFrom.format(amountFrom) + " to " + currencyTo.format(amountTo));
        this.eventManager.emit(new ExchangeEvent(account.getPlayer(),currencyFrom,currencyTo,amountFrom,currencyTo.getExchangeRate(),amountTo));

        return Result.success(amountFrom);
    }
}