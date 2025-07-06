package me.BlockDynasty.Economy.aplication.commands;

import me.BlockDynasty.Economy.BlockDynastyEconomy;
import me.BlockDynasty.Economy.aplication.commands.SubCommandsCurrency.*;
import me.BlockDynasty.Economy.aplication.commands.SubCommandsCurrency.*;
import me.BlockDynasty.Economy.aplication.commands.SubCommandsOffer.AcceptOfferCommand;
import me.BlockDynasty.Economy.aplication.commands.SubCommandsOffer.CancelOfferCommand;
import me.BlockDynasty.Economy.aplication.commands.SubCommandsOffer.CreateOfferCommand;
import me.BlockDynasty.Economy.aplication.commands.SubCommandsOffer.DenyOfferCommand;
import me.BlockDynasty.Economy.aplication.commands.SubcomandsEconomy.*;
import me.BlockDynasty.Economy.aplication.commands.SubcomandsEconomy.BuyCommand;
import me.BlockDynasty.Economy.aplication.commands.SubcomandsEconomy.DepositCommand;
import me.BlockDynasty.Economy.aplication.commands.SubcomandsEconomy.SetCommand;
import me.BlockDynasty.Economy.aplication.commands.SubcomandsEconomy.WithdrawCommand;
import me.BlockDynasty.Economy.aplication.useCase.UsesCase;

public class CommandRegistration {

    public static void registerCommands(BlockDynastyEconomy plugin)
    {
        UsesCase usesCase =  plugin.getUsesCase();

        CreateCurrencyCommand createCurrencyCommand = new CreateCurrencyCommand(usesCase.getCreateCurrencyUseCase());
        DeleteCurrencyCommand deleteCurrencyCommand = new DeleteCurrencyCommand(usesCase.deleteCurrencyUseCase());
        EditColorCoommand editColorCoommand = new EditColorCoommand(usesCase.getEditCurrencyUseCase());
        EditDecimalsCommand editDecimalsCommand = new EditDecimalsCommand(usesCase.getToggleFeaturesUseCase());
        EditPayableCommand editPayableCommand = new EditPayableCommand(usesCase.getToggleFeaturesUseCase());
        EditRateCommand editRateCommand = new EditRateCommand(usesCase.getEditCurrencyUseCase());
        EditStartBalCommand editStartBalCommand = new EditStartBalCommand(usesCase.getEditCurrencyUseCase());
        WithdrawCommand withdrawCommand = new WithdrawCommand(usesCase.getWithdrawUseCase(), plugin.getMessageService());
        DepositCommand depositCommand = new DepositCommand(usesCase.getDepositUseCase(), plugin.getMessageService());
        SetCommand setCommand = new SetCommand(usesCase.getSetBalanceUseCase(), plugin.getMessageService());
        ViewCommand ViewCommand = new ViewCommand(usesCase.getCurrencyUseCase());
        EditSymbolCommand editSymbolCommand = new EditSymbolCommand(usesCase.getEditCurrencyUseCase());
        ListCommand listCommand = new ListCommand(usesCase.getCurrencyUseCase());
        SetDefaultCommand setDefaultCommand = new SetDefaultCommand(usesCase.getEditCurrencyUseCase());
        CreateOfferCommand createOfferCommand = new CreateOfferCommand(usesCase.getCreateOfferUseCase(),plugin.getMessageService());
        CancelOfferCommand cancelOfferCommand = new CancelOfferCommand(usesCase.getCancelOfferUseCase(),plugin.getMessageService());
        AcceptOfferCommand acceptOfferCommand = new AcceptOfferCommand(usesCase.getAcceptOfferUseCase(),plugin.getMessageService());
        DenyOfferCommand denyOfferCommand = new DenyOfferCommand(usesCase.getCancelOfferUseCase(),plugin.getMessageService());
        BuyCommand buyCommand = new BuyCommand(usesCase.getWithdrawUseCase(), plugin.getMessageService());
        EditPluralNameCommand editPluralNameCommand = new EditPluralNameCommand(usesCase.getEditCurrencyUseCase());
        EditSingularNameCommand editSingularNameCommand = new EditSingularNameCommand(usesCase.getEditCurrencyUseCase());


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

        plugin.getCommand("pay").setExecutor(new PayCommand(usesCase.getPayUseCase(), plugin.getMessageService()));

        offerCommand.registerSubCommand("create", createOfferCommand);
        offerCommand.registerSubCommand("cancel", cancelOfferCommand);
        offerCommand.registerSubCommand("accept", acceptOfferCommand);
        offerCommand.registerSubCommand("deny", denyOfferCommand);

        plugin.getCommand("offer").setExecutor(offerCommand);


        plugin.getCommand("exchange").setExecutor(new ExchangeCommand(usesCase.getExchangeUseCase(), plugin.getMessageService()));
        plugin.getCommand("balance").setExecutor(new BalanceCommand(usesCase.getGetBalanceUseCase(), plugin.getMessageService()));
        plugin.getCommand("baltop").setExecutor(new BalanceTopCommand(usesCase.getAccountsUseCase(), plugin.getMessageService()));


    }
}