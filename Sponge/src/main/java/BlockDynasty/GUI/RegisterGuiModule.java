package BlockDynasty.GUI;

import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.OfferUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.GUI.services.GUIService;
import BlockDynasty.utils.Console;

public class RegisterGuiModule {
    private static final GUIService guiService= new GUIService();

    public static void register(
            TransactionsUseCase transactionsUseCase,
            AccountsUseCase accountsUseCase,
            CurrencyUseCase currencyUseCase,
            OfferUseCase offerUseCase)
    {

        GUIFactory.init(currencyUseCase, accountsUseCase, transactionsUseCase,offerUseCase);

        Console.log("GUI registered successfully.");
    }

    public static GUIService getGuiService() {
        return guiService;
    }
}
