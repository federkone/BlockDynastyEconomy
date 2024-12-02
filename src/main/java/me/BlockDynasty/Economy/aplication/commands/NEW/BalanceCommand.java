package me.BlockDynasty.Economy.aplication.commands.NEW;

import me.BlockDynasty.Economy.aplication.useCase.account.GetBalanceUseCase;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.config.file.F;

import me.BlockDynasty.Economy.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import me.BlockDynasty.Economy.config.file.MessageService;

import java.util.Map;

public class BalanceCommand implements CommandExecutor {
    private final GetBalanceUseCase balance;
    private final MessageService messageService;

    public BalanceCommand(GetBalanceUseCase balance, MessageService messageService) {
        this.balance = balance;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("gemseconomy.command.economy")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }
        if (!sender.hasPermission("gemseconomy.command.balance")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }

        //if (args.length == 0) {
        //    F.getManageHelp(sender); //todo el mensaje de eco help
        //    return true;
        //}

        final String target;
        if (args.length > 0) {
            target = args[0];
        }else {
            target = sender.getName();
        }

        SchedulerUtils.runAsync(() -> {
            try {
                Map<Currency, Double> balances = balance.getBalances(target);
                sender.sendMessage(F.getBalanceMultiple().replace("{player}", target));
                for (Map.Entry<Currency, Double> entry : balances.entrySet()) {
                    Currency currency = entry.getKey();
                    double balance = entry.getValue();
                    sender.sendMessage(F.getBalanceList().replace("{currencycolor}", currency.getColor() + "").replace("{format}", currency.format(balance)));
                }
            } catch (AccountNotFoundException e) {
                sender.sendMessage(messageService.getAccountNotFoundMessage());
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(messageService.getNoCurrencyFund(target));
            }
        });
        return true;
    }
}
