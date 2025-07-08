
package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands;

public class ChequeCommand  {

    /*private final BlockDynastyEconomy plugin;
    private final ChequeManager chequeManager;
    private final AccountManager accountManager;
    private final CurrencyManager currencyManager;

    public ChequeCommand(BlockDynastyEconomy plugin) {
        this.plugin = plugin;
        this.chequeManager = plugin.getChequeManager();
        this.accountManager = plugin.getAccountManager();
        this.currencyManager = plugin.getCurrencyManager();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!plugin.isChequesEnabled()){
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(F.getNoConsole());
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("gemseconomy.command.cheque")) {
            player.sendMessage(F.getNoPerms());
            return true;
        }
        if (args.length == 0) {
            F.getChequeHelp(player);
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("redeem")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType().equals(Material.valueOf(plugin.getConfig().getString("cheque.material")))) {
                    if (chequeManager.isValid(item)) {
                        double value = chequeManager.getValue(item);
                        if (item.getAmount() > 1) {
                            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                        } else {
                            player.getInventory().remove(item);
                        }
                        Account user = accountManager.getAccount(player);
                        Currency currency = chequeManager.getCurrency(item);
                        accountManager.deposit(user,currency, value);
                        player.sendMessage(F.getChequeRedeemed());
                        return true;
                    } else {
                        player.sendMessage(F.getChequeInvalid());
                    }
                } else {
                    player.sendMessage(F.getChequeInvalid());
                }
            } else {
                player.sendMessage(F.getUnknownSubCommand());
            }
        }

        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("write")) {

                if (UtilString.validateInput(sender, args[1])) {

                    double amount = Double.parseDouble(args[1]);
                    if (amount != 0) {

                        if (args.length == 3) {

                            Currency currency = currencyManager.getCurrency(args[2]);
                            Account user = accountManager.getAccount(player);
                            if (currency != null) {
                                if(user.hasEnough(currency, amount)) {
                                    accountManager.withdraw(user,currency, amount);
                                    player.getInventory().addItem(chequeManager.write(player.getName(), currency, amount));
                                    player.sendMessage(F.getChequeSucess());
                                    return true;
                                }else{
                                    player.sendMessage(F.getInsufficientFunds().replace("{currencycolor}", currency.getColor()+"").replace("{currency}", currency.getSingular()));
                                }
                            } else {
                                player.sendMessage(F.getUnknownCurrency());
                            }
                        }

                        Currency defaultCurrency = currencyManager.getDefaultCurrency();
                        Account user = accountManager.getAccount(player);
                        if(user.hasEnough(defaultCurrency,amount)) {
                            accountManager.withdraw(user,currencyManager.getDefaultCurrency(), amount);
                            player.getInventory().addItem(chequeManager.write(player.getName(), defaultCurrency, amount));
                            player.sendMessage(F.getChequeSucess());
                            return true;
                        }else{
                            player.sendMessage(F.getInsufficientFunds().replace("{currencycolor}", defaultCurrency.getColor()+"").replace("{currency}", defaultCurrency.getSingular()));
                        }
                    } else {
                        player.sendMessage(F.getUnvalidAmount());
                    }
                } else {
                    player.sendMessage(F.getUnvalidAmount());
                }
            } else {
                player.sendMessage(F.getUnknownSubCommand());
            }
        }
        return true;
    }*/
}
