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
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IPayUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.events.transactionsEvents.PayEvent;
import net.blockdynasty.economy.core.domain.services.IAccountService;
import net.blockdynasty.economy.core.domain.services.ICurrencyService;
import net.blockdynasty.economy.core.domain.services.courier.Courier;
import net.blockdynasty.economy.core.domain.services.log.Log;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.result.TransferResult;
import net.blockdynasty.economy.core.domain.persistence.entities.IRepository;

import java.math.BigDecimal;

public class PayUseCase extends TransferFundsUseCase implements IPayUseCase {
    private final Log economyLogger;
    private final EventManager eventManager;
    public PayUseCase(ICurrencyService currencyService,IAccountService accountService, IRepository dataStore,
                      Courier updateForwarder, Log economyLogger, EventManager eventManager) {
        super(currencyService, accountService,dataStore, updateForwarder, economyLogger, eventManager);
        this.economyLogger = economyLogger;
        this.eventManager = eventManager;
    }

    @Override
    protected void logSuccess(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount) {
        this.economyLogger.log("[PAY] Account: " + accountFrom.getNickname() + " paid " + currency.format(amount) + " to " + accountTo.getNickname());
    }
    @Override
    protected void logFailure(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount, Result<TransferResult> result) {
        this.economyLogger.log("[PAY Failed] Account: " + accountFrom.getNickname() + " paid " + currency.format(amount) + " to " + accountTo.getNickname() + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
    }
    @Override
    protected void emitEvent(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount) {
        this.eventManager.emit(new PayEvent(accountFrom.getPlayer(), accountTo.getPlayer(), currency, amount));
    }
}
