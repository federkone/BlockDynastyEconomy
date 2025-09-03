package lib.commands.commands;

import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.OfferUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import lib.commands.abstractions.Command;
import lib.commands.abstractions.PlatformAdapter;
import lib.commands.commands.administrators.EconomySubcommand.*;
import lib.commands.commands.administrators.EconomySubcommand.CurrencySubcommand.*;
import lib.commands.commands.administrators.EconomyCommand;
import lib.commands.commands.users.*;
import lib.commands.commands.users.OfferSubCommand.AcceptOfferCommand;
import lib.commands.commands.users.OfferSubCommand.CancelOfferCommand;
import lib.commands.commands.users.OfferSubCommand.CreateOfferCommand;
import lib.commands.commands.users.OfferSubCommand.DenyOfferCommand;

import java.util.ArrayList;
import java.util.List;


public class CommandsFactory {
    private static PlatformAdapter platformAdapter;
    private static TransactionsUseCase transactionsUseCase;
    private static CurrencyUseCase currencyUseCase;
    private static AccountsUseCase accountsUseCase;
    private static OfferUseCase offerUseCase;

    public static void init(TransactionsUseCase transactionsUseCase, OfferUseCase offerUseCase, CurrencyUseCase currencyUseCase, AccountsUseCase accountsUseCase,PlatformAdapter platformAdapter) {
        CommandsFactory.transactionsUseCase = transactionsUseCase;
        CommandsFactory.offerUseCase = offerUseCase;
        CommandsFactory.accountsUseCase = accountsUseCase;
        CommandsFactory.currencyUseCase = currencyUseCase;
        CommandsFactory.platformAdapter = platformAdapter;
        Commands.init();
    }

    public static PlatformAdapter getPlatformAdapter() {
        return platformAdapter;
    }

    public static class Commands{
        private static final List<Command> MainCommands = new ArrayList<>();

        public static void init(){
            OfferCommand offerCommand = new OfferCommand();//main
            EconomyCommand economyCommand = new EconomyCommand();//main
            CurrencyCommand currencyCommand = new CurrencyCommand();//main
            WithdrawCommand withdrawCommand = new WithdrawCommand(transactionsUseCase.getWithdrawUseCase());
            DepositCommand depositCommand = new DepositCommand(transactionsUseCase.getDepositUseCase());
            SetCommand setCommand = new SetCommand(transactionsUseCase.getSetBalanceUseCase());

            CreateCurrencyCommand createCurrencyCommand = new CreateCurrencyCommand(currencyUseCase.getCreateCurrencyUseCase());
            DeleteCurrencyCommand deleteCurrencyCommand = new DeleteCurrencyCommand(currencyUseCase.getDeleteCurrencyUseCase());
            EditColorCommand editColorCoommand = new EditColorCommand(currencyUseCase.getEditCurrencyUseCase());
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

            BuyCommand buyCommand = new BuyCommand(transactionsUseCase.getWithdrawUseCase());

            CreateOfferCommand createOfferCommand = new CreateOfferCommand(offerUseCase.getCreateOfferUseCase());
            CancelOfferCommand cancelOfferCommand = new CancelOfferCommand(offerUseCase.getCancelOfferUseCase());
            AcceptOfferCommand acceptOfferCommand = new AcceptOfferCommand(offerUseCase.getAcceptOfferUseCase());
            DenyOfferCommand denyOfferCommand = new DenyOfferCommand(offerUseCase.getCancelOfferUseCase());

            PayCommand payCommand = new PayCommand(transactionsUseCase.getPayUseCase());
            BalanceCommand balanceCommand = new BalanceCommand(accountsUseCase.getGetBalanceUseCase());
            BalanceTopCommand balanceTopCommand = new BalanceTopCommand(accountsUseCase.getGetAccountsUseCase());
            ExchangeCommand exchangeCommand = new ExchangeCommand(transactionsUseCase.getExchangeUseCase());

            economyCommand.registerSubCommand( withdrawCommand);
            economyCommand.registerSubCommand( depositCommand);
            economyCommand.registerSubCommand( setCommand);
            economyCommand.registerSubCommand(buyCommand);
            economyCommand.registerSubCommand( currencyCommand);

            currencyCommand.registerSubCommand( createCurrencyCommand);
            currencyCommand.registerSubCommand( deleteCurrencyCommand);
            currencyCommand.registerSubCommand( ViewCommand);
            currencyCommand.registerSubCommand( editColorCoommand);
            currencyCommand.registerSubCommand(editDecimalsCommand);
            currencyCommand.registerSubCommand( editPayableCommand);
            currencyCommand.registerSubCommand( editRateCommand);
            currencyCommand.registerSubCommand( editStartBalCommand);
            currencyCommand.registerSubCommand(editSymbolCommand);
            currencyCommand.registerSubCommand(listCommand);
            currencyCommand.registerSubCommand(setDefaultCommand);
            currencyCommand.registerSubCommand(editPluralNameCommand);
            currencyCommand.registerSubCommand(editSingularNameCommand);

            offerCommand.registerSubCommand( createOfferCommand);
            offerCommand.registerSubCommand(cancelOfferCommand);
            offerCommand.registerSubCommand( acceptOfferCommand);
            offerCommand.registerSubCommand(denyOfferCommand);


            MainCommands.add(economyCommand);
            MainCommands.add(offerCommand);
            MainCommands.add(payCommand);
            MainCommands.add(balanceCommand);
            MainCommands.add(balanceTopCommand);
            MainCommands.add(exchangeCommand);

        }

        public static List<Command> getMainCommands() {
            return MainCommands;
        }

    }
}
