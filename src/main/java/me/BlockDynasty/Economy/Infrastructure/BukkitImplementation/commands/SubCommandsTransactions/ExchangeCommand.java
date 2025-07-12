package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsTransactions;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.MessageService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class ExchangeCommand implements CommandExecutor {
    private ExchangeUseCase exchange;
    private MessageService messageService;

    public ExchangeCommand(ExchangeUseCase exchange, MessageService messageService) {
        this.exchange = exchange;
        this.messageService = messageService;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("gemseconomy.command.exchange")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }

       /* if (args.length < 4) {
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
        } catch (NumberFormatException ex) {
            sender.sendMessage(F.getUnvalidAmount());
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[2];

        double finalToExchangeAmount = toExchangeAmount;
        double finalToReceiveAmount = toReceiveAmount;
        */
        if (args.length < 3) {
            F.getExchangeHelp(sender);
            return true;
        }
        String player;
        if (args.length > 3) {
            player = args[3];
        }else {
            player =  sender.getName();
        }

        double toReceiveAmount = 0;

        try {
            toReceiveAmount = Double.parseDouble(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(F.getUnvalidAmount());
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[1];

        double finalToReceiveAmount = toReceiveAmount;
        Player targetPlayer = Bukkit.getPlayer(player);
        SchedulerUtils.runAsync(() -> {
           // Result<Void> result =exchange.execute(player, toExchange, toReceive, BigDecimal.valueOf(finalToExchangeAmount),BigDecimal.valueOf( finalToReceiveAmount));
            Result<BigDecimal> result =exchange.execute(player, toExchange, toReceive, null,BigDecimal.valueOf( finalToReceiveAmount));
            SchedulerUtils.run( () -> {
                if (result.isSuccess()){
                    if( targetPlayer != null){
                       // targetPlayer.sendMessage(messageService.getExchangeSuccess( toExchange,BigDecimal.valueOf( finalToExchangeAmount), toReceive));
                        targetPlayer.sendMessage(messageService.getExchangeSuccess( toExchange,result.getValue(), toReceive));
                    }else{
                        //sender.sendMessage(messageService.getExchangeSuccess( toExchange, BigDecimal.valueOf(finalToExchangeAmount), toReceive));
                        sender.sendMessage(messageService.getExchangeSuccess( toExchange, result.getValue(), toReceive));
                    }
                }else {
                    switch (result.getErrorCode()){
                        case ACCOUNT_CAN_NOT_RECEIVE -> sender.sendMessage(F.getCannotReceive());
                        case ACCOUNT_NOT_FOUND -> sender.sendMessage(messageService.getAccountNotFoundMessage());
                        case CURRENCY_NOT_FOUND ->  sender.sendMessage(F.getUnknownCurrency());
                        case DECIMAL_NOT_SUPPORTED -> sender.sendMessage("Intercambio invalido"+" no se puede extraer "+result.getValue()+" "+toExchange+" intenta con otro monto en "+toReceive);
                        case INVALID_AMOUNT -> sender.sendMessage(F.getUnvalidAmount());
                        case INSUFFICIENT_FUNDS -> {
                            if( targetPlayer != null){
                                targetPlayer.sendMessage(messageService.getInsufficientFundsMessage(toExchange));
                            }else {
                                sender.sendMessage(messageService.getInsufficientFundsMessage(toExchange));
                            }
                            }
                        case DATA_BASE_ERROR -> {
                            if( targetPlayer != null){
                                targetPlayer.sendMessage(messageService.getUnexpectedErrorMessage());
                                //console log informate about the error or another log
                            }else {
                                sender.sendMessage(messageService.getUnexpectedErrorMessage());
                            }
                        }
                        default -> sender.sendMessage(messageService.getUnexpectedErrorMessage());
                    }
            }
        });

        });
        return true;
    }
}
