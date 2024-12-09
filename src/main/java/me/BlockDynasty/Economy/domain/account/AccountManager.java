package me.BlockDynasty.Economy.domain.account;

import java.util.*;


//TODO ESTE MANAGER SE VA A ENCARGAR DE DECIDIR SI TRAER AL USUSARIO DE LA DB O USAR EL DE LA LISTA
public class AccountManager {
    private final Set<Account> accounts;

    public AccountManager() {

        this.accounts = new HashSet<>(); //para no repetir cuentas
    }
    //TODO: CACHE ACCOUNTS, ESTO PUEDE QUEDAR ACA
    public void removeAccount(UUID uuid) {  //removeAccountFromCache
        accounts.removeIf(account -> account.getUuid().equals(uuid));
    }

    //TODO:CACHE ACCOUNTS, ESTO PUEDE QUEDAR ACA
    public void addAccountToCache(Account account) {
        if (this.accounts.contains(account)) return;

        this.accounts.add(account);
    }

    //TODO: CACHE ACCOUNTS, ESTO PUEDE QUEDAR ACA
    public Set<Account> getAccounts() {
        return accounts;
    }

    public Account getAccount(String name){
        return  accounts.stream() //todo esta responsabilidad se traslada a account manager
                .filter(a -> name.equals(a.getNickname()))
                .findFirst()
                .orElse(null);
    }
    public Account getAccount(UUID uuid){
        return accounts.stream()
                .filter(a -> uuid.equals(a.getUuid()))
                .findFirst()
                .orElse(null);
    }


    // SE ENCARGA DE ACTUALIZAR LOS MONTOS DE CUENTA Y DE INTENTAR SALVAR EN LA DB
    //TODO: aqui se puede agregar el impuesto por cambio de divisa segun el rate de la moneda
    //exchange permite a persona cambiar sus monedas con si mismo
    //todo: se supone que tampoco deberia validar si la moneda es pagable o no, ya que es un exchange
    //todo: falta agregar validaciones de decimal support etc
    /*public void performExchange(UUID accountUuid, String currencyFromName, String currencyToname, double amountFrom, double amountTo) {
        Account account = getAccount(accountUuid);
        Currency currencyFrom = plugin.getCurrencyManager().getCurrency(currencyFromName);
        Currency currencyTo = plugin.getCurrencyManager().getCurrency(currencyToname);
        if(currencyFrom == null || currencyTo == null || account == null) {
            throw new IllegalArgumentException("Account or One or both currencies not found"); // Manejo de errores
        }
        if (!account.hasEnough(currencyFrom, amountFrom)) {
            throw new InsufficientFundsException("Insufficient balance for currency: " + currencyFrom.getUuid()); // Manejo de errores
        }
        //aca se puede calcular el ratio de impuesto
        account.getBalances().put(currencyFrom, account.getBalances().get(currencyFrom) - amountFrom); //restar moneda from
        account.getBalances().put(currencyTo, account.getBalances().get(currencyTo) + amountTo);   //sumar moneda to
        try {
            plugin.getDataStore().saveAccount(account);
            plugin.getUpdateForwarder().sendUpdateMessage("account", account.getUuid().toString());// esto es para bungee
            plugin.getEconomyLogger().log("[EXCHANGE] Account: " + account.getDisplayName() + " exchanged " + currencyFrom.format(amountFrom) + " to " + currencyTo.format(amountTo));
            //account.getBalances().put(currencyFrom, account.getBalances().get(currencyFrom) - amountFrom); //restar moneda from
            //account.getBalances().put(currencyTo, account.getBalances().get(currencyTo) + amountTo);   //sumar moneda to
            //plugin.getDataStore().exchangeCurrencies(accountUuid, currencyFrom, currencyTo, amountFrom, amountTo);
            // Log o cualquier otra acción post-exchange
        } catch (Exception e) {
            // Manejo de errores (puedes lanzar la excepción o manejarla de otra manera)
            throw new RuntimeException("Failed to perform exchange: " + e.getMessage(), e);
        }
    }*/

}