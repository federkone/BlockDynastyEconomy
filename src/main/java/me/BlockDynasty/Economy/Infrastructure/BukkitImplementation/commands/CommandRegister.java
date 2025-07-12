package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsCurrency.*;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsOffer.*;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsTransactions.BalanceCommand;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsTransactions.ExchangeCommand;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsTransactions.PayCommand;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubcomandsEconomy.*;

import me.BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;

public class CommandRegister {

    public static void registerCommands(BlockDynastyEconomy plugin)
    {
        UsesCaseFactory usesCaseFactory =  plugin.getUsesCase();

        CreateCurrencyCommand createCurrencyCommand = new CreateCurrencyCommand(usesCaseFactory.getCreateCurrencyUseCase());
        DeleteCurrencyCommand deleteCurrencyCommand = new DeleteCurrencyCommand(usesCaseFactory.deleteCurrencyUseCase());
        EditColorCoommand editColorCoommand = new EditColorCoommand(usesCaseFactory.getEditCurrencyUseCase());
        EditDecimalsCommand editDecimalsCommand = new EditDecimalsCommand(usesCaseFactory.getToggleFeaturesUseCase());
        EditPayableCommand editPayableCommand = new EditPayableCommand(usesCaseFactory.getToggleFeaturesUseCase());
        EditRateCommand editRateCommand = new EditRateCommand(usesCaseFactory.getEditCurrencyUseCase());
        EditStartBalCommand editStartBalCommand = new EditStartBalCommand(usesCaseFactory.getEditCurrencyUseCase());
        WithdrawCommand withdrawCommand = new WithdrawCommand(usesCaseFactory.getWithdrawUseCase(), plugin.getMessageService());
        DepositCommand depositCommand = new DepositCommand(usesCaseFactory.getDepositUseCase(), plugin.getMessageService());
        SetCommand setCommand = new SetCommand(usesCaseFactory.getSetBalanceUseCase(), plugin.getMessageService());
        ViewCommand ViewCommand = new ViewCommand(usesCaseFactory.getCurrencyUseCase());
        EditSymbolCommand editSymbolCommand = new EditSymbolCommand(usesCaseFactory.getEditCurrencyUseCase());
        ListCommand listCommand = new ListCommand(usesCaseFactory.getCurrencyUseCase());
        SetDefaultCommand setDefaultCommand = new SetDefaultCommand(usesCaseFactory.getEditCurrencyUseCase());
        CreateOfferCommand createOfferCommand = new CreateOfferCommand(usesCaseFactory.getCreateOfferUseCase(),plugin.getMessageService());
        CancelOfferCommand cancelOfferCommand = new CancelOfferCommand(usesCaseFactory.getCancelOfferUseCase(),plugin.getMessageService());
        AcceptOfferCommand acceptOfferCommand = new AcceptOfferCommand(usesCaseFactory.getAcceptOfferUseCase(),plugin.getMessageService());
        DenyOfferCommand denyOfferCommand = new DenyOfferCommand(usesCaseFactory.getCancelOfferUseCase(),plugin.getMessageService());
        BuyCommand buyCommand = new BuyCommand(usesCaseFactory.getWithdrawUseCase(), plugin.getMessageService());
        EditPluralNameCommand editPluralNameCommand = new EditPluralNameCommand(usesCaseFactory.getEditCurrencyUseCase());
        EditSingularNameCommand editSingularNameCommand = new EditSingularNameCommand(usesCaseFactory.getEditCurrencyUseCase());


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
        currencyCommand.registerSubCommand("plural",editPluralNameCommand);
        currencyCommand.registerSubCommand("singular",editSingularNameCommand);



        economyCommand.registerSubCommand("currency", currencyCommand);  //currency subcomand de eco

        plugin.getCommand("economy").setExecutor(economyCommand);

        plugin.getCommand("pay").setExecutor(new PayCommand(usesCaseFactory.getPayUseCase(), plugin.getMessageService()));

        offerCommand.registerSubCommand("create", createOfferCommand);
        offerCommand.registerSubCommand("cancel", cancelOfferCommand);
        offerCommand.registerSubCommand("accept", acceptOfferCommand);
        offerCommand.registerSubCommand("deny", denyOfferCommand);

        plugin.getCommand("offer").setExecutor(offerCommand);


        plugin.getCommand("exchange").setExecutor(new ExchangeCommand(usesCaseFactory.getExchangeUseCase(), plugin.getMessageService()));
        plugin.getCommand("balance").setExecutor(new BalanceCommand(usesCaseFactory.getGetBalanceUseCase(), plugin.getMessageService()));
        plugin.getCommand("baltop").setExecutor(new BalanceTopCommand(usesCaseFactory.getAccountsUseCase(), plugin.getMessageService()));


    }
}