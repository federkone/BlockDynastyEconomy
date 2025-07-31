package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminPanelGUI extends AbstractGUI {
    private final GUIService guiService;
    private final Player sender;
    private final JavaPlugin plugin;
    private final CurrencyUseCase currencyUseCase;
    private final AccountsUseCase accountsUseCase;
    private final TransactionsUseCase transactionsUseCase;

    public AdminPanelGUI(Player sender,GUIService guiService,
                         JavaPlugin plugin,
                         CurrencyUseCase currencyUseCase,
                         AccountsUseCase accountsUseCase,
                         TransactionsUseCase transactionsUseCase
                         ) {
        super("Economy Admin Panel", 5);
        this.sender = sender;
        this.guiService = guiService;
        this.plugin = plugin;
        this.currencyUseCase = currencyUseCase;
        this.accountsUseCase = accountsUseCase;
        this.transactionsUseCase = transactionsUseCase;

        initializeButtons();
    }

    private  void initializeButtons() {
        setItem(10, createItem(Material.PAPER, "Edit Currencies", "Click to edit currencies"), event -> {
            IGUI gui = new CurrencyPanelGUI(guiService,plugin,sender,currencyUseCase.getGetCurrencyUseCase(),
                    currencyUseCase.getEditCurrencyUseCase(),
                    currencyUseCase.getCreateCurrencyUseCase(),
                    currencyUseCase.getDeleteCurrencyUseCase());
            gui.open(sender);
            this.guiService.registerGUI(sender, gui);

        });


        setItem(14, createItem(Material.DIAMOND_SWORD, "Manage Accounts", "Click to manage accounts"), event -> {
            IGUI gui = new AccountPanelGUI(sender,guiService,accountsUseCase.getGetAccountsUseCase(),accountsUseCase.getDeleteAccountUseCase());
            gui.open(sender);
            this.guiService.registerGUI(sender, gui);
        });
    }
}
