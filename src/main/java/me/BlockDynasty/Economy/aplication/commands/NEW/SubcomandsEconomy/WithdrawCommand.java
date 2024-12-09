package me.BlockDynasty.Economy.aplication.commands.NEW.SubcomandsEconomy;

import me.BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
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

public class WithdrawCommand implements CommandExecutor {
    private final WithdrawUseCase withdraw;
    private final MessageService messageService;

    public WithdrawCommand(WithdrawUseCase withdraw, MessageService messageService) {
        this.withdraw = withdraw;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("gemseconomy.command.take")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }


        if (args.length == 0) {
            F.getManageHelp(sender); //todo el mensaje de eco help
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(F.getTakeUsage());
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
                withdraw.execute(target, currencyName, BigDecimal.valueOf(finalMount));
                sender.sendMessage(messageService.getWithdrawMessage(target, currencyName, BigDecimal.valueOf(finalMount)));
                Player targetPlayer = Bukkit.getPlayer(target);
                if (targetPlayer != null) {
                    targetPlayer.sendMessage("§a Se ha descontado " + finalMount + " " + currencyName);
                }
            } catch (AccountNotFoundException e) {
                sender.sendMessage(messageService.getAccountNotFoundMessage());
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            } catch (CurrencyAmountNotValidException | DecimalNotSupportedException e) {
                sender.sendMessage(messageService.getUnvalidAmount());
            }catch (InsufficientFundsException e){
                sender.sendMessage(messageService.getInsufficientFundsMessage(currencyName));
            } catch (TransactionException e) {
                sender.sendMessage("Error inesperado al realizar transacción");
            } catch (Exception e) {
                sender.sendMessage(messageService.getUnexpectedErrorMessage());
            }
        });
        return false;
    }
}
