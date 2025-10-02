/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lib.messages;

import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

public class DefaultProvider implements MessageProvider{
    private final MessageConfig messageConfig= new MessageConfig();

    public DefaultProvider(){
        loadMessages();
    }

    private void loadMessages(){
        messageConfig.addDefault("prefix", "BlockDynasty> ");
        messageConfig.addDefault("nopermission", ChatColor.stringValueOf(Colors.RED)+"You don't have permission to do this.");
        messageConfig.addDefault("noconsole", "Console cannot do this.");
        messageConfig.addDefault("invalidamount", "Not a valid amount.");
        messageConfig.addDefault("invalidpage", "Not a valid page number.");
        messageConfig.addDefault( "pay_yourself", "You can't pay yourself.");
        messageConfig.addDefault( "player_is_null", "The specified player does not exist.");
        messageConfig.addDefault( "unknownCurrency", "Unknown Currency.");
        messageConfig.addDefault( "unknownCommand", "Unknown sub-command.");
        messageConfig.addDefault( "noDefaultCurrency", "No default currency.");
        messageConfig.addDefault( "currencyExists", "Currency already exists.");
        messageConfig.addDefault( "accountMissing", "Your account is missing. Please relog the server.");
        messageConfig.addDefault( "cannotReceiveMoney", "{player} can't receive money.");
        messageConfig.addDefault( "insufficientFunds", "You don't have enough {currencycolor}{currency}!");
        messageConfig.addDefault( "targetInsufficientFunds", "{target} don't have enough {currencycolor}{currency}!");
        messageConfig.addDefault( "paid", "You were paid {currencycolor}{amount} from {player}.");
        messageConfig.addDefault( "payer", "You paid {currencycolor}{amount} to §a{player}§7.");
        messageConfig.addDefault( "payNoPermission", "You don't have permission to pay {currencycolor}{currency}.");

        messageConfig.addDefault("currencyNotPayable", "{currencycolor}{currency} is not payable.");
        messageConfig.addDefault("add", "You gave {player}: {currencycolor}{amount}. ");
        messageConfig.addDefault("take", "You took {currencycolor}{amount} from §a{player}.");
        messageConfig.addDefault("set", "You set {player}'s balance to {currencycolor}{amount}.");

        messageConfig.addDefault("exchange_rate_set", "Set the exchange rate for {currencycolor}{currency} to §a{amount}.");
        messageConfig.addDefault("exchange_success_custom_other", "Successfully exchanged {currencycolor}({currEx}) for {currencycolor2}{receivedCurr} to player {player}.");
        messageConfig.addDefault("exchange_success_custom", "Successfully exchanged {currencycolor}({currEx}) for {currencycolor2}{receivedCurr}.");
        messageConfig.addDefault("exchange_success", "Successfully exchanged {currencycolor}{ex_curr} for equivalent value in {currencycolor2}{re_curr}.");
        messageConfig.addDefault("exchange_command.no_perms.preset", "You don't have permission to exchange currencies with a preset rate.");
        messageConfig.addDefault("exchange_command.no_perms.custom", "You don't have permission to exchange currencies with a custom rate.");

        messageConfig.addDefault("balance.current", "{player}'s balance is: {currencycolor}{balance}");
        messageConfig.addDefault("balance.multiple", "{player}'s balances:");
        messageConfig.addDefault("balance.list", ">> {currencycolor}{format}");
        messageConfig.addDefault("balance.none", "No balances to show for §c{player}.");

        messageConfig.addDefault("balance_top.balance", "-> {number}. {player} - {currencycolor}{balance}");
        messageConfig.addDefault("balance_top.header", "----- {currencycolor} Top Balances for {currencyplural} (Page {page}) -----");
        messageConfig.addDefault("balance_top.empty", "No accounts to display.");
        messageConfig.addDefault("balance_top.next", "{currencycolor}/gbaltop {currencyplural} {page} for more.");
        messageConfig.addDefault("balance_top.nosupport", "{storage} doesn't support /baltop.");

        messageConfig.addDefault("withdraw_success","Se ha retirado {amount} de tu cuenta.");
        messageConfig.addDefault("deposit_success","Se ha depositado {amount} en tu cuenta.");
        messageConfig.addDefault("setbalance_success","Se ha establecido tu balance a {amount}.");

        //--------------------------new--Offer-----------------------------------------------------
        messageConfig.addDefault("too_far","El jugador objetivo está demasiado lejos. Deben estar dentro de {limit} bloques de distancia." );
        messageConfig.addDefault("offer_yourself","No puedes ofrecerte a ti mismo." );
        messageConfig.addDefault("offline","El jugador que te hizo la oferta ya no está en línea.");
        messageConfig.addDefault("not_offers","No tienes ofertas pendientes.");
        messageConfig.addDefault("already_offer","Ya tienes una oferta pendiente para este jugador.");
        messageConfig.addDefault("only_players",">> §cSolo los jugadores pueden hacer esto.");

        messageConfig.addDefault("send_offer", "Has ofrecido{currencycolorOffert} {amountOffert} por{currencycolorValue} {amountValue} a {player}.");
        messageConfig.addDefault("receive_offer", "Has recibido una oferta de{currencycolorOffert} {amountOffert} por{currencycolorValue} {amountValue} del jugador {player}. Usa /offer accept {player}, o /offer accept {player}."); //todo aqui
        messageConfig.addDefault("accept_offer_to", "Has aceptado la oferta de §a{player}.");
        messageConfig.addDefault("accept_offer", "Tu oferta para {player} ha sido aceptada.");
        messageConfig.addDefault("cancel_offer_to","Has cancelado la oferta para {player}."); //cancelado
        messageConfig.addDefault("cancel_offer","{player} cancelo la oferta."); //cancelado
        messageConfig.addDefault("deny_offer_to","Has rechazado la oferta de {player}."); //denegada
        messageConfig.addDefault("deny_offer","Tu oferta para §a{player} ha sido rechazada."); //denagada

        messageConfig.addDefault("offerExpiredTo","La oferta de §a{player} ha expirado."); //expirado
        messageConfig.addDefault("offerExpired","Tu oferta a §a{player} ha expirado."); //expirado


        //-----------------------------------buy command-------------------------------------------------------

        messageConfig.addDefault("buy_success","Compra realizada con exito!");
        messageConfig.addDefault("buy_no_perms","No tienes permiso para ejecutar el comando de compra.");
        messageConfig.addDefault("buy_usage",">> §aUso: /eco buycommand <jugador> <cantidad> <tipo> <comandoAEntregar>");
    }

    @Override
    public String getMessage(String key) {
        return messageConfig.getMessage(key);
    }
}
