package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;

import java.math.BigDecimal;
import java.util.List;

public class MessageService {

    private final ICurrencyService currencyService;

    public MessageService(ICurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public String getPayNoPerms() {
        return F.getPayNoPerms();
    }

    public String getPayYourselfMessage() {
        return F.getPayYourself();
    }

    public String getInsufficientFundsMessage(String currencyName) {
        Currency currency = currencyService.getCurrency(currencyName);
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
        Currency currency = currencyService.getCurrency(currencyName);
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
        Currency currency = currencyService.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return F.getPayerMessage()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", targetName);
    }

    public String getReceivedMessage(String payerName, String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return F.getPaidMessage()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", payerName);
    }

    public String getExchangeSuccess( String toExchange, BigDecimal toExchangeAmount, String toReceive) {
        Currency currencyTo = currencyService.getCurrency(toExchange);
        Currency currencyToRecive = currencyService.getCurrency(toReceive);
        return F.getExchangeSuccess()
                .replace("{currencycolor}", "" + currencyTo.getColor())
                .replace("{ex_curr}", currencyTo.format(toExchangeAmount))
                .replace("{currencycolor2}", "" + currencyToRecive.getColor())
                .replace("{re_curr}", currencyToRecive.getPlural());
    }
    public String getWithdrawMessage(String target, String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return F.getTakeMessage()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", target);
    }

    public String getDepositMessage(String target, String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return F.getAddMessage()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", target);
    }

    public String getOfferSendMessage(String target, String currencyName, BigDecimal amount, String currencyName2, BigDecimal amount2) {
        Currency currency = currencyService.getCurrency(currencyName);
        Currency currency2 = currencyService.getCurrency(currencyName2);
        return F.getOfferSender()
                .replace("{currencycolorOffert}", currency.getColor() + "")
                .replace("{amountOffert}",  currency.format(amount))
                .replace("{currencycolorValue}", currency2.getColor() + "")
                .replace("{amountValue}", currency2.format(amount2))
                .replace("{player}", target);

    }

    public String getOfferReceiveMessage(String target, String currencyName, BigDecimal amount, String currencyName2, BigDecimal amount2) {
        Currency currency = currencyService.getCurrency(currencyName);
        Currency currency2 = currencyService.getCurrency(currencyName2);
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

    public String getBalanceTopMessage(List<Account> accounts,String nameCurrency) {
        StringBuilder aux = new StringBuilder();
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            Balance balance = account.getBalance(nameCurrency);
            Currency currency = balance.getCurrency();
            BigDecimal balanceValue = balance.getBalance();
            //return F.getBalanceTop().replace("{player}", account.getName()).replace("{balance}", balance.getBalance().toString());
            aux.append(F.getBalanceTop()
                    .replace("{number}", String.valueOf(i+1))
                    .replace("{currencycolor}", "" + currency.getColor())
                    .replace("{player}", account.getNickname())
                    .replace("{balance}", currency.format(balanceValue)))
            .append("\n");
        }
        //return F.getBalanceTop().replace("{player}", account.getName()).replace("{balance}", balance.getBalance().toString());
    return aux.toString(); //F.getBalanceTop().replace("{number}", String.valueOf(num)).replace("{currencycolor}", "" + curr.getColor()).replace("{player}", entry.getName()).replace("{balance}", curr.format(balance));
    }

    public String getWithdrawSuccess(String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        return F.getWithdrawSuccess()
                .replace("{amount}", currency.format(amount));
    }

    public String getDepositSuccess(String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        return F.getDepositSuccess()
                .replace("{amount}", currency.format(amount));
    }

    public String getSetSuccess( String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        return F.getSetSuccess()
                .replace("{amount}", currency.format(amount));
    }
}