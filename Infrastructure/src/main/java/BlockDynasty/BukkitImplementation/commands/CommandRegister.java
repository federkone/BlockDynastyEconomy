package BlockDynasty.BukkitImplementation.commands;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency.*;
import BlockDynasty.BukkitImplementation.commands.SubCommandsOffer.*;
import BlockDynasty.BukkitImplementation.commands.SubCommandsTransactions.BalanceCommand;
import BlockDynasty.BukkitImplementation.commands.SubCommandsTransactions.ExchangeCommand;
import BlockDynasty.BukkitImplementation.commands.SubCommandsTransactions.PayCommand;
import BlockDynasty.BukkitImplementation.commands.SubcomandsEconomy.*;

import BlockDynasty.Economy.aplication.useCase.*;

public class CommandRegister {

    public static void registerCommands(BlockDynastyEconomy plugin, AccountsUseCase accountsUseCase, CurrencyUseCase currencyUseCase, TransactionsUseCase transactionsUseCase, OfferUseCase offerUseCase)
    {
        CreateCurrencyCommand createCurrencyCommand = new CreateCurrencyCommand(currencyUseCase.getCreateCurrencyUseCase());
        DeleteCurrencyCommand deleteCurrencyCommand = new DeleteCurrencyCommand(currencyUseCase.getDeleteCurrencyUseCase());
        EditColorCoommand editColorCoommand = new EditColorCoommand(currencyUseCase.getEditCurrencyUseCase());
        EditDecimalsCommand editDecimalsCommand = new EditDecimalsCommand(currencyUseCase.getEditCurrencyUseCase());
        EditPayableCommand editPayableCommand = new EditPayableCommand(currencyUseCase.getEditCurrencyUseCase());
        EditRateCommand editRateCommand = new EditRateCommand(currencyUseCase.getEditCurrencyUseCase());
        EditStartBalCommand editStartBalCommand = new EditStartBalCommand(currencyUseCase.getEditCurrencyUseCase());
        ViewCommand ViewCommand = new ViewCommand(currencyUseCase.getGetCurrencyUseCase());
        EditSymbolCommand editSymbolCommand = new EditSymbolCommand(currencyUseCase.getEditCurrencyUseCase());
        ListCommand listCommand = new ListCommand(currencyUseCase.getGetCurrencyUseCase());
        SetDefaultCommand setDefaultCommand = new SetDefaultCommand(currencyUseCase.getEditCurrencyUseCase());
        EditPluralNameCommand editPluralNameCommand = new EditPluralNameCommand(currencyUseCase.getEditCurrencyUseCase());
        EditSingularNameCommand editSingularNameCommand = new EditSingularNameCommand(currencyUseCase.getEditCurrencyUseCase());

        WithdrawCommand withdrawCommand = new WithdrawCommand(transactionsUseCase.getWithdrawUseCase(), plugin.getMessageService());
        DepositCommand depositCommand = new DepositCommand(transactionsUseCase.getDepositUseCase(), plugin.getMessageService());
        SetCommand setCommand = new SetCommand(transactionsUseCase.getSetBalanceUseCase(), plugin.getMessageService());

        CreateOfferCommand createOfferCommand = new CreateOfferCommand(offerUseCase.getCreateOfferUseCase(),plugin.getMessageService());
        CancelOfferCommand cancelOfferCommand = new CancelOfferCommand(offerUseCase.getCancelOfferUseCase(),plugin.getMessageService());
        AcceptOfferCommand acceptOfferCommand = new AcceptOfferCommand(offerUseCase.getAcceptOfferUseCase(),plugin.getMessageService());
        DenyOfferCommand denyOfferCommand = new DenyOfferCommand(offerUseCase.getCancelOfferUseCase(),plugin.getMessageService());

        BuyCommand buyCommand = new BuyCommand(transactionsUseCase.getWithdrawUseCase(), plugin.getMessageService());



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

        plugin.getCommand("pay").setExecutor(new PayCommand(transactionsUseCase.getPayUseCase(), plugin.getMessageService()));

        offerCommand.registerSubCommand("create", createOfferCommand);
        offerCommand.registerSubCommand("cancel", cancelOfferCommand);
        offerCommand.registerSubCommand("accept", acceptOfferCommand);
        offerCommand.registerSubCommand("deny", denyOfferCommand);

        plugin.getCommand("offer").setExecutor(offerCommand);


        plugin.getCommand("exchange").setExecutor(new ExchangeCommand(transactionsUseCase.getExchangeUseCase(), plugin.getMessageService()));
        plugin.getCommand("balance").setExecutor(new BalanceCommand(accountsUseCase.getGetBalanceUseCase(), plugin.getMessageService()));
        plugin.getCommand("baltop").setExecutor(new BalanceTopCommand(accountsUseCase.getGetAccountsUseCase(), plugin.getMessageService()));


    }
}