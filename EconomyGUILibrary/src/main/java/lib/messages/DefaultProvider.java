package lib.messages;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultProvider implements MessageProvider{
    private final MessageConfig messageConfig= new MessageConfig();

    public DefaultProvider(){
        loadMessages();
    }

    private void loadMessages(){
        messageConfig.addDefault("prefix", "§2§lBlockDynasty> ");
        messageConfig.addDefault("nopermission", "§7You don't have permission to do this.");
        messageConfig.addDefault("noconsole", "§7Console cannot do this.");
        messageConfig.addDefault("invalidamount", "§7Not a valid amount.");
        messageConfig.addDefault("invalidpage", "§7Not a valid page number.");
        messageConfig.addDefault( "pay_yourself", "§7You can't pay yourself.");
        messageConfig.addDefault( "player_is_null", "§7The specified player does not exist.");
        messageConfig.addDefault( "unknownCurrency", "§7Unknown Currency.");
        messageConfig.addDefault( "unknownCommand", "§7Unknown sub-command.");
        messageConfig.addDefault( "noDefaultCurrency", "§7No default currency.");
        messageConfig.addDefault( "currencyExists", "§7Currency already exists.");
        messageConfig.addDefault( "accountMissing", "§7Your account is missing. Please relog the server.");
        messageConfig.addDefault( "cannotReceiveMoney", "§a{player}§7 can't receive money.");
        messageConfig.addDefault( "insufficientFunds", "§7You don't have enough {currencycolor}{currency}§7!");
        messageConfig.addDefault( "targetInsufficientFunds", "§e{target} §7don't have enough {currencycolor}{currency}§7!");
        messageConfig.addDefault( "paid", "§7You were paid {currencycolor}{amount} §7from §a{player}§7.");
        messageConfig.addDefault( "payer", "§7You paid {currencycolor}{amount} §7to §a{player}§7.");
        messageConfig.addDefault( "payNoPermission", "§7You don't have permission to pay {currencycolor}{currency}§7.");

        messageConfig.addDefault("currencyNotPayable", "{currencycolor}{currency} §7is not payable.");
        messageConfig.addDefault("add", "§7You gave §a{player}§7: {currencycolor}{amount}. ");
        messageConfig.addDefault("take", "§7You took {currencycolor}{amount} §7from §a{player}§7.");
        messageConfig.addDefault("set", "§7You set §a{player}§7's balance to {currencycolor}{amount}§7.");

        messageConfig.addDefault("exchange_rate_set", "§7Set the exchange rate for {currencycolor}{currency} §7to §a{amount}§7.");
        messageConfig.addDefault("exchange_success_custom_other", "§7Successfully exchanged {currencycolor}({currEx}) §7for {currencycolor2}{receivedCurr}§7 to player {player}§7.");
        messageConfig.addDefault("exchange_success_custom", "§7Successfully exchanged {currencycolor}({currEx}) §7for {currencycolor2}{receivedCurr}§7.");
        messageConfig.addDefault("exchange_success", "§7Successfully exchanged {currencycolor}{ex_curr} §7for equivalent value in {currencycolor2}{re_curr}§7.");
        messageConfig.addDefault("exchange_command.no_perms.preset", "§7You don't have permission to exchange currencies with a preset rate.");
        messageConfig.addDefault("exchange_command.no_perms.custom", "§7You don't have permission to exchange currencies with a custom rate.");

        messageConfig.addDefault("balance.current", "§a{player}§7's balance is: {currencycolor}{balance}");
        messageConfig.addDefault("balance.multiple", "§a{player}§7's balances:");
        messageConfig.addDefault("balance.list", "§a§l>> {currencycolor}{format}");
        messageConfig.addDefault("balance.none", "§7No balances to show for §c{player}§7.");

        messageConfig.addDefault("balance_top.balance", "§a§l-> {number}. §b{player} §7- {currencycolor}{balance}");
        messageConfig.addDefault("balance_top.header", "§f----- {currencycolor} Top Balances for {currencyplural} §7(Page {page})§f -----");
        messageConfig.addDefault("balance_top.empty", "§7No accounts to display.");
        messageConfig.addDefault("balance_top.next", "{currencycolor}/gbaltop {currencyplural} {page} §7for more.");
        messageConfig.addDefault("balance_top.nosupport", "§a{storage} §7doesn't support /baltop.");

        messageConfig.addDefault("withdraw_success","§7Se ha retirado {amount} §7de tu cuenta.");
        messageConfig.addDefault("deposit_success","§7Se ha depositado {amount} §7en tu cuenta.");
        messageConfig.addDefault("setbalance_success","§7Se ha establecido tu balance a {amount}.");

        //--------------------------new--Offer-----------------------------------------------------
        messageConfig.addDefault("too_far","§cEl jugador objetivo está demasiado lejos. Deben estar dentro de {limit} bloques de distancia." );
        messageConfig.addDefault("offer_yourself","§cNo puedes ofrecerte a ti mismo." );
        messageConfig.addDefault("offline","§cEl jugador que te hizo la oferta ya no está en línea.");
        messageConfig.addDefault("not_offers","§cNo tienes ofertas pendientes.");
        messageConfig.addDefault("already_offer","§cYa tienes una oferta pendiente para este jugador.");
        messageConfig.addDefault("only_players","§2§l>> §cSolo los jugadores pueden hacer esto.");

        messageConfig.addDefault("send_offer", "§7Has ofrecido{currencycolorOffert} {amountOffert} §7por{currencycolorValue} {amountValue} §7a §a{player}§7.");
        messageConfig.addDefault("receive_offer", "§7Has recibido una oferta de{currencycolorOffert} {amountOffert} §7por{currencycolorValue} {amountValue} §7del jugador §a{player}§7. Usa /offer accept {player}, o /offer accept {player}."); //todo aqui
        messageConfig.addDefault("accept_offer_to", "§7Has aceptado la oferta de §a{player}§7.");
        messageConfig.addDefault("accept_offer", "§7Tu oferta para §a{player}§7 ha sido aceptada.");
        messageConfig.addDefault("cancel_offer_to","§7Has cancelado la oferta para §a{player}§7."); //cancelado
        messageConfig.addDefault("cancel_offer","§a{player}§7 cancelo la oferta."); //cancelado
        messageConfig.addDefault("deny_offer_to","§7Has rechazado la oferta de §a{player}§7."); //denegada
        messageConfig.addDefault("deny_offer","§7Tu oferta para §a{player}§7 ha sido rechazada."); //denagada

        messageConfig.addDefault("offerExpiredTo","§7La oferta de §a{player}§7 ha expirado."); //expirado
        messageConfig.addDefault("offerExpired","§7Tu oferta a §a{player}§7 ha expirado."); //expirado


        //-----------------------------------buy command-------------------------------------------------------

        messageConfig.addDefault("buy_success","§7Compra realizada con exito!");
        messageConfig.addDefault("buy_no_perms","§7No tienes permiso para ejecutar el comando de compra.");
        messageConfig.addDefault("buy_usage","§2§l>> §aUso: /eco buycommand <jugador> <cantidad> <tipo> <comandoAEntregar>");
    }

    @Override
    public String getMessage(String key) {
        return messageConfig.getMessage(key);
    }
}
