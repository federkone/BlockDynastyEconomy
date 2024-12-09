package me.BlockDynasty.Economy.aplication.commands.NEW;

import me.BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountCanNotReciveException;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotPayableException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import me.BlockDynasty.Economy.config.file.MessageService;

import java.math.BigDecimal;
import java.util.UUID;

public class PayCommandV2 implements CommandExecutor {
    private final PayUseCase pay;
    MessageService messageService;

    public PayCommandV2(PayUseCase pay,MessageService messageService) {
        this.pay = pay;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(F.getNoConsole());
            return true;
        }

        if (!sender.hasPermission("gemseconomy.command.pay")) {
            sender.sendMessage(messageService.getPayNoPerms()); //no tiene permisos para ejecutar comando pagar
            return true;
        }

        Player player = ((Player) sender).getPlayer();
        if(player==null){
            sender.sendMessage(messageService.getAccountNotFoundMessage());
            return true;
        }
        if (args.length < 3) {
            player.sendMessage(messageService.getPayUsage()); //informa como se usa el comando pagar
            return true;
        }



        UUID playerUuid =player.getUniqueId(); //PLAYER UUID
        String targetName = args[0]; //nombre del jugador


        String currencyName = args[2];  //nombre de la moneda

        if (player.getName().equals(targetName)) {
            player.sendMessage(messageService.getPayYourselfMessage()); //no puede pagarse a si mismo
            return true;
        }
        BigDecimal amount=BigDecimal.ZERO; //monto temporal
        try{
            amount = new BigDecimal(args[1]);  //intentar extraer monto
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                player.sendMessage(messageService.getUnvalidAmount());  // monto invalido en caso de ser menor o igual a 0
                return true;
            }
        }catch (NumberFormatException e){
            player.sendMessage(messageService.getUnvalidAmount());  //monto invalido en caso de ser menor o igual a 0
            return true;
        }

        BigDecimal finalAmount = amount;
        SchedulerUtils.runAsync(() -> {
            try {
                pay.execute(player.getName(), targetName, currencyName, finalAmount);
                player.sendMessage(messageService.getSuccessMessage(player.getName(), targetName, currencyName, finalAmount));

                Player targetPlayer = Bukkit.getPlayer(targetName);
                if (targetPlayer != null) {
                    targetPlayer.sendMessage(messageService.getReceivedMessage(player.getName(), currencyName, finalAmount));
                }
            } catch (InsufficientFundsException e) {
                player.sendMessage(messageService.getInsufficientFundsMessage(currencyName));
            } catch (AccountCanNotReciveException e){
                sender.sendMessage(messageService.getCannotReceiveMessage(targetName));
            } catch (DecimalNotSupportedException e){
                player.sendMessage(messageService.getUnvalidAmount());
            } catch (AccountNotFoundException e){
                player.sendMessage(messageService.getAccountNotFoundMessage());
            } catch (CurrencyNotFoundException e){
                player.sendMessage(F.getUnknownCurrency());
            } catch (CurrencyNotPayableException e){
                player.sendMessage(messageService.getCurrencyNotPayableMessage(currencyName));
            } catch (TransactionException e){
                player.sendMessage("§cError inesperado al realizar transacción");
            } catch (Exception e){
                player.sendMessage(messageService.getUnexpectedErrorMessage());
            }
        });

        return true;
    }
}