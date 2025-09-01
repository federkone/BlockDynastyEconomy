package BlockDynasty.GUI;

import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.OfferUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.GUI.adapters.PlatformAdapter;
import BlockDynasty.GUI.adapters.TextInput;
import BlockDynasty.utils.Console;
import lib.GUIFactory;


public class RegisterGuiModule {

    public static void register(
            TransactionsUseCase transactionsUseCase,
            AccountsUseCase accountsUseCase,
            CurrencyUseCase currencyUseCase,
            OfferUseCase offerUseCase)
    {

        GUIFactory.init(currencyUseCase, accountsUseCase, transactionsUseCase,offerUseCase,new TextInput(), new PlatformAdapter());

        Console.log("GUI registered successfully.");
    }


}
