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

package net.blockdynasty.economy.core.aplication.useCase.transaction;

import net.blockdynasty.economy.core.aplication.events.EventManager;
import net.blockdynasty.economy.core.aplication.useCase.transaction.genericOperations.SingleAccountSingleCurrencyOp;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.ISetBalanceUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.events.Context;
import net.blockdynasty.economy.core.domain.events.transactionsEvents.SetEvent;
import net.blockdynasty.economy.core.domain.services.IAccountService;
import net.blockdynasty.economy.core.domain.services.ICurrencyService;
import net.blockdynasty.economy.core.domain.services.courier.Courier;
import net.blockdynasty.economy.core.domain.services.courier.Message;
import net.blockdynasty.economy.core.domain.services.courier.PlayerTargetMessage;
import net.blockdynasty.economy.core.domain.services.log.Log;
import net.blockdynasty.economy.core.domain.result.ErrorCode;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.persistence.entities.IRepository;

import java.math.BigDecimal;

public class SetBalanceUseCase extends SingleAccountSingleCurrencyOp implements ISetBalanceUseCase {
    private final IRepository dataStore;
    private final IAccountService accountService;
    private final Log logger;
    private final Courier updateForwarder;
    private final EventManager eventManager;

    public SetBalanceUseCase(ICurrencyService currencyService,IAccountService accountService, IRepository dataStore,
                             Courier updateForwarder, Log economyLogger, EventManager eventManager) {
        super( accountService, currencyService, dataStore);
        this.dataStore = dataStore;
        this.accountService = accountService;
        this.logger = economyLogger;
        this.updateForwarder = updateForwarder;
        this.eventManager = eventManager;
    }

    @Override
    public Result<Void> execute(Account account, ICurrency currency, BigDecimal amount, Context context) {
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }

        if(!currency.isValidAmount(amount)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account> result =  this.dataStore.setBalance(account.getPlayer(), currency, amount);
        if (!result.isSuccess()) {
            this.logger.log("[BALANCE SET failed] Account: " + account.getNickname() + " were set to: " + currency.format(amount) + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.accountService.syncOnlineAccount(result.getValue());
        //this.updateForwarder.sendUpdateMessage("account", account.getUuid().toString());

        this.updateForwarder.sendUpdateMessage( PlayerTargetMessage.builder()
                .setType(Message.Type.EVENT)
                .setData(new SetEvent(account.getPlayer(), currency, amount,context).toJson())
                .setTarget(account.getUuid())
                .setTargetPlayer(account.getPlayer())
                .build());
        this.logger.log("[BALANCE SET] Account: " + account.getNickname() + " were set to: " + currency.format(amount));
        this.eventManager.emit( new SetEvent(account.getPlayer(), currency, amount,context));

        return Result.success();
    }
}
