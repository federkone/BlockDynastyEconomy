package BlockDynasty.BukkitImplementation.config.file;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import files.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationFile {
    private static BlockDynastyEconomy plugin;

    public static void init(BlockDynastyEconomy plugin) {
        ConfigurationFile.plugin = plugin;
        loadDefaultConfig();
        //MessageFile.init(plugin); se inicializaria el archivo de mensajes
    }

    private static void loadDefaultConfig() {
        FileConfiguration config = plugin.getConfig();
        Configuration configuration = new Configuration();

        config.options().header(configuration.getHeader());

        for (String key : configuration.getParameters().keySet()) {
            config.addDefault(key, configuration.getParameters().get(key));
        }

        config.options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
    }

}

        /*config.addDefault(path + "prefix", "&2&lBlockDynasty> ");
        config.addDefault(path + "nopermission", "&7You don't have permission to do this.");
        config.addDefault(path + "noconsole", "&7Console cannot do this.");
        config.addDefault(path + "invalidamount", "&7Not a valid amount.");
        config.addDefault(path + "invalidpage", "&7Not a valid page number.");
        config.addDefault(path + "pay_yourself", "&7You can't pay yourself.");
        config.addDefault(path + "player_is_null", "&7The specified player does not exist.");
        config.addDefault(path + "unknownCurrency", "&7Unknown Currency.");
        config.addDefault(path + "unknownCommand", "&7Unknown sub-command.");
        config.addDefault(path + "noDefaultCurrency", "&7No default currency.");
        config.addDefault(path + "currencyExists", "&7Currency already exists.");
        config.addDefault(path + "accountMissing", "&7Your account is missing. Please relog the server.");
        config.addDefault(path + "cannotReceiveMoney", "&a{player}&7 can't receive money.");
        config.addDefault(path + "insufficientFunds", "&7You don't have enough {currencycolor}{currency}&7!");
        config.addDefault(path + "targetInsufficientFunds", "&e{target} &7don't have enough {currencycolor}{currency}&7!");
        config.addDefault(path + "paid", "&7You were paid {currencycolor}{amount} &7from &a{player}&7.");
        config.addDefault(path + "payer", "&7You paid {currencycolor}{amount} &7to &a{player}&7.");
        config.addDefault(path + "payNoPermission", "&7You don't have permission to pay {currencycolor}{currency}&7.");
        config.addDefault(path + "currencyNotPayable", "{currencycolor}{currency} &7is not payable.");
        config.addDefault(path + "add", "&7You gave &a{player}&7: {currencycolor}{amount}. ");
        config.addDefault(path + "take", "&7You took {currencycolor}{amount} &7from &a{player}&7.");
        config.addDefault(path + "set", "&7You set &a{player}&7's balance to {currencycolor}{amount}&7.");

        config.addDefault(path + "exchange_rate_set", "&7Set the exchange rate for {currencycolor}{currency} &7to &a{amount}&7.");
        config.addDefault(path + "exchange_success_custom_other", "&7Successfully exchanged {currencycolor}({currEx}) &7for {currencycolor2}{receivedCurr}&7 to player {player}&7.");
        config.addDefault(path + "exchange_success_custom", "&7Successfully exchanged {currencycolor}({currEx}) &7for {currencycolor2}{receivedCurr}&7.");
        config.addDefault(path + "exchange_success", "&7Successfully exchanged {currencycolor}{ex_curr} &7for equivalent value in {currencycolor2}{re_curr}&7.");
        config.addDefault(path + "exchange_command.no_perms.preset", "&7You don't have permission to exchange currencies with a preset rate.");
        config.addDefault(path + "exchange_command.no_perms.custom", "&7You don't have permission to exchange currencies with a custom rate.");

        config.addDefault(path + "balance.current", "&a{player}&7's balance is: {currencycolor}{balance}");
        config.addDefault(path + "balance.multiple", "&a{player}&7's balances:");
        config.addDefault(path + "balance.list", "&a&l>> {currencycolor}{format}");
        config.addDefault(path + "balance.none", "&7No balances to show for &c{player}&7.");

        config.addDefault(path + "balance_top.balance", "&a&l-> {number}. &b{player} &7- {currencycolor}{balance}");
        config.addDefault(path + "balance_top.header", "&f----- {currencycolor} Top Balances for {currencyplural} &7(Page {page})&f -----");
        config.addDefault(path + "balance_top.empty", "&7No accounts to display.");
        config.addDefault(path + "balance_top.next", "{currencycolor}/gbaltop {currencyplural} {page} &7for more.");
        config.addDefault(path + "balance_top.nosupport", "&a{storage} &7doesn't support /baltop.");

        config.addDefault(path + "cheque.success", "&7Cheque successfully written.");
        config.addDefault(path + "cheque.redeemed", "&7Cheque has been cashed in.");
        config.addDefault(path + "cheque.invalid", "&7This is not a valid cheque.");

        config.addDefault(path+"withdraw_success","&7Se ha retirado {amount} &7de tu cuenta.");
        config.addDefault(path+"deposit_success","&7Se ha depositado {amount} &7en tu cuenta.");
        config.addDefault(path+"setbalance_success","&7Se ha establecido tu balance a {amount}.");

        config.addDefault(path + "help.eco_command", Arrays.asList(
                "{prefix}&e&lEconomy Help",
                "&2&l>> &a/eco give <user> <amount> [currency] &8- &7Give a player an amount of a currency.",
                "&2&l>> &a/eco take <user> <amount> [currency] &8- &7Take an amount of a currency from a player.",
                "&2&l>> &a/eco set <user> <amount> [currency] &8- &7Set a players amount of a currency.",
                "&2&l>> ",
                "&2&l>> &a/eco buycommand <jugador> <cantidad> <tipo> <comandoAEntregar> &8- &7sell command at player.",
                "&2&l>> &a/eco currency  &8- &7sub comands for create,edit currencies."
                ));

        config.addDefault(path + "help.exchange_command", Arrays.asList(
                "{prefix}&b&lExchange Help",
                "&2&l>> &a/exchange <currency_to_exchange> <currency_to_receive> <amount>  &8- &7Exchange between currencies.",
                "&2&l>> &a/exchange <currency_to_exchange> <currency_to_receive> <amount> <account> &8- &7Exchange between currencies for an account."));

        config.addDefault(path + "usage.pay_command", "&2&l>> &a/pay <user> <amount> [currency] &8- &7Pay the specified user the specified amount.");
        config.addDefault(path + "usage.give_command", "&2&l>> &a/eco give <user> <amount> [currency] &8- &7Give a player an amount of a currency.");
        config.addDefault(path + "usage.take_command", "&2&l>> &a/eco take <user> <amount> [currency] &8- &7Take an amount of a currency from a player.");
        config.addDefault(path + "usage.set_command", "&2&l>> &a/eco set <user> <amount> [currency] &8- &7Set a players amount of a currency.");
        config.addDefault(path + "usage.buycommand"," &2&l>> &a/eco buycommand <jugador> <cantidad> <tipo> <comandoAEntregar> &8- &7sell command at player.");

        config.addDefault(path + "help.cheque_command", Arrays.asList("{prefix}&e&lCheque Help",
                "&2&l>> &a/cheque write <amount> [currency] &8- &7Write a cheque with a specified amount and currency.",
                "&2&l>> &a/cheque redeem &8- &7Redeem the cheque."));

        config.addDefault(path + "help.currency_command", Arrays.asList("{prefix}&e&lCurrency Help",
                "&2&l>> &a/eco currency create <singular> <plural> &8- &7Create a currency.",
                "&2&l>> &a/eco currency delete <plural> &8- &7Delete a currency.",
                "&2&l>> &a/eco currency convert <method> &8- &7Convert storage method. WARN: Take backups first and make sure the storage you are switching to is empty!",
                "&2&l>> &a/eco currency backend <method> &8- &7Switch backend. This does not convert.",
                "&2&l>> &a/eco currency view <plural> &8- &7View information about a currency.",
                "&2&l>> &a/eco currency list &8- &7List of currencies.",
                "&2&l>> &a/eco currency symbol <plural> <char|remove> &8- &7Select a symbol for a currency or remove it.",
                "&2&l>> &a/eco currency color <plural> <color> &8- &7Select a color for a currency.",
                "&2&l>> &a/eco currency colorlist &8- &7List of Colors.",
                "&2&l>> &a/eco currency decimals <plural> &8- &7Enable decimals for a currency.",
                "&2&l>> &a/eco currency payable <plural> &8- &7Set a currency payable or not.",
                "&2&l>> &a/eco currency default <plural> &8- &7Set a currency as default.",
                "&2&l>> &a/eco currency startbal <plural> <amount> &8- &7Set the starting balance for a currency.",
                "&2&l>> &a/eco currency setrate <plural> <amount> &8- &7Sets the currency's exchange rate."));

        config.addDefault(path + "usage.currency_create", "&2&l>> &a/eoc currency create <singular> <plural> &8- &7Create a currency.");
        config.addDefault(path + "usage.currency_delete", "&2&l>> &a/eco currency delete <plural> &8- &7Delete a currency.");
        config.addDefault(path + "usage.currency_convert", "&2&l>> &a/eco currency convert <method> &8- &7Convert storage method. WARN: Take backups first and make sure the storage you are switching to is empty!");
        config.addDefault(path + "usage.currency_backend", "&2&l>> &a/eco currency backend <method> &8- &7Switch backend. This does not convert.");
        config.addDefault(path + "usage.currency_view", "&2&l>> &a/eco currency view <plural> &8- &7View information about a currency.");
        config.addDefault(path + "usage.currency_list", "&2&l>> &a/eco currency list &8- &7List of currencies.");
        config.addDefault(path + "usage.currency_symbol", "&2&l>> &a/eco currency symbol <plural> <char|remove> &8- &7Select a symbol for a currency or remove it.");
        config.addDefault(path + "usage.currency_color", "&2&l>> &a/eco currency color <plural> <color> &8- &7Select a color for a currency.");
        config.addDefault(path + "usage.currency_colorlist", "&2&l>> &a/eco currency colorlist &8- &7List of Colors.");
        config.addDefault(path + "usage.currency_payable", "&2&l>> &a/eco currency payable <plural> &8- &7Set a currency payable or not.");
        config.addDefault(path + "usage.currency_default", "&2&l>> &a/eco currency default <plural> &8- &7Set a currency as default.");
        config.addDefault(path + "usage.currency_decimals", "&2&l>> &a/eco currency decimals <plural> &8- &7Enable decimals for a currency.");
        config.addDefault(path + "usage.currency_startbal", "&2&l>> &a/eco currency startbal <plural> <amount> &8- &7Set the starting balance for a currency.");
        config.addDefault(path + "usage.currency_setrate", "&2&l>> &a/eco currency setrate <plural> <amount> &8- &7Sets the currency's exchange rate.");


        //--------------------------new--Offer-----------------------------------------------------
        config.addDefault(path+"too_far","&cEl jugador objetivo está demasiado lejos. Deben estar dentro de {limit} bloques de distancia." );
        config.addDefault(path+"offer_yourself","&cNo puedes ofrecerte a ti mismo." );
        config.addDefault(path+"offline","&cEl jugador que te hizo la oferta ya no está en línea.");
        config.addDefault(path+"not_offers","&cNo tienes ofertas pendientes.");
        config.addDefault(path+"already_offer","&cYa tienes una oferta pendiente para este jugador.");
        config.addDefault(path+"only_players","&2&l>> &cSolo los jugadores pueden hacer esto.");
        config.addDefault(path+"usage.offer","&2&l>> &aUso: /offer <create/accept/deny/cancel>");
        config.addDefault(path+"usage.offer_create","&2&l>> &aUso: /offer create <cantidad> <tipoMoneda> <monto> <tipoMoneda> <Ajugador>");
        config.addDefault(path+"usage.offer_accept","&2&l>> &aUso: /offer accept <Ajugador>");
        config.addDefault(path+"usage.offer_deny","&2&l>> &aUso: /offer deny <Ajugador>");
        config.addDefault(path+"usage.offer_cancel","&2&l>> &aUso: /offer cancel <Ajugador>");

        config.addDefault(path + "send_offer", "&7Has ofrecido{currencycolorOffert} {amountOffert} &7por{currencycolorValue} {amountValue} &7a &a{player}&7.");
        config.addDefault(path + "receive_offer", "&7Has recibido una oferta de{currencycolorOffert} {amountOffert} &7por{currencycolorValue} {amountValue} &7del jugador &a{player}&7. Usa /offer accept {player}, o /offer accept {player}."); //todo aqui
        config.addDefault(path + "accept_offer_to", "&7Has aceptado la oferta de &a{player}&7.");
        config.addDefault(path + "accept_offer", "&7Tu oferta para &a{player}&7 ha sido aceptada.");
        config.addDefault(path+ "cancel_offer_to","&7Has cancelado la oferta para &a{player}&7."); //cancelado
        config.addDefault(path+ "cancel_offer","&a{player}&7 cancelo la oferta."); //cancelado
        config.addDefault(path+ "deny_offer_to","&7Has rechazado la oferta de &a{player}&7."); //denegada
        config.addDefault(path+ "deny_offer","&7Tu oferta para &a{player}&7 ha sido rechazada."); //denagada

        config.addDefault(path+"offerExpiredTo","&7La oferta de &a{player}&7 ha expirado."); //expirado
        config.addDefault(path+"offerExpired","&7Tu oferta a &a{player}&7 ha expirado."); //expirado


       //-----------------------------------buy command-------------------------------------------------------

        config.addDefault(path+"buy_success","&7Compra realizada con exito!");
        config.addDefault(path+"buy_no_perms","&7No tienes permiso para ejecutar el comando de compra.");
        config.addDefault(path+"buy_usage","&2&l>> &aUso: /eco buycommand <jugador> <cantidad> <tipo> <comandoAEntregar>");
        config.addDefault(path+"buy_no_player","&cEl jugador no está en línea.");
*/