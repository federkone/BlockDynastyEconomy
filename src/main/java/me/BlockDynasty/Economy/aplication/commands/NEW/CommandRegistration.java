package me.BlockDynasty.Economy.aplication.commands.NEW;

import me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsCurrency.*;
import me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsOffer.AcceptOfferCommand;
import me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsOffer.CancelOfferCommand;
import me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsOffer.CreateOfferCommand;
import me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsOffer.DenyOfferCommand;
import me.BlockDynasty.Economy.aplication.commands.NEW.SubcomandsEconomy.*;
import me.BlockDynasty.Economy.aplication.useCase.account.GetBalanceUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.*;
import me.BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.*;

import me.BlockDynasty.Economy.config.file.MessageService;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegistration {
    public static void registerCommands(JavaPlugin plugin, PayUseCase payUseCase, ExchangeUseCase exchangeUseCase,
                                        GetBalanceUseCase balanceUseCase, WithdrawUseCase withdrawUseCase, SetBalanceUseCase setBalanceUseCase ,
                                        DepositUseCase depositUseCase, CreateCurrencyUseCase createCurrencyUseCase, MessageService messageService, GetCurrencyUseCase getCurrencyUseCase,
                                        DeleteCurrencyUseCase deleteCurrencyUseCase, EditCurrencyUseCase editCurrencyUseCase, ToggleFeaturesUseCase toggleFeaturesUseCase, CreateOfferUseCase createOfferUseCase,
                                        AcceptOfferUseCase acceptOfferUseCase, CancelOfferUseCase cancelOfferUseCase)
    {

        CreateCurrencyCommand createCurrencyCommand = new CreateCurrencyCommand(createCurrencyUseCase);
        DeleteCurrencyCommand deleteCurrencyCommand = new DeleteCurrencyCommand(deleteCurrencyUseCase);
        EditColorCoommand editColorCoommand = new EditColorCoommand(editCurrencyUseCase);
        EditDecimalsCommand editDecimalsCommand = new EditDecimalsCommand(toggleFeaturesUseCase);
        EditPayableCommand editPayableCommand = new EditPayableCommand(toggleFeaturesUseCase);
        EditRateCommand editRateCommand = new EditRateCommand(editCurrencyUseCase);
        EditStartBalCommand editStartBalCommand = new EditStartBalCommand(editCurrencyUseCase);
        WithdrawCommand withdrawCommand = new WithdrawCommand(withdrawUseCase, messageService);
        DepositCommand depositCommand = new DepositCommand(depositUseCase, messageService);
        SetCommand setCommand = new SetCommand(setBalanceUseCase, messageService);
        ViewCommand ViewCommand = new ViewCommand(getCurrencyUseCase);
        EditSymbolCommand editSymbolCommand = new EditSymbolCommand(editCurrencyUseCase);
        ListCommand listCommand = new ListCommand(getCurrencyUseCase);
        SetDefaultCommand setDefaultCommand = new SetDefaultCommand(editCurrencyUseCase);
        CreateOfferCommand createOfferCommand = new CreateOfferCommand(createOfferUseCase,messageService);
        CancelOfferCommand cancelOfferCommand = new CancelOfferCommand(cancelOfferUseCase,messageService);
        AcceptOfferCommand acceptOfferCommand = new AcceptOfferCommand(acceptOfferUseCase,messageService);
        DenyOfferCommand denyOfferCommand = new DenyOfferCommand(cancelOfferUseCase,messageService);
        BuyCommand buyCommand = new BuyCommand(withdrawUseCase, messageService);

        OfferCommand offerCommand = new OfferCommand();
        EconomyCommand economyCommand = new EconomyCommand();
        CurrencyCommand currencyCommand = new CurrencyCommand();

        economyCommand.registerSubCommand("take", withdrawCommand);
        economyCommand.registerSubCommand("give", depositCommand);
        economyCommand.registerSubCommand("set", setCommand);
        economyCommand.registerSubCommand("buycommand", buyCommand);

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

        offerCommand.registerSubCommand("create", createOfferCommand);
        offerCommand.registerSubCommand("cancel", cancelOfferCommand);
        offerCommand.registerSubCommand("accept", acceptOfferCommand);
        offerCommand.registerSubCommand("deny", denyOfferCommand);

        plugin.getCommand("offer").setExecutor(offerCommand);


        plugin.getCommand("exchange").setExecutor(new ExchangeCommandV2(exchangeUseCase, messageService));
        plugin.getCommand("balance").setExecutor(new BalanceCommand(balanceUseCase, messageService));
        //plugin.getCommand("baltop").setExecutor(new BalanceTopCommand(getAccountsUseCase, messageService));


    }
}