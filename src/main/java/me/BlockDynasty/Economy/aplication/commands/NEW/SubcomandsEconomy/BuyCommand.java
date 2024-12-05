package me.BlockDynasty.Economy.aplication.commands.NEW.SubcomandsEconomy;

import me.BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAmountNotValidException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuyCommand implements CommandExecutor {
   private final WithdrawUseCase withdraw;
    private final MessageService messageService;

    public BuyCommand(WithdrawUseCase withdraw, MessageService messageService) {
        this.withdraw = withdraw;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("gemseconomy.command.buycommand")) {
            sender.sendMessage(messageService.getPayNoPerms()); //no tiene permisos para ejecutar comando pagar
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("/eco buycommand <jugador> <cantidad> <tipo> <comandoAEntregar>");
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if(player==null) {
            sender.sendMessage("§cEl jugador no está en línea.");
            return false;
        }
        double cantidadDemoneda;
        try {
            cantidadDemoneda = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("invalid_number");
            return false;
        }

        String tipoDemoneda = args[2];

        //constructor de comando a entregar a partir del argumento 3
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            cmdBuilder.append(args[i]).append(" ");
        }
        String cmd = cmdBuilder.toString().trim();


            try{
                withdraw.execute(player.getName(),tipoDemoneda,cantidadDemoneda);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                player.sendMessage("&aTu compra ha sido realizada con exito");
            }catch (AccountNotFoundException e) {
                sender.sendMessage(messageService.getAccountNotFoundMessage());
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            } catch (CurrencyAmountNotValidException | DecimalNotSupportedException e) {
                sender.sendMessage(messageService.getUnvalidAmount());
            }catch (InsufficientFundsException e){
                player.sendMessage(messageService.getInsufficientFundsMessage(tipoDemoneda));
            } catch (TransactionException e) {
                sender.sendMessage("Error inesperado al realizar transacción");
            } catch (Exception e) {
                sender.sendMessage(messageService.getUnexpectedErrorMessage()+e.getMessage());
            }

        return false;
    }
}
