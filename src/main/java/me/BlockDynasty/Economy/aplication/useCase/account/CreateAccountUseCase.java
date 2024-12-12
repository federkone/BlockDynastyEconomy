package me.BlockDynasty.Economy.aplication.useCase.account;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountExeption;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

//TODO: DE MOMENTO ESTE CASO SE USA DE MOMENTO CUANDO UN USUARIO ENTRA AL SERVER, SI NO EXISTE SE CREARA UNA CUENTA. PENSAR EN QUE PARTES HARÍA FALTA
public class CreateAccountUseCase {
    private final AccountCache accountCache;
    private final CurrencyCache currencyCache;
    private final IRepository dataStore;
    //private final UpdateForwarder updateForwarder;
    private final GetAccountsUseCase getAccountsUseCase;

    public CreateAccountUseCase(AccountCache accountCache, CurrencyCache currencyCache, GetAccountsUseCase getAccountsUseCase, IRepository dataStore) {
        this.accountCache = accountCache;
        this.currencyCache = currencyCache;
        this.dataStore = dataStore;
       // this.updateForwarder = updateForwarder;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    //todo: se puede upgradear para aceptar solo uuid y obtener el nombre a trabez de la api bukkit
    public void execute(UUID userUuid , String userName) {
        try{
            Account existingAccount = getAccountsUseCase.getAccount(userUuid);
            if (existingAccount != null) {
                throw new AccountExeption("Account already exists for: " + existingAccount.getNickname());
            }
        }catch (AccountNotFoundException e){
            Account account = new Account(userUuid, userName);
            account.setCanReceiveCurrency(true);
            initializeAccountWithDefaultCurrencies(account);
            try {
                dataStore.createAccount(account);
                accountCache.addAccountToCache(account);
                //updateForwarder.sendUpdateMessage("account", account.getUuid().toString()); //todo :test sin esto, ya que no hace falta broadcastear la creacion de una cuenta
            } catch (TransactionException t) {
                throw new TransactionException("Error creating account for: " + account.getNickname());
            }
        }

    }

    private void initializeAccountWithDefaultCurrencies(Account account) {
        List<Balance> balances = currencyCache.getCurrencies().stream()
                .map(currency -> new Balance(currency))
                .collect(Collectors.toList());
        account.setBalances(balances);
    }

}
