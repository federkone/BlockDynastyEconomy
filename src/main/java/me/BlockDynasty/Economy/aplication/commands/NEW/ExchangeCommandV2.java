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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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

        String player = sender.getName();

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
        SchedulerUtils.runAsync(() -> {
            try
            {
                exchange.execute(player, toExchange, toReceive, finalToExchangeAmount, finalToReceiveAmount);
                sender.sendMessage(messageService.getExchangeSuccess( toExchange, finalToExchangeAmount, toReceive));
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
                sender.sendMessage(messageService.getInsufficientFundsMessage(toExchange));
            }
            catch (TransactionException e)
            {
                sender.sendMessage("Error en el proceso de exchange");

            }
            catch (Exception e)
            {
                sender.sendMessage(messageService.getUnexpectedErrorMessage());
            }
        });
        return true;
    }
}
