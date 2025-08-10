package BlockDynasty.BukkitImplementation.services;
import BlockDynasty.BukkitImplementation.config.file.Message;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.List;

public class MessageService {
    private final ICurrencyService currencyService;

    public MessageService(ICurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public void sendErrorMessage(ErrorCode e, CommandSender sender, String currencyName){
        switch (e) {
            case ACCOUNT_NOT_FOUND:
                sender.sendMessage(getAccountNotFoundMessage());
                break;
            case ACCOUNT_BLOCKED:
                sender.sendMessage(Message.getPrefix() + "Account is blocked");
                break;
            case INSUFFICIENT_FUNDS:
                sender.sendMessage(getInsufficientFundsMessage(currencyName));
                break;
            case ACCOUNT_NOT_HAVE_BALANCE:
                sender.sendMessage(Message.getInsufficientFunds());
                break;
            case CURRENCY_NOT_FOUND:
                sender.sendMessage(Message.getUnknownCurrency());
                break;
            case INVALID_AMOUNT:
            case DECIMAL_NOT_SUPPORTED:
                sender.sendMessage(getUnvalidAmount());
                break;
            case DATA_BASE_ERROR:
                sender.sendMessage(getUnexpectedErrorMessage());
                break;
            case OFFER_ALREADY_EXISTS:
                sender.sendMessage(Message.getAlreadyOffer());
                break;
            case ACCOUNT_CAN_NOT_RECEIVE:
                sender.sendMessage(getCannotReceiveMessage(sender.getName()));
                break;
            case OFFER_NOT_FOUND:
                sender.sendMessage(Message.getNotOffers());
                break;
            case INVALID_ARGUMENT:
                sender.sendMessage("invalid argument");
            break;
            case REPOSITORY_NOT_SUPPORT_TOP:
                sender.sendMessage("No support top");
            case CURRENCY_NOT_PAYABLE:
                sender.sendMessage(getCurrencyNotPayableMessage(currencyName));
                break;
            default:
                sender.sendMessage(getUnexpectedErrorMessage());
                break;
        }
    }

    public String getPayNoPerms() {
        return Message.getPayNoPerms();
    }

    public String getPayYourselfMessage() {
        return Message.getPayYourself();
    }

    public String getInsufficientFundsMessage(String currencyName) {
        Currency currency = currencyService.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found"; // Manejo de errores interno si no se encuentra la moneda
        }
        return Message.getInsufficientFunds()
                .replace("{currencycolor}", ChatColor.valueOf(currency.getColor()) + "")
                .replace("{currency}", currency.getPlural());
    }

    public String getAccountNotFoundMessage() {
        return Message.getPlayerDoesNotExist();
    }

    public String getPayUsage() {
        return Message.getPayUsage();
    }

    public String getUnvalidAmount() {
        return Message.getUnvalidAmount();
    }

    public String getCurrencyNotPayableMessage(String currencyName) {
        Currency currency = currencyService.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return Message.getCurrencyNotPayable()
                .replace("{currencycolor}", ChatColor.valueOf(currency.getColor()) + "")
                .replace("{currency}", currency.getPlural());
    }

    public String getCannotReceiveMessage(String playerName) {
        return Message.getCannotReceive()
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
        return Message.getPayerMessage()
                .replace("{currencycolor}", ChatColor.valueOf(currency.getColor()) + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", targetName);
    }

    public String getReceivedMessage(String payerName, String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return Message.getPaidMessage()
                .replace("{currencycolor}", ChatColor.valueOf(currency.getColor()) + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", payerName);
    }

    public String getExchangeSuccess( String toExchange, BigDecimal toExchangeAmount, String toReceive) {
        Currency currencyTo = currencyService.getCurrency(toExchange);
        Currency currencyToRecive = currencyService.getCurrency(toReceive);
        return Message.getExchangeSuccess()
                .replace("{currencycolor}", "" + ChatColor.valueOf(currencyTo.getColor()))
                .replace("{ex_curr}", currencyTo.format(toExchangeAmount))
                .replace("{currencycolor2}", "" + ChatColor.valueOf(currencyToRecive.getColor()))
                .replace("{re_curr}", currencyToRecive.getPlural());
    }
    public String getWithdrawMessage(String target, String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return Message.getTakeMessage()
                .replace("{currencycolor}", ChatColor.valueOf(currency.getColor())+ "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", target);
    }

    public String getDepositMessage(String target, String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }
        return Message.getAddMessage()
                .replace("{currencycolor}", ChatColor.valueOf(currency.getColor()) + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", target);
    }

    public String getOfferSendMessage(String target, String currencyName, BigDecimal amount, String currencyName2, BigDecimal amount2) {
        Currency currency = currencyService.getCurrency(currencyName);
        Currency currency2 = currencyService.getCurrency(currencyName2);
        return Message.getOfferSender()
                .replace("{currencycolorOffert}", ChatColor.valueOf(currency.getColor()) + "")
                .replace("{amountOffert}",  currency.format(amount))
                .replace("{currencycolorValue}", ChatColor.valueOf(currency2.getColor()) + "")
                .replace("{amountValue}", currency2.format(amount2))
                .replace("{player}", target);

    }

    public String getOfferReceiveMessage(String target, String currencyName, BigDecimal amount, String currencyName2, BigDecimal amount2) {
        Currency currency = currencyService.getCurrency(currencyName);
        Currency currency2 = currencyService.getCurrency(currencyName2);
        return Message.getOfferReceiver()
                .replace("{currencycolorOffert}", ChatColor.valueOf(currency.getColor()) + "")
                .replace("{amountOffert}",  currency.format(amount))
                .replace("{currencycolorValue}", ChatColor.valueOf(currency2.getColor()) + "")
                .replace("{amountValue}", currency2.format(amount2))
                .replace("{player}", target);
    }

    public String getOfferCancelMessage(String target) {
        return Message.getOfferCancel()
                .replace("{player}", target);
    }

    public String getOfferCancelToMessage(String target) {
        return Message.getOfferCancelTo()
                .replace("{player}", target);
    }

    public String getOfferDenyMessage(String target) {
        return Message.getOfferDeny()
                .replace("{player}", target);
    }

    public String getOfferDenyToMessage(String target) {
        return Message.getOfferDenyTo()
                .replace("{player}", target);
    }

    public String getOfferAcceptMessage(String target) {
        return Message.getOfferAccept()
                .replace("{player}", target);
    }

    public String getOfferAcceptToMessage(String target) {
        return Message.getOfferAcceptTo()
                .replace("{player}", target);
    }

    public String getNoDefaultCurrency() {
        return Message.getNoDefaultCurrency();
    }

    public String getNoCurrencyFund(String name){
        return Message.getBalanceNone().replace("{player}", name);
    }

    public String getBalanceTopMessage(List<Account> accounts,String nameCurrency,int finalLimit) {
        StringBuilder aux = new StringBuilder();
        aux.append("Top ").append(finalLimit).append(" ").append(nameCurrency).append(" : ").append("\n");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            Money money = account.getMoney(nameCurrency);
            Currency currency = money.getCurrency();
            BigDecimal balanceValue = money.getAmount();
            //return F.getBalanceTop().replace("{player}", account.getName()).replace("{balance}", balance.getBalance().toString());
            aux.append(Message.getBalanceTop()
                    .replace("{number}", String.valueOf(i+1))
                    .replace("{currencycolor}", "" + ChatColor.valueOf(currency.getColor()))
                    .replace("{player}", account.getNickname())
                    .replace("{balance}", currency.format(balanceValue)))
            .append("\n");
        }
        //return F.getBalanceTop().replace("{player}", account.getName()).replace("{balance}", balance.getBalance().toString());
    return aux.toString(); //F.getBalanceTop().replace("{number}", String.valueOf(num)).replace("{currencycolor}", "" + curr.getColor()).replace("{player}", entry.getName()).replace("{balance}", curr.format(balance));
    }

    public String getWithdrawSuccess(String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        return Message.getWithdrawSuccess()
                .replace("{amount}", currency.format(amount));
    }

    public String getDepositSuccess(String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        return Message.getDepositSuccess()
                .replace("{amount}", currency.format(amount));
    }

    public String getSetSuccess( String currencyName, BigDecimal amount) {
        Currency currency = currencyService.getCurrency(currencyName);
        return Message.getSetSuccess()
                .replace("{amount}", currency.format(amount));
    }
}