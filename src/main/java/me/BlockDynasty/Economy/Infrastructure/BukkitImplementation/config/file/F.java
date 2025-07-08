
package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
//TODO: MIGRAR EL MESSAGE SERVICE AQUI SI ES POSIBLE Y CONSTRUIR DINAMICAMENTE LOS MENSAJES
public class F {
    private static BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance(); //TODO: REVISAR LA NECEISTAD DE TENER UNA REFERENCIA
    private static FileConfiguration cfg = plugin.getConfig();

    public F(){
    }

    private static String get(String path){
        return colorize(cfg.getString(path));
    }

    private static List<String> getList(String path){
        List<String> str = new ArrayList<>();
        for(String s : cfg.getStringList(path)){
            str.add(colorize(s));
        }
        return str;
    }

    private static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getPrefix() {
        return colorize(cfg.getString("Messages.prefix"));
    }

    public static String getNoPerms() {
        return getPrefix() + colorize(cfg.getString("Messages.nopermission"));
    }

    public static String getNoConsole() {
        return getPrefix() + colorize(cfg.getString("Messages.noconsole"));
    }

    public static String getInsufficientFunds() { return getPrefix() + colorize(cfg.getString("Messages.insufficientFunds")); }

    public static String getTargetInsufficientFunds() { return getPrefix() + colorize(cfg.getString("Messages.targetInsufficientFunds")); }

    public static String getPayerMessage() {
        return getPrefix() + colorize(cfg.getString("Messages.payer"));
    }

    public static String getPaidMessage() {
        return getPrefix() + colorize(cfg.getString("Messages.paid"));
    }

    public static String getPayUsage() {
        return colorize(cfg.getString("Messages.usage.pay_command"));
    }

    public static String getAddMessage() {
        return getPrefix() + colorize(cfg.getString("Messages.add"));
    }

    public static String getTakeMessage() {
        return getPrefix() + colorize(cfg.getString("Messages.take"));
    }

    public static String getSetMessage() {
        return getPrefix() + colorize(cfg.getString("Messages.set"));
    }

    public static String getPlayerDoesNotExist() { return getPrefix() + colorize(cfg.getString("Messages.player_is_null")); }

    public static String getPayYourself() {
        return getPrefix() + colorize(cfg.getString("Messages.pay_yourself"));
    }

    public static String getUnknownCurrency() { return getPrefix() + colorize(cfg.getString("Messages.unknownCurrency")); }

    public static String getUnknownSubCommand() {
        return getPrefix() + colorize(cfg.getString("Messages.unknownCommand"));
    }

    public static void getManageHelp(CommandSender sender) {
        for (String s : cfg.getStringList("Messages.help.eco_command")) {
            sender.sendMessage(colorize(s.replace("{prefix}", getPrefix())));
        }
    }

    public static void getExchangeHelp(CommandSender sender) {
        for (String s : cfg.getStringList("Messages.help.exchange_command")) {
            sender.sendMessage(colorize(s.replace("{prefix}", getPrefix())));
        }
    }

    public static String getBalance() {
        return getPrefix() + colorize(cfg.getString("Messages.balance.current"));
    }
    public static String getBalanceMultiple() { return getPrefix() + colorize(cfg.getString("Messages.balance.multiple")); }
    public static String getBalanceList() {
        return colorize(cfg.getString("Messages.balance.list"));
    }

    public static String getUnvalidAmount() {
        return getPrefix() + colorize(cfg.getString("Messages.invalidamount"));
    }
    public static String getUnvalidPage() {
        return getPrefix() + colorize(cfg.getString("Messages.invalidpage"));
    }

    public static void getChequeHelp(CommandSender sender) {
        for (String s : cfg.getStringList("Messages.help.cheque_command")) {
            sender.sendMessage(colorize(s.replace("{prefix}", getPrefix())));
        }
    }

    public static String getChequeSucess() {
        return getPrefix() + colorize(cfg.getString("Messages.cheque.success"));
    }

    public static String getChequeRedeemed() { return getPrefix() + colorize(cfg.getString("Messages.cheque.redeemed")); }

    public static String getChequeInvalid() {
        return getPrefix() + colorize(cfg.getString("Messages.cheque.invalid"));
    }

    public static String getGiveUsage(){
        return colorize(cfg.getString("Messages.usage.give_command"));
    }

    public static String getTakeUsage(){
        return colorize(cfg.getString("Messages.usage.take_command"));
    }

    public static String getSetUsage(){
        return colorize(cfg.getString("Messages.usage.set_command"));
    }

    public static String getBalanceTopHeader(){
        return colorize(cfg.getString("Messages.balance_top.header"));
    }

    public static String getBalanceTopEmpty(){
        return colorize(cfg.getString("Messages.balance_top.empty"));
    }

    public static String getBalanceTopNext(){
        return colorize(cfg.getString("Messages.balance_top.next"));
    }

    public static String getBalanceTop(){
        return colorize(cfg.getString("Messages.balance_top.balance"));
    }

    public static String getNoDefaultCurrency(){
        return getPrefix() + colorize(cfg.getString("Messages.noDefaultCurrency"));
    }

    public static String getBalanceNone(){
        return getPrefix() + colorize(cfg.getString("Messages.balance.none"));
    }

    public static String getBalanceTopNoSupport(){
        return getPrefix() + colorize(cfg.getString("Messages.balance_top.nosupport"));
    }

    public static String getPayNoPerms(){
        return getPrefix() + colorize(cfg.getString("Messages.payNoPermission"));
    }

    public static String getCurrencyNotPayable(){
        return getPrefix() + colorize(cfg.getString("Messages.currencyNotPayable"));
    }

    public static String getAccountMissing(){
        return getPrefix() + colorize(cfg.getString("Messages.accountMissing"));
    }

    public static String getCannotReceive(){
        return getPrefix() + colorize(cfg.getString("Messages.cannotReceiveMoney"));
    }


    public static String getCurrencyUsage_Create() { return get("Messages.usage.currency_create"); }
    public static String getCurrencyUsage_Delete() { return get("Messages.usage.currency_delete"); }
    public static String getCurrencyUsage_View() { return get("Messages.usage.currency_view"); }
    public static String getCurrencyUsage_Default() { return get("Messages.usage.currency_default"); }
    public static String getCurrencyUsage_List() { return get("Messages.usage.currency_list"); }
    public static String getCurrencyUsage_Color() { return get("Messages.usage.currency_color"); }
    public static String getCurrencyUsage_Colorlist() { return get("Messages.usage.currency_colorlist"); }
    public static String getCurrencyUsage_Payable() { return get("Messages.usage.currency_payable"); }
    public static String getCurrencyUsage_Startbal() { return get("Messages.usage.currency_startbal"); }
    public static String getCurrencyUsage_Decimals() { return get("Messages.usage.currency_decimals"); }
    public static String getCurrencyUsage_Symbol() { return get("Messages.usage.currency_symbol"); }
    public static String getCurrencyUsage_Rate() { return get("Messages.usage.currency_setrate"); }
    public static String getCurrencyUsage_Backend() {
        return get("Messages.usage.currency_backend");
    }
    public static String getCurrencyUsage_Convert() {
        return get("Messages.usage.currency_convert");
    }

    public static String getOfferUsage() {return get("Messages.usage.offer");}
    public static String getOfferUsageCreate() {return get("Messages.usage.offer_create");}
    public static String getOfferUsageAccept() {return get("Messages.usage.offer_accept");}
    public static String getOfferUsageDeny() {return get("Messages.usage.offer_deny");}
    public static String getOfferUsageCancel() {return get("Messages.usage.offer_cancel");}
    public static String getOfferSender() {return get("Messages.send_offer");}
    public static String getOfferReceiver() {return get("Messages.receive_offer");}
    public static String getOfferAccept() {return get("Messages.accept_offer");}
    public static String getOfferAcceptTo() {return get("Messages.accept_offer_to");}
    public static String getOfferDeny() {return get("Messages.deny_offer");}
    public static String getOfferDenyTo() {return get("Messages.deny_offer_to");}
    public static String getOfferCancel() {return get("Messages.cancel_offer");}
    public static String getOfferCancelTo() {return get("Messages.cancel_offer_to");}
    public static String getNotOffers() {return get("Messages.not_offers");}
    public static String getAlreadyOffer() {return get("Messages.already_offer");}
    public static String getOfflinePlayer() { return get("Messages.offline"); }
    public static String getOfferExpired(String name){return get("Messages.offerExpired").replace("{player}",name);}
    public static String getOfferExpiredTo(String name){return get("Messages.offerExpiredTo").replace("{player}",name);}
    public static String getOfferYourself() { return get("Messages.offer_yourself"); }
    public static boolean getEnableDistanceLimitOffer(){
        return cfg.getBoolean("enableDistanceLimitOffer");
    }

    public static double getDistanceLimitOffer(){
        return cfg.getDouble("maxDistanceOffer");
    }

    public static String getTooFar(double limit) {
        return getPrefix() + colorize(cfg.getString("Messages.too_far")).replace("{limit}", String.valueOf(limit));
    }

    public static String getBuyCommandUsage() {
        return  getPrefix() + colorize(cfg.getString("Messages.usage.buy_usage"));
    }



    public static String getBuyCommandSuccess() {
        return  getPrefix() + colorize(cfg.getString("Messages.buy_success"));
    }

    public static String getBuyCommandOffline() {
        return getPrefix() + colorize(cfg.getString("Messages.buy_no_player"));
    }

    public static void sendCurrencyUsage(CommandSender sender){
        for(String s : getList("Messages.help.currency_command")){
            sender.sendMessage(s.replace("{prefix}", getPrefix()));
        }
    }

    public static String getExchangeSuccess(){
        return getPrefix() + colorize(cfg.getString("Messages.exchange_success"));
    }

    public static String getWithdrawSuccess(){
        return getPrefix() + colorize(cfg.getString("Messages.withdraw_success"));
    }

    public static String getDepositSuccess(){
        return getPrefix() + colorize(cfg.getString("Messages.deposit_success"));
    }

    public static String getSetSuccess(){
        return getPrefix() + colorize(cfg.getString("Messages.setbalance_success"));
    }

    public static String getExchangeSuccessCustom(){
        return getPrefix() + colorize(cfg.getString("Messages.exchange_success_custom"));
    }

    public static String getExchangeSuccessCustomOther(){
        return getPrefix() + colorize(cfg.getString("Messages.exchange_success_custom_other"));
    }

    public static String getExchangeRateSet(){
        return getPrefix() + colorize(cfg.getString("Messages.exchange_rate_set"));
    }

    public static String getExchangeNoPermCustom(){
        return getPrefix() + colorize(cfg.getString("Messages.exchange_command.no_perms.custom"));
    }

    public static String getExchangeNoPermPreset(){
        return getPrefix() + colorize(cfg.getString("Messages.exchange_command.no_perms.preset"));
    }
//-----------------------

}
