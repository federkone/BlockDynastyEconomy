package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AdminPanelGUI extends AbstractGUI {
    private final Player sender;
    private final CurrencyUseCase currencyUseCase;
    private final AccountsUseCase accountsUseCase;
    private final TransactionsUseCase transactionsUseCase;

    public AdminPanelGUI(Player sender,
                         CurrencyUseCase currencyUseCase,
                         AccountsUseCase accountsUseCase,
                         TransactionsUseCase transactionsUseCase)
    {
        super("Economy Admin Panel", 3);
        this.sender = sender;
        this.currencyUseCase = currencyUseCase;
        this.accountsUseCase = accountsUseCase;
        this.transactionsUseCase = transactionsUseCase;

        initializeButtons();
    }

    private  void initializeButtons() {
        setItem(11, createItem(Material.PAPER, "Edit Currencies", "Click to edit currencies"), event -> {
            IGUI gui = new CurrencyPanelGUI(sender,currencyUseCase.getGetCurrencyUseCase(),
                    currencyUseCase.getEditCurrencyUseCase(),
                    currencyUseCase.getCreateCurrencyUseCase(),
                    currencyUseCase.getDeleteCurrencyUseCase(),this);
            gui.open(sender);
        });


        setItem(15, createItem(Material.DIAMOND_SWORD, "Manage Accounts", "Click to manage accounts"), event -> {
            IGUI gui = new AccountPanelGUI(sender,
                    accountsUseCase.getGetBalanceUseCase(),
                    accountsUseCase.getGetAccountsUseCase(),
                    accountsUseCase.getDeleteAccountUseCase(),
                    transactionsUseCase,
                    currencyUseCase.getGetCurrencyUseCase(),this);
            gui.open(sender);
        });

        setItem(22, createItem(Material.BARRIER, "Close", "Click to close this menu"), event -> {
            sender.closeInventory();
        });


    }
}
