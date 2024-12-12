package me.BlockDynasty.Economy.aplication.commands.NEW.SubcomandsEconomy;

import me.BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAmountNotValidException;
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


public class DepositCommand implements CommandExecutor {
    private DepositUseCase deposit;
    private MessageService messageService;

    public DepositCommand(DepositUseCase deposit, MessageService messageService) {
        this.deposit = deposit;
        this.messageService = messageService;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("gemseconomy.command.give")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }

        if (args.length == 0) {
            F.getManageHelp(sender);  //todo el mensaje de eco help
            return true;
        }


        if (args.length < 3) {
            sender.sendMessage(F.getGiveUsage());
            return true;
        }

        String target = args[0];
        String montoString= args[1];
        String currencyName = args[2];

        double amount=0;
        try {
            amount = Double.parseDouble(montoString);
        } catch (NumberFormatException e) {
            sender.sendMessage(F.getUnvalidAmount());
            return true;
        }

        double finalMount = amount;
        SchedulerUtils.runAsync(() -> {
            try {
                deposit.execute(target, currencyName, BigDecimal.valueOf(finalMount));
                sender.sendMessage(messageService.getDepositMessage(target, currencyName, BigDecimal.valueOf(finalMount)));
                Player targetPlayer = Bukkit.getPlayer(target);
                if (targetPlayer != null) {
                    targetPlayer.sendMessage("§aHas recibido " + finalMount + " " + currencyName);
                }
            } catch (AccountNotFoundException e) {
                sender.sendMessage(messageService.getAccountNotFoundMessage());
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            } catch (CurrencyAmountNotValidException | DecimalNotSupportedException e) {
                sender.sendMessage(messageService.getUnvalidAmount());
            } catch (TransactionException e) {
                sender.sendMessage("Error inesperado al realizar transacción");
            } catch (Exception e) {
                sender.sendMessage(messageService.getUnexpectedErrorMessage());
                e.printStackTrace();
            }
        });
        return false;
    }
}
