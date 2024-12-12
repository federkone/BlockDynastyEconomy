package me.BlockDynasty.Economy.config.file;
import me.BlockDynasty.Economy.domain.currency.CachedTopListEntry;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;

import java.math.BigDecimal;
import java.util.LinkedList;

public class MessageService {

    private final CurrencyCache currencyCache;

    public MessageService(CurrencyCache currencyCache) {
        this.currencyCache = currencyCache;
    }

    public String getPayNoPerms() {
        return F.getPayNoPerms();
    }

    public String getPayYourselfMessage() {
        return F.getPayYourself();
    }

    public String getInsufficientFundsMessage(String currencyName) {
        Currency currency = currencyCache.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found"; // Manejo de errores interno si no se encuentra la moneda
        }
        return F.getInsufficientFunds()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{currency}", currency.getPlural());
    }

    public String getAccountNotFoundMessage() {
        return F.getPlayerDoesNotExist();
    }

    public String getPayUsage() {
        return F.getPayUsage();
    }

    public String getUnvalidAmount() {
        return F.getUnvalidAmount();
    }

    public String getCurrencyNotPayableMessage(String currencyName) {
        Currency currency = currencyCache.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return F.getCurrencyNotPayable()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{currency}", currency.getPlural());
    }

    public String getCannotReceiveMessage(String playerName) {
        return F.getCannotReceive()
                .replace("{player}", playerName);
    }

    public String getUnexpectedErrorMessage() {
        return "Error inesperado al realizar transacción";
    }

    public String getSuccessMessage(String payerName, String targetName, String currencyName, BigDecimal amount) {
        Currency currency = currencyCache.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return F.getPayerMessage()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", targetName);
    }

    public String getReceivedMessage(String payerName, String currencyName, BigDecimal amount) {
        Currency currency = currencyCache.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return F.getPaidMessage()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", payerName);
    }

    public String getExchangeSuccess( String toExchange, BigDecimal toExchangeAmount, String toReceive) {
        Currency currencyTo = currencyCache.getCurrency(toExchange);
        Currency currencyToRecive = currencyCache.getCurrency(toReceive);
        return F.getExchangeSuccess()
                .replace("{currencycolor}", "" + currencyTo.getColor())
                .replace("{ex_curr}", currencyTo.format(toExchangeAmount))
                .replace("{currencycolor2}", "" + currencyToRecive.getColor())
                .replace("{re_curr}", currencyToRecive.getPlural());
    }
    public String getWithdrawMessage(String target, String currencyName, BigDecimal amount) {
        Currency currency = currencyCache.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return F.getTakeMessage()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", target);
    }

    public String getDepositMessage(String target, String currencyName, BigDecimal amount) {
        Currency currency = currencyCache.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return F.getAddMessage()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", target);
    }

    public String getOfferSendMessage(String target, String currencyName, BigDecimal amount, String currencyName2, BigDecimal amount2) {
        Currency currency = currencyCache.getCurrency(currencyName);
        Currency currency2 = currencyCache.getCurrency(currencyName2);
        return F.getOfferSender()
                .replace("{currencycolorOffert}", currency.getColor() + "")
                .replace("{amountOffert}",  currency.format(amount))
                .replace("{currencycolorValue}", currency2.getColor() + "")
                .replace("{amountValue}", currency2.format(amount2))
                .replace("{player}", target);

    }

    public String getOfferReceiveMessage(String target, String currencyName, BigDecimal amount, String currencyName2, BigDecimal amount2) {
        Currency currency = currencyCache.getCurrency(currencyName);
        Currency currency2 = currencyCache.getCurrency(currencyName2);
        return F.getOfferReceiver()
                .replace("{currencycolorOffert}", currency.getColor() + "")
                .replace("{amountOffert}",  currency.format(amount))
                .replace("{currencycolorValue}", currency2.getColor() + "")
                .replace("{amountValue}", currency2.format(amount2))
                .replace("{player}", target);
    }

    public String getOfferCancelMessage(String target) {
        return F.getOfferCancel()
                .replace("{player}", target);
    }

    public String getOfferCancelToMessage(String target) {
        return F.getOfferCancelTo()
                .replace("{player}", target);
    }

    public String getOfferDenyMessage(String target) {
        return F.getOfferDeny()
                .replace("{player}", target);
    }

    public String getOfferDenyToMessage(String target) {
        return F.getOfferDenyTo()
                .replace("{player}", target);
    }

    public String getOfferAcceptMessage(String target) {
        return F.getOfferAccept()
                .replace("{player}", target);
    }

    public String getOfferAcceptToMessage(String target) {
        return F.getOfferAcceptTo()
                .replace("{player}", target);
    }

    public String getNoDefaultCurrency() {
        return F.getNoDefaultCurrency();
    }

    public String getNoCurrencyFund(String name){
        return F.getBalanceNone().replace("{player}", name);
    }

    public String getBalanceTopMessaje(LinkedList<CachedTopListEntry> cache){
    return "";// F.getBalanceTop().replace("{number}", String.valueOf(num)).replace("{currencycolor}", "" + curr.getColor()).replace("{player}", entry.getName()).replace("{balance}", curr.format(balance));
    }
}