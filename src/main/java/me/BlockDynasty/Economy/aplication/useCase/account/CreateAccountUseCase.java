package me.BlockDynasty.Economy.aplication.useCase.account;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountExeption;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//TODO: DE MOMENTO ESTE CASO SE USA DE MOMENTO CUANDO UN USUARIO ENTRA AL SERVER, SI NO EXISTE SE CREARA UNA CUENTA. PENSAR EN QUE PARTES HARÍA FALTA
public class CreateAccountUseCase {
    private final AccountManager accountManager;
    private final CurrencyManager currencyManager;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final GetAccountsUseCase getAccountsUseCase;

    public CreateAccountUseCase(AccountManager accountManager, CurrencyManager currencyManager,GetAccountsUseCase getAccountsUseCase,UpdateForwarder updateForwarder, IRepository dataStore) {
        this.accountManager = accountManager;
        this.currencyManager = currencyManager;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    //todo: se puede upgradear para aceptar solo uuid y obtener el nombre a trabez de la api bukkit
    public void execute(UUID userUuid , String userName) {
        try{
            Account existingAccount = getAccountsUseCase.getAccount(userUuid);
            if (existingAccount != null) {
                throw new AccountExeption("Account already exists for: " + existingAccount.getDisplayName());
            }
        }catch (AccountNotFoundException e){
            Account account = new Account(userUuid, userName);
            account.setCanReceiveCurrency(true);
            account.setNickname(userName);
            initializeAccountWithDefaultCurrencies(account);
            try {
                dataStore.createAccount(account);
                accountManager.addAccountToCache(account);
                //updateForwarder.sendUpdateMessage("account", account.getUuid().toString()); //todo :test sin esto, ya que no hace falta broadcastear la creacion de una cuenta
            } catch (TransactionException t) {
                throw new TransactionException("Error creating account for: " + account.getDisplayName());
            }
        }

    }

    private void initializeAccountWithDefaultCurrencies(Account account) {
        Map<Currency, BigDecimal> balances = new HashMap<>();
        for (Currency currency : currencyManager.getCurrencies()) {
            balances.put(currency, currency.getDefaultBalance());
        }
        account.setBalances(balances);
    }

}
