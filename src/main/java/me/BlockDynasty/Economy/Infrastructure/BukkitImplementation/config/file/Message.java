package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file;

import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.result.TransferResult;
import me.BlockDynasty.Economy.domain.services.IMessageService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class Message{
    /*private final MessageService messageService;

    public Message(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void sendMessage(Account a, Currency c, BigDecimal b, ErrorCode e) {

    }

    @Override
    public void sendMessageError(TransferResult t, Currency c, BigDecimal b, ErrorCode e) { //mensajes de errores genericos
        Player player = Bukkit.getPlayer(t.getFrom().getUuid());

        switch (e){
            case ACCOUNT_NOT_FOUND:
                player.sendMessage(messageService.getAccountNotFoundMessage());
                break;
            case ACCOUNT_CAN_NOT_RECEIVE:
                player.sendMessage(messageService.getCannotReceiveMessage(t.getTo().getNickname()));
                break;
            case INSUFFICIENT_FUNDS:
                player.sendMessage(messageService.getInsufficientFundsMessage(c.getSingular()));
                break;
            case DECIMAL_NOT_SUPPORTED:
                player.sendMessage(messageService.getUnvalidAmount());
                break;
            case CURRENCY_NOT_FOUND:
                player.sendMessage(F.getUnknownCurrency());
                break;
            case CURRENCY_NOT_PAYABLE:
                player.sendMessage(messageService.getCurrencyNotPayableMessage(c.getSingular()));
                break;
            default:
                player.sendMessage(messageService.getUnexpectedErrorMessage());
                break;
        }
    }

    @Override
    public void sendMessageSuccesful(TransferResult t, Currency c, BigDecimal b, ErrorCode e) {
        Player player = Bukkit.getPlayer(t.getFrom().getUuid());

        switch (e){
            case PAY_SUCCESS: //involucra a 2

                break;
            case TRANSFER_SUCCESS: //involucra a 2

                break;

            case TRADE_SUCCESS: //involucra a 2 personas, 2 monedas y 2 montos

                break;
            default:

                break;
        }
    }

    @Override
    public void sendMessageSuccesful(Account t, Currency c, BigDecimal b, ErrorCode e) {
        Player player = Bukkit.getPlayer(t.getFrom().getUuid());

        switch (e){
            case SET_SUCCESS: //involucra a 1 persona, 1 moneda y 1 monto

                break;
            case DEPOSIT_SUCCESS: //involucra a 1 persona, 1 moneda y 1 monto

                break;
            case WITHDRAW_SUCCESS: //involucra a 1 persona, 1 moneda y 1 monto

                break;
            case EXCHANGE_SUCCESS: //involucra a 1 persona, 2 monedas y 2 montos

                break;
            default:

                break;
        }
    }


    @Override
    public void sendMessage(String a, Result r) {



    }*/
}
