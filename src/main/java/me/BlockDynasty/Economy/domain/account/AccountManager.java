package me.BlockDynasty.Economy.domain.account;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


//TODO ESTE MANAGER SE VA A ENCARGAR DE DECIDIR SI TRAER AL USUSARIO DE LA DB O USAR EL DE LA LISTA
public class AccountManager {
    private final List<Account> accounts;

    public AccountManager() {

        this.accounts = new ArrayList<>();
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
    public List<Account> getAccounts() {
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

    //TODO--------------------------------------------------------------------------------------------------------------------------------------------------------
   /* public void createAccount(String nickname) {
        createAccount(Bukkit.getOfflinePlayer(nickname).getUniqueId(), nickname);
    }
*/
    //todo: CREA CUENTA, VALIDA SI EXISTE EN LA LISTA CACHEADA, SI EXISTE NO HACE NADA, SI NO EXISTE LA CREA
    //TODO: LA CREA EN MEMORIA Y EN LA BASE DE DATOS
   /* public synchronized void createAccount(UUID uuid, String name) {
        Account existingAccount = getAccount(uuid);

        if (existingAccount != null) {
            UtilServer.consoleLog("Account already exists for: " + existingAccount.getDisplayName());
            return;
        }

        String playerName = getPlayerName(uuid, name);

        // Crear nueva cuenta
        Account account = new Account(uuid, playerName);
        account.setCanReceiveCurrency(true);
        account.setNickname(playerName);

        // Guardar en memoria y base de datos
        initializeAccountWithDefaultCurrencies(account);
        addAccountToCache(account); //AÑADE A CACHE
        plugin.getDataStore().createAccount(account);
        plugin.getUpdateForwarder().sendUpdateMessage("account", account.getUuid().toString());
        UtilServer.consoleLog("New Account created for: " + account.getDisplayName());

    }*/

   /* public void initializeAccountWithDefaultCurrencies(Account account) {  //todo logica de degocio en caso de uso
        Map<Currency, Double> balances = new HashMap<>();
        for (Currency currency : plugin.getCurrencyManager().getCurrencies()) {
            balances.put(currency, currency.getDefaultBalance());
        }
        account.setBalances(balances);
    }*/
//TODO--------------------------------------------------------------------------------------------------------------------------------------------------------
    //TODO: OBTENER 1 CUENTA SEGUN PLAYER, SI LA CUENTA EXISTE EN CACHE, DEVUELVE LA CACHE, SINO BUSCA EN LA DB
    /*public Account getAccount(Player player) {
        return getAccount(player.getUniqueId());
    }

    //TODO: OBTENER 1 CUENTA SEGUN STRING, SI LA CUENTA EXISTE EN CACHE, DEVUELVE LA CACHE, SINO BUSCA EN LA DB
    public Account getAccount(String name) {
        for (Account account : this.accounts) {  //TODO:BUSCA EN CACHE Y SINO BUSCA EN DB
            if (account.getNickname() == null || !account.getNickname().equals(name)) continue; //TODO: TEST, IGNORE CASE? QUIZAS SEAN DOS NOMBRES DISTINTOS DISTINGIDOS POR MAYUSCULAS, por lo tanto quite el ecualsIgnoreCase
            return account;
        }
        Criteria criteria = Criteria.create().filter("nickname", name).limit(1); //prepare for get account with uuid
        List<Account> accounts = plugin.getDataStore().loadAccounts(criteria); //get account with uuid //todo: puede ir en caso de uso
        if(!accounts.isEmpty()) {
            updateAccountBalances(accounts.get(0)); //TODO TEST //AL OBTENER LA CUENTA DE LA DB, ACTUALIZA LOS BALANCES EN LA MEMORIA LOCAL
            return accounts.get(0);
        }
        return null;
    }

    //TODO: OBTENER 1 CUENTA SEGUN UUID, SI LA CUENTA EXISTE EN CACHE, DEVUELVE LA CACHE, SINO BUSCA EN LA DB
    public Account getAccount(UUID uuid) {
        for (Account account : this.accounts) { // This throws CME randomly //TODO: BUSCA EN CACHE Y SINO BUSCA EN DB
            if (!account.getUuid().equals(uuid)) continue;
            return account;
        }
        Criteria criteria = Criteria.create().filter("uuid", uuid.toString()).limit(1); //prepare for get account with uuid
        List<Account> accounts= plugin.getDataStore().loadAccounts(criteria); //get account with uuid
        if(!accounts.isEmpty()) {
            updateAccountBalances(accounts.get(0));  //TODO TEST //AL OBTENER LA CUENTA DE LA DB, ACTUALIZA LOS BALANCES EN LA MEMORIA LOCAL
            return accounts.get(0);
        }
        return null;
    }
    public void updateAccountBalances(Account account) {
        Map<Currency, Double> updatedBalances = plugin.getCurrencyManager().getCurrencies().stream()
                .collect(Collectors.toMap(
                        systemCurrency -> systemCurrency,
                        systemCurrency -> account.getBalances().entrySet().stream()
                                .filter(entry -> entry.getKey().getUuid().equals(systemCurrency.getUuid()))
                                .map(Map.Entry::getValue)
                                .findFirst()
                                .orElse(systemCurrency.getDefaultBalance())
                ));
        account.setBalances(updatedBalances);
    }*/

    //TODO: TEST
    /*public void updateAccountBalances(Account account) {
        Map<Currency, Double> updatedBalances = new HashMap<>();
        for (Currency systemCurrency : plugin.getCurrencyManager().getCurrencies()) {
            Double balance = account.getBalances().getOrDefault(systemCurrency, systemCurrency.getDefaultBalance());
            updatedBalances.put(systemCurrency, balance);
        }
        account.setBalances(updatedBalances);
    }*/


//TODO----------------------------------------------------------------------------------------------------------------------


    /*public List<Account> getAllAccountsOFFLINE() {
        return plugin.getDataStore().loadAccounts(Criteria.create());
        //return plugin.getDataStore().getOfflineAccounts(); //plugin.getDataStore().loadAccounts(Criteria.create());
    }*/

    //TODO: OBTIENE EL NOMBRE SEGUN UN UUID, SI NO HAY NOMBRE, DEVUELVE UNKNOWN. ESTO NO ES PARTE DE LA LOGICA PRINCIPAL
    //TODO: LO ESTA USANDO CREATEAACCOUNT PARA OBTENER EL NOMBRE DEL JUGADOR
    //TODO: ESTE METODO DE BUKKIT HAY QUE INVESTIGARLO DE DONDE TRAE EL NOMBRE, SI DE LA DB DE MINECRAFT O DE LA CACHE DE USUARIOS DEL SERVER
    /*private String getPlayerName(UUID uuid, String providedName) {
        if (providedName != null && !providedName.isEmpty()) {
            return providedName;
        }

        // Obtener el nombre del jugador de Bukkit
        String bukkitName = Bukkit.getOfflinePlayer(uuid).getName();
        if (bukkitName != null && !bukkitName.isEmpty()) {
            return bukkitName;
        }

        // Nombre predeterminado si no hay otro disponible
        return "Unknown";
    }*/

    //extraer , TODO: IMPLEMENTAR LA LOGICA PRINCIPAL Y LANZAR EXCEPCIONES, ESTO DEBERIA RECIBIR UN STRING NOMBRE Y REALIZAR LA TRANSACCION O LANZAR EXCEPCIONES SEGUN CORRESPONDA
   /* public boolean withdraw(Account account,Currency currency, double amount) {  //necesita account
        if (account.hasEnough(currency, amount)) {
            GemsTransactionEvent event = new GemsTransactionEvent(currency, account, amount, TranactionType.WITHDRAW);
            SchedulerUtils.run(() -> Bukkit.getPluginManager().callEvent(event));
            if(event.isCancelled())return false;

            double finalAmount = account.getBalance(currency) - amount;
            this.modifyBalance(account,currency, finalAmount, true); //escribe en la db
            plugin.getEconomyLogger().log("[WITHDRAW] Account: " + account.getDisplayName() + " were withdrawn: " + currency.format(amount) + " and now has " + currency.format(finalAmount));
            return true;
        }
        return false;
    }*/

    //depositar
    /*
    public boolean deposit(Account account ,Currency currency, double amount) { //necesita accoount
        if (account.canReceiveCurrency()) {

            GemsTransactionEvent event = new GemsTransactionEvent(currency, account, amount, TranactionType.DEPOSIT);
            SchedulerUtils.run(() -> Bukkit.getPluginManager().callEvent(event));
            if(event.isCancelled())return false;

            double finalAmount = account.getBalance(currency) + amount;
            this.modifyBalance(account,currency, finalAmount, true);//escribe en la db
            plugin.getEconomyLogger().log("[DEPOSIT] Account: " + account.getDisplayName() + " were deposited: " + currency.format(amount) + " and now has " + currency.format(finalAmount));
            return true;
        }
        return false;
    }*/
//TODO VERIFICAR LOGICA/CONVERT=EXCHANGE?. claramente es un exchange, tengo mi propia implementacion en performExchange
   /* @Deprecated
    public boolean convert(Account account,Currency exchanged, double exchangeAmount, Currency received, double amount) { //necesita account
        GemsConversionEvent event = new GemsConversionEvent(exchanged, received, account, exchangeAmount, amount);
        SchedulerUtils.run(() -> Bukkit.getPluginManager().callEvent(event));
        if(event.isCancelled())return false;

        if (amount != -1) {
            double removed = account.getBalance(exchanged) - exchangeAmount;
            double added = account.getBalance(received) + amount;
            modifyBalance(account,exchanged, removed, false);
            modifyBalance(account,received, added, false);
            saveAccountWithCurrencies(account);
            plugin.getDataStore().saveAccount(account);
            plugin.getEconomyLogger().log("[CONVERSION - Custom Amount] Account: " + account.getDisplayName() + " converted " + exchanged.format(exchangeAmount) + " to " + received.format(amount));
            plugin.getUpdateForwarder().sendUpdateMessage("account", account.getUuid().toString());
            return true;
        }
        double rate;
        boolean receiveRate = false;
//TODO: OBSERVACION DE USO DE RATE PARA EXCHANGE, LO CUAL ES EL IMPUESTO POR CAMBIAR DIVISA SEGUN LA DIVISA DE ORIGEN O DESTINO
        if(exchanged.getExchangeRate() > received.getExchangeRate()){
            rate = exchanged.getExchangeRate();
        }else{
            rate = received.getExchangeRate();
            receiveRate = true;
        }

        if(!receiveRate){

            double finalAmount = Math.round(exchangeAmount * rate);
            double removed = account.getBalance(exchanged) - exchangeAmount;
            double added = account.getBalance(received) + finalAmount;

            if(plugin.isDebug()){
                UtilServer.consoleLog("Rate: " + rate);
                UtilServer.consoleLog("Finalized amount: " + finalAmount);
                UtilServer.consoleLog("Amount to remove: " + exchanged.format(removed));
                UtilServer.consoleLog("Amount to add: " + received.format(added));
            }

            if(account.hasEnough(exchanged, exchangeAmount)){
                this.modifyBalance(account,exchanged, removed, false);
                this.modifyBalance(account,received, added, false);
                saveAccountWithCurrencies(account);
                plugin.getDataStore().saveAccount(account);
                plugin.getUpdateForwarder().sendUpdateMessage("account", account.getUuid().toString());
                plugin.getEconomyLogger().log("[CONVERSION - Preset Rate] Account: " + account.getDisplayName() + " converted " + exchanged.format(removed) + " (Rate: " + rate + ") to " + received.format(added));
                return true;
            }
            return false;
        }

        double finalAmount = Math.round(exchangeAmount * rate);
        double removed = account.getBalance(exchanged) - finalAmount;
        double added = account.getBalance(received) + exchangeAmount;

        if(plugin.isDebug()){
            UtilServer.consoleLog("Rate: " + rate);
            UtilServer.consoleLog("Finalized amount: " + finalAmount);
            UtilServer.consoleLog("Amount to remove: " + exchanged.format(removed));
            UtilServer.consoleLog("Amount to add: " + received.format(added));
        }

        if(account.hasEnough(exchanged, finalAmount)){
            this.modifyBalance(account,exchanged, removed, false);
            this.modifyBalance(account,received, added, false);
            saveAccountWithCurrencies(account);
            plugin.getDataStore().saveAccount(account);
            plugin.getUpdateForwarder().sendUpdateMessage("account", account.getUuid().toString());
            plugin.getEconomyLogger().log("[CONVERSION - Preset Rate] Account: " + account.getDisplayName() + " converted " + exchanged.format(removed) + " (Rate: " + rate + ") to " + received.format(added));
            return true;
        }

        return false;
    }
*/
    /*public void setBalance(Account account,Currency currency, double amount) { //necesita account
        GemsTransactionEvent event = new GemsTransactionEvent(currency, account, amount, TranactionType.SET);
        SchedulerUtils.run(() -> Bukkit.getPluginManager().callEvent(event));
        if(event.isCancelled())return;

        account.getBalances().put(currency, amount);

        plugin.getEconomyLogger().log("[BALANCE SET] Account: " + account.getDisplayName() + " were set to: " + currency.format(amount));
        saveAccountWithCurrencies(account);
        plugin.getDataStore().saveAccount(account);
        plugin.getUpdateForwarder().sendUpdateMessage("account", account.getUuid().toString());
    }*/

    /**
     * DO NOT USE UNLESS YOU HAVE VIEWED WHAT THIS DOES!
     *
     * This directly modifies the account balance for a currency, with the option of saving.
     *
     * @param currency - Currency to modify
     * @param amount - Amount of cash to modify.
     * @param save - Save the account or not. Should be done async!
     */
    /*public void modifyBalance(Account account,Currency currency, double amount, boolean save){ //necesita account
        account.getBalances().put(currency, amount);
        if(save){
            saveAccountWithCurrencies(account);
            plugin.getDataStore().saveAccount(account);
            plugin.getUpdateForwarder().sendUpdateMessage("account", account.getUuid().toString());
        }
    }*/

   /* public void saveAccountWithCurrencies(Account account) {
        Map<Currency, Double> balances = new HashMap<>();
        for (Currency currency : plugin.getCurrencyManager().getCurrencies()) {
            balances.put(currency, account.getBalance(currency));
        }
        account.setBalances(balances);
    }*/































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

    //transfer permite a persona transferir  1 tipo de moneda a otra persona
    //TODO: IMPLEMENTAR MISMA LOGICA QUE METODO PERFORMEXCHANGE
    //todo: pensar que si aca pregunto si la moneda es pagable o no, estoy limitando la funcionalidad de transferir monedas, como por ejemplo el uso en la api, es transparente a si es pagable o no
    /*public void performTransfer(UUID userFrom, UUID userTo, String currency, double amount){
        Account accountFrom = getAccount(userFrom);
        Account accountTo = getAccount(userTo);
        Currency currencyFrom = plugin.getCurrencyManager().getCurrency(currency);

        if (accountFrom == null || accountTo == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if(currencyFrom == null ) {
            throw new CurrencyNotFoundException("currencies not found");
        }

        if (!accountFrom.hasEnough(currencyFrom, amount)) { //si no tiene suficiente moneda
            throw new InsufficientFundsException("Insufficient balance for currency: " + currencyFrom.getSingular());
        }
        if(!currencyFrom.isDecimalSupported()){ //si la moneda no soporta decimales
            if(amount % 1 != 0){
                throw new DecimalNotSupportedException("Currency does not support decimals");  //lanzar excepcion si no soporta decimales
            }
        }
        //TODO CANRECEIVE QUIZAS PUEDE MANEJARSE CON EL PERMISO DEL JUGADOR POR FUERA DE ESTA LOGICA
        //TODO: ACLARACION, ESTA FUNCIONALIDAD PARECE NO PODERSE MANIPULAR BAJO COMANDOS NI CON PERMISOS DENTRO DEL JUEGO: REVISAR
        if (!accountTo.canReceiveCurrency()){
            throw new AccountCanNotReciveException("Account can't receive currency"); //lanzar excepcion si no puede recibir monedas, lanzar una excepcion de tipo cuenta
        }
        //preguntar si el receptor puede recibir monedas
        accountFrom.getBalances().put(currencyFrom, accountFrom.getBalances().get(currencyFrom) - amount);
        accountTo.getBalances().put(currencyFrom, accountTo.getBalances().get(currencyFrom) + amount);
        try {
            plugin.getDataStore().transfer(accountFrom, accountTo); //actualizo ambos accounts de manera atomica, en una operacion segun hibernate
            plugin.getUpdateForwarder().sendUpdateMessage("account", accountFrom.getUuid().toString());// esto es para bungee
            plugin.getUpdateForwarder().sendUpdateMessage("account", accountTo.getUuid().toString());// esto es para bungee
            plugin.getEconomyLogger().log("[TRANSFER] Account: " + accountFrom.getDisplayName() + " transferred " + currencyFrom.format(amount) + " to " + accountTo.getDisplayName());
            // Log o cualquier otra acción post-transfer
        } catch (TransactionException e) {
            // Manejo de errores (puedes lanzar la excepción o manejarla de otra manera)
            throw new TransactionException("Failed to perform transfer: " + e.getMessage(), e);

        }

    }*/
//TODO ESTA FUNCIONALIDAD PERMITE TRADEAR 2 MONEDAS DISTINTAS ENTRE 2 PERSONAS DISTINTAS, NO NECESITA VALIDAR SI LA MONEDA ES PAGABLE O NO, YA QUE ES UN TRADE
    //trade, que 2 personas intercambien 2 monedas distintas
    /*public void performTrade(UUID userFrom, UUID userTo, String currencyFromS, String currencyToS, double amountFrom, double amountTo){
        Account accountFrom = getAccount(userFrom);
        Account accountTo = getAccount(userTo);
        Currency currencyFrom = plugin.getCurrencyManager().getCurrency(currencyFromS);
        Currency currencyTo = plugin.getCurrencyManager().getCurrency(currencyToS);

        if (currencyFrom == null || currencyTo == null || accountFrom == null || accountTo == null) {
            throw new IllegalArgumentException("Accounts or currencies not found");
        }
        if (!accountFrom.hasEnough(currencyFrom, amountFrom)) {
            throw new IllegalStateException("Insufficient balance for currency: " + currencyFrom.getUuid());
        }
        if (!accountTo.hasEnough(currencyTo, amountTo)) {
            throw new IllegalStateException("Insufficient balance for currency: " + currencyTo.getUuid());
        }
        //preguntar si el receptor puede recibir monedas
        accountFrom.getBalances().put(currencyFrom, accountFrom.getBalances().get(currencyFrom) - amountFrom);
        accountFrom.getBalances().put(currencyTo, accountFrom.getBalances().get(currencyTo) + amountTo);
        accountTo.getBalances().put(currencyTo, accountTo.getBalances().get(currencyTo) - amountTo);
        accountTo.getBalances().put(currencyFrom, accountTo.getBalances().get(currencyFrom) + amountFrom);
        try {
            plugin.getDataStore().transfer(accountFrom, accountTo); //actualizo ambos accounts de manera atomica, en una operacion segun hibernate
            plugin.getUpdateForwarder().sendUpdateMessage("account", accountFrom.getUuid().toString());// esto es para bungee
            plugin.getUpdateForwarder().sendUpdateMessage("account", accountTo.getUuid().toString());// esto es para bungee
            plugin.getEconomyLogger().log("[TRADE] Account: " + accountFrom.getDisplayName() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getDisplayName() + " for " + currencyTo.format(amountTo));
            // Log o cualquier otra acción post-trade
        } catch (Exception e) {
            // Manejo de errores (puedes lanzar la excepción o manejarla de otra manera)
            throw new RuntimeException("Failed to perform trade: " + e.getMessage(), e);
        }
    }*/



}