package me.BlockDynasty.Economy.aplication.commands.NEW;

import me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsCurrency.*;
import me.BlockDynasty.Economy.aplication.useCase.account.GetBalanceUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.*;
import me.BlockDynasty.Economy.aplication.commands.NEW.SubcomandsEconomy.DepositCommand;
import me.BlockDynasty.Economy.aplication.commands.NEW.SubcomandsEconomy.SetCommand;
import me.BlockDynasty.Economy.aplication.commands.NEW.SubcomandsEconomy.WithdrawCommand;

import me.BlockDynasty.Economy.config.file.MessageService;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegistration {
    public static void registerCommands(JavaPlugin plugin, PayUseCase payUseCase, ExchangeUseCase exchangeUseCase, GetBalanceUseCase balanceUseCase, WithdrawUseCase withdrawUseCase, SetBalanceUseCase setBalanceUseCase , DepositUseCase depositUseCase, CreateCurrencyUseCase createCurrencyUseCase, MessageService messageService) {
        CreateCurrencyCommand createCurrencyCommand = new CreateCurrencyCommand(createCurrencyUseCase);
        DeleteCurrencyCommand deleteCurrencyCommand = new DeleteCurrencyCommand(createCurrencyUseCase);
        EditColorCoommand editColorCoommand = new EditColorCoommand(createCurrencyUseCase);
        EditDecimalsCommand editDecimalsCommand = new EditDecimalsCommand(createCurrencyUseCase);
        EditPayableCommand editPayableCommand = new EditPayableCommand(createCurrencyUseCase);
        EditRateCommand editRateCommand = new EditRateCommand(createCurrencyUseCase);
        EditStartBalCommand editStartBalCommand = new EditStartBalCommand(createCurrencyUseCase);
        WithdrawCommand withdrawCommand = new WithdrawCommand(withdrawUseCase, messageService);
        DepositCommand depositCommand = new DepositCommand(depositUseCase, messageService);
        SetCommand setCommand = new SetCommand(setBalanceUseCase, messageService);
        ViewCommand ViewCommand = new ViewCommand(createCurrencyUseCase);
        EditSymbolCommand editSymbolCommand = new EditSymbolCommand(createCurrencyUseCase);
        ListCommand listCommand = new ListCommand(createCurrencyUseCase);
        SetDefaultCommand setDefaultCommand = new SetDefaultCommand(createCurrencyUseCase);

        EconomyCommand economyCommand = new EconomyCommand();
        CurrencyCommand currencyCommand = new CurrencyCommand();

        economyCommand.registerSubCommand("take", withdrawCommand);
        economyCommand.registerSubCommand("give", depositCommand);
        economyCommand.registerSubCommand("set", setCommand);

        currencyCommand.registerSubCommand("create", createCurrencyCommand);
        currencyCommand.registerSubCommand("delete", deleteCurrencyCommand);
        currencyCommand.registerSubCommand("view", ViewCommand);
        currencyCommand.registerSubCommand("color", editColorCoommand);
        currencyCommand.registerSubCommand("decimals", editDecimalsCommand);
        currencyCommand.registerSubCommand("payable", editPayableCommand);
        currencyCommand.registerSubCommand("rate", editRateCommand);
        currencyCommand.registerSubCommand("startbal", editStartBalCommand);
        currencyCommand.registerSubCommand("symbol",editSymbolCommand);
        currencyCommand.registerSubCommand("list", listCommand);
        currencyCommand.registerSubCommand("default",setDefaultCommand);

        economyCommand.registerSubCommand("currency", currencyCommand);  //currency subcomand de eco

        plugin.getCommand("economy").setExecutor(economyCommand);

        plugin.getCommand("pay").setExecutor(new PayCommandV2(payUseCase, messageService));


        plugin.getCommand("exchange").setExecutor(new ExchangeCommandV2(exchangeUseCase, messageService));
        plugin.getCommand("balance").setExecutor(new BalanceCommand(balanceUseCase, messageService));
        //plugin.getCommand("baltop").setExecutor(new BalanceTopCommand(getAccountsUseCase, messageService));


    }
}