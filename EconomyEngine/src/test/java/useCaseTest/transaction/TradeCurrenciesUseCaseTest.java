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

package useCaseTest.transaction;

import net.blockdynasty.economy.core.aplication.events.EventManager;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import mockClass.CourierTest;
import net.blockdynasty.economy.core.domain.result.ErrorCode;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.SearchAccountUseCase;
import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.TradeCurrenciesUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.aplication.services.AccountService;
import net.blockdynasty.economy.core.domain.entities.account.Exceptions.AccountCanNotReciveException;
import net.blockdynasty.economy.core.domain.entities.account.Exceptions.AccountNotFoundException;
import net.blockdynasty.economy.core.domain.entities.account.Exceptions.InsufficientFundsException;
import net.blockdynasty.economy.core.domain.entities.currency.Currency;
import net.blockdynasty.economy.core.aplication.services.CurrencyService;
import net.blockdynasty.economy.core.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import net.blockdynasty.economy.core.domain.entities.currency.Exceptions.CurrencyNotPayableException;
import net.blockdynasty.economy.core.domain.entities.currency.Exceptions.DecimalNotSupportedException;
import net.blockdynasty.economy.core.domain.persistence.Exceptions.TransactionException;
import net.blockdynasty.economy.core.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TradeCurrenciesUseCaseTest {
    Account nullplague;
    Account cris;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    SearchAccountUseCase searchAccountUseCase;
    SearchCurrencyUseCase searchCurrencyUseCase;
    TradeCurrenciesUseCase tradeCurrenciesUseCase;
    ICurrency coin;
    ICurrency dinero;

    @BeforeEach
    void setUp() {
        coin = Currency.builder().setSingular("Coin").setPlural("Coins").build();
        dinero =Currency.builder().setSingular("dinero").setPlural("dinero").build();

        nullplague = new Account(UUID.randomUUID(), "nullplague");
        cris = new Account(UUID.randomUUID(), "cris");

        nullplague.setMoney(coin, BigDecimal.valueOf(1));
        nullplague.setMoney(dinero, BigDecimal.valueOf(0));
        cris.setMoney(coin, BigDecimal.valueOf(0));
        cris.setMoney(dinero, BigDecimal.valueOf(30000));

        repository = FactoryRepo.getDb();


        repository.saveCurrency(coin);
        repository.saveCurrency(dinero);
        try{
            repository.createAccount(nullplague);
            repository.createAccount(cris);
        }catch (Exception e){
            fail("Error saving accounts " + e.getMessage());
        }


        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5 ,repository, currencyService);

        //accountManager.addAccountToCache(account1); //se conecto el player1
        //accountManager.addAccountToCache(account2); //se conecto el player2

        searchAccountUseCase = new SearchAccountUseCase(accountService,repository);
        searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, repository);

        tradeCurrenciesUseCase = new TradeCurrenciesUseCase(currencyService,accountService,repository,new CourierTest(),new LoggerTest(),new EventManager());
    }

    @Test
    void TradeCurrencyUseCaseTest (){
        try {
            tradeCurrenciesUseCase.execute( "nullplague","cris","Coin","dinero",BigDecimal.valueOf(1),BigDecimal.valueOf(30000));
        }catch (AccountNotFoundException e){
            fail("Account not found");
        }catch (InsufficientFundsException e){
            fail("Insufficient funds");
        }catch (CurrencyNotFoundException e){
            fail("Currency not found");
        }catch (AccountCanNotReciveException e){
            fail("Account can not recive");
        }catch (CurrencyNotPayableException e){
            fail("Currency not payable");
        }catch (DecimalNotSupportedException e){
            fail("Decimal not supported");
        }catch (TransactionException e){
            fail("Transaction exception"+ e);
        }catch (Exception e){
            fail("Exception"+ e);
        }

        assertEquals(BigDecimal.valueOf(30000).setScale(2), searchAccountUseCase.execute("nullplague").getValue().getMoney(dinero).getAmount());
        assertEquals(BigDecimal.valueOf(1).setScale(2), searchAccountUseCase.execute("cris").getValue().getMoney(coin).getAmount());
    }

    @Test
    void TradeCurrencyUseCseTestInsufficientFounds(){
        Result<Void> result = tradeCurrenciesUseCase.execute("nullplague","cris","Coin","dinero",BigDecimal.valueOf(2),BigDecimal.valueOf(30000));
        assertEquals(ErrorCode.INSUFFICIENT_FUNDS, result.getErrorCode());
        assertEquals(BigDecimal.valueOf(1).setScale(2), searchAccountUseCase.execute("nullplague").getValue().getMoney(coin).getAmount());
        assertEquals(BigDecimal.valueOf(30000).setScale(2), searchAccountUseCase.execute("cris").getValue().getMoney(dinero).getAmount());
    }


    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
