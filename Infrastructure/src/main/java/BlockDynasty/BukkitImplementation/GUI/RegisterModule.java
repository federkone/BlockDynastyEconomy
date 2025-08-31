package BlockDynasty.BukkitImplementation.GUI;

import BlockDynasty.BukkitImplementation.GUI.adapters.BukkitAdapter;
import BlockDynasty.BukkitImplementation.GUI.adapters.TextInput;
import BlockDynasty.BukkitImplementation.GUI.commands.AdminGUICommand;
import BlockDynasty.BukkitImplementation.GUI.commands.BankGUICommand;
import BlockDynasty.BukkitImplementation.GUI.listener.GUIListener;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.OfferUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import lib.GUIFactory;

public class RegisterModule {

    public static void register(
            TransactionsUseCase transactionsUseCase,
            AccountsUseCase accountsUseCase,
            CurrencyUseCase currencyUseCase,
            OfferUseCase offerUseCase,
            MessageService messageService
    ) {

        GUIFactory.init(currencyUseCase, accountsUseCase, transactionsUseCase,offerUseCase,new TextInput(),new BukkitAdapter());

        Console.log("GUI registered successfully.");
    }

    public static GUIListener guiListener(){
        return new GUIListener();
    };

    public static AdminGUICommand AdminCommand(){
        return new AdminGUICommand();
    }

    public static BankGUICommand BankCommand(){
        return new BankGUICommand();
    }


}
