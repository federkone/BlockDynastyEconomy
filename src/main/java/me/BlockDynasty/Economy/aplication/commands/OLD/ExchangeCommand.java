package me.BlockDynasty.Economy.aplication.commands.OLD;

public class ExchangeCommand  {
/*
    private final BlockDynastyEconomy plugin ;
    private final AccountManager accountManager;
    private final CurrencyManager currencyManager;

    public ExchangeCommand(BlockDynastyEconomy plugin) {
            this.plugin = plugin;
            this.accountManager=plugin.getAccountManager();
            this.currencyManager=plugin.getCurrencyManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String v21315, String[] args) {
        SchedulerUtils.runAsync(() -> {
            if (!sender.hasPermission("gemseconomy.command.exchange")) {
                sender.sendMessage(F.getNoPerms());
                return;
            }

            if (args.length == 0) {
                F.getExchangeHelp(sender);
            } else if (args.length == 3) {

                if (!sender.hasPermission("gemseconomy.command.exchange.preset")) {
                    sender.sendMessage(F.getExchangeNoPermPreset());
                    return;
                }
                Currency toExchange = currencyManager.getCurrency(args[0]);
                Currency toReceive = currencyManager.getCurrency(args[2]);
                double amount;

                if (toExchange != null && toReceive != null) {
                    if (toReceive.isDecimalSupported()) {
                        try {
                            amount = Double.parseDouble(args[1]);
                            if (amount <= 0.0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            sender.sendMessage(F.getUnvalidAmount());
                            return;
                        }
                    } else {
                        try {
                            amount = Integer.parseInt(args[1]);
                            if (amount <= 0.0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            sender.sendMessage(F.getUnvalidAmount());
                            return;
                        }
                    }
                    Account account = accountManager.getAccount(sender.getName());
                    if (account != null) {
                        if (accountManager.convert(account,toExchange, amount, toReceive, -1)) {
                            sender.sendMessage(F.getExchangeSuccess()
                                    .replace("{currencycolor}", "" + toExchange.getColor())
                                    .replace("{ex_curr}", toExchange.format(amount))
                                    .replace("{currencycolor2}", "" + toReceive.getColor())
                                    .replace("{re_curr}", toReceive.getPlural()));
                        }
                    }
                } else {
                    sender.sendMessage(F.getUnknownCurrency());
                }

            } else if (args.length == 4) {
                if (!sender.hasPermission("gemseconomy.command.exchange.custom")) {
                    sender.sendMessage(F.getExchangeNoPermCustom());
                    return;
                }
                Currency toExchange = currencyManager.getCurrency(args[0]);
                Currency toReceive = currencyManager.getCurrency(args[2]);
                double toExchangeAmount = 0.0;
                double toReceiveAmount = 0.0;

                if (toExchange != null && toReceive != null) {
                    if (toExchange.isDecimalSupported() || toReceive.isDecimalSupported()) {
                        try {
                            toExchangeAmount = Double.parseDouble(args[1]);
                            toReceiveAmount = Double.parseDouble(args[3]);
                            if (toExchangeAmount <= 0.0 || toReceiveAmount <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            sender.sendMessage(F.getUnvalidAmount());
                        }
                    } else {
                        try {
                            toExchangeAmount = Integer.parseInt(args[1]);
                            toReceiveAmount = Integer.parseInt(args[3]);
                            if (toExchangeAmount <= 0.0 || toReceiveAmount <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            sender.sendMessage(F.getUnvalidAmount());
                        }
                    }
                    Account account = accountManager.getAccount(sender.getName());
                    if (account != null) {
                        if (accountManager.convert(account,toExchange, toExchangeAmount, toReceive, toReceiveAmount)) {
                            sender.sendMessage(F.getExchangeSuccessCustom()
                                    .replace("{currencycolor}", "" + toExchange.getColor())
                                    .replace("{currEx}", toExchange.format(toExchangeAmount))
                                    .replace("{currencycolor2}", "" + toReceive.getColor())
                                    .replace("{receivedCurr}", toReceive.format(toReceiveAmount)));
                        }
                    }
                } else {
                    sender.sendMessage(F.getUnknownCurrency());
                }
            } else if (args.length == 5) {
                if (!sender.hasPermission("gemseconomy.command.exchange.custom.other")) {
                    sender.sendMessage(F.getExchangeNoPermCustom());
                    return;
                }
                Account account =accountManager.getAccount(args[0]);
                if (account == null) {
                    sender.sendMessage(F.getPlayerDoesNotExist());
                    return;
                }
                Currency toExchange = currencyManager.getCurrency(args[1]);
                Currency toReceive = currencyManager.getCurrency(args[3]);
                double toExchangeAmount = 0.0;
                double toReceiveAmount = 0.0;

                if (toExchange != null && toReceive != null) {
                    if (toExchange.isDecimalSupported() || toReceive.isDecimalSupported()) {
                        try {
                            toExchangeAmount = Double.parseDouble(args[2]);
                            toReceiveAmount = Double.parseDouble(args[4]);
                            if (toExchangeAmount <= 0.0 || toReceiveAmount <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            sender.sendMessage(F.getUnvalidAmount());
                        }
                    } else {
                        try {
                            toExchangeAmount = Integer.parseInt(args[2]);
                            toReceiveAmount = Integer.parseInt(args[4]);
                            if (toExchangeAmount <= 0.0 || toReceiveAmount <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            sender.sendMessage(F.getUnvalidAmount());
                        }
                    }

                    if (accountManager.convert(account,toExchange, toExchangeAmount, toReceive, toReceiveAmount)) {
                        sender.sendMessage(F.getExchangeSuccessCustomOther()
                                .replace("{player}", account.getDisplayName())
                                .replace("{currencycolor}", "" + toExchange.getColor())
                                .replace("{currEx}", toExchange.format(toExchangeAmount))
                                .replace("{currencycolor2}", "" + toReceive.getColor())
                                .replace("{receivedCurr}", toReceive.format(toReceiveAmount)));
                    }

                } else {
                    sender.sendMessage(F.getUnknownCurrency());
                }
            } else {
                sender.sendMessage(F.getUnknownSubCommand());
            }
        });
        return true;
    }

 */
}
