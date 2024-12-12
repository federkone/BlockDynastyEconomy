package me.BlockDynasty.Economy.aplication.commands.NEW;

import me.BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class ExchangeCommandV2 implements CommandExecutor {
    private ExchangeUseCase exchange;
    private MessageService messageService;

    public ExchangeCommandV2(ExchangeUseCase exchange, MessageService messageService) {
        this.exchange = exchange;
        this.messageService = messageService;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("gemseconomy.command.exchange")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }

        if (args.length < 4) {
            F.getExchangeHelp(sender);
            return true;
        }

        String player;
        if (args.length > 4) {
            player = args[4];
        }else {
            player =  sender.getName();
        }

        double toExchangeAmount = 0;
        double toReceiveAmount = 0;
        try {
            toExchangeAmount = Double.parseDouble(args[1]);
            toReceiveAmount = Double.parseDouble(args[3]);
            if (toExchangeAmount <= 0.0 || toReceiveAmount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            sender.sendMessage(F.getUnvalidAmount());
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[2];

        double finalToExchangeAmount = toExchangeAmount;
        double finalToReceiveAmount = toReceiveAmount;

        Player targetPlayer = Bukkit.getPlayer(player); //para informar al jugador que hace el exchange
        SchedulerUtils.runAsync(() -> {
            try
            {
                exchange.execute(player, toExchange, toReceive, BigDecimal.valueOf(finalToExchangeAmount),BigDecimal.valueOf( finalToReceiveAmount));

                if( targetPlayer != null){
                    targetPlayer.sendMessage(messageService.getExchangeSuccess( toExchange,BigDecimal.valueOf( finalToExchangeAmount), toReceive));
                }else{
                    sender.sendMessage(messageService.getExchangeSuccess( toExchange, BigDecimal.valueOf(finalToExchangeAmount), toReceive));
                }

            }
            catch (CurrencyNotFoundException e)
            {
                sender.sendMessage(F.getUnknownCurrency());
            }
            catch (AccountNotFoundException e)
            {
                sender.sendMessage(messageService.getAccountNotFoundMessage());
            }
            catch (DecimalNotSupportedException e)
            {
                sender.sendMessage(F.getUnvalidAmount());
            }
            catch (InsufficientFundsException e)
            {
                if( targetPlayer != null){
                    targetPlayer.sendMessage(messageService.getInsufficientFundsMessage(toExchange));
                }else {
                    sender.sendMessage(messageService.getInsufficientFundsMessage(toExchange));
                }
            }
            catch (TransactionException e)
            {
                if( targetPlayer != null){
                    targetPlayer.sendMessage("§cError en el proceso de exchange");
                    //console log informate about the error or another log
                }else {
                    sender.sendMessage("§cError en el proceso de exchange");
                }

            }
            catch (Exception e)
            {
                sender.sendMessage(messageService.getUnexpectedErrorMessage());
                //console log informate about the error
                e.printStackTrace();
            }
        });
        return true;
    }
}
