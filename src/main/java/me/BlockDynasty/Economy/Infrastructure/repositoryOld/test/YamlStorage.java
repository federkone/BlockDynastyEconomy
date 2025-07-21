/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */
package me.BlockDynasty.Economy.Infrastructure.repositoryOld.test;

public class YamlStorage {
    /*public final BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();
    private YamlConfiguration configuration;
    private File file;

    public YamlStorage(File file) {
        //super("YAML", false);
        this.file = file;
    }


    public void initialize() {
        if (!getFile().exists()) {
            try {
                if (getFile().createNewFile()) {
                    UtilServer.consoleLog("Data file created.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        configuration = new YamlConfiguration();
        try {
            configuration.load(getFile());
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
    }


    public void close() {

    }


    public void loadCurrencies() {
        ConfigurationSection section = getConfig().getConfigurationSection("currencies");
        if (section != null) {
            Set<String> currencies = section.getKeys(false);
            for (String uuid : currencies) {
                String path = "currencies." + uuid;
                String single = getConfig().getString(path + ".singular");
                String plural = getConfig().getString(path + ".plural");
                Currency currency = new Currency(UUID.fromString(uuid), single, plural);
                currency.setColor(ChatColor.valueOf(getConfig().getString(path + ".color").toUpperCase()));
                currency.setDecimalSupported(getConfig().getBoolean(path + ".decimalsupported"));
                currency.setDefaultBalance(getConfig().getDouble(path + ".defaultbalance"));
                currency.setDefaultCurrency(getConfig().getBoolean(path + ".defaultcurrency"));
                currency.setPayable(getConfig().getBoolean(path + ".payable"));
                currency.setSymbol(getConfig().getString(path + ".symbol"));
                currency.setExchangeRate(getConfig().getDouble(path + ".exchange_rate"));
                plugin.getCurrencyManager().add(currency);
                UtilServer.consoleLog("Loaded currency: " + currency.getSingular());
            }
        }
    }


    public void saveCurrency(Currency currency) {
        String path = "currencies." + currency.getUuid().toString();
        getConfig().set(path + ".singular", currency.getSingular());
        getConfig().set(path + ".plural", currency.getPlural());
        getConfig().set(path + ".defaultbalance", currency.getDefaultBalance());
        getConfig().set(path + ".symbol", currency.getSymbol());
        getConfig().set(path + ".decimalsupported", currency.isDecimalSupported());
        getConfig().set(path + ".defaultcurrency", currency.isDefaultCurrency());
        getConfig().set(path + ".payable", currency.isPayable());
        getConfig().set(path + ".color", currency.getColor().name());
        getConfig().set(path + ".exchange_rate", currency.getExchangeRate());
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteCurrency(Currency currency) {
        String path = "currencies." + currency.getUuid().toString();
        getConfig().set(path, null);
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getTopList(Currency currency, int offset, int amount, Callback<LinkedList<CachedTopListEntry>> callback) {
        throw new UnsupportedOperationException("YAML does not support Top Lists!");
    }

    private void loadBalances(Account account) {
        String path = "accounts." + account.getUuid().toString();
        ConfigurationSection bsection = getConfig().getConfigurationSection(path + ".balances");
        if (bsection != null) {
            Set<String> balances = bsection.getKeys(false);
            if (balances != null && !balances.isEmpty()) {
                for (String currency : balances) {
                    String path2 = path + ".balances." + currency;
                    double balance = getConfig().getDouble(path2);
                    Currency c = plugin.getCurrencyManager().getCurrency(UUID.fromString(currency));
                    if (c != null) {
                        account.modifyBalance(c, balance, false);
                    }
                }
            }
        }
    }


    public ArrayList<Account> getOfflineAccounts() {
        String path = "accounts";
        ArrayList<Account> accounts = new ArrayList<>();
        for(String uuid : getConfig().getConfigurationSection(path).getKeys(false)){
            Account acc = loadAccount(UUID.fromString(uuid));
            accounts.add(acc);
        }
        return accounts;
    }


    public void createAccount(Account account) {
        saveAccount(account);
    }


    public Account loadAccount(String name) {
        ConfigurationSection section = getConfig().getConfigurationSection("accounts");
        if (section != null) {
            Set<String> accounts = section.getKeys(false);
            if (accounts != null && !accounts.isEmpty()) {
                for (String uuid : accounts) {
                    String path = "accounts." + uuid;
                    String nick = getConfig().getString(path + ".nickname");
                    if (nick != null && nick.equalsIgnoreCase(name)) {
                        Account account = new Account(UUID.fromString(uuid), nick);
                        account.setCanReceiveCurrency(getConfig().getBoolean(path + ".payable"));
                        loadBalances(account);
                        return account;
                    }
                }
            }
        }
        UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
        Account account = new Account(uuid, name);
        createAccount(account);
        saveAccount(account);
        return account;
    }


    public Account loadAccount(UUID uuid) {
        String path = "accounts." + uuid.toString();
        String nick = getConfig().getString(path + ".nickname");
        if (nick != null) {
            Account account = new Account(uuid, nick);
            account.setCanReceiveCurrency(getConfig().getBoolean(path + ".payable"));
            loadBalances(account);
            return account;
        }
            Account account =  new Account(uuid, Bukkit.getOfflinePlayer(uuid).getName());
            createAccount(account);
            saveAccount(account);
            return account;
    }


    public void loadAccount(UUID uuid, Callback<Account> callback) {
        SchedulerUtils.runAsync(() -> {
            Account account = this.loadAccount(uuid);
            SchedulerUtils.run(() -> callback.call(account));
        });
    }


    public void loadAccount(String name, Callback<Account> callback) {
        SchedulerUtils.runAsync(() -> {
            Account account = this.loadAccount(name);
            SchedulerUtils.run(() -> callback.call(account));
        });
    }


    public void saveAccount(Account account) {
        String path = "accounts." + account.getUuid().toString();
        getConfig().set(path + ".nickname", account.getNickname());
        getConfig().set(path + ".uuid", account.getUuid().toString());
        for (Currency currency : account.getBalances().keySet()) {
            double balance = account.getBalance(currency);
            getConfig().set(path + ".balances." + currency.getUuid().toString(), balance);
        }
        getConfig().set(path + ".payable", account.canReceiveCurrency());
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteAccount(Account account) {
        String path = "accounts." + account.getUuid().toString();
        getConfig().set(path, null);
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateCurrencyLocally(Currency currency) {
        throw new UnsupportedOperationException("YAML does not support updates. Only READ/WRITE");
    }

    public YamlConfiguration getConfig() {
        return configuration;
    }

    public File getFile() {
        return file;
    }
    */
}

