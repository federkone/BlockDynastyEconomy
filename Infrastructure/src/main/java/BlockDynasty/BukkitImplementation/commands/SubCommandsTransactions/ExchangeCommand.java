package BlockDynasty.BukkitImplementation.commands.SubCommandsTransactions;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.BukkitImplementation.config.file.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class ExchangeCommand implements CommandExecutor {
    private ExchangeUseCase exchange;
    private BlockDynasty.BukkitImplementation.services.MessageService messageService;

    public ExchangeCommand(ExchangeUseCase exchange, BlockDynasty.BukkitImplementation.services.MessageService messageService) {
        this.exchange = exchange;
        this.messageService = messageService;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("BlockDynastyEconomy.command.exchange")) {
            sender.sendMessage(Message.getNoPerms());
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
            Message.getExchangeHelp(sender);
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
            sender.sendMessage(Message.getUnvalidAmount());
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[1];

        double finalToReceiveAmount = toReceiveAmount;
        Player targetPlayer = Bukkit.getPlayer(player);

        Scheduler.runAsync(ContextualTask.build(() -> {
            Result<BigDecimal> result = exchange.execute(player, toExchange, toReceive, null, BigDecimal.valueOf(finalToReceiveAmount));

            Runnable resultTask = () -> {
                if (result.isSuccess()) {
                    Runnable successMessage = () -> {
                        String message = messageService.getExchangeSuccess(toExchange, result.getValue(), toReceive);
                        if (targetPlayer != null) {
                            targetPlayer.sendMessage(message);
                        } else {
                            sender.sendMessage(message);
                        }
                    };

                    // Ejecutar el mensaje en el contexto correcto
                    if (targetPlayer != null) {
                        Scheduler.run(ContextualTask.build(successMessage, targetPlayer));
                    } else if (sender instanceof Player) {
                        Scheduler.run(ContextualTask.build(successMessage, (Player) sender));
                    } else {
                        successMessage.run(); // consola o comando externo
                    }

                } else {
                    Runnable errorTask = () -> {
                        switch (result.getErrorCode()) {
                            case ACCOUNT_CAN_NOT_RECEIVE:
                                sender.sendMessage(Message.getCannotReceive());
                                break;
                            case ACCOUNT_NOT_FOUND:
                                sender.sendMessage(messageService.getAccountNotFoundMessage());
                                break;
                            case CURRENCY_NOT_FOUND:
                                sender.sendMessage(Message.getUnknownCurrency());
                                break;
                            case DECIMAL_NOT_SUPPORTED:
                                sender.sendMessage("Intercambio inválido: no se puede extraer " + result.getValue() + " " + toExchange + ", intenta con otro monto en " + toReceive);
                                break;
                            case INVALID_AMOUNT:
                                sender.sendMessage(Message.getUnvalidAmount());
                                break;
                            case INSUFFICIENT_FUNDS:
                                if (targetPlayer != null) {
                                    Scheduler.run(ContextualTask.build(() -> {
                                        targetPlayer.sendMessage(messageService.getInsufficientFundsMessage(toExchange));
                                    }, targetPlayer));
                                } else {
                                    sender.sendMessage(messageService.getInsufficientFundsMessage(toExchange));
                                }
                                break;
                            case DATA_BASE_ERROR:
                                if (targetPlayer != null) {
                                    Scheduler.run(ContextualTask.build(() -> {
                                        targetPlayer.sendMessage(messageService.getUnexpectedErrorMessage());
                                    }, targetPlayer));
                                } else {
                                    sender.sendMessage(messageService.getUnexpectedErrorMessage());
                                }
                                break;
                            default:
                                sender.sendMessage(messageService.getUnexpectedErrorMessage());
                                break;
                        }
                    };

                    if (sender instanceof Player player1) {
                        Scheduler.run(ContextualTask.build(errorTask, player1));
                    } else {
                        errorTask.run();
                    }
                }
            };
            Scheduler.run(ContextualTask.build(resultTask,targetPlayer));
        }));
        return true;
    }
}
